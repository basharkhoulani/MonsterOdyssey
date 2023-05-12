package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.GroupService;
import de.uniks.stpmon.team_m.service.GroupStorage;
import de.uniks.stpmon.team_m.service.UsersService;
import impl.org.controlsfx.skin.AutoCompletePopup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import javax.inject.Inject;
import javax.inject.Provider;

import java.util.ArrayList;
import java.util.List;

import static de.uniks.stpmon.team_m.Constants.*;

public class GroupController extends Controller {

    private String TITLE;
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
    private final ObservableList<User> allUsers = FXCollections.observableArrayList();


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
        app.show(messagesControllerProvider.get());
    }

    public void searchForGroupMembers() {
        disposables.add(usersService.getUsers(null, null).observeOn(FX_SCHEDULER).subscribe(users -> {
            allUsers.setAll(users);
            final List<String> userNames = new ArrayList<>();
            for (final User user : users) {
                userNames.add(user.name());
            }
            final AutoCompletePopup<String> autoCompletePopup = new AutoCompletePopup<>();
            autoCompletePopup.getSuggestions().addAll(userNames);
            autoCompletePopup.show(searchFieldGroupMembers);
            final AutoCompletionBinding<String> autoCompletionBinding = TextFields.bindAutoCompletion(searchFieldGroupMembers, userNames);
            autoCompletionBinding.setOnAutoCompleted(event -> {
                final User user = users.get(userNames.indexOf(event.getCompletion()));
                final Text text = new Text(user.name());
                groupMembersVBox.getChildren().add(text);
                searchFieldGroupMembers.clear();
            });
        }));
    }
}
