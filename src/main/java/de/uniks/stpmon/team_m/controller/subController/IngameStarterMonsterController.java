package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.IngameController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;

import javax.inject.Inject;

public class IngameStarterMonsterController extends Controller {
    @FXML
    public AnchorPane starterSelectionAnchorPane;
    @FXML
    public Label starterSelectionLabel;
    @FXML
    public VBox starterSelectionVBox;
    @FXML
    public HBox starterSelectionHBox;
    @FXML
    public ImageView starterImageView;
    @FXML
    public ImageView typeImageView;
    @FXML
    public TextFlow starterDescription;
    @FXML
    public ImageView arrowLeft;
    @FXML
    public ImageView arrowRight;
    @Inject
    IngameController ingameController;
    private VBox popUpVBox;

    @Inject
    public IngameStarterMonsterController() {

    }

    public void init(IngameController ingameController, VBox starterSelectionVBox, App app) {
        this.ingameController = ingameController;
        this.popUpVBox = starterSelectionVBox;
        this.app = app;
    }

    public Parent render() {
        final Parent parent = super.render();

        return parent;
    }

    public void rotateLeft() {

    }

    public void rotateRight() {

    }
}
