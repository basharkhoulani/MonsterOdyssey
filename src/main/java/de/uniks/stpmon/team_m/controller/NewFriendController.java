package de.uniks.stpmon.team_m.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javax.inject.Inject;
import javax.inject.Provider;

import static de.uniks.stpmon.team_m.Constants.NEW_FRIEND_TITLE;

public class NewFriendController extends Controller {

    @FXML
    public Button mainMenuButton;
    @FXML
    public Button addFriendButton;
    @FXML
    public Button messageButton;
    @FXML
    public TextField searchTextField;
    @Inject
    Provider<MainMenuController> mainMenuControllerProvider;

    @Inject
    public NewFriendController() {
    }

    @Override
    public String getTitle() {
        return NEW_FRIEND_TITLE;
    }

    @Override
    public Parent render() {
        return super.render();
    }

    public void changeToMainMenu() {
        app.show(mainMenuControllerProvider.get());
    }

    public void addAsAFriend() {
    }

    public void sendMessage() {
    }
}
