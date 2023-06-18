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
import io.reactivex.rxjava3.core.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;

import static de.uniks.stpmon.team_m.Constants.MESSAGE_NAMESPACE_GROUPS;

public class MessagesBoxController extends Controller {

    @Inject
    MessageService messageService;
    @Inject
    GroupService groupService;
    @Inject
    Provider<GroupStorage> groupStorageProvider;
    @Inject
    Provider<UserStorage> userStorageProvider;
    @Inject
    Provider<EventListener> eventListener;
    private final ObservableList<Message> messages = FXCollections.observableArrayList();
    private List<User> allUsers;
    private User user;
    private Group group;
    private String chatID;
    private ListView<Message> messageListView;

    /**
     * For every group and friend chat opened, a new instance of this class is created. This solves the problem of
     * multiple server calls when switching between chats.
     */

    @Inject
    public MessagesBoxController() {
    }

    /**
     * Initializes the messages box.
     * Sets the messageListView and the placeholder.
     */

    @Override
    public void init() {
        messageListView = new ListView<>();
        messageListView.setItems(messages);
        messageListView.setCellFactory(param -> new MessageCell(resourceBundleProvider, this, userStorageProvider.get()));
        messageListView.setPlaceholder(new Label(resources.getString("NO.MESSAGES.YET")));
        messageListView.setId("messageListView");
        if (group == null) {
            openFriendChat("userListView");
        }
        if (user == null) {
            openFriendChat("groupListView");
        }
        messageListView.setFocusTraversable(false);
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
            group = new Group(null, null, List.of(user._id(), userStorageProvider.get().get_id()));
        }

        disposables.add(groupService.getGroups(group.membersToString()).doOnNext(groups -> {
                    findGroupID(groups);
                    groupStorageProvider.get().set_id(chatID);
                }).flatMap(groups -> messageService.getGroupMessages(chatID).observeOn(FX_SCHEDULER))
                .doOnNext(this.messages::setAll)
                .flatMap(messages -> Observable.just(messages).observeOn(FX_SCHEDULER))
                .doOnNext(messages1 -> listenToMessages(this.messages, chatID))
                .observeOn(FX_SCHEDULER).subscribe(event -> {
                }, error -> showError(error.getMessage())));
    }

    /**
     * Finds the groupID of the chat.
     *
     * @param groups The list of groups
     */

    private void findGroupID(List<Group> groups) {
        if (group.name() == null) {
            for (Group group : groups) {
                if (group.members().contains(user._id()) && group.members().contains(userStorageProvider.get().get_id())) {
                    this.group = group;
                    chatID = group._id();
                    break;
                }
            }
        } else {
            chatID = group._id();
        }
    }

    /**
     * Renders the message listview.
     *
     * @return The message listview
     */

    @Override
    public Parent render() {
        groupStorageProvider.get().set_id(chatID);
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
        return resourceBundleProvider.get().getString("Unknown");
    }

    /**
     * Search for the avatar of the user with the given id.
     *
     * @param _id The id of the user
     * @return The avatar of the user
     */

    public String getAvatar(String _id) {
        for (User user : allUsers) {
            if (user._id().equals(_id)) {
                return user.avatar();
            }
        }
        return resourceBundleProvider.get().getString("Unknown");
    }

    /**
     * Updates the message on the server.
     *
     * @param newBody The new message body
     * @param message The message to update
     */

    public void editMessage(String newBody, Message message) {
        disposables.add(messageService.updateMessage(groupStorageProvider.get().get_id(), message._id(), newBody, MESSAGE_NAMESPACE_GROUPS)
                .observeOn(FX_SCHEDULER).subscribe(event -> {
                }, error -> showError(error.getMessage())));
    }

    /**
     * Deletes a message from the server.
     *
     * @param item The message to delete
     */

    public void deleteMessage(Message item) {
        disposables.add(messageService.deleteMessage(item._id(), groupStorageProvider.get().get_id(), MESSAGE_NAMESPACE_GROUPS)
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
                }, error -> showError(error.getMessage())));
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

    /**
     * Sets all users
     *
     * @param allUsers The list of all users
     */

    public void setAllUsers(List<User> allUsers) {
        this.allUsers = allUsers;
    }

    /**
     * Sets the user
     *
     * @param user The {@link User}
     */

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Sets the group
     *
     * @param group The {@link Group}
     */

    public void setGroup(Group group) {
        this.group = group;
    }

    public ObservableList<Message> getMessages() {
        return messages;
    }
}
