package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.Constants;
import de.uniks.stpmon.team_m.controller.subController.FriendSettingsController;
import de.uniks.stpmon.team_m.controller.subController.RegionCell;
import de.uniks.stpmon.team_m.dto.Region;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.rest.RegionsApiService;
import de.uniks.stpmon.team_m.service.UserStorage;
import de.uniks.stpmon.team_m.service.UsersService;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.controlsfx.control.PopOver;

import javax.inject.Inject;
import javax.inject.Provider;

import java.util.List;
import java.util.Objects;

import static de.uniks.stpmon.team_m.Constants.MAIN_MENU_TITLE;

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
    Provider<UserStorage> userStorageProvider;

    @Inject
    Provider<UsersService> usersServiceProvider;

    private final ObservableList<Region> regions = FXCollections.observableArrayList();
    private ToggleGroup regionToggleGroup;
    private Disposable disposable;


    @Override
    public void init() {
        disposables.add(regionsApiService.getRegions().observeOn(FX_SCHEDULER).subscribe(this.regions::setAll));
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
        initFriendNodes();
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

    private void initFriendNodes() {
        List<String> friends = userStorageProvider.get().getFriends();
        if (friends.isEmpty()) {
            return;
        }

        usersServiceProvider.get().getUsers(friends, null).subscribe(new Observer<>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(@NonNull List<User> users) {
                for (User friend : users) {
                    HBox newFriendNode = createFriendNode(friend);
                    Platform.runLater(() -> friendsListVBox.getChildren().add(newFriendNode));
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                disposable.dispose();
            }

            @Override
            public void onComplete() {
                disposable.dispose();
            }
        });
    }

    private HBox createFriendNode(User user) {
        HBox friendHBox = new HBox();

        friendHBox.setPrefHeight(Constants.MESSAGES_FRIEND_NODE_HEIGHT);
        friendHBox.setMinHeight(Constants.MESSAGES_FRIEND_NODE_HEIGHT);
        friendHBox.setPadding(Constants.MESSAGES_FRIEND_NODE_PADDING);
        friendHBox.getStyleClass().add("normalFriendHBox");

        friendHBox.hoverProperty().addListener((observable, oldValue, newValue) -> {
            friendHBox.getStyleClass().clear();

            if (newValue) {
                friendHBox.getStyleClass().add("onHoverFriendHBox");
            } else {
                friendHBox.getStyleClass().add("normalFriendHBox");
            }
        });
        friendHBox.setOnMouseClicked((event -> {
            friendHBox.requestFocus();
            showPopOver(event, user);
        }));

        Circle status = new Circle();
        status.setRadius(Constants.MESSAGES_FRIEND_NODE_STATUS_RADIUS);
        status.setStroke(Color.BLACK);
        status.setStrokeWidth(1);
        status.setFill(Objects.equals(user.status(), Constants.USER_STATUS_ONLINE) ? Color.LIGHTGREEN : Color.RED);

        Text friendName = new Text();
        friendName.setTextAlignment(TextAlignment.CENTER);
        friendName.setStyle("-fx-font-size: 20");
        friendName.setText(user.name());

        friendHBox.getChildren().add(status);
        friendHBox.getChildren().add(friendName);

        return friendHBox;
    }

    private void showPopOver(MouseEvent event, User user) {
        PopOver popOver = new PopOver();
        popOver.setContentNode(new FriendSettingsController(user).render());
        popOver.setDetachable(false);
        popOver.show((Node) event.getSource());
    }

    public void changeToFindNewFriends() {
        app.show(newFriendControllerProvider.get());
    }

    public void changeToMessages() {
        app.show(messagesControllerProvider.get());
    }

    public void changeToLogin() {
        app.show(loginControllerProvider.get());
    }

    public void changeToSettings() {
        app.show(accountSettingControllerProvider.get());
    }

    public void changeToIngame() {
        app.show(ingameControllerProvider.get());
    }
}
