package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.controller.subController.MainMenuUserCell;
import de.uniks.stpmon.team_m.controller.subController.RegionCell;
import de.uniks.stpmon.team_m.dto.Group;
import de.uniks.stpmon.team_m.dto.Region;
import de.uniks.stpmon.team_m.dto.User;
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
    @FXML
    public ListView<User> friendsListView;
    @FXML
    public ListView<Region> regionListView;

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
    RegionsService regionsService;
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
    private ToggleGroup regionToggleGroup;

    @Override
    public void init() {
        disposables.add(regionsService.getRegions()
                .observeOn(FX_SCHEDULER).subscribe(this.regions::setAll, error -> System.out.println(error.getMessage())));
        if (!userStorageProvider.get().getFriends().isEmpty()) {
            disposables.add(usersService.getUsers(userStorageProvider.get().getFriends(), null)
                    .observeOn(FX_SCHEDULER).subscribe(users -> {
                        friends.setAll(users);
                        friendsListView.getItems().setAll(users);
                        FriendListUtils.sortListView(friendsListView);
                    }, error -> showError(error.getMessage())));
            listenToUserUpdate(friends, friendsListView);
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
        initFriendslist();
        initRadioButtons();
        return parent;
    }

    private void switchToMessageScreen() {
        Group privateGroup = new Group(friendsListView.getSelectionModel().getSelectedItem()._id(), friendsListView.getSelectionModel().getSelectedItem().name(), null);
        groupStorageProvider.get().set_id(privateGroup._id());
        groupStorageProvider.get().setName(privateGroup.name());
        app.show(messagesControllerProvider.get());
    }

    private void initRadioButtons() {
        regionToggleGroup = new ToggleGroup();
        regionListView.setCellFactory(param -> new RegionCell(regionToggleGroup));
        regionListView.setItems(regions);
        startGameButton.disableProperty().bind(regionToggleGroup.selectedToggleProperty().isNull());
    }

    private void initFriendslist() {
        friendsListView.setCellFactory(param -> new MainMenuUserCell(preferencesProvider.get(), userStorageProvider.get(), usersService));
        friendsListView.setPlaceholder(new Label(NO_FRIENDS_FOUND));
        friendsListView.setOnMouseClicked(event -> {
            if (!friendsListView.getSelectionModel().isEmpty()) {
                switchToMessageScreen();
            }
        });
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
