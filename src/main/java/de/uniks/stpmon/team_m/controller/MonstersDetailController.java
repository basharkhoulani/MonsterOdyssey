package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.dto.AbilityDto;
import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.dto.MonsterAttributes;
import de.uniks.stpmon.team_m.dto.MonsterTypeDto;
import de.uniks.stpmon.team_m.service.PresetsService;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.inject.Inject;
import javax.inject.Provider;
import java.awt.*;
import java.util.*;
import java.util.List;


public class MonstersDetailController extends Controller{
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
    public Label description1;
    @FXML
    public Label ability2;
    @FXML
    public Label accuracy2;
    @FXML
    public Label power2;
    @FXML
    public Label description2;
    @FXML
    public Label ability3;
    @FXML
    public Label accuracy3;
    @FXML
    public Label power3;
    @FXML
    public Label description3;
    @FXML
    public Label ability4;
    @FXML
    public Label accuracy4;
    @FXML
    public Label power4;
    @FXML
    public Label description4;
    public ImageView monsterImageView;

    @Inject
    Provider<IngameController> ingameControllerProvider;
    PresetsService presetsService;
    MonstersListController monstersListController;
    private Monster monster;
    private MonsterTypeDto monsterTypeDto;
    private Image monsterImage;

    @Override
    public Parent render() {
        final Parent parent = super.render();
        if (!GraphicsEnvironment.isHeadless()) {
            parent.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../styles.css")).toExternalForm());
        }
        initMonsterDetails();
        return parent;
    }

    public void init(MonstersListController monstersListController, Monster monster, MonsterTypeDto monsterTypeDto, Image monsterImage, ResourceBundle resources, PresetsService presetsService) {
        super.init();
        this.monstersListController = monstersListController;
        this.monster = monster;
        this.monsterTypeDto = monsterTypeDto;
        this.monsterImage = monsterImage;
        this.resources = resources;
        this.presetsService = presetsService;
    }

    @Inject
    public MonstersDetailController() {}

    private void initMonsterDetails(){
        monsterImageView.setImage(monsterImage);

        // Top Middle
        monsterName.setText(resources.getString("NAME") + monsterTypeDto.name());
        StringBuilder type = new StringBuilder(resources.getString("TYPE"));
        for (String s : monsterTypeDto.type()) {
            type.append(s).append(" ");
        }
        monsterType.setText(type.toString());
        monsterExperience.setText(resources.getString("EXPERIENCE") + monster.experience());
        monsterLevel.setText(resources.getString("LEVEL") + monster.level());

        // Top Right
        monsterHealth.setText(resources.getString("HEALTH") + monster.currentAttributes().health() + "/" + monster.attributes().health());
        monsterAttack.setText(resources.getString("ATTACK") + monster.currentAttributes().attack() + "/" + monster.attributes().attack());
        monsterDefense.setText(resources.getString("DEFENSE") + monster.currentAttributes().defense() + "/" + monster.attributes().defense());
        monsterSpeed.setText(resources.getString("SPEED") + monster.currentAttributes().speed() + "/" + monster.attributes().speed());
        // First Ability
        List<Label> abilityLabels = new ArrayList<>(Arrays.asList(ability1, ability2, ability3, ability4));
        List<Label> accuracyLabels = new ArrayList<>(Arrays.asList(accuracy1, accuracy2, accuracy3, accuracy4));
        List<Label> powerLabels = new ArrayList<>(Arrays.asList(power1, power2, power3, power4));
        List<Label> descriptionLabels = new ArrayList<>(Arrays.asList(description1, description2, description3, description4));

        disposables.add(presetsService.getAbilities().observeOn(FX_SCHEDULER).subscribe(abilities -> {
            int i = 0;
            for (Map.Entry<String, Integer> entry : monster.abilities().entrySet()) {
                AbilityDto ability = abilities.get(Integer.parseInt(entry.getKey()) - 1);
                abilityLabels.get(i).setText(ability.name() + " " + entry.getValue() + "/" + ability.maxUses());
                accuracyLabels.get(i).setText(resources.getString("ACCURACY") + ability.accuracy());
                powerLabels.get(i).setText(resources.getString("POWER") + ability.power());
                descriptionLabels.get(i).setText(ability.description());
                i++;
            }}));
    }


    public void goBackToMonsters() {
        Stage stage = (Stage) goBackMonstersButton.getScene().getWindow();
        stage.close();
        monstersListController.init();
        Scene scene = new Scene(monstersListController.render());
        stage.setScene(scene);
        stage.show();
    }
}
