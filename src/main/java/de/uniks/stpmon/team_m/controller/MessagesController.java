package de.uniks.stpmon.team_m.controller;

import javafx.scene.Parent;

import javax.inject.Inject;
import javax.inject.Provider;

public class MessagesController extends Controller{

    @Inject
    Provider<MainMenuController> mainMenuControllerProvider;

    @Inject
    public MessagesController() {

    }

    @Override
    public String getTitle() {
        return "Monster Odyssey - Messages";
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
