package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.service.GroupService;
import de.uniks.stpmon.team_m.service.GroupStorage;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.inject.Inject;
import javax.inject.Provider;

import java.util.Optional;

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
    @FXML
    public Pane buttonPane;

    @Inject
    Provider<MessagesController> messagesControllerProvider;
    private final GroupStorage groupStorage;
    @Inject
    GroupService groupService;

    @Inject
    public GroupController(GroupStorage groupStorage) {
        this.groupStorage = groupStorage;
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public Parent render() {
        final Parent parent = super.render();
        if (groupStorage.get_id().equals(EMPTY_STRING)) {
            newGroup();
        } else {
            editGroup();
        }
        return parent;
    }

    private void newGroup() {
        TITLE = NEW_GROUP_TITLE;
        buttonPane.getChildren().remove(deleteGroupButton);
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
        if(result.isPresent() && result.get() == ButtonType.YES) {
            disposables.add(groupService.delete(groupStorage.get_id()).subscribe(lr-> app.show(messagesControllerProvider.get()), error -> {
                alert.setContentText(DELETE_ERROR_403);
                alert.getButtonTypes().remove(ButtonType.YES);
            }));
        } else {
            alert.close();
        }
    }

    public void saveGroup() {
        //TODO server communication
        app.show(messagesControllerProvider.get());
    }
}
