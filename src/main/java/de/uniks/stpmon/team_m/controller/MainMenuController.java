package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.controller.subController.MainMenuUserCell;
import de.uniks.stpmon.team_m.controller.subController.RegionCell;
import de.uniks.stpmon.team_m.dto.Group;
import de.uniks.stpmon.team_m.dto.Region;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.rest.RegionsApiService;
import de.uniks.stpmon.team_m.service.*;
import de.uniks.stpmon.team_m.utils.FriendListUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.prefs.Preferences;

import static de.uniks.stpmon.team_m.Constants.*;

public class MainMenuController extends Controller {


    @FXML
    public VBox friendsListVBox;
    @FXML
    public Button findNewFriendsButton;
    @FXML
    public Button messagesButton;
    @FXML
    public Button logoutButton;
    @FXML
    public Button settingsButton;
    @FXML
    public Button startGameButton;
    @FXML
    public VBox regionRadioButtonList;
    @Inject
    Provider<LoginController> loginControllerProvider;
    @Inject
    Provider<IngameController> ingameControllerProvider;
    @Inject
    Provider<AccountSettingController> accountSettingControllerProvider;
    @Inject
    Provider<NewFriendController> newFriendControllerProvider;
    @Inject
    Provider<MessagesController> messagesControllerProvider;
    @Inject
    RegionsApiService regionsApiService;
    @Inject
    UsersService usersService;
    @Inject
    AuthenticationService authenticationService;
    @Inject
    Provider<UserStorage> userStorageProvider;
    @Inject
    Provider<Preferences> preferencesProvider;
    @Inject
    Provider<GroupService> groupServiceProvider;
    @Inject
    Provider<GroupStorage> groupStorageProvider;
    private final ObservableList<Region> regions = FXCollections.observableArrayList();
    private final ObservableList<User> friends = FXCollections.observableArrayList();
    private ListView<User> friendsListView;
    private ToggleGroup regionToggleGroup;

    @Override
    public void init() {
        friendsListView = new ListView<>(friends);
        friendsListView.setId("friendsListView");
        friendsListView.setCellFactory(param -> new MainMenuUserCell(preferencesProvider.get(), userStorageProvider.get(), usersService));
        friendsListView.setPlaceholder(new Label(NO_FRIENDS_FOUND));
        disposables.add(regionsApiService.getRegions()
                .observeOn(FX_SCHEDULER).subscribe(this.regions::setAll));
        if (!userStorageProvider.get().getFriends().isEmpty()) {
            disposables.add(usersService.getUsers(userStorageProvider.get().getFriends(), null)
                    .observeOn(FX_SCHEDULER).subscribe(users -> {
                        friends.setAll(users);
                        FriendListUtils.sortListView(friendsListView);
                    }, error -> showError(error.getMessage())));
            listenToStatusUpdate(friends, friendsListView);
        }
    }

    @Inject
    public MainMenuController() {
    }

    @Override
    public String getTitle() {
        return MAIN_MENU_TITLE;
    }

    @Override
    public Parent render() {
        final Parent parent = super.render();
        initRadioButtons();
        friendsListVBox.getChildren().add(friendsListView);
        friendsListView.setOnMouseClicked(event -> {
            if (!friendsListView.getSelectionModel().isEmpty()) {
                switchToMessageScreen();
            }
        });
        return parent;
    }

    private void switchToMessageScreen() {
        Group privateGroup = new Group(friendsListView.getSelectionModel().getSelectedItem()._id(), friendsListView.getSelectionModel().getSelectedItem().name(), null);
        groupStorageProvider.get().set_id(privateGroup._id());
        groupStorageProvider.get().setName(privateGroup.name());
        app.show(messagesControllerProvider.get());
    }

    private void initRadioButtons() {
        ListView<Region> regionListView = new ListView<>();
        regionToggleGroup = new ToggleGroup();
        regionListView.setId("regionListView");
        regionListView.setCellFactory(param -> new RegionCell(regionToggleGroup));
        regionListView.setItems(regions);
        regionRadioButtonList.getChildren().add(regionListView);
        startGameButton.disableProperty().bind(regionToggleGroup.selectedToggleProperty().isNull());
    }

    public void changeToFindNewFriends() {
        app.show(newFriendControllerProvider.get());
    }

    public void changeToMessages() {
        groupStorageProvider.get().set_id(null);
        groupStorageProvider.get().setName(null);
        app.show(messagesControllerProvider.get());
    }

    public void changeToLogin() {
        disposables.add(usersService.updateUser(null, USER_STATUS_OFFLINE, null, null, null)
                .observeOn(FX_SCHEDULER)
                .subscribe(user -> {
                }, error -> showError(error.getMessage())));
        disposables.add(authenticationService.logout().observeOn(FX_SCHEDULER)
                .subscribe(logoutResult -> app.show(loginControllerProvider.get()), error -> showError(error.getMessage())));

    }

    public void changeToSettings() {
        app.show(accountSettingControllerProvider.get());
    }

    public void changeToIngame() {
        app.show(ingameControllerProvider.get());
    }
}
