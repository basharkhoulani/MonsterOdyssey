package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.Constants;
import de.uniks.stpmon.team_m.controller.subController.MainMenuUserCell;
import de.uniks.stpmon.team_m.controller.subController.RegionCell;
import de.uniks.stpmon.team_m.dto.Region;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.rest.RegionsApiService;
import de.uniks.stpmon.team_m.service.AuthenticationService;
import de.uniks.stpmon.team_m.service.UserStorage;
import de.uniks.stpmon.team_m.service.UsersService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;

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
    private final ObservableList<Region> regions = FXCollections.observableArrayList();
    private final ObservableList<User> friends = FXCollections.observableArrayList();
    private ListView<User> friendsListView;
    private ToggleGroup regionToggleGroup;

    @Override
    public void init() {
        friendsListView = new ListView<>(friends);
        friendsListView.setId("friendsListView");
        friendsListView.setCellFactory(param -> new MainMenuUserCell());
        disposables.add(regionsApiService.getRegions()
                .observeOn(FX_SCHEDULER).subscribe(this.regions::setAll));
        if (!userStorageProvider.get().getFriends().isEmpty()) {
            disposables.add(usersService.getUsers(userStorageProvider.get().getFriends(), null)
                    .observeOn(FX_SCHEDULER).subscribe(this.friends::setAll));
        }
        userStatusUpdate();
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
        return parent;
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
        app.show(messagesControllerProvider.get());
    }

    public void changeToLogin(ActionEvent actionEvent) {
        if(userStorageProvider.get().get_id() == null) {
            return;
        }

        if(actionEvent == null){
            usersService.updateUser(null, USER_STATUS_OFFLINE, null, null, null)
                    .observeOn(FX_SCHEDULER)
                    .subscribe();
            authenticationService.logout().observeOn(FX_SCHEDULER).subscribe();
            return;
        }

        authenticationService.removeRemember();
        usersService.updateUser(null, USER_STATUS_OFFLINE, null, null, null)
                    .observeOn(FX_SCHEDULER)
                    .subscribe(
                            user ->{
                                if(!user.status().contains(USER_STATUS_OFFLINE)){
                                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                                    errorAlert.setContentText(CUSTOM_ERROR);
                                    errorAlert.showAndWait();
                                }
                                app.show(loginControllerProvider.get());
                            }, error ->{
                                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                                errorAlert.setContentText(CUSTOM_ERROR);
                                errorAlert.showAndWait();
                            }
                    );

    }

    public void changeToSettings() {
        app.show(accountSettingControllerProvider.get());
    }

    public void changeToIngame() {
        app.show(ingameControllerProvider.get());
    }

    public void userStatusUpdate() {
        disposables.add(usersService.updateUser(null, USER_STATUS_ONLINE, null, null, null)
                .observeOn(FX_SCHEDULER).subscribe(user -> userStorageProvider.get().setStatus(user.status())));
    }
}
