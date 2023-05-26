package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.controller.subController.GroupCell;
import de.uniks.stpmon.team_m.controller.subController.MessagesBoxController;
import de.uniks.stpmon.team_m.controller.subController.UserCell;
import de.uniks.stpmon.team_m.dto.Group;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.*;
import de.uniks.stpmon.team_m.utils.FriendListUtils;
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
    public Text currentFriendOrGroupText; //needs to be set each time a different chat is selected
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
    @FXML
    public ListView<User> userListView;
    @FXML
    public ListView<Group> groupListView;

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
    @Inject
    MessageService messageService;
    @Inject
    GroupService groupService;
    @Inject
    Preferences preferences;
    private final ObservableList<User> friends = FXCollections.observableArrayList();
    private final ObservableList<Group> groups = FXCollections.observableArrayList();
    private final ObservableList<Group> groupsToAdd = FXCollections.observableArrayList();
    private final List<Controller> subControllers = new ArrayList<>();
    Map<User, MessagesBoxController> messagesBoxControllerUserMap = new HashMap<>();
    Map<Group, MessagesBoxController> messagesBoxControllerGroupMap = new HashMap<>();
    private List<User> allUsers;

    @Inject
    public MessagesController() {
    }

    @Override
    public void init() {
        disposables.add(usersService.getUsers(null, null).observeOn(FX_SCHEDULER)
                .subscribe(users -> {
                    allUsers = users;
                    if (!userStorageProvider.get().getFriends().isEmpty()) {
                        disposables.add(usersService.getUsers(userStorageProvider.get().getFriends(), null).observeOn(FX_SCHEDULER)
                                .subscribe(friends -> {
                                    this.friends.setAll(friends);
                                    userListView.getItems().setAll(friends);
                                    FriendListUtils.sortListView(userListView);
                                    for (User user : friends) {
                                        if (user._id().equals(groupStorageProvider.get().get_id())) {
                                            openPrivateChat(user);
                                            currentFriendOrGroupText.setText(user.name());
                                        }
                                    }
                                }, error -> showError(error.getMessage())));
                    }
                    groupListView.getItems().clear();
                    disposables.add(groupService.getGroups(null).observeOn(FX_SCHEDULER).subscribe(groups -> groups.stream().filter(this::groupFilter).forEach(group -> {
                        groupListView.getItems().add(group);
                        if (group.members().size() == 2 && group.name() == null) {
                            for (String id : group.members()) {
                                if (!id.equals(userStorageProvider.get().get_id())) {
                                    for (User user : allUsers) {
                                        if (user._id().equals(id)) {
                                            this.groups.add(new Group(group._id(), user.name(), group.members()));
                                        }
                                    }
                                }
                            }
                        } else if (group.members().size() == 1 && group.name() == null) {
                            this.groups.add(new Group(group._id(), ALONE, group.members()));
                        } else {
                            this.groups.add(group);
                        }
                        if (group._id().equals(groupStorageProvider.get().get_id())) {
                            openGroupChat(group);
                            currentFriendOrGroupText.setText(groupStorageProvider.get().getName());
                        }
                    }), error -> showError(error.getMessage())));
                }, error -> showError(error.getMessage())));

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
        userListView.setPlaceholder(new Label(NO_FRIENDS_FOUND));
        userListView.setCellFactory(param -> new UserCell(preferences));

        listenToUserUpdate(friends, userListView);

        groupListView.setCellFactory(param -> new GroupCell());
        groupListView.setPlaceholder(new Label(NO_GROUPS_FOUND));
        listenToGroupChanges();

        settingsButton.setVisible(false);
        //friendsListViewVBox.getChildren().add(userListView);
        //groupsListViewVBox.getChildren().add(groupListView);
        messageTextArea.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER && !event.isShiftDown()) {
                event.consume();
                this.onSendMessage();
            }
        });
        userListView.setOnMouseClicked(event -> {
            if (!(userListView.getSelectionModel().getSelectedItem() == null)) {
                settingsButton.setVisible(false);
                openPrivateChat(userListView.getSelectionModel().getSelectedItem());
                currentFriendOrGroupText.setText(userListView.getSelectionModel().getSelectedItem().name());
            }
        });

        groupListView.setOnMouseClicked(event -> {
            if (!(groupListView.getSelectionModel().getSelectedItem() == null)) {
                settingsButton.setVisible(true);
                openGroupChat(groupListView.getSelectionModel().getSelectedItem());
                currentFriendOrGroupText.setText(groupListView.getSelectionModel().getSelectedItem().name());
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
            MessagesBoxController messagesBoxController = new MessagesBoxController(
                    messageService,
                    groupService,
                    eventListenerProvider,
                    groupStorageProvider.get(),
                    userStorageProvider.get(),
                    allUsers,
                    user,
                    null
            );
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
            MessagesBoxController messagesBoxController = new MessagesBoxController(
                    messageService,
                    groupService,
                    eventListenerProvider,
                    groupStorageProvider.get(),
                    userStorageProvider.get(),
                    allUsers,
                    null,
                    group
            );
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
        if (groupID == null) {
            return;
        }

        String messageBody = messageTextArea.getText();
        disposables.add(messageService.newMessage(groupID, messageBody, MESSAGE_NAMESPACE_GROUPS)
                .observeOn(FX_SCHEDULER).subscribe(
                        message -> {
                        }, Throwable::printStackTrace
                ));
        messageTextArea.setText("");
    }

    public void listenToGroupChanges() {
        disposables.add(eventListenerProvider.get().listen("groups.*.*", Group.class).observeOn(FX_SCHEDULER)
                .subscribe(groupEvent -> {
                    final Group group = groupEvent.data();
                    switch (groupEvent.suffix()) {
                        case "created" -> groups.add(group);
                        case "updated" -> updateGroup(group);
                        case "deleted" -> groups.removeIf(g -> g._id().equals(group._id()));
                    }
                }, error -> showError(error.getMessage())));
    }

    private void updateGroup(Group group) {
        Group groupToUpdate = groups.stream()
                .filter(g -> g._id().equals(group._id()))
                .findFirst()
                .orElse(null);
        if (groupToUpdate != null) {
            groupListView.getItems().set(groupListView.getItems().indexOf(groupToUpdate), group);
        } else {
            groups.remove(group);
        }
    }

}
