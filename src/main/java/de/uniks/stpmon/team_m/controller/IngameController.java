package de.uniks.stpmon.team_m.controller;


import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

import javax.inject.Inject;

public class IngameController extends Controller {

    @FXML
    public Button helpSymbol;

    @Inject
    public IngameController() {
    }

    @Override
    public String getTitle() {
        return "Ingame";
    }

    @Override
    public Parent render() {
        final Parent parent = super.render();
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
}
