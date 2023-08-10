package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.EncounterController;
import de.uniks.stpmon.team_m.controller.IngameController;
import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.dto.MonsterTypeDto;
import de.uniks.stpmon.team_m.service.MonstersService;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;


public class CaughtMonsterController extends Controller {

    @FXML
    public VBox caughtMonsterVbox;
    @FXML
    public Label congratulationLabel;
    @FXML
    public Label caughtMonsterLabel;
    @FXML
    public ImageView newMonsterImageView;
    @FXML
    public Label newMonsterLabel;
    @FXML
    public Label newMonsterLevelLabel;
    @FXML
    public Button okButton;
    @Inject
    MonstersService monstersService;
    @Inject
    Provider<TrainerStorage> trainerStorageProvider;
    @Inject
    Provider<IngameController> ingameControllerProvider;

    private VBox container;
    private StackPane root;
    public List<Integer> monsterList;
    public Integer monsterType;
    public Monster caughtMonster;
    public MonsterTypeDto caughtMonsterType;
    public Image newMonsterImage;
    public boolean monsterEncountered = false;
    private EncounterController encounterController;

    @Inject
    public CaughtMonsterController() {
    }

    public void init(VBox container, StackPane root, Monster caughtMonster, MonsterTypeDto caughtMonsterType, Image enemyMonsterImage, EncounterController encounterController) {
        this.container = container;
        this.root = root;
        this.caughtMonster = caughtMonster;
        this.caughtMonsterType = caughtMonsterType;
        this.newMonsterImage = enemyMonsterImage;
        this.encounterController = encounterController;
    }

    public Parent render() {
        final Parent parent = super.render();

        congratulationLabel.setText(resources.getString("CONGRATULATION"));

        //Set Labels and Image
        newMonsterLevelLabel.setText(resources.getString("LEVEL") + " " + caughtMonster.level());
        caughtMonsterLabel.setText(caughtMonsterType.name() + " " + resources.getString("WAS.CAUGHT"));
        newMonsterImageView.setImage(newMonsterImage);

        monsterType = caughtMonster.type();
        monsterList = trainerStorageProvider.get().getTrainer().encounteredMonsterTypes();
        for (Integer i : monsterList) {
            if (i.equals(monsterType)) {
                monsterEncountered = true;
                break;
            }
        }
        if (!monsterEncountered) {
            newMonsterLabel.setText(resources.getString("NEW"));
        }
        return parent;
    }

    public void okButtonPressed() {
        root.getChildren().remove(container);
        encounterController.showIngameController();
    }

}
