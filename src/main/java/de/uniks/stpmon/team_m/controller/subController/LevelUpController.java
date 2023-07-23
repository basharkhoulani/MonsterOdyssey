package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.EncounterController;
import de.uniks.stpmon.team_m.dto.AbilityDto;
import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.dto.MonsterTypeDto;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import javax.inject.Inject;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


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
    @FXML
    public ImageView expImageView;
    @FXML
    public ImageView hpImageView;
    @FXML
    public ImageView atkImageView;
    @FXML
    public ImageView defImageView;
    @FXML
    public ImageView spImageView;
    @FXML
    public VBox abilityVBox;
    private VBox container;
    private EncounterController encounterController;
    private StackPane root;
    private Monster monster;
    private MonsterTypeDto monsterTypeDto;
    private Monster oldMonster;
    private ArrayList<Integer> newAbilities;
    private List<AbilityDto> abilityDtos;


    @Inject
    public LevelUpController() {

    }

    public void init(VBox container, StackPane root, EncounterController encounterController, Monster currentMonster, MonsterTypeDto currentMonsterTypeDto, Monster oldMonster, ArrayList<Integer> newAbilities, List<AbilityDto> abilityDtos) {
        this.container = container;
        this.root = root;
        this.encounterController = encounterController;
        this.monster = currentMonster;
        this.monsterTypeDto = currentMonsterTypeDto;
        this.oldMonster = oldMonster;
        this.newAbilities = newAbilities;
        this.abilityDtos = abilityDtos;
    }

    public Parent render() {
        final Parent parent = super.render();
        if (!GraphicsEnvironment.isHeadless()) {
            expImageView.setImage(new Image(Objects.requireNonNull(App.class.getResource("images/star.png")).toString()));
            hpImageView.setImage(new Image(Objects.requireNonNull(App.class.getResource("images/heart.png")).toString()));
            atkImageView.setImage(new Image(Objects.requireNonNull(App.class.getResource("images/attack.png")).toString()));
            defImageView.setImage(new Image(Objects.requireNonNull(App.class.getResource("images/defense.png")).toString()));
            spImageView.setImage(new Image(Objects.requireNonNull(App.class.getResource("images/speed.png")).toString()));
        }

        levelLabel.setText(oldMonster.level() + " -> " + monster.level());
        healthLabel.setText(oldMonster.attributes().health() + " -> " + monster.attributes().health());
        attackLabel.setText(oldMonster.attributes().attack() + " -> " + monster.attributes().attack());
        defenseLabel.setText(oldMonster.attributes().defense() + " -> " + monster.attributes().defense());
        speedLabel.setText(oldMonster.attributes().speed() + " -> " + monster.attributes().speed());

        levelUpTextFlow.getChildren().add(new Text(resources.getString("LEVEL.UP!") + "\n"));
        levelUpTextFlow.getChildren().add(new Text(monsterTypeDto.name() + " " + resources.getString("NOW.HAS.THE.FOLLOWING.ATTRIBUTES") + ":"));

        if (!newAbilities.isEmpty()) {
            Label label = new Label(resources.getString("NEW.ABILITY"));
            label.setAlignment(Pos.CENTER);
            abilityVBox.getChildren().add(label);
            newAbilities.forEach(integer -> abilityVBox.getChildren().add(getAbilityVBox(integer)));
        }
        return parent;
    }

    public HBox getAbilityVBox(Integer integer) {
        AbilityDto ability = abilityDtos.get(integer-1);
        System.out.println("ability: " + ability);
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER);
        Label name = new Label(ability.name());
        box.getChildren().add(name);
        return box;
    }

    public void okButtonPressed() {
        root.getChildren().remove(container);
        encounterController.continueBattle();
    }
}
