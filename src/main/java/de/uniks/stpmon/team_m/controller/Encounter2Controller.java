package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.controller.subController.BattleMenuController;
import de.uniks.stpmon.team_m.controller.subController.EncounterOpponentController;
import de.uniks.stpmon.team_m.dto.Opponent;
import de.uniks.stpmon.team_m.service.EncounterOpponentsService;
import de.uniks.stpmon.team_m.service.RegionEncountersService;
import de.uniks.stpmon.team_m.utils.EncounterOpponentStorage;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
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
    boolean isWild = true;
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
    EncounterOpponentsService encounterOpponentsService;
    @Inject
    RegionEncountersService regionEncountersService;

    @Inject
    EncounterOpponentStorage encounterOpponentStorage;
    @Inject
    TrainerStorage trainerStorage;
    @Inject
    public BattleMenuController battleMenuController;

    private EncounterOpponentController enemy1Controller;
    private EncounterOpponentController enemy2Controller;
    private EncounterOpponentController ownTrainerController;
    private EncounterOpponentController coopTrainerController;

    private String regionId;
    private String encounterId;

    @Inject
    public Encounter2Controller() {
    }

    @Override
    public void init() {
        super.init();
        opponentsSize = encounterOpponentStorage.getEncounterSize();
        regionId = trainerStorage.getRegion()._id();
        encounterId = encounterOpponentStorage.getEncounterId();
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        // init battle menu
        battleMenuController.init(this, actionButtonVBox, encounterOpponentStorage);
        actionButtonVBox.getChildren().add(battleMenuController.render());

        // Init opponent controller for own trainer
        ownTrainerController = new EncounterOpponentController(false, false, true);
        ownTrainerController.init();
        Parent ownTrainerParent = ownTrainerController.render();
        showOwnInfo(ownTrainerController, encounterOpponentStorage.getSelfOpponent());

        disposables.add(regionEncountersService.getEncounter(regionId, encounterId)
                .observeOn(FX_SCHEDULER)
                .subscribe(encounter -> {
                    encounterOpponentStorage.setWild(encounter.isWild());
                    if (encounter.isWild()) {
                        renderForWild(ownTrainerParent);
                        //battleMenuController.showFleeButton();
                    } else {
                        if (opponentsSize == 2) {
                            renderFor1vs1(ownTrainerParent);
                        } else if (opponentsSize == 3) {
                            renderFor1vs2(ownTrainerParent);
                        } else {
                            renderFor2vs2(ownTrainerParent);
                        }
                    }
                }));
        return parent;
    }

    private void renderForWild(Parent ownTrainerParent) {
        // Wild situation

        // Wild monster (init opponent controller for enemy)
        enemy1Controller = new EncounterOpponentController(true, true, false);
        enemy1Controller.init();
        enemyHBox.setPadding(new Insets(0, 400, 0, 0));
        enemyHBox.getChildren().add(enemy1Controller.render());
        showWildMonster(enemy1Controller, encounterOpponentStorage.getEnemyOpponents().get(0));

        // Own trainer
        teamHBox.setPadding(new Insets(0, 0, 0, 400));
        teamHBox.getChildren().add(ownTrainerParent);
    }

    private void renderFor1vs1(Parent ownTrainerParent) {
        // 1 vs 1 situation

        // Enemy as a trainer (init opponent controller for the enemy)
        enemy1Controller = new EncounterOpponentController(true, false, false);
        enemy1Controller.init();
        Parent enemy1Parent = enemy1Controller.render();
        enemyHBox.setPadding(new Insets(0, 400, 0, 0));
        enemyHBox.getChildren().add(enemy1Parent);
        // setup controllers with data
        showEnemyInfo(enemy1Controller, encounterOpponentStorage.getEnemyOpponents().get(0));

        teamHBox.setPadding(new Insets(0, 0, 0, 400));
        teamHBox.getChildren().add(ownTrainerParent);

    }

    private void renderFor1vs2(Parent ownTrainerParent) {
        // 1 vs 2 situation

        // 1st Enemy
        enemy1Controller = new EncounterOpponentController(true, false, false);
        enemy1Controller.init();
        Parent enemy1Parent = enemy1Controller.render();
        enemyHBox.getChildren().add(enemy1Parent);
        showEnemyInfo(enemy1Controller, encounterOpponentStorage.getEnemyOpponents().get(0));

        // 2nd Enemy
        enemy2Controller = new EncounterOpponentController(true, false, true);
        enemy2Controller.init();
        enemyHBox.getChildren().add(enemy2Controller.render());
        showEnemyInfo(enemy2Controller, encounterOpponentStorage.getEnemyOpponents().get(1));

        teamHBox.setPadding(new Insets(0, 0, 0, 400));
        teamHBox.getChildren().add(ownTrainerParent);
    }

    private void renderFor2vs2(Parent ownTrainerParent) {
        // 2 vs 2 situation

        // 1st Enemy
        enemy1Controller = new EncounterOpponentController(true, false, false);
        enemy1Controller.init();
        Parent enemy1Parent = enemy1Controller.render();
        enemyHBox.getChildren().add(enemy1Parent);
        showEnemyInfo(enemy1Controller, encounterOpponentStorage.getEnemyOpponents().get(0));

        // 2nd Enemy
        enemy2Controller = new EncounterOpponentController(true, false, true);
        enemy2Controller.init();
        enemyHBox.getChildren().add(enemy2Controller.render());
        showEnemyInfo(enemy2Controller, encounterOpponentStorage.getEnemyOpponents().get(1));

        coopTrainerController = new EncounterOpponentController(false, false, false);
        coopTrainerController.init();
        teamHBox.getChildren().add(coopTrainerController.render());
        showCoopInfo(coopTrainerController, encounterOpponentStorage.getCoopOpponent());
        teamHBox.getChildren().add(ownTrainerParent);
        // TODO: setup controllers with data
    }

    // Hier soll allen Serveranfragen kommen
    private void showWildMonster(EncounterOpponentController encounterOpponentController, Opponent opponent){
        encounterOpponentController.setMonsterImage(new Image(String.valueOf(Main.class.getResource("images/Monster2-color.png"))));
    }

    private void showEnemyInfo(EncounterOpponentController encounterOpponentController, Opponent opponent) {
        encounterOpponentController.setHealthBarValue(enemyHealthBarValue)
                .setLevelLabel(String.valueOf(enemyLevel))
                .setMonsterNameLabel(enemyMonsterName);
    }

    private void showOwnInfo(EncounterOpponentController encounterOpponentController, Opponent opponent) {
        encounterOpponentController.setMonsterNameLabel(teamMonsterName)
                .setHealthBarValue(teamMonsterHeathBarValue)
                .setLevelLabel(String.valueOf(teamLevel))
                .setHealthLabel(String.valueOf(teamHeath))
                .setExperienceBarValue(teamLevelBarValue);
    }

    private void showCoopInfo(EncounterOpponentController encounterOpponentController, Opponent opponent) {
        encounterOpponentController.setMonsterImage(new Image(String.valueOf(Main.class.getResource("images/Monster2-color.png"))));
    }
}
