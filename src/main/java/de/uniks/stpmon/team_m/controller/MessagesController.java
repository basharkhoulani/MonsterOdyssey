package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.Constants;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.GroupStorage;
import de.uniks.stpmon.team_m.service.UserStorage;
import de.uniks.stpmon.team_m.service.UsersService;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import javax.inject.Inject;
import javax.inject.Provider;

import java.util.List;
import java.util.Objects;

import static de.uniks.stpmon.team_m.Constants.*;

public class MessagesController extends Controller {

    @FXML
    public HBox rootHBox;
    @FXML
    public VBox leftSideVBox;
    @FXML
    public VBox rightSideVBox;
    @FXML
    public Text friendsAndGroupText;
    @FXML
    public ScrollPane friendsAndGroupsScrollPane;
    @FXML
    public Button findNewFriendsButton;
    @FXML
    public Button newGroupButton;
    @FXML
    public Button mainMenuButton;
    @FXML
    public Text currentFriendOrGroupText; //needs to be set each time a different chat is selected
    @FXML
    public ScrollPane chatScrollPane;
    @FXML
    public VBox chatVBox;
    @FXML
    public TextArea messageTextArea;
    @FXML
    public Button sendButton;
    @FXML
    public Button settingsButton;
    @FXML
    public VBox friendsAndGroupsVBox;
    @FXML
    public Pane buttonPane;
    @FXML
    public Pane friendsAndGroupTextPane;
    @FXML
    public Pane chatPaneHeaderText;
    @FXML
    public Pane chatPaneInputArea;

    @Inject
    Provider<MainMenuController> mainMenuControllerProvider;

    @Inject
    Provider<NewFriendController> newFriendControllerProvider;

    @Inject
    Provider<GroupController> groupControllerProvider;

    @Inject
    Provider<UserStorage> userStorageProvider;
    @Inject
    Provider<UsersService> usersServiceProvider;
    private final GroupStorage groupStorage;

    @Inject
    public MessagesController(GroupStorage groupStorage) {

        this.groupStorage = groupStorage;
    }

    @Override
    public String getTitle() {
        return MESSAGES_TITLE;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        this.setupWindowScalingCompatibility();
        this.initializeFriendNodes();

        return parent;
    }

    private void initializeFriendNodes() {
        List<String> friends = userStorageProvider.get().getFriends();
        usersServiceProvider.get().getUsers(friends, null).subscribe(new Observer<>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull List<User> users) {
                for (User friend : users) {
                    HBox newFriendNode = createFriendNode(friend);
                    friendsAndGroupsVBox.getChildren().add(newFriendNode);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public int getHeight() {
        return MESSAGES_HEIGHT;
    }

    @Override
    public int getWidth() {
        return MESSAGES_WIDTH;
    }

    private void setupWindowScalingCompatibility() {
        Scene scene = app.getStage().getScene();
        scene.setRoot(rootHBox);

        // base window
        rootHBox.prefWidthProperty().bind(scene.widthProperty());
        rootHBox.prefHeightProperty().bind(scene.heightProperty());

        // left and right vBoxes
        leftSideVBox.maxHeightProperty().bind(rootHBox.prefHeightProperty());
        rightSideVBox.maxHeightProperty().bind(rootHBox.prefHeightProperty());
        rightSideVBox.maxWidthProperty().bind(rootHBox.prefWidthProperty().map(number ->
                number.intValue() - leftSideVBox.getPrefWidth()));

        leftSideVBox.prefHeightProperty().bind(rootHBox.prefHeightProperty());
        rightSideVBox.prefHeightProperty().bind(rootHBox.prefHeightProperty());
        rightSideVBox.prefWidthProperty().bind(rootHBox.prefWidthProperty().map(number ->
                number.intValue() - leftSideVBox.getPrefWidth()));

        for (Node child : leftSideVBox.getChildren()) {
            VBox.setVgrow(child, Priority.NEVER);
        }
        VBox.setVgrow(friendsAndGroupsScrollPane, Priority.ALWAYS);

        for (Node child : rightSideVBox.getChildren()) {
            VBox.setVgrow(child, Priority.NEVER);
        }
        VBox.setVgrow(chatScrollPane, Priority.ALWAYS);

        // messageHeaderArea
        currentFriendOrGroupText.layoutXProperty().bind(rightSideVBox.prefWidthProperty().map(number ->
                (number.intValue() - chatPaneHeaderText.getPrefWidth()) / 2));

        settingsButton.layoutXProperty().bind(currentFriendOrGroupText.layoutXProperty().map(number ->
                number.intValue() + currentFriendOrGroupText.getWrappingWidth() + 3));

        // message input area
        messageTextArea.prefWidthProperty().bind(rightSideVBox.prefWidthProperty().map(number ->
                number.intValue() - sendButton.getPrefWidth() - 7));
        sendButton.layoutXProperty().bind(messageTextArea.prefWidthProperty().map(number ->
                number.intValue() + 2));
    }

    public void changeToMainMenu() {
        app.show(mainMenuControllerProvider.get());
    }

    public void changeToFindNewFriends() {
        app.show(newFriendControllerProvider.get());
    }

    public void changeToNewGroup() {
        groupStorage.set_id(EMPTY_STRING);
        app.show(groupControllerProvider.get());
    }

    public HBox createFriendNode(User user) {
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

    public void creteMessageNode() {
        // TODO:
        /*
         * This will probably work similiar to the 'createFriendNode()' method.
         * */
    }

    public void sendMessage() {
        // TODO:
        // button fx:id: '#sendButton'
    }

    public void changeToSettings() {
        groupStorage.set_id(LOADING);
        app.show(groupControllerProvider.get());
    }
}
