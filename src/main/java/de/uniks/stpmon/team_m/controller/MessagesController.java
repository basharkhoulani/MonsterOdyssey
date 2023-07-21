package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.inject.Inject;
import javax.inject.Provider;
import java.awt.*;
import java.util.List;
import java.util.*;
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
    @FXML
    public ListView<User> userListView;
    @FXML
    public ListView<Group> groupListView;
    @FXML
    public ImageView messagesMonsterWithoutImageView;
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
    private final ObservableList<User> allUsers = FXCollections.observableArrayList();
    private final List<Controller> subControllers = new ArrayList<>();
    final Map<User, MessagesBoxController> messagesBoxControllerUserMap = new HashMap<>();
    final Map<Group, MessagesBoxController> messagesBoxControllerGroupMap = new HashMap<>();
    private boolean userChosenFromMainMenu;
    private boolean userChosenFromNewFriend;


    /**
     * MessagesController handles the chatting system with friends and groups of users. The friends and groups are
     * separated in two list view. The user can choose a friend or group to chat with.
     */

    @Inject
    public MessagesController() {
    }

    /**
     * Initializes the MessagesController. The friends and groups list view are initialized and the content of them is set.
     */

    @Override
    public void init() {
        setContentFriendsAndGroupsList();
    }

    /**
     * Gets a list of friends or groups and displays them in the list view.
     */

    private void setContentFriendsAndGroupsList() {
        disposables.add(usersService.getUsers(null, null).observeOn(FX_SCHEDULER)
                .doOnNext(allUsers::setAll)
                .flatMap(users -> getListOfFriends().observeOn(FX_SCHEDULER))
                .doOnNext(friends -> {
                    this.friends.setAll(friends);
                    FriendListUtils.sortListView(userListView);
                    if (userChosenFromMainMenu) {
                        selectUserChosenFromMainMenu();
                    }
                }).flatMap(friends -> groupService.getGroups(null).observeOn(FX_SCHEDULER))
                .doOnNext(groups -> {
                    filterAndSetGroups(groups);
                    if (userChosenFromNewFriend) {
                        selectGroupChosenFromMainMenu();
                    }
                }).observeOn(FX_SCHEDULER).subscribe(event -> {
                }, error -> showError(error.getMessage())));
    }

    private void selectGroupChosenFromMainMenu() {
        groupListView.getItems().stream().filter(group -> group._id().equals(groupStorageProvider.get().get_id())).findFirst().ifPresent(group -> {
            groupListView.getSelectionModel().select(group);
            groupListView.scrollTo(group);
        });
        userChosenFromNewFriend = false;
    }

    /**
     * This method filters the groups whether the user is alone, with a friend, or a group of users.
     *
     * @param groups List of groups
     */

    private void filterAndSetGroups(List<Group> groups) {
        boolean groupFound = false;
        for (Group group : groups) {
            if (group.members().size() == 2 && group.name() == null) {
                for (String id : userStorageProvider.get().getFriends()) {
                    if (group.members().contains(id)) {
                        groupFound = true;
                        break;
                    }
                }
                for (String id : group.members()) {
                    if (!id.equals(userStorageProvider.get().get_id()) && !groupFound) {
                        allUsers.stream().filter(u -> u._id().equals(id))
                                .findFirst()
                                .ifPresent(u -> this.groups.add(new Group(group._id(), u.name(), group.members())));
                    }
                }
            } else if (group.members().size() == 1 && group.name() == null) {
                this.groups.add(new Group(group._id(), resources.getString("ALONE"), group.members()));
            } else {
                this.groups.add(group);
            }
            groupFound = false;
        }
    }

    /**
     * This method selects the user and open
     * s a chat with them, if the friend was chosen from the main menu.
     */

    private void selectUserChosenFromMainMenu() {
        userListView.getItems().stream().filter(user -> user._id().equals(groupStorageProvider.get().get_id())).findFirst().ifPresent(user -> {
            userListView.getSelectionModel().select(user);
            userListView.scrollTo(user);
        });
        userChosenFromMainMenu = false;
    }

    /**
     * This method gets a list of friends and returns an observable list of users.
     *
     * @return Observable list of friends
     */

    private Observable<List<User>> getListOfFriends() {
        List<String> friends = userStorageProvider.get().getFriends();
        if (!friends.isEmpty()) {
            return usersService.getUsers(friends, null);
        }
        return Observable.just(new ArrayList<>());
    }

    /**
     * This method sets the title of the controller.
     *
     * @return Title of the controller
     */

    @Override
    public String getTitle() {
        return resources.getString("MESSAGES.TITLE");
    }

    /**
     * This method sets the content of the controller and adds listeners to selected property of the list views.
     *
     * @return Parent object
     */

    @Override
    public Parent render() {
        Parent parent = super.render();

        if (!GraphicsEnvironment.isHeadless()) {
            messagesMonsterWithoutImageView.setImage(new Image(Objects.requireNonNull(App.class.getResource(MONSTER1_WITHOUT)).toString()));
        }

        initListViews();
        settingsButton.setVisible(false);
        messageTextArea.addEventHandler(KeyEvent.KEY_PRESSED, this::enterButtonPressedToSend);
        userListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> addListenerOnSelectedFriends(newValue));
        groupListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> addListenerOnSelectedGroups(newValue));
        return parent;

    }

    /**
     * This method enables the user to press Enter to send messages.
     *
     * @param event Key event
     */

    private void enterButtonPressedToSend(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER && !event.isShiftDown()) {
            event.consume();
            this.onSendMessage();
        }
    }

    /**
     * This method is to set the behaviour of a selected group.
     *
     * @param newValue Selected group
     */

    private void addListenerOnSelectedGroups(Group newValue) {
        if (newValue != null) {
            settingsButton.setVisible(true);
            openGroupChat(newValue);
            currentFriendOrGroupText.setText(newValue.name());
            if (userListView.getSelectionModel().getSelectedItem() != null) {
                userListView.getSelectionModel().clearSelection();
            }
        }
    }

    /**
     * This method is to set the behaviour of a selected friend.
     *
     * @param newValue Selected friend
     */

    private void addListenerOnSelectedFriends(User newValue) {
        if (newValue != null) {
            settingsButton.setVisible(false);
            openPrivateChat(newValue);
            currentFriendOrGroupText.setText(newValue.name());
            if (groupListView.getSelectionModel().getSelectedItem() != null) {
                groupListView.getSelectionModel().clearSelection();
            }
        }
    }

    /**
     * This method initializes group and friend list views and listens to changes of groups and friends.
     */

    private void initListViews() {
        userListView.setPlaceholder(new Label(resources.getString("NO.FRIENDS.FOUND")));
        userListView.setCellFactory(param -> new UserCell(preferences));
        userListView.setItems(friends);
        listenToUserUpdate(friends, userListView);

        groupListView.setCellFactory(param -> new GroupCell());
        groupListView.setPlaceholder(new Label(resources.getString("NO.GROUPS.FOUND")));
        groupListView.setItems(groups);
        listenToGroupChanges();
    }

    /**
     * This method destroys the subcontrollers which are MessagesBoxControllers and clears the maps.
     */

    @Override
    public void destroy() {
        super.destroy();
        subControllers.forEach(Controller::destroy);
        subControllers.clear();
        messagesBoxControllerUserMap.clear();
        messagesBoxControllerGroupMap.clear();
    }

    /**
     * This method creates a subcontroller for the selected user if it does not exist and opens a private chat with them.
     * Else it opens the chat with the selected user.
     *
     * @param user Selected user
     */

    public void openPrivateChat(User user) {
        groupStorageProvider.get().set_id(user._id());
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

    /**
     * This method creates a subcontroller for the selected group if it does not exist and opens a group chat with them.
     * Else it opens the chat with the selected group.
     *
     * @param group Selected group
     */

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

    /**
     * This method enables the user to change to the main menu.
     */

    public void changeToMainMenu() {
        destroy();
        app.show(mainMenuControllerProvider.get());
    }

    /**
     * This method enables the user to change to the find new friends view.
     */

    public void changeToFindNewFriends() {
        destroy();
        app.show(newFriendControllerProvider.get());
    }

    /**
     * This method enables the user to change to the new group view. GroupStorage ID needs to be cleared.
     */

    public void changeToNewGroup() {
        groupStorageProvider.get().set_id(EMPTY_STRING);
        destroy();
        app.show(groupControllerProvider.get());
    }

    /**
     * This method enables the user to change to the settings view. GroupStorage ID needs to be set.
     */

    public void changeToSettings() {
        if (groupListView.getSelectionModel().getSelectedItem() != null) {
            groupStorageProvider.get().set_id(groupListView.getSelectionModel().getSelectedItem()._id());
            destroy();
            app.show(groupControllerProvider.get());
        }
    }

    /**
     * This method sends the current text in the TextField as a message to the selected group or user.
     */

    public void onSendMessage() {
        String groupID = groupStorageProvider.get().get_id();
        if (groupID != null) {
            String messageBody = messageTextArea.getText();
            disposables.add(messageService.newMessage(groupID, messageBody, MESSAGE_NAMESPACE_GROUPS).observeOn(FX_SCHEDULER).subscribe(message -> {
            }, error -> showError(error.getMessage())));
            messageTextArea.setText(EMPTY_STRING);
        }
    }

    /**
     * This method listens to changes in the groups and updates the groups list accordingly.
     */

    public void listenToGroupChanges() {
        disposables.add(eventListenerProvider.get().listen("groups.*.*", Group.class).observeOn(FX_SCHEDULER).subscribe(groupEvent -> {
            final Group group = groupEvent.data();
            switch (groupEvent.suffix()) {
                case "created" -> groups.add(group);
                case "updated" -> updateGroup(group);
                case "deleted" -> groups.remove(group);
            }
        }, error -> showError(error.getMessage())));
    }

    /**
     * This method listens to changes of the group name or members and updates the groups list accordingly.
     *
     * @param group Updated group
     */

    private void updateGroup(Group group) {
        groups.removeIf(g -> g._id().equals(group._id()));
        if (group.members().contains(userStorageProvider.get().get_id())) {
            groups.add(group);
        }
    }

    /**
     * This method sets a boolean if a user was selected from the main menu.
     *
     * @param userChosenFromMainMenu Boolean if a user was selected from the main menu
     */

    public void setUserChosenFromMainMenu(boolean userChosenFromMainMenu) {
        this.userChosenFromMainMenu = userChosenFromMainMenu;
    }

    /**
     * This method sets a boolean if a user was selected from the new friend view.
     *
     * @param userChosenFromNewFriend Boolean if a user was selected from the new friend view
     */

    public void setUserChosenFromNewFriend(boolean userChosenFromNewFriend) {
        this.userChosenFromNewFriend = userChosenFromNewFriend;
    }
}
