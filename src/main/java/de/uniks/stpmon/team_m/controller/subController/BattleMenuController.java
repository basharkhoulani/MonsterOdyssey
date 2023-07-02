package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.EncounterController;
import de.uniks.stpmon.team_m.service.EncounterOpponentsService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import javax.inject.Inject;
import java.awt.*;
import java.util.Objects;

public class BattleMenuController extends Controller {

    @FXML
    public Button abilitiesButton;
    @FXML
    public Button changeMonsterButton;
    @FXML
    public Button currentInfoButton;
    public Button fleeButton;
    private EncounterController encounterController;
    private HBox battleMenuHBox;


    @Inject
    public BattleMenuController(
    ) {}

    public void init(EncounterController encounterController, HBox battleMenuHBox) {
        super.init();
        this.encounterController = encounterController;
        this.battleMenuHBox = battleMenuHBox;
    }

    public Parent render(){
        final Parent parent = super.render();
        if(!GraphicsEnvironment.isHeadless()){
            parent.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../styles.css")).toExternalForm());
        }
        return parent;
    }


    public void showAbilities() {
        // change to AbilitiesSubView
    }

    public void changeMonster(ActionEvent actionEvent) {
        // show the ChangeMonster VBox
    }

    public void showMonsterInformation(ActionEvent actionEvent) {
        // show the MonsterInformation VBox
    }

    public void changeToIngame(ActionEvent actionEvent) {
    }
}
