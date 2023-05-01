package de.uniks.stpmon.team_m.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javax.inject.Inject;
import javax.inject.Provider;

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
        return "Add a new friend";
    }

    @Override
    public Parent render() {
        final Parent parent = super.render();
        return parent;
    }

    public void changeToMainMenu() {
        app.show(mainMenuControllerProvider.get());
    }

    public void addAsAFriend() {
    }

    public void sendMessage() {
    }
}
