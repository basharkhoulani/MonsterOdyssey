package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.IngameController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.awt.*;
import java.util.Objects;

import static de.uniks.stpmon.team_m.Constants.WARNING;

public class IngameDeleteTrainerWarningController extends Controller {
    @FXML
    public VBox deleteTrainerVbox;
    @FXML
    public ImageView warningImageView;
    @FXML
    public Label deleteTrainerLabel;
    @FXML
    public Label areYouSureLabel;
    @FXML
    public Button cancelButton;
    @FXML
    public Button okButton;
    @Inject
    IngameController ingameController;
    @Inject
    IngameTrainerSettingsController ingameTrainerSettingsController;
    private VBox ingameVbox;

    @Inject
    public IngameDeleteTrainerWarningController() {
    }

    public Parent render() {
        final Parent parent = super.render();
        if (!GraphicsEnvironment.isHeadless()) {
            warningImageView.setImage(new javafx.scene.image.Image(Objects.requireNonNull(App.class.getResource(WARNING)).toString()));
        }
        return parent;
    }

    public void init(IngameTrainerSettingsController ingameTrainerSettingsController, VBox ingameVbox) {
        this.ingameTrainerSettingsController = ingameTrainerSettingsController;
        this.ingameVbox = ingameVbox;
    }

    public void clickCancel() {
        ingameTrainerSettingsController.trainerSettingsStackpane.getChildren().remove(ingameVbox);
        ingameTrainerSettingsController.buttonsDisableTrainer(false);
    }

    public void clickOK() {
        clickCancel();
        ingameTrainerSettingsController.deleteTrainer();
    }
}
