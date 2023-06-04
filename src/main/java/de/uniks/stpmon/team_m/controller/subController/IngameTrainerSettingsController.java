package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.MainMenuController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Optional;

import static de.uniks.stpmon.team_m.Constants.*;


public class IngameTrainerSettingsController extends Controller {
    @FXML
    public ImageView trainerAvatarImageView;

    @FXML
    public Button cancelButton;

    @FXML
    public Button deleteTrainerButton;

    @Inject
    Provider<MainMenuController> mainMenuControllerProvider;

    @Inject
    public IngameTrainerSettingsController() {
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public Parent render() {
        return super.render();
    }

    public void onCancelButtonClick() {
        ((Stage) cancelButton.getScene().getWindow()).close();
    }

    public void onDeleteTrainerButtonClick() {
        final Alert alert = new Alert(Alert.AlertType.WARNING);
        final DialogPane dialogPane = alert.getDialogPane();
        final ButtonType cancelButton = new ButtonType(CANCEL);
        final ButtonType okButton = alert.getButtonTypes().stream()
                        .filter(buttonType -> buttonType.getButtonData().isDefaultButton()).findFirst().orElse(null);

        dialogPane.getButtonTypes().addAll(cancelButton);

        final Button cancelButton2 = (Button) alert.getDialogPane().lookupButton(cancelButton);
        final Button okButton2 = (Button) alert.getDialogPane().lookupButton(okButton);
        cancelButton2.getStyleClass().add(WHITE_BUTTON);
        okButton2.getStyleClass().add(WHITE_BUTTON);

        alert.setTitle(DELETE_TRAINER_ALERT);
        dialogPane.getStyleClass().add(ALERT_DIALOG_NAME);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == cancelButton) {
            alert.close();
        } else if (result.isPresent() && result.get() == okButton) {
            app.show(mainMenuControllerProvider.get());
            alert.close();
        }
    }
}
