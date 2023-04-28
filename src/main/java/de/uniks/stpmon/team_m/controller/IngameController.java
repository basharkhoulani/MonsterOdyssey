package de.uniks.stpmon.team_m.controller;


import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;

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
    }
}
