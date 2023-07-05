package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.controller.subController.BattleMenuController;
import de.uniks.stpmon.team_m.controller.subController.EncounterOpponentController;
import de.uniks.stpmon.team_m.dto.Opponent;
import de.uniks.stpmon.team_m.service.EncounterOpponentsService;
import de.uniks.stpmon.team_m.service.MonstersService;
import de.uniks.stpmon.team_m.service.RegionEncountersService;
import de.uniks.stpmon.team_m.service.TrainersService;
import de.uniks.stpmon.team_m.utils.EncounterOpponentStorage;
import de.uniks.stpmon.team_m.utils.ImageProcessor;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Encounter2Controller extends Controller {
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
    MonstersService monstersService;
    @Inject
    TrainersService trainersService;

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

    private int opponentsSize;
    private String regionId;
    private String encounterId;
    private String trainerId;

    @Inject
    public Encounter2Controller() {
    }

    @Override
    public void init() {
        super.init();
        opponentsSize = encounterOpponentStorage.getEncounterSize();
        regionId = trainerStorage.getRegion()._id();
        encounterId = encounterOpponentStorage.getEncounterId();
        trainerId = trainerStorage.getTrainer()._id();
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
                        battleMenuController.showFleeButton(true);
                    } else {
                        if (opponentsSize == 2) {
                            renderFor1vs1(ownTrainerParent);
                        } else if (opponentsSize == 3) {
                            renderFor1vs2(ownTrainerParent);
                        } else {
                            renderFor2vs2(ownTrainerParent);
                        }
                        battleMenuController.showFleeButton(false);
                    }
                }));
        return parent;
    }

    @Override
    public String getTitle() {
        return resources.getString("ENCOUNTER");
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
        disposables.add(monstersService.getMonster(regionId, opponent.trainer(), opponent.monster())
                .observeOn(FX_SCHEDULER).subscribe(monster -> {
                    encounterOpponentStorage.setCurrentEnemyMonster(monster);
                    encounterOpponentController.setLevelLabel(String.valueOf(monster.level()))
                            .setHealthBarValue((double) monster.currentAttributes().health() / monster.attributes().health());
                    disposables.add(presetsService.getMonster(monster.type())
                            .observeOn(FX_SCHEDULER).subscribe(m -> {
                                encounterOpponentController.setMonsterNameLabel(m.name());
                                encounterOpponentStorage.setCurrentEnemyMonsterType(m);
                                battleDialogText.setText(resources.getString("ENCOUNTER_DESCRIPTION_BEGIN") + " " + m.name());
                            }, Throwable::printStackTrace));
                    disposables.add(presetsService.getMonsterImage(monster.type())
                            .observeOn(FX_SCHEDULER).subscribe(mImage -> {
                                Image enemyMonsterImage = ImageProcessor.resonseBodyToJavaFXImage(mImage);
                                encounterOpponentController.setMonsterImage(enemyMonsterImage);
                            }, Throwable::printStackTrace));
                }, Throwable::printStackTrace));
    }

    private void showEnemyInfo(EncounterOpponentController encounterOpponentController, Opponent opponent) {
        // Trainer Sprite
        disposables.add(trainersService.getTrainer(regionId, opponent.trainer())
                .observeOn(FX_SCHEDULER).subscribe(trainer -> {
                    battleDialogText.setText(resources.getString("ENCOUNTER_DESCRIPTION_BEGIN") + " " + trainer.name());
                    ImageView opponentTrainer = encounterOpponentController.getTrainerImageView();
                    setTrainerSpriteImageView(trainer, opponentTrainer,3);
                }, Throwable::printStackTrace));

        // Monster
        showWildMonster(encounterOpponentController, opponent);
    }

    private void showOwnInfo(EncounterOpponentController encounterOpponentController, Opponent opponent) {
        // Monster
        disposables.add(monstersService.getMonster(regionId, trainerId, opponent.monster())
                .observeOn(FX_SCHEDULER).subscribe(monster -> {
                    encounterOpponentStorage.setCurrentTrainerMonster(monster);
                    encounterOpponentController.setLevelLabel(monster.level() + " LVL")
                            .setExperienceBarValue((double) monster.experience() / requiredExperience(monster.level() + 1))
                            .setHealthBarValue((double) monster.currentAttributes().health() / monster.attributes().health())
                            .setHealthLabel(monster.currentAttributes().health() + "/" + monster.attributes().health() + " HP");
                    //write monster name
                    disposables.add(presetsService.getMonster(monster.type())
                            .observeOn(FX_SCHEDULER).subscribe(m -> {
                                encounterOpponentController.setMonsterNameLabel(m.name());
                                encounterOpponentStorage.setCurrentTrainerMonsterType(m);
                            }, Throwable::printStackTrace));
                    disposables.add(presetsService.getMonsterImage(monster.type())
                            .observeOn(FX_SCHEDULER).subscribe(mImage -> {
                                Image myMonsterImage = ImageProcessor.resonseBodyToJavaFXImage(mImage);
                                encounterOpponentController.setMonsterImage(myMonsterImage);
                            }, Throwable::printStackTrace));
                }, Throwable::printStackTrace));

        // Own
        ImageView sprite = encounterOpponentController.getTrainerImageView();
        setTrainerSpriteImageView(trainerStorage.getTrainer(), sprite,1);
    }

    private void showCoopInfo(EncounterOpponentController encounterOpponentController, Opponent opponent) {
        encounterOpponentController.setMonsterImage(new Image(String.valueOf(Main.class.getResource("images/Monster2-color.png"))));
    }

    public int requiredExperience(int currentLevel) {
        return (int) (Math.pow(currentLevel, 3) - Math.pow(currentLevel - 1, 3));
    }
}
