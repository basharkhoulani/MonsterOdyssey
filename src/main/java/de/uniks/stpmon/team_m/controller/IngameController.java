package de.uniks.stpmon.team_m.controller;


import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Optional;

import static de.uniks.stpmon.team_m.Constants.*;

public class IngameController extends Controller {

    @FXML
    public Button helpSymbol;

    @Inject
    Provider<MainMenuController> mainMenuControllerProvider;

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
            if (event.getCode() == PAUSE_MENU_KEY) {
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
        alert.setContentText(HELP_LABEL);
        final DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle(FX_STYLE_BORDER_COLOR_BLACK);
        alert.showAndWait();
    }

    public void pauseGame() {
        final Alert alert = new Alert(Alert.AlertType.NONE);
        final DialogPane dialogPane = alert.getDialogPane();
        final ButtonType resume = new ButtonType(RESUME_BUTTON_LABEL);
        final ButtonType saveAndExit = new ButtonType(SAVE_GAME_AND_LEAVE_BUTTON_LABEL);
        dialogPane.getButtonTypes().addAll(resume, saveAndExit);
        final Button resumeButton = (Button) dialogPane.lookupButton(resume);
        resumeButton.setOnKeyPressed(event -> {
            if (event.getCode() == PAUSE_MENU_KEY) {
                alert.setResult(resume);
            }
        });

        alert.setTitle(PAUSE_MENU_TITLE);
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setContentText(PAUSE_MENU_LABEL);
        alert.initOwner(app.getStage());
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initStyle(StageStyle.UNDECORATED);
        dialogPane.setStyle(FX_STYLE_BORDER_COLOR_BLACK);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == resume) {
            alert.close();
        } else if (result.isPresent() && result.get() == saveAndExit) {
            alert.close();
            app.getStage().getScene().setOnKeyPressed(null);
            app.show(mainMenuControllerProvider.get());
        }
    }
}
