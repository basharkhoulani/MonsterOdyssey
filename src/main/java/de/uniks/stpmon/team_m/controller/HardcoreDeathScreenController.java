package de.uniks.stpmon.team_m.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import javax.inject.Inject;
import javax.inject.Provider;

public class HardcoreDeathScreenController extends Controller {
    @FXML
    public Button backToMainMenuButton;
    @Inject
    Provider<MainMenuController> mainMenuControllerProvider;

    @Inject
    public HardcoreDeathScreenController() {
        super();
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        backToMainMenuButton.setOnAction(event -> app.show(mainMenuControllerProvider.get()));
        return parent;
    }
}
