package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.EncounterController;
import de.uniks.stpmon.team_m.service.EncounterOpponentsService;
import de.uniks.stpmon.team_m.controller.EncounterController;
import de.uniks.stpmon.team_m.utils.EncounterOpponentStorage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.inject.Inject;


public class BattleMenuController extends Controller {

    @FXML
    public Button abilitiesButton;
    @FXML
    public Button changeMonsterButton;
    @FXML
    public Button currentInfoButton;
    @FXML
    public Button fleeButton;
    public Runnable onFleeButtonClick;
    private EncounterController encounterController;
    private EncounterOpponentStorage encounterOpponentStorage;
    private VBox battleMenuVBox;


    @Inject
    public BattleMenuController(
    ) {}

    public void init(EncounterController encounterController, VBox battleMenuVBox, EncounterOpponentStorage encounterOpponentStorage, App app) {
        super.init();
        this.encounterController = encounterController;
        this.battleMenuVBox = battleMenuVBox;
        this.encounterOpponentStorage = encounterOpponentStorage;
        this.app = app;
    }

    @Override
    public Parent render(){
        final Parent parent = super.render();
        fleeButton.setVisible(encounterOpponentStorage.isWild());
        fleeButton.setOnAction(this::changeToIngame);
        return parent;
    }


    public void showAbilities() {
        encounterController.showAbilities();
    }

    public void changeMonster(ActionEvent actionEvent) {
        // show the ChangeMonster VBox
    }

    public void showMonsterInformation(ActionEvent actionEvent) {
        // show the MonsterInformation VBox
    }

    public void changeToIngame(ActionEvent event) {
        if (onFleeButtonClick != null) {
            onFleeButtonClick.run();
        }
    }

    public void showFleeButton(boolean isWild){
        fleeButton.setVisible(isWild);
        fleeButton.setOnAction(this::changeToIngame);
    }
}
