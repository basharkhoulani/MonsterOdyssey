package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import javax.inject.Inject;

public class IngameHelpController extends Controller {


    @FXML
    public Button backToGameButton;

    @Inject
    public IngameHelpController() {
    }

    @Override
    public Parent render() {
        final Parent parent = super.render();
        return parent;
    }

    public void resumeGame() {

    }

}
