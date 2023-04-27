package de.uniks.stpmon.team_m.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import javax.inject.Inject;

public class MainMenuController extends Controller {


    @FXML
    public AnchorPane friendsListScrollPane;
    @FXML
    public Button findNewFriendsButton;
    @FXML
    public Button messagesButton;
    @FXML
    public Button logoutButton;
    @FXML
    public Button settingsButton;
    @FXML
    public Button startGameButton;
    @FXML
    public VBox regionRadioButtonList;

    @Inject
    public MainMenuController() {
    }

    @Override
    public Parent render() {
        final Parent parent = super.render();
        return parent;
    }


    public void changeToFindNewFriends() {
    }

    public void changeToMessages() {
    }

    public void changeToLogin() {
    }

    public void changeToSettings() {
    }

    public void changeToIngame() {
    }
}
