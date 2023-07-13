package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.EncounterController;
import de.uniks.stpmon.team_m.controller.IngameController;
import de.uniks.stpmon.team_m.dto.AbilityDto;
import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.dto.MonsterTypeDto;
import de.uniks.stpmon.team_m.service.PresetsService;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.awt.*;
import java.net.URL;
import java.util.List;
import java.util.*;

import static de.uniks.stpmon.team_m.Constants.*;


public class MonstersDetailController extends Controller {
    @FXML
    public Button goBackMonstersButton;
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
    public ProgressBar lvlProgressBar;
    @FXML
    public ProgressBar attackProgressBar;
    @FXML
    public ProgressBar healthProgressBar;
    @FXML
    public ProgressBar speedProgressBar;
    @FXML
    public ProgressBar defenseProgressBar;
    @FXML
    public ListView<AbilityDto> abilityListView;
    public ImageView monsterImageView;
    @FXML
    public ImageView typeImageView;
    @FXML
    public VBox typeIcon;
    @FXML
    public HBox levelBox;
    @FXML
    public HBox attackBox;
    @FXML
    public HBox hpBox;
    @FXML
    public HBox speedBox;
    @FXML
    public HBox defenseBox;
    @Inject
    Provider<IngameController> ingameControllerProvider;
    PresetsService presetsService;
    private Monster monster;
    private MonsterTypeDto monsterTypeDto;
    private Image monsterImage;
    public IngameController ingameController;
    public EncounterController encounterController;
    public VBox monsterDetailVBox;
    public final List<AbilityDto> monsterAbilities = new ArrayList<>();
    @Inject
    public Provider<PresetsService> presetsServiceProvider;
    private String monsterType;
    @FXML
    public Label monsterName;
    @FXML
    public ImageView starImageView;
    @FXML
    public ImageView attackImageView;
    @FXML
    public ImageView heartImageView;
    @FXML
    public ImageView speedImageView;
    @FXML
    public ImageView defenseImageView;

    @Override
    public Parent render() {
        final Parent parent = super.render();
        initMonsterDetails();
        return parent;
    }

    public void init(IngameController ingameController, VBox monsterDetailVBox,
                     Monster monster, MonsterTypeDto monsterTypeDto, Image monsterImage, ResourceBundle resources, PresetsService presetsService,
                     String type) {
        super.init();
        this.monsterType = type;
        this.ingameController = ingameController;
        this.monsterDetailVBox = monsterDetailVBox;
        this.monster = monster;
        this.monsterTypeDto = monsterTypeDto;
        this.monsterImage = monsterImage;
        this.resources = resources;
        this.presetsService = presetsService;
    }

    public void initFromBattleMenu(EncounterController encounterController, VBox monsterDetailVBox, Monster monster, MonsterTypeDto monsterTypeDto, Image monsterImage,
                                   ResourceBundle resources, PresetsService presetsService, String type) {
        super.init();
        this.monsterType = type;
        this.encounterController = encounterController;
        this.monsterDetailVBox = monsterDetailVBox;
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
            String typeColor = TYPESCOLORPALETTE.get(monsterType);
            String style = "-fx-background-color: " + typeColor + ";";
            typeIcon.setStyle(style);

            String typeImagePath = ABILITYPALETTE.get(monsterType);
            URL resourceType = Main.class.getResource("images/" + typeImagePath);
            assert resourceType != null;
            Image typeImage = new Image(resourceType.toString());
            typeImageView.setImage(typeImage);
            typeImageView.setFitHeight(45);
            typeImageView.setFitWidth(45);

            for (String imagePath: ATTRIBUTE_IMAGES) {
                URL resourceImage = Main.class.getResource("images/" + imagePath);
                assert resourceImage != null;
                Image attributeImage = new Image(resourceImage.toString());

                switch(imagePath) {
                    case "star.png" -> starImageView.setImage(attributeImage);
                    case "attack.png" -> attackImageView.setImage(attributeImage);
                    case "heart.png" -> heartImageView.setImage(attributeImage);
                    case "speed.png" -> speedImageView.setImage(attributeImage);
                    case "defense.png" -> defenseImageView.setImage(attributeImage);
                }
            }
        }

        // Name, Type, Experience, Level

        monsterName.setText(monsterTypeDto.name());

        // Attribute bars
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

        // Attribute values
        monsterHealth.setText(resources.getString("HEALTH") + " " + monster.currentAttributes().health() + "/" + monster.attributes().health());
        monsterAttack.setText(resources.getString("ATTACK") + " " + monster.currentAttributes().attack() + "/" + monster.attributes().attack());
        monsterDefense.setText(resources.getString("DEFENSE") + " " + monster.currentAttributes().defense() + "/" + monster.attributes().defense());
        monsterSpeed.setText(resources.getString("SPEED") + " " + monster.currentAttributes().speed() + "/" + monster.attributes().speed());

        disposables.add(presetsService.getAbilities().observeOn(FX_SCHEDULER).subscribe(abilities -> {
            for (Map.Entry<String, Integer> entry : monster.abilities().entrySet()) {
                AbilityDto ability = abilities.get(Integer.parseInt(entry.getKey()) - 1);
                monsterAbilities.add(ability);
            }
            initMonsterAbilities(monsterAbilities, monster);
        }, error ->
            showError(error.getMessage())));
    }


    private void initMonsterAbilities(List<AbilityDto> abilities, Monster monster) {
        abilityListView.setCellFactory(param -> new AbilityCell(monster, resources, presetsServiceProvider.get(), this));
        abilityListView.getItems().addAll(abilities);
        abilityListView.setFocusModel(null);
        abilityListView.setSelectionModel(null);
    }

    public void goBackToMonsters() {
        if (monsterDetailVBox.getParent().getId().equals("root")) {
            ingameController.root.getChildren().remove(monsterDetailVBox);
        } else {
            encounterController.rootStackPane.getChildren().remove(monsterDetailVBox);
        }
    }

    public double getMaxExp(int lvl) {
        return Math.pow(lvl, 3) - Math.pow((lvl - 1), 3);
    }
}
