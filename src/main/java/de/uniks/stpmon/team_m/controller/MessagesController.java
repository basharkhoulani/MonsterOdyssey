package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.service.GroupStorage;
import de.uniks.stpmon.team_m.service.UserStorage;
import de.uniks.stpmon.team_m.service.UsersService;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.text.Text;

import javax.inject.Inject;
import javax.inject.Provider;

import static de.uniks.stpmon.team_m.Constants.*;

public class MessagesController extends Controller {

    @FXML
    public Text currentFriendOrGroupText; //needs to be set each time a different chat is selected


    @Inject
    Provider<MainMenuController> mainMenuControllerProvider;

    @Inject
    Provider<NewFriendController> newFriendControllerProvider;

    @Inject
    Provider<GroupController> groupControllerProvider;

    @Inject
    Provider<UserStorage> userStorageProvider;
    @Inject
    Provider<GroupStorage> groupStorageProvider;
    @Inject
    UsersService usersServiceProvider;

    @Inject
    public MessagesController() {
    }

    @Override
    public String getTitle() {
        return MESSAGES_TITLE;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        return parent;
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
