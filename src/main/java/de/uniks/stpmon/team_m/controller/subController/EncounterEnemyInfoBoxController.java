package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class EncounterEnemyInfoBoxController extends Controller {
    @FXML
    public Label opponentLevel;
    @FXML
    public ProgressBar opponentHealthBar;
    @FXML
    public Label opponentMonsterName;

    public void setOpponentLevel(int level) {
        this.opponentLevel.setText("Lvl " + level);
    }

    public void setOpponentMonsterName(String monsterName) {
        this.opponentMonsterName.setText(monsterName);
    }

    public void setOpponentHealthBarValue(double value) {
        this.opponentHealthBar.setProgress(value);
    }
}
