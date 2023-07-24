package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;

import javax.inject.Inject;

public class evolutionController extends Controller {
    @FXML
    public VBox evolutionVBox;
    @FXML
    public TextFlow evolutionTextFlow;
    @FXML
    public ImageView oldMonsterImageView;
    @FXML
    public Label oldMonsterLabel;
    @FXML
    public ImageView newMonsterImageView;
    @FXML
    public Label newMonster;
    @FXML
    public Button okButton;

    @Inject
    public evolutionController() {
    }

    public void init() {

    }

    public Parent render() {
        final Parent parent = super.render();
        return parent;
    }

    public void okButtonPressed() {

    }
}
