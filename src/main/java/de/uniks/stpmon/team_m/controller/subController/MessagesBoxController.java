package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.dto.Group;
import de.uniks.stpmon.team_m.dto.Message;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.GroupService;
import de.uniks.stpmon.team_m.service.GroupStorage;
import de.uniks.stpmon.team_m.service.MessageService;
import de.uniks.stpmon.team_m.service.UserStorage;
import de.uniks.stpmon.team_m.ws.EventListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;

import javax.inject.Provider;
import java.util.List;

import static de.uniks.stpmon.team_m.Constants.MESSAGE_NAMESPACE_GROUPS;
import static de.uniks.stpmon.team_m.Constants.UNKNOWN_USER;

public class MessagesBoxController extends Controller {

    private final User user;
    private final MessageService messageService;
    private final GroupService groupService;
    private final GroupStorage groupStorage;
    private final UserStorage userStorage;
    private final Group group;
    private final List<User> allUsers;
    private String chatID;
    private final ObservableList<Message> messages = FXCollections.observableArrayList();
    Provider<EventListener> eventListener;
    private ListView<Message> messageListView;

    public MessagesBoxController(MessageService messageService, GroupService groupService, Provider<EventListener> eventListener, GroupStorage groupStorage, UserStorage userStorage, List<User> allUsers, User user, Group group) {
        this.messageService = messageService;
        this.groupService = groupService;
        this.eventListener = eventListener;
        this.groupStorage = groupStorage;
        this.userStorage = userStorage;
        this.allUsers = allUsers;
        this.user = user;
        this.group = group;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void init() {
        messageListView = new ListView<>(messages);
        messageListView.setCellFactory(param -> new MessageCell(this, userStorage.get_id()));
        messageListView.setPlaceholder(new Label("\t\tNo messages yet\n" +
                "or there is no user or group selected"));
        if (group == null) {
            openFriendChat("userListView");
        }
        if (user == null) {
            openFriendChat("groupListView");
        }
        messageListView.setFocusTraversable(false);
    }

    @Override
    public Parent render() {
        groupStorage.set_id(chatID);
        messageListView.scrollTo(messageListView.getItems().size() - 1);
        return messageListView;
    }

    private void openFriendChat(String origin) {

        final Group chat;
        if (origin.equals("userListView")) {
            chat = new Group(null, null, List.of(user._id(), userStorage.get_id()));
        } else if (origin.equals("groupListView")) {
            chat = group;
        } else {
            chat = new Group(null, null, List.of(user._id(), userStorage.get_id()));
        }

        disposables.add(groupService.getGroups(chat.membersToString())
                .observeOn(FX_SCHEDULER).subscribe(gotGroups -> {
                    if (chat.name() == null) {
                        for (Group group : gotGroups) {
                            if (group.name() == null) {
                                chatID = group._id();
                                break;
                            }
                            chatID = null;
                        }
                    } else {
                        chatID = chat._id();
                    }

                    if (chatID != null) {
                        groupStorage.set_id(chatID);

                        disposables.add(messageService.getGroupMessages(chatID)
                                .observeOn(FX_SCHEDULER).subscribe(messages -> {
                                    this.messages.setAll(messages);
                                    listenToMessages(messageListView, this.messages, chatID);
                                    messageListView.scrollTo(messageListView.getItems().size() - 1);
                                }, error -> showError(error.getMessage())));
                    }
                }, error -> showError(error.getMessage())));
    }

    public String getUsername(String _id) {
        for (User user : allUsers) {
            if (user._id().equals(_id)) {
                return user.name();
            }
        }
        return UNKNOWN_USER;
    }

    public void editMessage(TextInputDialog dialog, String newBody, Message message) {
        disposables.add(messageService.updateMessage(groupStorage.get_id(), message._id(), newBody, MESSAGE_NAMESPACE_GROUPS)
                .observeOn(FX_SCHEDULER).subscribe(event -> {
                }, error -> showError(error.getMessage())));
        dialog.close();
    }

    public void deleteMessage(Alert alert, Message item) {
        disposables.add(messageService.deleteMessage(item._id(), groupStorage.get_id(), MESSAGE_NAMESPACE_GROUPS)
                .observeOn(FX_SCHEDULER).subscribe(event -> {
                }, error -> showError(error.getMessage())));
        alert.close();
    }

    public void listenToMessages(ListView<Message> messageListView, ObservableList<Message> messages, String id) {
        disposables.add(eventListener.get().listen("groups." + id + ".messages.*.*", Message.class)
                .observeOn(FX_SCHEDULER).subscribe(event -> {
                    final Message message = event.data();
                    switch (event.suffix()) {
                        case "created" -> {
                            messages.add(message);
                            messageListView.scrollTo(message);
                        }
                        case "updated" -> {
                            String messageID = message._id();
                            messageListView.getItems().stream()
                                    .filter(m -> m._id().equals(messageID))
                                    .findFirst()
                                    .ifPresent(m -> messageListView.getItems().set(messageListView.getItems().indexOf(m), message));
                        }
                        case "deleted" -> messages.removeIf(m -> m._id().equals(message._id()));
                    }
                }));
    }


}
