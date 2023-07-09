package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.Constants;
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
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.HashMap;

import static de.uniks.stpmon.team_m.Constants.TRAINER_DIRECTION_DOWN;

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

    @Inject
    public Provider<TrainerStorage> trainerStorageProvider;

    @Inject
    public IngameController ingameController;


    private EncounterOpponentController enemy1Controller;
    private EncounterOpponentController enemy2Controller;
    private EncounterOpponentController ownTrainerController;
    private EncounterOpponentController coopTrainerController;
    private int currentImageIndex;

    private int opponentsSize;
    private String regionId;
    private String encounterId;
    private String trainerId;

    private HashMap<Opponent, EncounterOpponentController> encounterOpponentControllerHashMap;

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
        encounterOpponentControllerHashMap = new HashMap<>();
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        // init battle menu
        battleMenuController.init(this, actionButtonVBox, encounterOpponentStorage);
        actionButtonVBox.getChildren().add(battleMenuController.render());
        battleMenuController.onFleeButtonClick = this::onFleeButtonClick;

        // Init opponent controller for own trainer
        ownTrainerController = new EncounterOpponentController(false, false, true, false);
        ownTrainerController.init();
        encounterOpponentControllerHashMap.put(encounterOpponentStorage.getSelfOpponent(), ownTrainerController);
        Parent ownTrainerParent = ownTrainerController.render();
        HBox.setHgrow(ownTrainerParent, javafx.scene.layout.Priority.ALWAYS);
        showTeamMonster(ownTrainerController, encounterOpponentStorage.getSelfOpponent(), true);
        // showMySprite
        ImageView sprite = ownTrainerController.getTrainerImageView();
        setTrainerSpriteImageView(trainerStorage.getTrainer(), sprite, 1);

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
        enemy1Controller = new EncounterOpponentController(true, true, false, false);
        enemy1Controller.init();
        encounterOpponentControllerHashMap.put(encounterOpponentStorage.getEnemyOpponents().get(0), enemy1Controller);
        enemyHBox.setPadding(new Insets(0, 400, 0, 0));
        Parent enemy1Parent = enemy1Controller.render();
        HBox.setHgrow(enemy1Parent, javafx.scene.layout.Priority.ALWAYS);
        enemyHBox.getChildren().add(enemy1Parent);
        targetOpponent(encounterOpponentStorage.getEnemyOpponents().get(0));
        showWildMonster(enemy1Controller, encounterOpponentStorage.getEnemyOpponents().get(0));

        // Own trainer
        teamHBox.setPadding(new Insets(0, 0, 0, 400));
        teamHBox.getChildren().add(ownTrainerParent);
    }

    private void renderFor1vs1(Parent ownTrainerParent) {
        // 1 vs 1 situation

        // Enemy as a trainer (init opponent controller for the enemy)
        enemy1Controller = new EncounterOpponentController(true, false, false, false);
        enemy1Controller.init();
        encounterOpponentControllerHashMap.put(encounterOpponentStorage.getEnemyOpponents().get(0), enemy1Controller);
        Parent enemy1Parent = enemy1Controller.render();
        HBox.setHgrow(enemy1Parent, javafx.scene.layout.Priority.ALWAYS);
        enemyHBox.getChildren().add(enemy1Parent);
        targetOpponent(encounterOpponentStorage.getEnemyOpponents().get(0));
        showEnemyInfo(enemy1Controller, encounterOpponentStorage.getEnemyOpponents().get(0));

        // Own trainer
        teamHBox.setPadding(new Insets(0, 0, 0, 400));
        teamHBox.getChildren().add(ownTrainerParent);
    }

    private void renderFor1vs2(Parent ownTrainerParent) {
        // 1 vs 2 situation

        // 1st enemy as a trainer
        enemy1Controller = new EncounterOpponentController(true, false, false, true);
        enemy1Controller.init();
        encounterOpponentControllerHashMap.put(encounterOpponentStorage.getEnemyOpponents().get(0), enemy1Controller);
        VBox enemy1Parent = (VBox) enemy1Controller.render();
        HBox.setHgrow(enemy1Parent, javafx.scene.layout.Priority.ALWAYS);
        enemyHBox.getChildren().add(enemy1Parent);
        targetOpponent(encounterOpponentStorage.getEnemyOpponents().get(0));
        showEnemyInfo(enemy2Controller, encounterOpponentStorage.getEnemyOpponents().get(0));

        // 2nd enemy as a trainer
        enemy2Controller = new EncounterOpponentController(true, false, true, true);
        enemy2Controller.init();
        encounterOpponentControllerHashMap.put(encounterOpponentStorage.getEnemyOpponents().get(1), enemy2Controller);
        VBox enemy2Parent = (VBox) enemy2Controller.render();
        HBox.setHgrow(enemy2Parent, javafx.scene.layout.Priority.ALWAYS);
        enemyHBox.getChildren().add(enemy2Parent);
        showEnemyInfo(enemy2Controller, encounterOpponentStorage.getEnemyOpponents().get(1));

        // Own trainer
        teamHBox.setPadding(new Insets(0, 0, 0, 450));
        teamHBox.getChildren().add(ownTrainerParent);
    }

    private void targetOpponent(Opponent opponent) {
        if (encounterOpponentControllerHashMap.containsKey(opponent)) {
            if (ownTrainerController.getCurrentTarget() != opponent) {
                ownTrainerController.setCurrentTarget(opponent);
            }
            if (encounterOpponentControllerHashMap.get(opponent).isMultipleEnemyEncounter) {
                encounterOpponentControllerHashMap.get(opponent).onTarget();
            }

        }
    }

    private void renderFor2vs2(Parent ownTrainerParent) {
        // 2 vs 2 situation

        // 1st enemy as a trainer
        enemy1Controller = new EncounterOpponentController(true, false, false, true);
        enemy1Controller.init();
        encounterOpponentControllerHashMap.put(encounterOpponentStorage.getEnemyOpponents().get(0), enemy1Controller);
        VBox enemy1Parent = (VBox) enemy1Controller.render();
        HBox.setHgrow(enemy1Parent, javafx.scene.layout.Priority.ALWAYS);
        enemyHBox.getChildren().add(enemy1Parent);
        targetOpponent(encounterOpponentStorage.getEnemyOpponents().get(0));
        showEnemyInfo(enemy2Controller, encounterOpponentStorage.getEnemyOpponents().get(0));

        // 2nd enemy as a trainer
        enemy2Controller = new EncounterOpponentController(true, false, true, true);
        enemy2Controller.init();
        encounterOpponentControllerHashMap.put(encounterOpponentStorage.getEnemyOpponents().get(1), enemy2Controller);
        VBox enemy2Parent = (VBox) enemy2Controller.render();
        HBox.setHgrow(enemy2Parent, javafx.scene.layout.Priority.ALWAYS);
        enemyHBox.getChildren().add(enemy2Parent);
        showEnemyInfo(enemy2Controller, encounterOpponentStorage.getEnemyOpponents().get(1));

        // Coop Trainer
        coopTrainerController = new EncounterOpponentController(false, false, false, false);
        coopTrainerController.init();
        encounterOpponentControllerHashMap.put(encounterOpponentStorage.getCoopOpponent(), coopTrainerController);
        Parent coopTrainerParent = coopTrainerController.render();
        HBox.setHgrow(coopTrainerParent, javafx.scene.layout.Priority.ALWAYS);
        teamHBox.getChildren().add(coopTrainerParent);
        showCoopImage(coopTrainerController, encounterOpponentStorage.getCoopOpponent());
        showTeamMonster(coopTrainerController, encounterOpponentStorage.getCoopOpponent(), false);

        // Own trainer
        teamHBox.getChildren().add(ownTrainerParent);
    }

    // Hier soll allen Serveranfragen kommen
    private void showWildMonster(EncounterOpponentController encounterOpponentController, Opponent opponent) {
        disposables.add(monstersService.getMonster(regionId, opponent.trainer(), opponent.monster())
                .observeOn(FX_SCHEDULER).subscribe(monster -> {
                    encounterOpponentStorage.addCurrentMonster(monster);
                    encounterOpponentController.setLevelLabel(String.valueOf(monster.level()))
                            .setHealthBarValue((double) monster.currentAttributes().health() / monster.attributes().health());
                    disposables.add(presetsService.getMonster(monster.type())
                            .observeOn(FX_SCHEDULER).subscribe(m -> {
                                encounterOpponentController.setMonsterNameLabel(m.name());
                                encounterOpponentStorage.addCurrentMonsterType(m);
                                if (encounterOpponentStorage.isWild()) {
                                    battleDialogText.setText(resources.getString("ENCOUNTER_DESCRIPTION_BEGIN") + " " + m.name());
                                }
                            }, Throwable::printStackTrace));
                    disposables.add(presetsService.getMonsterImage(monster.type())
                            .observeOn(FX_SCHEDULER).subscribe(mImage -> {
                                Image enemyMonsterImage = ImageProcessor.resonseBodyToJavaFXImage(mImage);
                                encounterOpponentController.setMonsterImage(enemyMonsterImage);
                            }, Throwable::printStackTrace));
                }, Throwable::printStackTrace));
    }

    private void showEnemyInfo(EncounterOpponentController encounterOpponentController, Opponent opponent) {
        // Monster
        showWildMonster(encounterOpponentController, opponent);

        // Trainer Sprite
        disposables.add(trainersService.getTrainer(regionId, opponent.trainer())
                .observeOn(FX_SCHEDULER).subscribe(trainer -> {
                    battleDialogText.setText(resources.getString("ENCOUNTER_DESCRIPTION_BEGIN") + " " + trainer.name());
                    ImageView opponentTrainer = encounterOpponentController.getTrainerImageView();
                    setTrainerSpriteImageView(trainer, opponentTrainer, TRAINER_DIRECTION_DOWN);
                }, Throwable::printStackTrace));
    }

    private void showTeamMonster(EncounterOpponentController encounterOpponentController, Opponent opponent, boolean isSelf) {
        // Monster
        disposables.add(monstersService.getMonster(regionId, trainerId, opponent.monster())
                .observeOn(FX_SCHEDULER).subscribe(monster -> {
                    if (isSelf) {
                        encounterOpponentStorage.setCurrentTrainerMonster(monster);
                    }
                    encounterOpponentStorage.addCurrentMonster(monster);
                    encounterOpponentController.setLevelLabel("LVL " + monster.level())
                            .setExperienceBarValue((double) monster.experience() / requiredExperience(monster.level() + 1))
                            .setHealthBarValue((double) monster.currentAttributes().health() / monster.attributes().health())
                            .setHealthLabel(monster.currentAttributes().health() + "/" + monster.attributes().health() + " HP");
                    //write monster name
                    disposables.add(presetsService.getMonster(monster.type())
                            .observeOn(FX_SCHEDULER).subscribe(m -> {
                                encounterOpponentController.setMonsterNameLabel(m.name());
                                if (isSelf) {
                                    encounterOpponentStorage.setCurrentTrainerMonsterType(m);
                                }
                                encounterOpponentStorage.addCurrentMonsterType(m);
                            }, Throwable::printStackTrace));
                    disposables.add(presetsService.getMonsterImage(monster.type())
                            .observeOn(FX_SCHEDULER).subscribe(mImage -> {
                                Image myMonsterImage = ImageProcessor.resonseBodyToJavaFXImage(mImage);
                                encounterOpponentController.setMonsterImage(myMonsterImage);
                            }, Throwable::printStackTrace));
                }, Throwable::printStackTrace));
    }

    private void showCoopImage(EncounterOpponentController encounterOpponentController, Opponent opponent) {
        disposables.add(trainersService.getTrainer(regionId, opponent.trainer())
                .observeOn(FX_SCHEDULER).subscribe(trainer -> {
                    ImageView opponentTrainer = encounterOpponentController.getTrainerImageView();
                    setTrainerSpriteImageView(trainer, opponentTrainer, 3);
                }, Throwable::printStackTrace));
    }


    public int requiredExperience(int currentLevel) {
        return (int) (Math.pow(currentLevel, 3) - Math.pow(currentLevel - 1, 3));
    }

    public void onFleeButtonClick() {
        SequentialTransition fleeAnimation = buildFleeAnimation();
        PauseTransition firstPause = new PauseTransition(Duration.millis(500));

        firstPause.setOnFinished(event -> {
            ownTrainerController.setMonsterImage(null);
            fleeAnimation.play();
        });

        fleeAnimation.setOnFinished(evt -> disposables.add(encounterOpponentsService.deleteOpponent(
                encounterOpponentStorage.getRegionId(),
                encounterOpponentStorage.getEncounterId(),
                encounterOpponentStorage.getSelfOpponent()._id()
        ).observeOn(FX_SCHEDULER).subscribe(
                result -> {
                    app.show(ingameController);
                    System.out.println("Deleted opponent: " + encounterOpponentStorage.getEnemyOpponents().get(0));
                }, error -> {
                    showError(error.getMessage());
                    error.printStackTrace();
                })));

        firstPause.play();
    }

    private SequentialTransition buildFleeAnimation() {
        SequentialTransition transition = new SequentialTransition();

        Image[] images = ImageProcessor.cropTrainerImages(trainerStorage.getTrainerSpriteChunk(), TRAINER_DIRECTION_DOWN, true);

        KeyFrame animationFrame = new KeyFrame(Duration.millis(Constants.DELAY), event -> {
            ownTrainerController.setTrainerImage(images[currentImageIndex]);
            currentImageIndex = (currentImageIndex + 1) % 6;
        });
        KeyFrame movementFrame = new KeyFrame(Duration.millis(Constants.DELAY), evt -> {
            TranslateTransition translateTransition = new TranslateTransition();
            translateTransition.setNode(ownTrainerController.trainerMonsterVBox);
            translateTransition.setByY(16);
            translateTransition.setDuration(Duration.millis(Constants.DELAY));
            translateTransition.setCycleCount(1);
            translateTransition.play();
        });
        for (int i = 0; i < 12; i++) {
            Timeline fleeAnimation = new Timeline(animationFrame);
            Timeline trainerMovement = new Timeline(movementFrame);
            ParallelTransition parallelTransition = new ParallelTransition(fleeAnimation, trainerMovement);
            transition.getChildren().add(parallelTransition);
        }
        return transition;
    }
}
