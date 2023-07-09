package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.EncounterController;
import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.dto.MonsterTypeDto;
import de.uniks.stpmon.team_m.utils.EncounterOpponentStorage;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
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
    public Label levelUpLabel;
    @FXML
    public TextFlow levelUpTextFlow;
    @FXML
    public Button okButton;
    private VBox container;
    private EncounterController encounterController;
    private StackPane root;
    private Monster monster;
    private MonsterTypeDto monsterTypeDto;

    @Inject
    public LevelUpController() {

    }

    public void init(VBox container, StackPane root, EncounterController encounterController, Monster monster, MonsterTypeDto monsterTypeDto) {
        this.container = container;
        this.root = root;
        this.encounterController = encounterController;
        this.monster = monster;
        this.monsterTypeDto = monsterTypeDto;
    }

    public Parent render() {
        final Parent parent = super.render();
        int oldLevel = monster.level() - 1;

        levelUpTextFlow.getChildren().add(new Text(monsterTypeDto.name() + " " + resources.getString("NOW.HAS.THE.FOLLOWING.ATTRIBUTES") + ":\n"));
        levelUpTextFlow.getChildren().add(new Text(resources.getString("LEVEL") + " " + oldLevel + " -> " + monster.level() + "\n"));
        return parent;
    }

    public void okButtonPressed() {
        root.getChildren().remove(container);
    }
}
