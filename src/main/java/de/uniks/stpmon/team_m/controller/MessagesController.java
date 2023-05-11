package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.service.GroupStorage;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.inject.Inject;
import javax.inject.Provider;

import static de.uniks.stpmon.team_m.Constants.*;

public class MessagesController extends Controller {

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
    @FXML
    public Button settingsButton;

    @Inject
    Provider<MainMenuController> mainMenuControllerProvider;

    @Inject
    Provider<NewFriendController> newFriendControllerProvider;

    @Inject
    Provider<GroupController> groupControllerProvider;
    @Inject
    Provider<GroupStorage> groupStorageProvider;

    @Inject
    public MessagesController( {
    }

    @Override
    public String getTitle() {
        return MESSAGES_TITLE;
    }

    @Override
    public Parent render() {
        return super.render();
    }

    @Override
    public int getHeight() {
        return MESSAGES_HEIGHT;
    }

    @Override
    public int getWidth() {
        return MESSAGES_WIDTH;
    }

    public void changeToMainMenu() {
        app.show(mainMenuControllerProvider.get());
    }

    public void changeToFindNewFriends() {
        app.show(newFriendControllerProvider.get());
    }

    public void changeToNewGroup() {
        groupStorageProvider.get().set_id(EMPTY_STRING);
        app.show(groupControllerProvider.get());
    }

    public void createFriendNode() {
        // TODO:
        /*
         * I imagined this to be a new Pane that gets inserted in the #chatVBox.
         * The Pane should be a new fxml fragment that gets loaded into one of
         * the VBoxs' children.
         * */
    }

    public void creteMessageNode() {
        // TODO:
        /*
         * This will probably work similiar to the 'createFriendNode()' method.
         * */
    }

    public void sendMessage() {
        // TODO:
        // button fx:id: '#sendButton'
    }

    public void changeToSettings() {
        groupStorageProvider.get().set_id(LOADING);
        app.show(groupControllerProvider.get());
    }
}
