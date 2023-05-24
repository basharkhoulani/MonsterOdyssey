package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.dto.Group;
import de.uniks.stpmon.team_m.dto.Message;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.GroupService;
import de.uniks.stpmon.team_m.service.MessageService;
import de.uniks.stpmon.team_m.utils.GroupStorage;
import de.uniks.stpmon.team_m.utils.UserStorage;
import de.uniks.stpmon.team_m.ws.EventListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import javax.inject.Provider;
import java.util.List;

import static de.uniks.stpmon.team_m.Constants.*;

public class MessagesBoxController extends Controller {

    private final MessageService messageService;
    private final GroupService groupService;
    private final GroupStorage groupStorage;
    private final UserStorage userStorage;
    private final List<User> allUsers;
    private final Provider<EventListener> eventListener;
    private final ObservableList<Message> messages = FXCollections.observableArrayList();
    private final User user;
    private Group group;
    private String chatID;
    private ListView<Message> messageListView;

    /**
     * For every group and friend chat opened, a new instance of this class is created. This solves the problem of
     * multiple server calls when switching between chats.
     *
     * @param messageService The {@link MessageService} to use for sending and receiving messages
     * @param groupService   The {@link GroupService} to use for receiving groups and group members
     * @param eventListener  The {@link EventListener} to use for receiving messages
     * @param groupStorage   The {@link GroupStorage} to use for storing group ids
     * @param userStorage    The {@link UserStorage} to use for storing user ids
     * @param allUsers       The {@link List} of all users
     * @param user           The {@link User} to check if the chat is with a friend
     * @param group          The {@link Group} to check if the chat is with a group
     */

    public MessagesBoxController(MessageService messageService, GroupService groupService,
                                 Provider<EventListener> eventListener, GroupStorage groupStorage,
                                 UserStorage userStorage, List<User> allUsers,
                                 User user, Group group) {
        this.messageService = messageService;
        this.groupService = groupService;
        this.eventListener = eventListener;
        this.groupStorage = groupStorage;
        this.userStorage = userStorage;
        this.allUsers = allUsers;
        this.user = user;
        this.group = group;
    }

    /**
     * Initializes the messages box.
     * Sets the messageListView and the placeholder.
     */

    @Override
    public void init() {
        messageListView = new ListView<>(messages);
        messageListView.setCellFactory(param -> new MessageCell(this, userStorage.get_id()));
        messageListView.setPlaceholder(new Label(NO_MESSAGES_YET));
        if (group == null) {
            openFriendChat("userListView");
        }
        if (user == null) {
            openFriendChat("groupListView");
        }
    }

    /**
     * Opens a chat with a friend or a group. If the chat is with a friend, the group is created and the chatID is set.
     * If the chat is with a group, the chatID is set.
     * The messages are loaded and the listener is set.
     *
     * @param origin The origin of the chat
     */

    private void openFriendChat(String origin) {
        if (!origin.equals("groupListView")) {
            group = new Group(null, null, List.of(user._id(), userStorage.get_id()));
        }
        disposables.add(groupService.getGroups(group.membersToString()).observeOn(FX_SCHEDULER).subscribe(groups -> {
            if (group.name() == null) {
                groups.stream().filter(g -> g.name() == null).findFirst().ifPresent(g -> group = g);
            } else {
                chatID = group._id();
            }
            if (chatID != null) {
                groupStorage.set_id(chatID);
                disposables.add(messageService.getGroupMessages(chatID)
                        .observeOn(FX_SCHEDULER).subscribe(messages -> {
                            this.messages.setAll(messages);
                            listenToMessages(this.messages, chatID);
                        }, error -> showError(error.getMessage())));
            }
        }, error -> showError(error.getMessage())));
    }

    /**
     * Renders the message listview.
     *
     * @return The message listview
     */

    @Override
    public Parent render() {
        groupStorage.set_id(chatID);
        messageListView.scrollTo(messageListView.getItems().size() - 1);
        return messageListView;
    }

    /**
     * Search for the username of the user with the given id.
     *
     * @param _id The id of the user
     * @return The username of the user
     */

    public String getUsername(String _id) {
        for (User user : allUsers) {
            if (user._id().equals(_id)) {
                return user.name();
            }
        }
        return UNKNOWN_USER;
    }

    /**
     * Updates the message on the server.
     *
     * @param newBody The new message body
     * @param message The message to update
     */

    public void editMessage(String newBody, Message message) {
        disposables.add(messageService.updateMessage(groupStorage.get_id(), message._id(), newBody, MESSAGE_NAMESPACE_GROUPS)
                .observeOn(FX_SCHEDULER).subscribe(event -> {
                }, error -> showError(error.getMessage())));
    }

    /**
     * Deletes a message from the server.
     *
     * @param item The message to delete
     */

    public void deleteMessage(Message item) {
        disposables.add(messageService.deleteMessage(item._id(), groupStorage.get_id(), MESSAGE_NAMESPACE_GROUPS)
                .observeOn(FX_SCHEDULER).subscribe(event -> {
                }, error -> showError(error.getMessage())));
    }

    /**
     * Listens to messages from the server. If a message is created, updated or deleted, the listview is updated.
     *
     * @param messages The list of messages
     * @param id       The id of the chat
     */

    public void listenToMessages(ObservableList<Message> messages, String id) {
        disposables.add(eventListener.get().listen("groups." + id + ".messages.*.*", Message.class)
                .observeOn(FX_SCHEDULER).subscribe(event -> {
                    final Message message = event.data();
                    switch (event.suffix()) {
                        case "created" -> messages.add(message);
                        case "updated" -> updateMessage(messages, message);
                        case "deleted" -> messages.removeIf(m -> m._id().equals(message._id()));
                    }
                }));
    }

    /**
     * Updates the message in the listview.
     *
     * @param messages The list of messages
     * @param message  The message to update
     */

    private void updateMessage(ObservableList<Message> messages, Message message) {
        String messageID = message._id();
        messages.stream()
                .filter(m -> m._id().equals(messageID))
                .findFirst()
                .ifPresent(m -> messages.set(messages.indexOf(m), message));
    }

}
