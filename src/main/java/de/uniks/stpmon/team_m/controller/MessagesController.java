package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.controller.subController.GroupCell;
import de.uniks.stpmon.team_m.controller.subController.MessagesBoxController;
import de.uniks.stpmon.team_m.controller.subController.UserCell;
import de.uniks.stpmon.team_m.dto.Group;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.GroupService;
import de.uniks.stpmon.team_m.service.MessageService;
import de.uniks.stpmon.team_m.service.UsersService;
import de.uniks.stpmon.team_m.utils.FriendListUtils;
import de.uniks.stpmon.team_m.utils.GroupStorage;
import de.uniks.stpmon.team_m.utils.UserStorage;
import io.reactivex.rxjava3.core.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

import static de.uniks.stpmon.team_m.Constants.*;

public class MessagesController extends Controller {

    @FXML
    public Text currentFriendOrGroupText;
    @FXML
    public Label friendsAndGroupText;
    @FXML
    public VBox friendsListViewVBox;
    @FXML
    public VBox groupsListViewVBox;
    @FXML
    public Button findNewFriendsButton;
    @FXML
    public Button newGroupButton;
    @FXML
    public Button mainMenuButton;
    @FXML
    public Button settingsButton;
    @FXML
    public TextArea messageTextArea;
    @FXML
    public Button sendButton;
    @FXML
    public VBox chatViewVBox;
    @FXML
    public ScrollPane chatScrollPane;
    @Inject
    Provider<MainMenuController> mainMenuControllerProvider;
    @Inject
    Provider<NewFriendController> newFriendControllerProvider;
    @Inject
    Provider<GroupController> groupControllerProvider;
    @Inject
    Provider<MessagesBoxController> messagesBoxControllerProvider;
    @Inject
    Provider<UserStorage> userStorageProvider;
    @Inject
    Provider<GroupStorage> groupStorageProvider;
    @Inject
    UsersService usersService;
    @Inject
    MessageService messageService;
    @Inject
    GroupService groupService;
    @Inject
    Preferences preferences;
    private final ObservableList<User> friends = FXCollections.observableArrayList();
    private final ObservableList<Group> groups = FXCollections.observableArrayList();
    private final ObservableList<Group> groupsToAdd = FXCollections.observableArrayList();
    private final ObservableList<User> allUsers = FXCollections.observableArrayList();
    private ListView<User> userListView;
    private ListView<Group> groupListView;
    private final List<Controller> subControllers = new ArrayList<>();
    Map<User, MessagesBoxController> messagesBoxControllerUserMap = new HashMap<>();
    Map<Group, MessagesBoxController> messagesBoxControllerGroupMap = new HashMap<>();
    private boolean userChosenFromMainMenu;

    @Inject
    public MessagesController() {
    }

    @Override
    public void init() {
        initializeFriendsList();
        initializeGroupsList();
        setContentFriendsAndGroupsList();
    }

    private void setContentFriendsAndGroupsList() {
        disposables.add(usersService.getUsers(null, null)
                .doOnNext(allUsers::setAll).flatMap(users -> getListOfFriends().observeOn(FX_SCHEDULER))
                .doOnNext(friends -> {
                    this.friends.setAll(friends);
                    FriendListUtils.sortListView(userListView);
                    if (userChosenFromMainMenu) {
                        selectUserChosenFromMainMenu();
                    }
                }).flatMap(friends -> groupService.getGroups(null).observeOn(FX_SCHEDULER))
                .doOnNext(groups -> groups.stream().filter(this::groupFilter).forEach(this::setGroups))
                .observeOn(FX_SCHEDULER).subscribe(event -> {
                }, error -> showError(error.getMessage())));
    }

    private void setGroups(Group group) {
        if (group.members().size() == 2 && group.name() == null) {
            for (String id : group.members()) {
                if (!id.equals(userStorageProvider.get().get_id())) {
                    allUsers.stream().filter(u -> u._id().equals(id)).findFirst()
                            .ifPresent(u -> this.groups.add(new Group(group._id(), u.name(), group.members())));
                }
            }
        } else if (group.members().size() == 1 && group.name() == null) {
            this.groups.add(new Group(group._id(), ALONE, group.members()));
        } else {
            this.groups.add(group);
        }
        if (group._id().equals(groupStorageProvider.get().get_id())) {
            groupListView.getSelectionModel().select(group);
            groupListView.scrollTo(group);
        }
    }

    private void selectUserChosenFromMainMenu() {
        userListView.getItems().stream()
                .filter(user -> user._id().equals(groupStorageProvider.get().get_id()))
                .findFirst()
                .ifPresent(user -> {
                    userListView.getSelectionModel().select(user);
                    userListView.scrollTo(user);
                });
        userChosenFromMainMenu = false;
    }

    private Observable<List<User>> getListOfFriends() {
        List<String> friends = userStorageProvider.get().getFriends();
        if (friends.isEmpty()) {
            return Observable.empty();
        } else {
            return usersService.getUsers(friends, null);
        }
    }

    private void initializeGroupsList() {
        groupListView = new ListView<>(groups);
        groupListView.setId("groups");
        groupListView.setCellFactory(param -> new GroupCell());
        groupListView.setPlaceholder(new Label(NO_GROUPS_FOUND));
        listenToGroupChanges();
    }

    private void initializeFriendsList() {
        userListView = new ListView<>(friends);
        userListView.setId("friends");
        userListView.setPlaceholder(new Label(NO_FRIENDS_FOUND));
        userListView.setCellFactory(param -> new UserCell(preferences));
        listenToUserUpdate(friends, userListView);
    }

    private boolean groupFilter(Group group) {
        if (group.members().size() == 2 && group.name() == null) {
            for (String id : userStorageProvider.get().getFriends()) {
                if (group.members().contains(id)) {
                    return false;
                }
            }
            for (String id : group.members()) {
                if (!id.equals(userStorageProvider.get().get_id())) {
                    return true;
                }
            }
        } else if (group.members().size() == 1 && group.name() == null) {
            return true;
        } else return group.name() != null;
        return false;
    }

    @Override
    public String getTitle() {
        return MESSAGES_TITLE;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        settingsButton.setVisible(false);
        friendsListViewVBox.getChildren().add(userListView);
        groupsListViewVBox.getChildren().add(groupListView);
        messageTextArea.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER && !event.isShiftDown()) {
                event.consume();
                this.onSendMessage();
            }
        });
        userListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                settingsButton.setVisible(false);
                openPrivateChat(newValue);
                currentFriendOrGroupText.setText(newValue.name());
                if (groupListView.getSelectionModel().getSelectedItem() != null) {
                    groupListView.getSelectionModel().clearSelection();
                }
            }
        });
        groupListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                settingsButton.setVisible(true);
                openGroupChat(newValue);
                currentFriendOrGroupText.setText(newValue.name());
                if (userListView.getSelectionModel().getSelectedItem() != null) {
                    userListView.getSelectionModel().clearSelection();
                }
            }
        });
        if (groupStorageProvider.get().get_id() != null) {
            for (User user : friends) {
                if (user._id().equals(groupStorageProvider.get().get_id())) {
                    openPrivateChat(user);
                }
            }
            for (Group group : groupsToAdd) {
                if (group._id().equals(groupStorageProvider.get().get_id())) {
                    openGroupChat(group);
                }
            }
        }
        return parent;

    }

    @Override
    public void destroy() {
        super.destroy();
        subControllers.forEach(Controller::destroy);
        subControllers.clear();
        messagesBoxControllerUserMap.clear();
        messagesBoxControllerGroupMap.clear();
    }

    public void openPrivateChat(User user) {

        try {
            chatScrollPane.setContent(messagesBoxControllerUserMap.get(user).render());
        } catch (Exception ignored) {
            MessagesBoxController messagesBoxController = messagesBoxControllerProvider.get();
            messagesBoxController.setUser(user);
            messagesBoxController.setGroup(null);
            messagesBoxController.setAllUsers(allUsers);
            messagesBoxController.init();
            messagesBoxControllerUserMap.put(user, messagesBoxController);
            subControllers.add(messagesBoxController);
            chatScrollPane.setContent(messagesBoxControllerUserMap.get(user).render());
        }
    }

    public void openGroupChat(Group group) {
        groupStorageProvider.get().set_id(group._id());
        groupStorageProvider.get().setName(group.name());
        groupStorageProvider.get().setMembers(group.members());
        try {
            chatScrollPane.setContent(messagesBoxControllerGroupMap.get(group).render());
        } catch (Exception ignored) {
            MessagesBoxController messagesBoxController = messagesBoxControllerProvider.get();
            messagesBoxController.setUser(null);
            messagesBoxController.setGroup(group);
            messagesBoxController.setAllUsers(allUsers);
            messagesBoxController.init();
            messagesBoxControllerGroupMap.put(group, messagesBoxController);
            subControllers.add(messagesBoxController);
            chatScrollPane.setContent(messagesBoxControllerGroupMap.get(group).render());
        }
    }

    public void changeToMainMenu() {
        destroy();
        app.show(mainMenuControllerProvider.get());
    }

    public void changeToFindNewFriends() {
        destroy();
        app.show(newFriendControllerProvider.get());
    }

    public void changeToNewGroup() {
        groupStorageProvider.get().set_id(EMPTY_STRING);
        destroy();
        app.show(groupControllerProvider.get());
    }

    public void changeToSettings() {
        if (groupListView.getSelectionModel().getSelectedItem() != null) {
            groupStorageProvider.get().set_id(groupListView.getSelectionModel().getSelectedItem()._id());
            destroy();
            app.show(groupControllerProvider.get());
        }
    }

    public void onSendMessage() {
        String groupID = groupStorageProvider.get().get_id();
        if (groupID != null) {
            String messageBody = messageTextArea.getText();
            disposables.add(messageService.newMessage(groupID, messageBody, MESSAGE_NAMESPACE_GROUPS)
                    .observeOn(FX_SCHEDULER).subscribe(message -> {
                    }, error -> showError(error.getMessage())));
            messageTextArea.setText(EMPTY_STRING);
        }
    }

    public void listenToGroupChanges() {
        disposables.add(eventListenerProvider.get().listen("groups.*.*", Group.class).observeOn(FX_SCHEDULER)
                .subscribe(groupEvent -> {
                    final Group group = groupEvent.data();
                    switch (groupEvent.suffix()) {
                        case "created" -> groups.add(group);
                        case "updated" -> updateGroup(group);
                        case "deleted" -> groups.remove(group);
                    }
                }, error -> showError(error.getMessage())));
    }

    private void updateGroup(Group group) {
        groups.removeIf(g -> g._id().equals(group._id()));
        if (group.members().contains(userStorageProvider.get().get_id())) {
            groups.add(group);
        }
    }

    public void setUserChosenFromMainMenu(boolean userChosenFromMainMenu) {
        this.userChosenFromMainMenu = userChosenFromMainMenu;
    }
}
