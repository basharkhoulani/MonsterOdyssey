package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.Encounter2Controller;
import de.uniks.stpmon.team_m.controller.IngameController;
import de.uniks.stpmon.team_m.utils.EncounterOpponentStorage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Objects;

public class BattleMenuController extends Controller {

    @FXML
    public Button abilitiesButton;
    @FXML
    public Button changeMonsterButton;
    @FXML
    public Button currentInfoButton;
    public Button fleeButton;
    public Runnable onFleeButtonClick;
    private Encounter2Controller encounterController;
    private VBox battleMenuVBox;
    private EncounterOpponentStorage encounterOpponentStorage;


    @Inject
    public BattleMenuController(
    ) {}

    public void init(Encounter2Controller encounterController, VBox battleMenuVBox, EncounterOpponentStorage encounterOpponentStorage) {
        super.init();
        this.encounterController = encounterController;
        this.battleMenuVBox = battleMenuVBox;
        this.encounterOpponentStorage = encounterOpponentStorage;
    }

    public Parent render(){
        final Parent parent = super.render();
        if(encounterOpponentStorage.isWild()){
            showFleeButton();
        } else {
            hideFleeButton();
        }
        return parent;
    }

    public void hideFleeButton() {
        fleeButton.setVisible(false);
    }

    public void showFleeButton() {
        fleeButton.setVisible(true);
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

    public void changeToIngame() {

    }

    public void showFleeButton(boolean isWild){
        fleeButton.setVisible(isWild);
    public void changeToIngame(ActionEvent actionEvent) {
        if (onFleeButtonClick != null) {
            onFleeButtonClick.run();
        }
    }
}
