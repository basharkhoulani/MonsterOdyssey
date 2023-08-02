package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.EncounterController;
import de.uniks.stpmon.team_m.utils.EncounterOpponentStorage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import javax.inject.Inject;

public class BattleMenuController extends Controller {

    @FXML
    public Button abilitiesButton;
    @FXML
    public Button changeMonsterButton;
    @FXML
    public Button fleeButton;
    @FXML
    public Button itemButton;
    public Runnable onFleeButtonClick;

    private EncounterController encounterController;
    EncounterOpponentStorage encounterOpponentStorage;


    @Inject
    public BattleMenuController() {
    }

    public void init(EncounterController encounterController, EncounterOpponentStorage encounterOpponentStorage, App app) {
        super.init();
        this.encounterController = encounterController;
        this.encounterOpponentStorage = encounterOpponentStorage;
        this.app = app;
    }

    @Override
    public Parent render() {
        final Parent parent = super.render();
        fleeButton.setVisible(encounterOpponentStorage.isWild());
        fleeButton.setOnAction(this::changeToIngame);
        return parent;
    }


    public void showAbilities() {
        encounterController.showAbilities();
    }

    public void changeMonster() {
        this.encounterController.showChangeMonsterList();
    }



    public void changeToIngame(ActionEvent event) {
        if (onFleeButtonClick != null) {
            onFleeButtonClick.run();
        }
    }

    public void showFleeButton(boolean isWild) {
        fleeButton.setVisible(isWild);
        fleeButton.setOnAction(this::changeToIngame);
    }

    public void buttonDisable(boolean isDisable) {
        abilitiesButton.setDisable(isDisable);
        changeMonsterButton.setDisable(isDisable);
        itemButton.setDisable(isDisable);
        fleeButton.setDisable(isDisable);
    }

    public void showItem() {
        encounterController.showItems();
    }
}
