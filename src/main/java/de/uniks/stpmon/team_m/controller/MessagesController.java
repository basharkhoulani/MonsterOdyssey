package de.uniks.stpmon.team_m.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.inject.Inject;
import javax.inject.Provider;

public class MessagesController extends Controller{

    @FXML
    public VBox leftSideVBox;

    @FXML
    public VBox rightSideVBox;

    @FXML
    public Text friendsAndGroupText;

    @FXML
    public ScrollPane friendsAndGroupsScrollPane;

    @FXML
    public VBox friendsAndGroupsVBox;

    @FXML
    public Button findNewFriendsButton;

    @FXML
    public Button newGroupButton;

    @FXML
    public Button mainMenuButton;

    @FXML
    public Text currentFriendOrGroupText; //needs to be set each time a different chat is selected

    @FXML
    public ScrollPane chatScrollPane;

    @FXML
    public VBox chatVBox;

    @FXML
    public TextArea messageTextArea;

    @FXML
    public Button sendButton;

    @Inject
    Provider<MainMenuController> mainMenuControllerProvider;

    @Inject
    public MessagesController() {

    }

    @Override
    public String getTitle() {
        return "Messages";
    }

    @Override
    public Parent render() {
        final Parent parent = super.render();
        return parent;
    }

    public void changeToMainMenu() {
        // TO DO
        // button fx:id: '#mainMenuButton'
    }

    public void changeToFindNewFriends() {
        // TO DO
        // button fx:id: '#findNewFriendsButton'
    }

    public void changeToNewGroup() {
        // TO DO
        // button fx:id: '#newGroupButton'
    }

    public void createFriendNode() {
        // TO DO
        /*
        * I imagined this to be a new Pane that gets inserted in the #chatVBox.
        * The Pane should be a new fxml fragment that gets loaded into one of
        * the VBoxs' children.
        * */
    }

    public void creteMessageNode() {
        // TO DO
        /*
        * This will probably work similiar to the 'createFriendNode()' method.
        * */
    }

    public void sendMessage() {
        // TO DO
        // button fx:id: '#sendButton'
    }
}
