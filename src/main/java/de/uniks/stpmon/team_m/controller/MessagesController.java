package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.controller.views.UserCell;
import de.uniks.stpmon.team_m.dto.Message;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.GroupStorage;
import de.uniks.stpmon.team_m.service.UserStorage;
import de.uniks.stpmon.team_m.service.UsersService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import javax.inject.Inject;
import javax.inject.Provider;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static de.uniks.stpmon.team_m.Constants.*;

public class MessagesController extends Controller {

    @FXML
    public Text currentFriendOrGroupText; //needs to be set each time a different chat is selected
    @FXML
    public Label friendsAndGroupText;
    @FXML
    public VBox friendsListViewVBox;
    @FXML
    public Button findNewFriendsButton;
    @FXML
    public Button newGroupButton;
    @FXML
    public Button mainMenuButton;
    @FXML
    public Button settingsButton;
    @FXML
    public VBox messagesListViewVBox;
    @FXML
    public TextArea messageTextArea;
    @FXML
    public Button sendButton;
    @FXML
    public VBox chatViewVBox;
    @Inject
    Provider<MainMenuController> mainMenuControllerProvider;

    @Inject
    Provider<NewFriendController> newFriendControllerProvider;

    @Inject
    Provider<GroupController> groupControllerProvider;

    @Inject
    Provider<UserStorage> userStorageProvider;
    @Inject
    Provider<GroupStorage> groupStorageProvider;
    @Inject
    UsersService usersService;
    private final ObservableList<User> friends = FXCollections.observableArrayList();
    private ListView<User> listView;

    @Inject
    public MessagesController() {
    }

    @Override
    public void init() {
        listView = new ListView<>(friends);
        listView.setId("friendsAndGroups");
        listView.setCellFactory(param -> new UserCell());
        disposables.add(usersService.getUsers(userStorageProvider.get().getFriends(), null)
                .observeOn(FX_SCHEDULER).subscribe(friends::setAll));
    }

    @Override
    public String getTitle() {
        return MESSAGES_TITLE;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        friendsListViewVBox.getChildren().add(listView);
        return parent;
    }

    public void changeToMainMenu() {
        app.show(mainMenuControllerProvider.get());
    }

    public void changeToFindNewFriends() {
        app.show(newFriendControllerProvider.get());
    }

    public void changeToNewGroup() {
        groupStorageProvider.get().set_id(EMPTY_STRING);
        app.show(groupControllerProvider.get());
    }

    public VBox createMessageNode(Message message) {
        VBox newMessageNode = new VBox();
        HBox newMessageValueArea = new HBox();
        HBox newMessageInfoArea = new HBox();

        newMessageValueArea.maxWidthProperty().bind(chatViewVBox.widthProperty().map(number -> number.intValue() / 2 - 20));
        newMessageInfoArea.maxWidthProperty().bind(chatViewVBox.widthProperty().map(number -> number.intValue() / 2 - 20));

        boolean userIsSender = message.sender().equals(userStorageProvider.get().get_id());
        // if user is sender: message is on the right, else on the left
        newMessageNode.setAlignment(userIsSender ? Pos.TOP_RIGHT : Pos.TOP_LEFT);

        newMessageValueArea.setBorder(new Border(
                new BorderStroke(
                        Color.BLACK,
                        BorderStrokeStyle.SOLID,
                        new CornerRadii(10),
                        new BorderWidths(2)
                )
        ));

        Text messageText = new Text();
        messageText.setText(message.body());
        messageText.wrappingWidthProperty().bind(chatViewVBox.widthProperty().map(number -> number.intValue() / 2 - 20));

        VBox newMessageValueContainer = new VBox();
        newMessageValueContainer.getChildren().add(messageText);
        newMessageValueArea.getChildren().add(newMessageValueContainer);

        newMessageValueArea.setPadding(new Insets(2));
        newMessageValueContainer.setPadding(new Insets(5));

        Label authorAndTime = new Label();
        if (userIsSender) {
            authorAndTime.setText(userStorageProvider.get().getName() +
                    " " +
                    formatTimeString(message.createdAt()) +
                    " " +
                    (message.updatedAt() == null ? "" : "\u270F"));


            newMessageValueContainer.setBackground(Background.fill(Paint.valueOf("lightblue")));
        } else {
            final String[] senderName = {""};
            System.out.println(message);
            usersService.getUser(message.sender()).map(
                    user -> {
                        senderName[0] = user.name();
                        return user;
                    }
            ).subscribe().dispose();

            authorAndTime.setText(senderName[0] +
                    " " +
                    formatTimeString(message.createdAt()) +
                    " " +
                    (message.updatedAt() == null ? "" : "\u270F"));
        }

        newMessageInfoArea.getChildren().add(authorAndTime);

        Button editButton = new Button("\u270F");
        editButton.setOnAction(event -> {
            // @Cheng
        });

        Button deleteButton = new Button("\uD83D\uDDD1");
        deleteButton.setOnAction(event -> {
            // @Cheng
        });
        newMessageInfoArea.getChildren().addAll(editButton, deleteButton);

        newMessageInfoArea.setFillHeight(true);
        HBox.setMargin(editButton, new Insets(0, 0, 0, 5));
        HBox.setMargin(deleteButton, new Insets(0, 0, 0, 5));
        editButton.setVisible(false);
        deleteButton.setVisible(false);

        newMessageNode.getChildren().add(newMessageValueArea);
        newMessageNode.getChildren().add(newMessageInfoArea);

        newMessageNode.hoverProperty().addListener((observable, oldValue, newValue) -> {
            editButton.setVisible(newValue);
            deleteButton.setVisible(newValue);
        });

        return newMessageNode;
    }

    public void sendMessage() {
        // TODO:
        // button fx:id: '#sendButton'
    }

    public void changeToSettings() {
        groupStorageProvider.get().set_id(LOADING);
        app.show(groupControllerProvider.get());
    }

    private String formatTimeString(String dateTime) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        System.out.println(dateTime);
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, inputFormatter);

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("HH:mm, dd.MM.yy");
        return localDateTime.format(outputFormatter);
    }
}
