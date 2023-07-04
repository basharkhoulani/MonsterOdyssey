package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class EncounterTeamInfoBoxController extends Controller {
    @FXML
    public ProgressBar myLevelBar;
    @FXML
    public Label myLevel;
    @FXML
    public ProgressBar myHealthBar;
    @FXML
    public Label myHealth;
    @FXML
    public Label myMonsterName;

    public void setLevel(int level) {
        this.myLevel.setText("Lvl " + level);
    }
    public void setLevelBarValue(double value) {
        this.myLevelBar.setProgress(value);
    }
    public void setHealth(int health) {
        this.myHealth.setText(health + " HP");
    }
    public void setHealthBar(double value) {
        this.myHealthBar.setProgress(value);
    }
    public void setMyMonsterName(String monsterName) {
        this.myMonsterName.setText(monsterName);
    }
}
