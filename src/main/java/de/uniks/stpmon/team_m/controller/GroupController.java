package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.controller.views.UserCell;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.GroupService;
import de.uniks.stpmon.team_m.service.GroupStorage;
import de.uniks.stpmon.team_m.service.UserStorage;
import de.uniks.stpmon.team_m.service.UsersService;
import impl.org.controlsfx.skin.AutoCompletePopup;
import impl.org.controlsfx.skin.AutoCompletePopupSkin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static de.uniks.stpmon.team_m.Constants.*;

public class GroupController extends Controller {

    private String TITLE;
    @FXML
    public Label errorMessage;
    @FXML
    public Text selectGroupMembersText;
    @FXML
    public VBox groupMembersVBox;
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
    private ListView<User> listView;
    private final ObservableList<User> allUsers = FXCollections.observableArrayList();
    private final ObservableList<User> newGroupMembers = FXCollections.observableArrayList();

    @Inject
    public GroupController() {
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public void init() {
        final String groupId = groupStorageProvider.get().get_id();
        listView = new ListView<>();
        listView.setSelectionModel(null);
        listView.setFocusModel(null);
        if (groupId.equals(EMPTY_STRING)) {
            final List<String> friendsByID = userStorage.get().getFriends();
            final List<User> friendsByUserObject = new ArrayList<>();
            disposables.add(usersService.getUsers(friendsByID, null).observeOn(FX_SCHEDULER).subscribe(users -> {
                friendsByUserObject.addAll(users);
                listView.setCellFactory(param -> new UserCell(newGroupMembers, listView, friendsByUserObject, friendsByID));
                for (User user : users) {
                    if (friendsByID.contains(user._id())) {
                        listView.getItems().add(user);
                    }
                }
            }));
        }
    }

    @Override
    public Parent render() {
        final Parent parent = super.render();
        if (groupStorageProvider.get().get_id().equals(EMPTY_STRING)) {
            newGroup();
        } else {
            editGroup();
        }
        groupMembersVBox.getChildren().add(listView);
        return parent;
    }

    private void newGroup() {
        TITLE = NEW_GROUP_TITLE;
        deleteGroupButton.setVisible(false);
    }

    private void editGroup() {
        TITLE = EDIT_GROUP_TITLE;
        groupNameInput.setPromptText(CHANGE_GROUP);
    }

    public void changeToMessages() {
        app.show(messagesControllerProvider.get());
    }

    public void deleteGroup() {
        Alert alert = new Alert(Alert.AlertType.WARNING, DELETE_WARNING, ButtonType.YES, ButtonType.NO);
        alert.setTitle(SURE);
        alert.setHeaderText(null);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            disposables.add(groupService.delete(groupStorageProvider.get().get_id())
                    .observeOn(FX_SCHEDULER)
                    .subscribe(deleted -> app.show(messagesControllerProvider.get()),
                            error -> errorAlert(error.getMessage(), alert)));
        } else {
            alert.close();
        }
    }

    private void errorAlert(String error, Alert alert) {
        if (error.equals(HTTP_403)) {
            alert.setContentText(DELETE_ERROR_403);
        } else {
            alert.setContentText(GENERIC_ERROR);
        }
        alert.setTitle(ERROR);
        alert.getButtonTypes().remove(ButtonType.NO);
        alert.getButtonTypes().remove(ButtonType.YES);
        alert.getButtonTypes().add(ButtonType.OK);
        alert.showAndWait();
    }


    public void saveGroup() {
        List<String> newGroupMembersIDs = new ArrayList<>();
        newGroupMembersIDs.add(userStorage.get().get_id());
        newGroupMembers.forEach(user -> newGroupMembersIDs.add(user._id()));
        disposables.add(groupService.create(groupNameInput.getText(), newGroupMembersIDs)
                .observeOn(FX_SCHEDULER).subscribe(group -> {
                    groupStorageProvider.get().set_id(group._id());
                    groupStorageProvider.get().setName(group.name());
                    groupStorageProvider.get().setMembers(group.members());
                    app.show(messagesControllerProvider.get());
                }, error -> errorMessage.setText(error.getMessage())));
    }

    public void searchForGroupMembers() {
        disposables.add(usersService.getUsers(null, null).observeOn(FX_SCHEDULER).subscribe(users -> {
            allUsers.setAll(users);
            final AutoCompletePopup<User> autoCompletePopup = new AutoCompletePopup<>();
            searchFieldGroupMembers.textProperty().addListener((observable, oldValue, newValue) -> {
                autoCompletePopup.getSuggestions().clear();
                autoCompletePopup.hide();
                autoCompletePopup.setSkin(new AutoCompletePopupSkin<>(autoCompletePopup, listView.getCellFactory()));
                allUsers.stream().filter(user -> user.name().contains(searchFieldGroupMembers.getText()))
                        .forEach(autoCompletePopup.getSuggestions()::add);
                autoCompletePopup.show(searchFieldGroupMembers);
            });
        }));
    }
}