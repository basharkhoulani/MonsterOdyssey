package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.EncounterController;
import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.dto.MonsterTypeDto;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import javax.inject.Inject;


public class LevelUpController extends Controller {
    @FXML
    public VBox levelUpVBox;
    @FXML
    public TextFlow levelUpTextFlow;
    @FXML
    public Button okButton;
    @FXML
    public Label levelLabel;
    @FXML
    public Label healthLabel;
    @FXML
    public Label attackLabel;
    @FXML
    public Label defenseLabel;
    @FXML
    public Label speedLabel;
    private VBox container;
    private EncounterController encounterController;
    private StackPane root;
    private Monster monster;
    private MonsterTypeDto monsterTypeDto;
    private Monster oldMonster;

    @Inject
    public LevelUpController() {

    }

    public void init(VBox container, StackPane root, EncounterController encounterController, Monster currentMonster, MonsterTypeDto currentMonsterTypeDto, Monster oldMonster) {
        this.container = container;
        this.root = root;
        this.encounterController = encounterController;
        this.monster = currentMonster;
        this.monsterTypeDto = currentMonsterTypeDto;
        this.oldMonster = oldMonster;
    }

    public Parent render() {
        final Parent parent = super.render();

        levelLabel.setText(oldMonster.level() + " -> " + monster.level());
        healthLabel.setText(oldMonster.attributes().health() + " -> " + monster.attributes().health());
        attackLabel.setText(oldMonster.attributes().attack() + " -> " + monster.attributes().attack());
        defenseLabel.setText(oldMonster.attributes().defense() + " -> " + monster.attributes().defense());
        speedLabel.setText(oldMonster.attributes().speed() + " -> " + monster.attributes().speed());

        levelUpTextFlow.getChildren().add(new Text(resources.getString("LEVEL.UP!") + "\n"));
        levelUpTextFlow.getChildren().add(new Text(monsterTypeDto.name() + " " + resources.getString("NOW.HAS.THE.FOLLOWING.ATTRIBUTES") + ":"));
        return parent;
    }

    public void okButtonPressed() {
        root.getChildren().remove(container);
    }
}
