package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.EncounterController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;

import javax.inject.Inject;

public class LevelUpController extends Controller {
    @FXML
    public VBox levelUpVBox;
    @FXML
    public Label levelUpLabel;
    @FXML
    public TextFlow levelUpTextFlow;
    @FXML
    public Button okButton;
    private VBox root;
    private EncounterController encounterController;

    @Inject
    public LevelUpController() {

    }

    public void init(VBox root, EncounterController encounterController) {
        this.root = root;
        this.encounterController = encounterController;
    }

    public Parent render() {
        final Parent parent = super.render();


        return parent;
    }

    public void okButtonPressed() {
        root.getChildren().remove(levelUpVBox);
    }
}
