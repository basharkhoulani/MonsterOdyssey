package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.controller.subController.FriendSettingsController;
import de.uniks.stpmon.team_m.controller.subController.MainMenuUserCell;
import de.uniks.stpmon.team_m.controller.subController.RegionCell;
import de.uniks.stpmon.team_m.dto.Region;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.*;
import de.uniks.stpmon.team_m.utils.*;
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
import java.util.Objects;
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
    public Button muteButton;
    @FXML
    public Button startGameButton;
    @FXML
    public VBox regionRadioButtonList;
    @FXML
    public ListView<User> friendsListView;
    @FXML
    public ListView<Region> regionListView;
    @FXML
    public Label trainerDeletedSuccessfully;
    public String information;
    @Inject
    Provider<LoginController> loginControllerProvider;
    @Inject
    Provider<IngameController> ingameControllerProvider;
    @Inject
    Provider<WelcomeSceneController> welcomeSceneControllerProvider;
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
    AudioService audioService;
    @Inject
    AuthenticationService authenticationService;
    @Inject
    Provider<FriendSettingsController> friendSettingsControllerProvider;
    @Inject
    Provider<UserStorage> userStorageProvider;
    @Inject
    Provider<Preferences> preferencesProvider;
    @Inject
    Provider<GroupStorage> groupStorageProvider;
    @Inject
    Provider<TrainersService> trainersServiceProvider;
    @Inject
    TrainerStorage trainerStorage;
    @Inject
    Provider<PresetsService> presetsServiceProvider;

    private final ObservableList<Region> regions = FXCollections.observableArrayList();
    private final ObservableList<User> friends = FXCollections.observableArrayList();
    private ToggleGroup regionToggleGroup;

    /**
     * MainMenuController is used as a lobby and puffer zone for all other controllers.
     */

    @Inject
    public MainMenuController() {
    }

    /**
     * This method initializes the friends list and regions list. It also adds a listener to the friends list
     * if the list is not empty.
     */

    @Override
    public void init() {
        disposables.add(regionsService.getRegions()
                .observeOn(FX_SCHEDULER).subscribe(this.regions::setAll, error -> showError(error.getMessage())));
        if (!userStorageProvider.get().getFriends().isEmpty()) {
            disposables.add(usersService.getUsers(userStorageProvider.get().getFriends(), null)
                    .observeOn(FX_SCHEDULER).subscribe(users -> {
                        friends.setAll(users);
                        friendsListView.getItems().setAll(users);
                        FriendListUtils.sortListView(friendsListView);
                    }, error -> showError(error.getMessage())));

        }
    }

    /**
     * This method sets the controller title.
     *
     * @return Title of the controller.
     */

    @Override
    public String getTitle() {
        return resources.getString("MAIN.MENU.TITLE");
    }

    /**
     * This method renders the main menu. If the user clicks on a friend in the friends list, the user will be
     * selected and the user will be able to send messages to the selected user in the MessagesController.
     *
     * @return Parent of the main menu.
     */

    @Override
    public Parent render() {
        final Parent parent = super.render();
        initFriendslist();
        initRadioButtons();
        showTrainerDeletion();
        return parent;
    }

    /**
     * This method is used to switch to the MessagesController with the selected user.
     */

    private void switchToMessageScreen() {
        User selectedUser = friendsListView.getSelectionModel().getSelectedItem();
        groupStorageProvider.get().set_id(selectedUser._id());
        groupStorageProvider.get().setName(selectedUser.name());
        MessagesController messagesController = messagesControllerProvider.get();
        messagesController.setUserChosenFromMainMenu(true);
        destroy();
        app.show(messagesController);
    }

    /**
     * This method is used to initialize the radio buttons for the regions list.
     * It also disables the start game button if no region was selected.
     */

    private void initRadioButtons() {
        regionToggleGroup = new ToggleGroup();
        regionListView.setCellFactory(param -> new RegionCell(regionToggleGroup));
        regionListView.setItems(regions);
        startGameButton.disableProperty().bind(regionToggleGroup.selectedToggleProperty().isNull());
    }

    private void initFriendslist() {
        friendsListView.setCellFactory(param -> new MainMenuUserCell(preferencesProvider.get(), friendSettingsControllerProvider));
        friendsListView.setPlaceholder(new Label(resources.getString("NO.FRIENDS.FOUND")));
        listenToUserUpdate(friends, friendsListView);
        friendsListView.setOnMouseClicked(event -> {
            if (!friendsListView.getSelectionModel().isEmpty()) {
                switchToMessageScreen();
            }
        });
    }

    /**
     * This method is used to navigate to NewFriendsController.
     */

    public void changeToFindNewFriends() {
        destroy();
        app.show(newFriendControllerProvider.get());
    }

    /**
     * This method is used to navigate to MessagesController.
     */

    public void changeToMessages() {
        groupStorageProvider.get().set_id(null);
        groupStorageProvider.get().setName(null);
        destroy();
        app.show(messagesControllerProvider.get());
    }

    /**
     * This method is used to navigate to LoginController.
     * It logs out the current user and sets the user status to offline.
     */

    public void changeToLogin() {
        disposables.add(usersService.updateUser(null, USER_STATUS_OFFLINE, null, null, null)
                .doOnNext(user -> {
                })
                .flatMap(user -> authenticationService.logout()).observeOn(FX_SCHEDULER)
                .subscribe(event -> {
                    destroy();
                    app.show(loginControllerProvider.get());
                }, error -> showError(error.getMessage())));
    }

    /**
     * This method is used to navigate to AccountSettingController.
     */

    public void changeToSettings() {
        destroy();
        app.show(accountSettingControllerProvider.get());
    }

    /**
     * This method is used to navigate to IngameController.
     */

    public void changeToIngame() {
        Region selectedRegion = (Region) regionToggleGroup.getSelectedToggle().getUserData();
        WelcomeSceneController welcomeSceneController = welcomeSceneControllerProvider.get();
        trainerStorage.setRegion(selectedRegion);


        disposables.add(trainersServiceProvider.get().getTrainers(selectedRegion._id(), null, userStorageProvider.get().get_id()).observeOn(FX_SCHEDULER).subscribe(result -> {
                    if (result.isEmpty()) {
                        destroy();
                        app.show(welcomeSceneController);
                    } else {
                        trainerStorage.setTrainer(result.get(0));
                        trainerStorage.setTrainerName(result.get(0).name());
                        trainerStorage.setTrainerSprite(result.get(0).image());
                        disposables.add(presetsServiceProvider.get().getCharacter(result.get(0).image()).observeOn(FX_SCHEDULER).subscribe(
                                response -> {
                                    trainerStorage.setTrainerSpriteChunk(ImageProcessor.resonseBodyToJavaFXImage(response));
                                    destroy();
                                    AudioService.getInstance().stopSound();
                                    AudioService.getInstance().playSound(CITY_SOUND);
                                    app.show(ingameControllerProvider.get());
                                },
                                error -> {
                                    showError(error.getMessage());
                                    error.printStackTrace();
                                }
                        ));
                    }
                }, error -> showError(error.getMessage())
        ));
    }

    public void showTrainerDeletion() {
        trainerDeletedSuccessfully.setText(this.information);
    }

    public void setTrainerDeletion() {
        this.information = resources.getString("DELETE.TRAINER.SUCCESS");
    }

    public void muteOrUnmuteSound() {
        if(AudioService.getInstance().checkMuted()) {
            muteButton.getStyleClass().remove("unmuteSymbol");
            muteButton.getStyleClass().add("muteSymbol");
            AudioService.getInstance().unmuteSound();
        } else {
            muteButton.getStyleClass().remove("muteSymbol");
            muteButton.getStyleClass().add("unmuteSymbol");
            AudioService.getInstance().muteSound();
        }
    }
}
