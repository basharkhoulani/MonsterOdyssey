package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import javax.inject.Inject;

public class IngamePauseMenuController extends Controller {

    @FXML
    public VBox pauseMenuVbox;

    @Inject
    public IngamePauseMenuController() {
    }

    public Parent render() {
        final Parent parent = super.render();
        return parent;
    }
}
