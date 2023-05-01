package de.uniks.stpmon.team_m.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.inject.Inject;
import javax.inject.Provider;

public class NewGroupController extends Controller{

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
    public Button createGroupButton;

    @Inject
    Provider<MessagesController> messagesControllerProvider;

    @Inject
    public NewGroupController() {

    }

    @Override
    public String getTitle() {
        return "New Group";
    }

    @Override
    public Parent render() {
        final Parent parent = super.render();
        return parent;
    }

    public void changeToMessages() {
        app.show(messagesControllerProvider.get());
    }

    public void createGroup() {
        // onAction from createGroupButton
    }
}
