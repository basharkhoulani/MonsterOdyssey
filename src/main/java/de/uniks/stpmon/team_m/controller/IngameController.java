package de.uniks.stpmon.team_m.controller;


import de.uniks.stpmon.team_m.controller.subController.IngameHelpController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.inject.Inject;
import javax.inject.Provider;

public class IngameController extends Controller {

    @FXML
    public Button helpSymbol;

    private boolean helpShown = false;

    @Inject
    Provider<IngameHelpController> ingameHelpControllerProvider;

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
        if (!helpShown) {
            helpShown = true;
            final Stage dialog = new Stage();
            dialog.initModality(Modality.NONE);
            dialog.initStyle(StageStyle.UNDECORATED);
            dialog.initOwner(app.getStage());
            Scene scene = new Scene(ingameHelpControllerProvider.get().render());
            dialog.setScene(scene);
            dialog.show();
        }
    }
}
