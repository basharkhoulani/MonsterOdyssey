package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.IngameController;
import de.uniks.stpmon.team_m.dto.AbilityDto;
import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.dto.MonsterTypeDto;
import de.uniks.stpmon.team_m.service.PresetsService;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.inject.Inject;
import javax.inject.Provider;
import java.awt.*;
import java.util.List;
import java.util.*;


public class MonstersDetailController extends Controller {
    @FXML
    public Button goBackMonstersButton;
    @FXML
    public Label monsterName;
    @FXML
    public Label monsterType;
    @FXML
    public Label monsterExperience;
    @FXML
    public Label monsterLevel;
    @FXML
    public Label monsterHealth;
    @FXML
    public Label monsterAttack;
    @FXML
    public Label monsterDefense;
    @FXML
    public Label monsterSpeed;
    @FXML
    public Label ability1;
    @FXML
    public Label accuracy1;
    @FXML
    public Label power1;
    @FXML
    public Text description1;
    @FXML
    public Label ability2;
    @FXML
    public Label accuracy2;
    @FXML
    public Label power2;
    @FXML
    public Text description2;
    @FXML
    public Label ability3;
    @FXML
    public Label accuracy3;
    @FXML
    public Label power3;
    @FXML
    public Text description3;
    @FXML
    public Label ability4;
    @FXML
    public Label accuracy4;
    @FXML
    public Label power4;
    @FXML
    public Text description4;
    @FXML
    public ProgressBar lvlProgressBar;
    @FXML
    public ProgressBar attackProgressBar;
    @FXML
    public ProgressBar healthProgressBar;
    @FXML
    public ProgressBar speedProgressBar;
    @FXML
    public ProgressBar defenseProgressBar;
    public ImageView monsterImageView;
    public Label type4;
    public Label type3;
    public Label type2;
    public Label type1;

    @Inject
    Provider<IngameController> ingameControllerProvider;
    PresetsService presetsService;
    MonstersListController monstersListController;
    private Monster monster;
    private MonsterTypeDto monsterTypeDto;
    private Image monsterImage;
    public IngameController ingameController;
    public VBox monsterDetailVBox;

    @Override
    public Parent render() {
        final Parent parent = super.render();
        if (!GraphicsEnvironment.isHeadless()) {
            parent.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../styles.css")).toExternalForm());
        }
        initMonsterDetails();
        return parent;
    }

    public void init(IngameController ingameController, VBox monsterDetailVBox, MonstersListController monstersListController, Monster monster, MonsterTypeDto monsterTypeDto, Image monsterImage, ResourceBundle resources, PresetsService presetsService) {
        super.init();
        this.ingameController = ingameController;
        this.monsterDetailVBox = monsterDetailVBox;
        this.monstersListController = monstersListController;
        this.monster = monster;
        this.monsterTypeDto = monsterTypeDto;
        this.monsterImage = monsterImage;
        this.resources = resources;
        this.presetsService = presetsService;
    }

    @Inject
    public MonstersDetailController() {
    }

    private void initMonsterDetails() {
        // Sprite
        if (!GraphicsEnvironment.isHeadless()) {
            monsterImageView.setImage(monsterImage);
        }

        // Name, Type, Experience, Level
        //monsterName.setText(resources.getString("NAME") + " " + monsterTypeDto.name());
        StringBuilder type = new StringBuilder(resources.getString("TYPE"));
        for (String s : monsterTypeDto.type()) {
            type.append(" ").append(s);
        }
        //monsterType.setText(type.toString());
        lvlProgressBar.setProgress(monster.experience() / getMaxExp(monster.level()));
        lvlProgressBar.setStyle("-fx-background-color: #FFFFFF; -fx-accent: #2B8B03;");

        attackProgressBar.setProgress((double) monster.currentAttributes().attack() / monster.attributes().attack());
        attackProgressBar.setStyle("-fx-background-color: #FFFFFF; -fx-accent: #999900;");

        healthProgressBar.setProgress((double) monster.currentAttributes().health() / monster.attributes().health());
        healthProgressBar.setStyle("-fx-background-color: #FFFFFF; -fx-accent: #430000;");

        speedProgressBar.setProgress((double) monster.currentAttributes().speed() / monster.attributes().speed());
        speedProgressBar.setStyle("-fx-background-color: #FFFFFF; -fx-accent: #FFC88F;");

        defenseProgressBar.setProgress((double) monster.currentAttributes().defense() / monster.attributes().defense());
        defenseProgressBar.setStyle("-fx-background-color: #FFFFFF; -fx-accent: #848480;");

        monsterLevel.setText(resources.getString("LEVEL") + " " + monster.level());

        // Attributes
        monsterHealth.setText(resources.getString("HEALTH") + " " + monster.currentAttributes().health() + "/" + monster.attributes().health());
        monsterAttack.setText(resources.getString("ATTACK") + " " + monster.currentAttributes().attack() + "/" + monster.attributes().attack());
        monsterDefense.setText(resources.getString("DEFENSE") + " " + monster.currentAttributes().defense() + "/" + monster.attributes().defense());
        monsterSpeed.setText(resources.getString("SPEED") + " " + monster.currentAttributes().speed() + "/" + monster.attributes().speed());

        // Abilities
        List<Label> abilityLabels = new ArrayList<>(Arrays.asList(ability1, ability2, ability3, ability4));
        List<Label> accuracyLabels = new ArrayList<>(Arrays.asList(accuracy1, accuracy2, accuracy3, accuracy4));
        List<Label> powerLabels = new ArrayList<>(Arrays.asList(power1, power2, power3, power4));
        List<Text> descriptionLabels = new ArrayList<>(Arrays.asList(description1, description2, description3, description4));
        List<Label> typeLabels = new ArrayList<>(Arrays.asList(type1, type2, type3, type4));

        disposables.add(presetsService.getAbilities().observeOn(FX_SCHEDULER).subscribe(abilities -> {
            int i = 0;
            for (Map.Entry<String, Integer> entry : monster.abilities().entrySet()) {
                AbilityDto ability = abilities.get(Integer.parseInt(entry.getKey()) - 1);
                abilityLabels.get(i).setText(ability.name() + ": " + entry.getValue() + "/" + ability.maxUses());
                accuracyLabels.get(i).setText(resources.getString("ACCURACY") + " " + (int) (ability.accuracy() * 100) + "%");
                powerLabels.get(i).setText(resources.getString("POWER") + " " + ability.power());
                typeLabels.get(i).setText(resources.getString("TYPE") + " " + ability.type());
                descriptionLabels.get(i).setText(ability.description());
                i++;
            }
        }, error -> showError(error.getMessage())));
    }


    public void goBackToMonsters() {
        ingameController.root.getChildren().remove(monsterDetailVBox);
    }

    public double getMaxExp(int lvl) {
        return Math.pow(lvl, 3) - Math.pow((lvl - 1), 3);
    }
}
