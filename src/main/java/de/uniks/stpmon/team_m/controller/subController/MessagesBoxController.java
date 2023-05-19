package de.uniks.stpmon.team_m.controller.subController;

import com.sun.javafx.collections.ElementObservableListDecorator;
import de.uniks.stpmon.team_m.Constants;
import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.dto.Group;
import de.uniks.stpmon.team_m.dto.Message;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.*;
import de.uniks.stpmon.team_m.ws.EventListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import org.controlsfx.tools.Platform;

import javax.inject.Provider;
import java.lang.reflect.Parameter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static de.uniks.stpmon.team_m.Constants.MESSAGE_NAMESPACE_GROUPS;

public class MessagesBoxController extends Controller {

    private User user;
    private final VBox chatViewVBox;
    private final ScrollPane chatScrollPane;

    private final Text currentFriendOrGroupText;
    private final MessageService messageService;
    private final GroupService groupService;
    private final GroupStorage groupStorage;
    private final UserStorage userStorage;
    private final UsersService usersService;
    private final Group group;
    private String chatID;
    private final ObservableList<Message> messages = FXCollections.observableArrayList();
    Provider<EventListener> eventListener;

    public MessagesBoxController(MessageService messageService, GroupService groupService, Provider<EventListener> eventListener, UsersService usersService, GroupStorage groupStorage, UserStorage userStorage, User user, Group group, VBox chatViewVBox, ScrollPane chatScrollPane, Text currentFriendOrGroupText) {
        this.messageService = messageService;
        this.groupService = groupService;
        this.eventListener = eventListener;
        this.usersService = usersService;
        this.groupStorage = groupStorage;
        this.userStorage = userStorage;
        this.user = user;
        this.group = group;
        this.chatViewVBox = chatViewVBox;
        this.chatScrollPane = chatScrollPane;
        this.currentFriendOrGroupText = currentFriendOrGroupText;
    }
    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void init() {
    }

    @Override
    public Parent render() {
        Parent parent = new Parent() {
        };
        if (group == null) {
            openFriendChat("userListView");
        }
        if (user == null) {
            openFriendChat("groupListView");
        }

        return parent;
    }

    private void openFriendChat(String origin) {

        final Group chat;
        if (origin.equals("userListView")) {
            chat = new Group(null, null, List.of(user._id(), userStorage.get_id()));
        } else if (origin.equals("groupListView")) {
            chat = group;
        } else {
            chat = new Group(null, null, List.of(groupStorage.get_id(), userStorage.get_id()));
        }

        disposables.add(groupService.getGroups(chat.membersToString())
                .observeOn(FX_SCHEDULER).subscribe(gotGroups -> {
                    if (chat.name() == null) {
                        for (Group group : gotGroups) {
                            System.out.println("for-loop");
                            if (group.name() == null) {
                                System.out.println("group.name() == null");
                                System.out.println(group._id());
                                chatID = group._id();
                                break;
                            }
                            chatID = null;
                        }
                    } else {
                        chatID = chat._id();
                    }

                    if (origin.equals("userListView")) {
                        this.currentFriendOrGroupText.setText(user.name());
                    } else if (origin.equals("groupListView")) {
                        this.currentFriendOrGroupText.setText(chat.name());
                    } else {
                        this.currentFriendOrGroupText.setText(groupStorage.getName());
                    }
                    chatViewVBox.getChildren().clear();

                    System.out.println(chatID);
                    if (chatID != null) {
                        groupStorage.set_id(chatID);

                        disposables.add(messageService.getGroupMessages(chatID)
                                .observeOn(FX_SCHEDULER).subscribe(messages -> {
                                    chatViewVBox.getChildren().clear();
                                    this.messages.setAll(messages);

                                    for (Message message : messages) {
                                        chatViewVBox.getChildren().add(this.createMessageNode(message));
                                    }
                                    System.out.println(chatID);
                                    System.out.println(eventListener.get());
                                    if (!isInitialized) {
                                        disposables.add(eventListener.get()
                                                .listen("groups." + chatID + ".messages.*.*", Message.class)
                                                .observeOn(FX_SCHEDULER)
                                                .subscribe(messageEvent -> {
                                                    System.out.println(messageEvent);
                                                    final Message message = messageEvent.data();
                                                    switch (messageEvent.suffix()) {
                                                        case "created":
                                                            chatViewVBox.getChildren().add(createMessageNode(message));
                                                            chatScrollPane.setVvalue(1.0);
                                                            break;
                                                        case "updated":
                                                            for (Node messageNode : chatViewVBox.getChildren()) {
                                                                if (Objects.equals(messageNode.getId(), message._id())) {
                                                                    VBox messageVBox = (VBox) messageNode;
                                                                    HBox messageValueAreaContainer = (HBox) messageVBox.getChildren().get(0);
                                                                    VBox messageValueArea = (VBox) messageValueAreaContainer.getChildren().get(0);
                                                                    Text messageValueText = (Text) messageValueArea.getChildren().get(0);

                                                                    messageValueText.setText(message.body());

                                                                    HBox messageInfoArea = (HBox) messageVBox.getChildren().get(1);
                                                                    Label edited = (Label) messageInfoArea.getChildren().get(1);
                                                                    edited.setVisible(true);
                                                                }
                                                            }
                                                            break;
                                                        case "deleted":
                                                            chatViewVBox.getChildren().removeIf(
                                                                    node -> Objects.equals(node.getId(), message._id())
                                                            );
                                                            break;
                                                    }
                                                }));
                                        isInitialized = true;
                                    }
                                }));
                    }
                }));
    }

    public VBox createMessageNode(Message message) {
        VBox newMessageNode = new VBox();
        HBox newMessageValueArea = new HBox();
        HBox newMessageInfoArea = new HBox();

        // @Cheng, so you can call delete and edit functionality on this message
        newMessageNode.setId(message._id());

        newMessageValueArea.maxWidthProperty().bind(chatViewVBox.widthProperty().map(number -> number.intValue() / 2 - 20));
        newMessageInfoArea.maxWidthProperty().bind(chatViewVBox.widthProperty().map(number -> number.intValue() / 2 - 20));

        boolean userIsSender = message.sender().equals(userStorage.get_id());
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
        Label edited = new Label();
        edited.setText("\u270F");

        if (userIsSender) {
            authorAndTime.setText(userStorage.getName() +
                    " " +
                    formatTimeString(message.createdAt()) +
                    " ");

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
                    " ");
        }

        edited.setVisible(!Objects.equals(message.updatedAt(), message.createdAt()));

        newMessageInfoArea.getChildren().add(authorAndTime);
        newMessageInfoArea.getChildren().add(edited);

        Button editButton = new Button("\u270F");
        editButton.setOnAction(event -> {
            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("Edit message");
            dialog.setHeaderText(null);
            dialog.setContentText("Message:");

            TextArea messageArea = new TextArea();
            messageArea.setText(message.body());
            messageArea.setPrefRowCount(4);

            dialog.getDialogPane().setContent(messageArea);
            Button ok = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
            ok.setOnAction( event1 -> {
                disposables.add(messageService.updateMessage(chatID, message._id(), messageArea.getText(), MESSAGE_NAMESPACE_GROUPS)
                        .observeOn(FX_SCHEDULER).subscribe());
            });
            dialog.showAndWait();
        });

        Button deleteButton = new Button("\uD83D\uDDD1");
        deleteButton.setOnAction(event -> {
            Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
            deleteAlert.setTitle("Confirm delete");
            deleteAlert.setHeaderText(null);
            deleteAlert.setContentText("Do you really want to delete this message?");

            deleteAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    disposables.add(messageService.deleteMessage(message._id(), chatID, Constants.MESSAGE_NAMESPACE_GROUPS)
                            .observeOn(FX_SCHEDULER).subscribe());
                }
                deleteAlert.close();
            });
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
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.parse(dateTime), ZoneId.of("Europe/Berlin"));

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("HH:mm, dd.MM.yy");
        return localDateTime.format(outputFormatter);
    }
}
