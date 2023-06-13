package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.dto.MonsterAttributes;
import de.uniks.stpmon.team_m.dto.MonsterTypeDto;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.LinkedHashMap;


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

    @Inject
    Provider<IngameController> ingameControllerProvider;
    MonstersListController monstersListController;
    private Monster monster;
    private MonsterTypeDto monsterTypeDto;
    private Image monsterImage;

    @Override
    public Parent render() {
        final Parent parent = super.render();
        //initMonsterDetails();
        return parent;
    }

    public void init(MonstersListController monstersListController, Monster monster, MonsterTypeDto monsterTypeDto, Image monsterImage) {
        super.init();
        this.monstersListController = monstersListController;
        this.monster = monster;
        this.monsterTypeDto = monsterTypeDto;
        this.monsterImage = monsterImage;
    }

    @Inject
    public MonstersDetailController() {}

    /*private void initMonsterDetails(){
        Monster monster1 = new Monster("TestCreated", "TestUpdated", "123456789", "Me", 1, 2, 2, new LinkedHashMap<String, Integer>(), new MonsterAttributes(10, 5, 5, 5), new MonsterAttributes(10, 3, 3, 3));
        monsterName.setText(monster.createdAt());
        monsterLevel.setText("" + monster.level());
        monsterType.setText("" + monster.type());
        monsterExperience.setText("" + monster.experience());
    }
     */

    public void goBackToMonsters() {
        Stage stage = (Stage) goBackMonstersButton.getScene().getWindow();
        stage.close();
        monstersListController.init();
        Scene scene = new Scene(monstersListController.render());
        stage.setScene(scene);
        stage.show();
    }
}
