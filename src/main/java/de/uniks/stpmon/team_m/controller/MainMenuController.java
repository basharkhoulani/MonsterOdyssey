package de.uniks.stpmon.team_m.controller;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

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

    @Override
    public Parent render() {
        final Parent parent = super.render();
        initRadioButtons();
        return parent;
    }

    private void initRadioButtons() {
        ToggleGroup group = addAllRadioButtonsToGroup(new ToggleGroup());
        BooleanBinding booleanBinding = new BooleanBinding() {
            {
                super.bind(group.selectedToggleProperty());
            }

            @Override
            protected boolean computeValue() {
                return group.getSelectedToggle() == null;
            }
        };
        startGameButton.disableProperty().bind(booleanBinding);
    }

    private ToggleGroup addAllRadioButtonsToGroup(ToggleGroup group) {
        for (Node node : regionRadioButtonList.getChildren()) {
            if (node instanceof RadioButton) {
                ((RadioButton) node).setToggleGroup(group);
            }
        }
        return group;
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
