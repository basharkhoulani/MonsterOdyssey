package de.uniks.stpmon.team_m.controller;


import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

import javax.inject.Inject;
import java.util.Optional;

import static de.uniks.stpmon.team_m.Constants.INGAME_TITLE;

public class IngameController extends Controller {

    @FXML
    public Button helpSymbol;

    @Inject
    public IngameController() {
    }

    @Override
    public String getTitle() {
        return INGAME_TITLE;
    }

    @Override
    public Parent render() {
        final Parent parent = super.render();
        app.getStage().getScene().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.P) {
                pauseGame();
            }
        });
        return parent;
    }

    public void showHelp() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(null);
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.initOwner(app.getStage());
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setContentText("Click 'p' on your keyboard for pause menu.");
        final DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-border-color: black");
        alert.showAndWait();
    }

    public void pauseGame() {
        final Alert alert = new Alert(Alert.AlertType.NONE);
        final DialogPane dialogPane = alert.getDialogPane();
        final ButtonType resume = new ButtonType("Resume Game");
        final ButtonType saveAndExit = new ButtonType("Save Game & Leave");
        dialogPane.getButtonTypes().addAll(resume, saveAndExit);
        final Button resumeButton = (Button) dialogPane.lookupButton(resume);
        resumeButton.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.P) {
                alert.setResult(resume);
            }
        });

        alert.setTitle("Pause Menu");
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setContentText("What do you want to do?");
        alert.initOwner(app.getStage());
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initStyle(StageStyle.UNDECORATED);
        dialogPane.setStyle("-fx-border-color: black");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == resume) {
            alert.close();
        } else if (result.isPresent() && result.get() == saveAndExit) {
            // TODO: Save Game, https://jira.uniks.de/browse/STP23M-51
        }
    }
}
