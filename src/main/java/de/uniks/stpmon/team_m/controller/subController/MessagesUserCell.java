package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.dto.Group;
import de.uniks.stpmon.team_m.dto.Message;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.GroupService;
import de.uniks.stpmon.team_m.service.MessageService;
import de.uniks.stpmon.team_m.service.UserStorage;
import de.uniks.stpmon.team_m.service.UsersService;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import javax.inject.Provider;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.prefs.Preferences;

import static de.uniks.stpmon.team_m.controller.Controller.FX_SCHEDULER;

public class MessagesUserCell extends UserCell{
    private final VBox chatVBox;
    protected final CompositeDisposable disposables;
    private final ObservableList<Group> groups = FXCollections.observableArrayList();
    private final ObservableList<Message> messages = FXCollections.observableArrayList();
    private final Text currentFriendOrGroupText;
    private final ScrollPane chatScrollPane;
    private Group privateChat;

    private final Provider<UserStorage> userStorageProvider;
    private final UsersService usersService;
    private final MessageService messageService;
    private final GroupService groupService;

    // Inject won't work, so I had to do it the old-fashioned way. Figured it would take too long to look for the problem
    public MessagesUserCell(Preferences preferences,
                            VBox chatVbox,
                            Text currentFriendOrGroupText,
                            ScrollPane chatScrollPane,
                            Provider<UserStorage> userStorageProvider,
                            UsersService usersService,
                            MessageService messageService,
                            GroupService groupService,
                            CompositeDisposable disposable) {
        super(preferences);
        this.chatVBox = chatVbox;
        this.currentFriendOrGroupText = currentFriendOrGroupText;
        this.userStorageProvider = userStorageProvider;
        this.usersService = usersService;
        this.messageService = messageService;
        this.groupService = groupService;
        this.chatScrollPane = chatScrollPane;
        this.disposables = disposable;
    }
    @Override
    protected void updateItem(User item, boolean empty) {
        super.updateItem(item, empty);
        HBox friendNode = this.getRootHBox();

        if (item == null) {
            setText(null);
            setGraphic(null);
        } else {
            disposables.add(groupService.getGroups(List.of(item._id(), userStorageProvider.get().get_id()))
                    .observeOn(FX_SCHEDULER).subscribe(groups::setAll));

            for (Group group : groups) {
                if (Objects.equals(group.name(), "") || group.name() == null) {
                    this.privateChat = group;
                    break;
                }
                this.privateChat = null;
            }

            friendNode.setOnMouseClicked(event -> {
                this.currentFriendOrGroupText.setText(item.name());
                chatVBox.getChildren().clear();

                if (this.privateChat != null) {
                    disposables.add(messageService.getGroupMessages(privateChat._id())
                            .observeOn(FX_SCHEDULER).subscribe(messages::setAll));

                    for (Message message : messages) {
                        chatVBox.getChildren().add(this.createMessageNode(message));
                    }

                    chatScrollPane.setVvalue(1.0);
                }
            });
        }
    }

    public VBox createMessageNode(Message message) {
        VBox newMessageNode = new VBox();
        HBox newMessageValueArea = new HBox();
        HBox newMessageInfoArea = new HBox();

        // @Cheng, so you can call delete and edit functionality on this message
        newMessageNode.setId(message._id());

        newMessageValueArea.maxWidthProperty().bind(chatVBox.widthProperty().map(number -> number.intValue() / 2 - 20));
        newMessageInfoArea.maxWidthProperty().bind(chatVBox.widthProperty().map(number -> number.intValue() / 2 - 20));

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
        messageText.wrappingWidthProperty().bind(chatVBox.widthProperty().map(number -> number.intValue() / 2 - 20));

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

    private String formatTimeString(String dateTime) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, inputFormatter);

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("HH:mm, dd.MM.yy");
        return localDateTime.format(outputFormatter);
    }
}
