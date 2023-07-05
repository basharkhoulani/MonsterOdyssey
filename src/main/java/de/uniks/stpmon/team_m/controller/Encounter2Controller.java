package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.controller.subController.BattleMenuController;
import de.uniks.stpmon.team_m.controller.subController.EncounterOpponentController;
import de.uniks.stpmon.team_m.utils.EncounterOpponentStorage;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Encounter2Controller extends Controller {
    // Sample data
    int opponentsSize = 4;
    boolean isWild = false;
    String enemyMonsterName = "Flamuntel";
    double enemyHealthBarValue = 0.93;
    int enemyLevel = 8;
    String teamMonsterName = "Envias";
    double teamMonsterHeathBarValue = 0.93;
    int teamLevel = 9;
    int teamHeath = 100;
    double teamLevelBarValue = 0.93;


    @FXML
    public Text battleDialogText;
    @FXML
    public TextFlow battleDialogTextFlow;
    @FXML
    public VBox actionButtonVBox;
    @FXML
    public VBox battleBackgroundVBox;
    @FXML
    public HBox enemyHBox;
    @FXML
    public HBox teamHBox;

    @Inject
    EncounterOpponentStorage encounterOpponentStorage;

    @Inject
    public BattleMenuController battleMenuController;


    private EncounterOpponentController enemy1Controller;
    private EncounterOpponentController enemy2Controller;
    private EncounterOpponentController ownTrainerController;
    private EncounterOpponentController coopTrainerController;

    @Inject
    public Encounter2Controller() {
    }

    @Override
    public void init() {
        super.init();
        opponentsSize = encounterOpponentStorage.getEncounterSize();
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        // Init opponent controller for own trainer
        ownTrainerController = new EncounterOpponentController(false, false, true);
        ownTrainerController.init();
        VBox ownTrainerParent = (VBox) ownTrainerController.render();
        HBox.setHgrow(ownTrainerParent, javafx.scene.layout.Priority.ALWAYS);
        ownTrainerController.setMonsterNameLabel(teamMonsterName)
                            .setHealthBarValue(teamMonsterHeathBarValue)
                            .setLevelLabel(String.valueOf(teamLevel))
                            .setHealthLabel(String.valueOf(teamHeath))
                            .setExperienceBarValue(teamLevelBarValue);

        // Init opponent controller for the enemy
        enemy1Controller = new EncounterOpponentController(true, isWild, false);
        enemy1Controller.init();
        Parent enemy1Parent = enemy1Controller.render();
        HBox.setHgrow(enemy1Parent, javafx.scene.layout.Priority.ALWAYS);
        enemy1Controller.setHealthBarValue(enemyHealthBarValue)
                .setLevelLabel(String.valueOf(enemyLevel))
                .setMonsterNameLabel(enemyMonsterName);

        if (opponentsSize == 2) {
            renderForDuel(enemy1Parent, ownTrainerParent);
        }
        else if (opponentsSize == 3) {
            renderFor1vs2(enemy1Parent, ownTrainerParent);
        }
        else {
            renderFor2vs2(enemy1Parent, ownTrainerParent);
        }
        battleMenuController.init(this, actionButtonVBox, encounterOpponentStorage);
        actionButtonVBox.getChildren().add(battleMenuController.render());
        return parent;
    }

    private void renderForDuel(Parent enemy1Parent, Parent ownTrainerParent) {
        enemyHBox.setPadding(new Insets(0, 400, 0, 0));
        enemyHBox.getChildren().add(enemy1Parent);
        // TODO: setup controllers with data

        teamHBox.setPadding(new Insets(0, 0, 0, 400));
        teamHBox.getChildren().add(ownTrainerParent);
        // TODO: setup controllers with data

    }

    private void renderFor1vs2(Parent enemy1Parent, Parent ownTrainerParent) {
        // 1 vs 2 situation
        enemy2Controller = new EncounterOpponentController(true, false, true);
        enemy2Controller.init();
        enemyHBox.getChildren().add(enemy1Parent);
        VBox enemy2Parent = (VBox) enemy2Controller.render();
        HBox.setHgrow(enemy2Parent, javafx.scene.layout.Priority.ALWAYS);
        enemyHBox.getChildren().add(enemy2Parent);
        // TODO: setup controllers with data

        teamHBox.setPadding(new Insets(0, 0, 0, 400));
        teamHBox.getChildren().add(ownTrainerParent);
        // TODO: setup controllers with data
    }

    private void renderFor2vs2(Parent enemy1Parent, Parent ownTrainerParent) {
        // 2 vs 2 situation
        enemy2Controller = new EncounterOpponentController(true, false, true);
        enemy2Controller.init();
        VBox enemy2Parent = (VBox) enemy2Controller.render();
        HBox.setHgrow(enemy2Parent, javafx.scene.layout.Priority.ALWAYS);

        enemyHBox.getChildren().add(enemy1Parent);
        enemyHBox.getChildren().add(enemy2Parent);
        // TODO: setup controllers with data

        coopTrainerController = new EncounterOpponentController(false, false, false);
        coopTrainerController.init();
        VBox coopTrainerParent = (VBox) coopTrainerController.render();
        HBox.setHgrow(coopTrainerParent, javafx.scene.layout.Priority.ALWAYS);

        teamHBox.getChildren().add(coopTrainerParent);
        teamHBox.getChildren().add(ownTrainerParent);
        // TODO: setup controllers with data
        coopTrainerController.setMonsterImage(new Image(String.valueOf(Main.class.getResource("images/Monster2-color.png"))));
    }
}
