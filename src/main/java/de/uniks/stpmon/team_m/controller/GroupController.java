package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.controller.views.UserCell;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.GroupService;
import de.uniks.stpmon.team_m.service.GroupStorage;
import de.uniks.stpmon.team_m.service.UserStorage;
import de.uniks.stpmon.team_m.service.UsersService;
import impl.org.controlsfx.skin.AutoCompletePopup;
import impl.org.controlsfx.skin.AutoCompletePopupSkin;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.inject.Inject;
import javax.inject.Provider;

import java.util.ArrayList;
import java.util.List;

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
    UserStorage userStorage;
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
    public Parent render() {
        final Parent parent = super.render();
        if (groupStorageProvider.get().get_id().equals(EMPTY_STRING)) {
            newGroup();
        } else {
            editGroup();
        }
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
        app.show(messagesControllerProvider.get());
    }

    public void saveGroup() {
        List<String> newGroupMembersIDs = new ArrayList<>();
        newGroupMembersIDs.add(userStorage.get_id());
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
            ChangeListener<String> changeListener = (observable, oldValue, newValue) -> {
                autoCompletePopup.getSuggestions().clear();
                autoCompletePopup.hide();
                autoCompletePopup.setHideOnEscape(true);
                autoCompletePopup.setSkin(new AutoCompletePopupSkin<>(autoCompletePopup,
                        param -> new UserCell(newGroupMembers, groupMembersVBox)));
                allUsers.stream()
                        .filter(user -> user.name().contains(searchFieldGroupMembers.getText()))
                        .forEach(autoCompletePopup.getSuggestions()::add);
                autoCompletePopup.show(searchFieldGroupMembers);
            };
            searchFieldGroupMembers.textProperty().addListener(changeListener);
        }));
    }
}

