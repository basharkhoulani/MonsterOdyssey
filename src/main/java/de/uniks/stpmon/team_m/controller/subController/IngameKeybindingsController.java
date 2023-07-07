package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.IngameController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import javax.inject.Inject;

public class IngameKeybindingsController extends Controller {

    @FXML
    public Button walkUpButton;
    @FXML
    public Button walkDownButton;
    @FXML
    public Button walkRightButton;
    @FXML
    public Button walkLeftButton;
    @FXML
    public Button interactionButton;
    @FXML
    public Button pauseMenuButton;
    @FXML
    public Button goBackButton;
    @FXML
    public Button defaultButton;
    @FXML
    public Button checkButton;
    @Inject
    IngameController ingameController;
    private VBox ingameVbox;

    @Inject
    public IngameKeybindingsController() {
    }

    public Parent render() {
        return super.render();
    }

    public void init(IngameController ingameController, VBox ingameVbox) {
        this.ingameController = ingameController;
        this.ingameVbox = ingameVbox;
    }

    public void goBack() {
    }

    public void setDefault() {
    }

    public void check() {
    }

    public void setWalkLeft() {
    }

    public void setPauseMenu() {
    }

    public void setWalkRight() {
    }

    public void setInteraction() {
    }

    public void setWalkDown() {
    }

    public void setWalkUp() {
    }
}
