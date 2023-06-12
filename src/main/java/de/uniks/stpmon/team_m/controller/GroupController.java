package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.controller.subController.GroupUserCell;
import de.uniks.stpmon.team_m.dto.Group;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.GroupService;
import de.uniks.stpmon.team_m.service.UsersService;
import de.uniks.stpmon.team_m.utils.FriendListUtils;
import de.uniks.stpmon.team_m.utils.GroupStorage;
import de.uniks.stpmon.team_m.utils.UserStorage;
import impl.org.controlsfx.skin.AutoCompletePopup;
import impl.org.controlsfx.skin.AutoCompletePopupSkin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.prefs.Preferences;

import static de.uniks.stpmon.team_m.Constants.EMPTY_STRING;
import static de.uniks.stpmon.team_m.Constants.MAX_SUGGESTIONS_NEW_GROUP;

public class GroupController extends Controller {

    private String TITLE;
    @FXML
    public Label errorMessage;
    @FXML
    public Label selectGroupMembersLabel;
    @FXML
    public Button backToMessagesButton;
    @FXML
    public TextField searchFieldGroupMembers;
    @FXML
    public TextField groupNameInput;
    @FXML
    public Button saveGroupButton;
    @FXML
    public Button deleteGroupButton;
    @FXML
    public ListView<User> friendsListView;
    @FXML
    public ListView<User> foreignListView;
    @Inject
    UsersService usersService;
    @Inject
    GroupService groupService;
    @Inject
    Provider<MessagesController> messagesControllerProvider;
    @Inject
    Provider<GroupStorage> groupStorageProvider;
    @Inject
    Provider<UserStorage> userStorage;
    @Inject
    Preferences preferences;
    private final ObservableList<User> friends = FXCollections.observableArrayList();
    private final ObservableList<User> foreign = FXCollections.observableArrayList();
    private final ObservableList<User> allUsers = FXCollections.observableArrayList();
    private final ObservableList<User> newGroupMembers = FXCollections.observableArrayList();

    /**
     * GroupController is used to create a new group or edit an existing one.
     */

    @Inject
    public GroupController() {
    }

    /**
     * This method sets the title depending on the selected button.
     *
     * @return the title of GroupController
     */

    @Override
    public String getTitle() {
        return TITLE;
    }

    /**
     * This method initializes the important elements such as the friends list and participants list of the group.
     * It also adds listeners to the friends list.
     */

    @Override
    public void init() {
        disposables.add(usersService.getUsers(null, null).observeOn(FX_SCHEDULER)
                .subscribe(allUsers::setAll, error -> showError(error.getMessage())));
        final String groupId = groupStorageProvider.get().get_id();
        if (groupId.equals(EMPTY_STRING)) {
            initNewGroupView();
        } else {
            initEditGroupView();
        }
    }

    /**
     * This method is used to initialize the ListView of the participants - that are not friends - the group.
     */

    private void initForeignListView() {
        foreignListView.setSelectionModel(null);
        foreignListView.setFocusModel(null);
        foreignListView.setPlaceholder(new Label(resources.getString("NO.USERS.ADDED.TO.GROUP")));
        foreignListView.setCellFactory(friendsListView.getCellFactory());
        foreignListView.setItems(foreign);
    }

    /**
     * This method is used to initialize the ListView of the friends of the user.
     */

    private void initFriendsListView() {
        friendsListView.setSelectionModel(null);
        friendsListView.setFocusModel(null);
        friendsListView.setPlaceholder(new Label(resources.getString("NO.FRIENDS.FOUND")));
        friendsListView.setCellFactory(param -> new GroupUserCell(preferences, newGroupMembers, friendsListView,
                foreignListView, friends));
        friendsListView.setItems(friends);
    }

    /**
     * This method is used to request the group members of the group from the server if edit button is clicked.
     * It also sorts the participants into the correct lists.
     */

    private void initEditGroupView() {
        TITLE = resources.getString("EDIT.GROUP.TITLE");
        disposables.add(usersService.getUsers(groupStorageProvider.get().getMembers(), null).observeOn(FX_SCHEDULER)
                .doOnNext(newGroupMembers::setAll)
                .flatMap(users -> usersService.getUsers(userStorage.get().getFriends(), null).observeOn(FX_SCHEDULER))
                .doOnNext(this::sortGroupMembersIntoLists)
                .observeOn(FX_SCHEDULER)
                .subscribe(event -> {
                }, error -> showError(error.getMessage())));
    }

    /**
     * This method is used to sort the group members into the correct lists.
     *
     * @param friends List of friends of the user
     */

    private void sortGroupMembersIntoLists(List<User> friends) {
        if (userStorage.get().getFriends().isEmpty()) {
            this.friends.setAll(List.of());
        } else {
            this.friends.setAll(friends);
        }
        final List<User> users = new ArrayList<>(newGroupMembers);
        users.removeAll(this.friends);
        foreign.setAll(users);
        foreign.removeIf(user -> user._id().equals(userStorage.get().get_id()));
        FriendListUtils.sortListView(foreignListView);
        FriendListUtils.sortListView(friendsListView);
    }

    /**
     * This method is used to initialize the view for creating a new group and adds the friends of the user to the list.
     */

    private void initNewGroupView() {
        TITLE = resources.getString("NEW.GROUP.TITLE");
        final List<String> friendsByID = userStorage.get().getFriends();
        if (!friendsByID.isEmpty()) {
            disposables.add(usersService.getUsers(friendsByID, null).observeOn(FX_SCHEDULER).subscribe(users -> {
                friends.setAll(users);
                FriendListUtils.sortListView(friendsListView);
            }, error -> showError(error.getMessage())));
        }
    }

    /**
     * This method is used to render JavaFX elements and set the correct texts.
     *
     * @return Parent object
     */

    @Override
    public Parent render() {
        final Parent parent = super.render();
        final String groupId = groupStorageProvider.get().get_id();
        if (groupId.equals(EMPTY_STRING)) {
            deleteGroupButton.setVisible(false);
        } else {
            groupNameInput.setPromptText(resources.getString("CHANGE.GROUP"));
            groupNameInput.setText(groupStorageProvider.get().getName());
        }

        initFriendsListView();
        initForeignListView();
        listenToUserUpdate(friends, friendsListView);
        listenToUserUpdate(foreign, foreignListView);
        return parent;
    }

    /**
     * This method is used to change to MessagesController when the back button is clicked.
     */

    public void changeToMessages() {
        app.show(messagesControllerProvider.get());
    }

    /**
     * This method is used to delete a group. A group can only be deleted when there is only the user left in the group.
     */

    public void deleteGroup() {
        Alert alert = new Alert(Alert.AlertType.WARNING, resources.getString("DELETE.WARNING"), ButtonType.YES, ButtonType.NO);
        alert.setTitle(resources.getString("SURE"));
        alert.setHeaderText(null);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            if (groupStorageProvider.get().getMembers().size() > 1) {
                groupStorageProvider.get().getMembers().remove(userStorage.get().get_id());
                disposables.add(groupService.update(groupStorageProvider.get().get_id(),groupStorageProvider.get().getName(), groupStorageProvider.get().getMembers())
                        .observeOn(FX_SCHEDULER)
                        .subscribe(group -> app.show(messagesControllerProvider.get())
                                , error -> showError(error.getMessage())));
            } else {
                disposables.add(groupService.delete(groupStorageProvider.get().get_id())
                        .observeOn(FX_SCHEDULER)
                        .subscribe(deleted -> app.show(messagesControllerProvider.get())
                                , error -> showError(error.getMessage())));
            }
        } else {
            alert.close();
        }
    }

    /**
     * This method is used to save a group. If the group is new, it will be created. If the group already exists,
     * it will be updated.
     */

    public void saveGroup() {
        final String groupId = groupStorageProvider.get().get_id();
        if (groupId.equals(EMPTY_STRING)) {
            createGroup();
        } else {
            updateGroup(groupId);
        }
    }

    /**
     * This method is used to update a group.
     *
     * @param groupId ID of the {@link Group} that should be updated
     */

    private void updateGroup(String groupId) {
        List<String> newGroupMembersIDs = new ArrayList<>();
        newGroupMembers.forEach(user -> newGroupMembersIDs.add(user._id()));
        disposables.add(groupService.update(groupId, groupNameInput.getText(), newGroupMembersIDs)
                .observeOn(FX_SCHEDULER).subscribe(group -> app.show(messagesControllerProvider.get()), error -> showError(error.getMessage())));
    }

    /**
     * This method is used to create a new group.
     */

    private void createGroup() {
        List<String> newGroupMembersIDs = new ArrayList<>();
        newGroupMembersIDs.add(userStorage.get().get_id());
        newGroupMembers.forEach(user -> newGroupMembersIDs.add(user._id()));
        disposables.add(groupService.create(groupNameInput.getText(), newGroupMembersIDs)
                .observeOn(FX_SCHEDULER).subscribe(group -> app.show(messagesControllerProvider.get()), error -> showError(error.getMessage())));
    }

    /**
     * This method is used to change the queried users based on the input of the user.
     */

    public void searchForGroupMembers() {
        final AutoCompletePopup<User> autoCompletePopup = new AutoCompletePopup<>();
        searchFieldGroupMembers.textProperty().addListener((observable, oldValue, newValue) -> {
            autoCompletePopup.getSuggestions().clear();
            autoCompletePopup.hide();
            autoCompletePopup.setVisibleRowCount(MAX_SUGGESTIONS_NEW_GROUP);
            autoCompletePopup.setPrefWidth(searchFieldGroupMembers.getWidth());
            autoCompletePopup.setSkin(new AutoCompletePopupSkin<>(autoCompletePopup, friendsListView.getCellFactory()));
            allUsers.stream()
                    .filter(user -> user.name().contains(searchFieldGroupMembers.getText()))
                    .forEach(autoCompletePopup.getSuggestions()::add);
            autoCompletePopup.show(searchFieldGroupMembers);
        });
    }
}