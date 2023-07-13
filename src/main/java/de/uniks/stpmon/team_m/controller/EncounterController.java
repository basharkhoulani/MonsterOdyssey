package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.Constants;
import de.uniks.stpmon.team_m.controller.subController.AbilitiesMenuController;
import de.uniks.stpmon.team_m.controller.subController.BattleMenuController;
import de.uniks.stpmon.team_m.controller.subController.EncounterOpponentController;
import de.uniks.stpmon.team_m.dto.*;
import de.uniks.stpmon.team_m.service.*;
import de.uniks.stpmon.team_m.utils.EncounterOpponentStorage;
import de.uniks.stpmon.team_m.utils.ImageProcessor;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import de.uniks.stpmon.team_m.ws.EventListener;
import javafx.animation.*;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import javafx.scene.text.TextAlignment;

import javax.inject.Inject;
import javax.inject.Provider;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;
import java.util.HashMap;


import static de.uniks.stpmon.team_m.Constants.*;


@Singleton
public class EncounterController extends Controller {
    @FXML
    public StackPane rootStackPane;
    @FXML
    public Text battleDialogText;
    @FXML
    public TextFlow battleDialogTextFlow;
    @FXML
    public VBox battleMenuVBox;
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
    TrainersService trainersService;
    @Inject
    MonstersService monstersService;
    @Inject
    PresetsService presetsService;
    @Inject
    Provider<IngameController> ingameControllerProvider;
    @Inject
    Provider<EventListener> eventListener;
    @Inject
    EncounterOpponentStorage encounterOpponentStorage;
    @Inject
    BattleMenuController battleMenuController;
    @Inject
    AbilitiesMenuController abilitiesMenuController;
    @Inject
    Provider<TrainerStorage> trainerStorageProvider;

    private EncounterOpponentController enemy1Controller;
    private EncounterOpponentController enemy2Controller;
    private EncounterOpponentController ownTrainerController;
    private EncounterOpponentController coopTrainerController;

    private int opponentsSize;
    private String regionId;
    private String encounterId;
    private String trainerId;

    private List<Controller> subControllers = new ArrayList<>();
    private int currentImageIndex = 0;
    private List<AbilityDto> abilityDtos = new ArrayList<>();
    private HashMap<String, Opponent> opponentsUpdate = new HashMap<>();
    private List<Opponent> opponentsDelete = new ArrayList<>();
    private int repeatedTimes = 0;

    // Hier finde ich mit dem Key (Opponent id) besser
    private HashMap<String, EncounterOpponentController> encounterOpponentControllerHashMap;

    @Inject
    public EncounterController() {
    }

    @Override
    public void init() {
        super.init();
        opponentsSize = encounterOpponentStorage.getEncounterSize();
        regionId = trainerStorageProvider.get().getRegion()._id();
        encounterId = encounterOpponentStorage.getEncounterId();
        trainerId = trainerStorageProvider.get().getTrainer()._id();
        listenToOpponents(encounterId);
        battleMenuController.setValues(resources, preferences, resourceBundleProvider, battleMenuController, app);
        battleMenuController.init();
        subControllers.addAll(List.of(battleMenuController, abilitiesMenuController));
        encounterOpponentControllerHashMap = new HashMap<>();
        if (!GraphicsEnvironment.isHeadless() && !AudioService.getInstance().checkMuted()) {
            AudioService.getInstance().stopSound();
            AudioService.getInstance().playSound(FIGHT_SOUND);
            AudioService.getInstance().setCurrentSound(FIGHT_SOUND);
            AudioService.getInstance().setVolume(preferences.getDouble("volume", AudioService.getInstance().getVolume()));
        }
    }

    public String getTitle() {
        return resources.getString("ENCOUNTER");
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        // init battle menu
        battleMenuController.init(this, battleMenuVBox, encounterOpponentStorage, app);
        battleMenuVBox.getChildren().add(battleMenuController.render());
        battleMenuController.onFleeButtonClick = this::onFleeButtonClick;

        // Init opponent controller for own trainer
        ownTrainerController = new EncounterOpponentController(false, false, true, false);
        ownTrainerController.init();
        encounterOpponentControllerHashMap.put(encounterOpponentStorage.getSelfOpponent()._id(), ownTrainerController);
        Parent ownTrainerParent = ownTrainerController.render();
        HBox.setHgrow(ownTrainerParent, javafx.scene.layout.Priority.ALWAYS);
        //showMyMonster
        showTeamMonster(ownTrainerController, encounterOpponentStorage.getSelfOpponent(), true);
        // showMySprite
        ImageView sprite = ownTrainerController.getTrainerImageView();
        setTrainerSpriteImageView(trainerStorageProvider.get().getTrainer(), sprite, 1);

        disposables.add(presetsService.getAbilities().observeOn(FX_SCHEDULER).subscribe(abilities -> abilityDtos.addAll(abilities)));

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

    private void renderForWild(Parent ownTrainerParent) {
        // Wild situation

        // Wild monster (init opponent controller for enemy)
        enemy1Controller = new EncounterOpponentController(true, true, false, false);
        enemy1Controller.init();
        encounterOpponentControllerHashMap.put(encounterOpponentStorage.getEnemyOpponents().get(0)._id(), enemy1Controller);
        enemyHBox.setPadding(new Insets(0, 400, 0, 0));
        Parent enemy1Parent = enemy1Controller.render();
        HBox.setHgrow(enemy1Parent, javafx.scene.layout.Priority.ALWAYS);
        enemyHBox.getChildren().add(enemy1Parent);
        targetOpponent(encounterOpponentStorage.getEnemyOpponents().get(0));
        showWildMonster(enemy1Controller, encounterOpponentStorage.getEnemyOpponents().get(0), true);

        // Own trainer
        teamHBox.setPadding(new Insets(0, 0, 0, 400));
        teamHBox.getChildren().add(ownTrainerParent);
    }

    private void renderFor1vs1(Parent ownTrainerParent) {
        // 1 vs 1 situation

        // Enemy as a trainer (init opponent controller for the enemy)
        enemy1Controller = new EncounterOpponentController(true, false, false, false);
        enemy1Controller.init();
        encounterOpponentControllerHashMap.put(encounterOpponentStorage.getEnemyOpponents().get(0)._id(), enemy1Controller);
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
        encounterOpponentControllerHashMap.put(encounterOpponentStorage.getEnemyOpponents().get(0)._id(), enemy1Controller);
        VBox enemy1Parent = (VBox) enemy1Controller.render();
        HBox.setHgrow(enemy1Parent, javafx.scene.layout.Priority.ALWAYS);
        enemyHBox.getChildren().add(enemy1Parent);
        targetOpponent(encounterOpponentStorage.getEnemyOpponents().get(0));
        showEnemyInfo(enemy1Controller, encounterOpponentStorage.getEnemyOpponents().get(0));

        // 2nd enemy as a trainer
        enemy2Controller = new EncounterOpponentController(true, false, true, true);
        enemy2Controller.init();
        encounterOpponentControllerHashMap.put(encounterOpponentStorage.getEnemyOpponents().get(1)._id(), enemy2Controller);
        VBox enemy2Parent = (VBox) enemy2Controller.render();
        HBox.setHgrow(enemy2Parent, javafx.scene.layout.Priority.ALWAYS);
        enemyHBox.getChildren().add(enemy2Parent);
        showEnemyInfo(enemy2Controller, encounterOpponentStorage.getEnemyOpponents().get(1));

        // Own trainer
        teamHBox.setPadding(new Insets(0, 0, 0, 450));
        teamHBox.getChildren().add(ownTrainerParent);
    }

    private void renderFor2vs2(Parent ownTrainerParent) {
        // 2 vs 2 situation

        // 1st enemy as a trainer
        enemy1Controller = new EncounterOpponentController(true, false, false, true);
        enemy1Controller.init();
        encounterOpponentControllerHashMap.put(encounterOpponentStorage.getEnemyOpponents().get(0)._id(), enemy1Controller);
        VBox enemy1Parent = (VBox) enemy1Controller.render();
        HBox.setHgrow(enemy1Parent, javafx.scene.layout.Priority.ALWAYS);
        enemyHBox.getChildren().add(enemy1Parent);
        targetOpponent(encounterOpponentStorage.getEnemyOpponents().get(0));
        showEnemyInfo(enemy1Controller, encounterOpponentStorage.getEnemyOpponents().get(0));

        // 2nd enemy as a trainer
        enemy2Controller = new EncounterOpponentController(true, false, true, true);
        enemy2Controller.init();
        encounterOpponentControllerHashMap.put(encounterOpponentStorage.getEnemyOpponents().get(1)._id(), enemy2Controller);
        VBox enemy2Parent = (VBox) enemy2Controller.render();
        HBox.setHgrow(enemy2Parent, javafx.scene.layout.Priority.ALWAYS);
        enemyHBox.getChildren().add(enemy2Parent);
        showEnemyInfo(enemy2Controller, encounterOpponentStorage.getEnemyOpponents().get(1));

        // Coop Trainer
        coopTrainerController = new EncounterOpponentController(false, false, false, false);
        coopTrainerController.init();
        encounterOpponentControllerHashMap.put(encounterOpponentStorage.getCoopOpponent()._id(), coopTrainerController);
        Parent coopTrainerParent = coopTrainerController.render();
        HBox.setHgrow(coopTrainerParent, javafx.scene.layout.Priority.ALWAYS);
        teamHBox.getChildren().add(coopTrainerParent);
        showCoopImage(coopTrainerController, encounterOpponentStorage.getCoopOpponent());
        showTeamMonster(coopTrainerController, encounterOpponentStorage.getCoopOpponent(), false);

        // Own trainer
        teamHBox.getChildren().add(ownTrainerParent);
    }

    private void targetOpponent(Opponent opponent) {
        if (encounterOpponentControllerHashMap.containsKey(opponent._id())) {
            if (ownTrainerController.getCurrentTarget() != null && encounterOpponentControllerHashMap.containsKey(ownTrainerController.getCurrentTarget()._id())) {
                encounterOpponentControllerHashMap.get(ownTrainerController.getCurrentTarget()._id()).unTarget();
            }
            if (ownTrainerController.getCurrentTarget() != opponent) {
                ownTrainerController.setCurrentTarget(opponent);
            }
            if (encounterOpponentControllerHashMap.get(opponent._id()).isMultipleEnemyEncounter) {
                encounterOpponentControllerHashMap.get(opponent._id()).onTarget();
            }

        }
    }

    // Hier soll allen Serveranfragen kommen
    private void showWildMonster(EncounterOpponentController encounterOpponentController, Opponent opponent, boolean isInit) {
        disposables.add(monstersService.getMonster(regionId, opponent.trainer(), opponent.monster())
                .observeOn(FX_SCHEDULER).subscribe(monster -> {
                    encounterOpponentStorage.addCurrentMonster(monster);
                    encounterOpponentController.setLevelLabel("Lvl " + monster.level())
                            .setHealthBarValue((double) monster.currentAttributes().health() / monster.attributes().health());
                    disposables.add(presetsService.getMonster(monster.type())
                            .observeOn(FX_SCHEDULER).subscribe(m -> {
                                encounterOpponentController.setMonsterNameLabel(m.name());
                                encounterOpponentStorage.addCurrentMonsterType(m);
                                if (encounterOpponentStorage.isWild() && isInit) {
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
        if (opponent != null) {
            // Monster
            showWildMonster(encounterOpponentController, opponent, true);

            // Trainer Sprite
            disposables.add(trainersService.getTrainer(regionId, opponent.trainer())
                    .observeOn(FX_SCHEDULER).subscribe(trainer -> {
                        battleDialogText.setText(resources.getString("ENCOUNTER_DESCRIPTION_BEGIN") + " " + trainer.name());
                        ImageView opponentTrainer = encounterOpponentController.getTrainerImageView();
                        setTrainerSpriteImageView(trainer, opponentTrainer, TRAINER_DIRECTION_DOWN);
                    }, Throwable::printStackTrace));
        }
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
        rootStackPane.getChildren().add(this.buildFleePopup());
    }


    @Override
    public void destroy() {
        super.destroy();
        subControllers.forEach(Controller::destroy);
        //add destroy methode for the elements in encounterOpponentControllerHashMap
        encounterOpponentControllerHashMap.values().forEach(Controller::destroy);
    }

    public void listenToOpponents(String encounterId) {
        disposables.add(eventListener.get().listen("encounters." + encounterId + ".trainers.*.opponents.*.*", Opponent.class)
                .observeOn(FX_SCHEDULER).subscribe(event -> {
                    final Opponent opponent = event.data();
                    if(event.suffix().contains("updated")){
                        updateOpponent(opponent);
                    }
                }, error -> showError(error.getMessage())));
    }

    // manage the response of all the opponents (include the move and all results after the move, there are 2 Updates per opponent)
    private void updateOpponent(Opponent opponent) {
        // For komplexer Situation for example with more opponents should be considered in the future
        String trainerId = trainerStorageProvider.get().getTrainer()._id();
        if(opponent.move() != null){
            opponentsUpdate.put(opponent._id() + "Move", opponent);
        } else if (opponent.results().size() != 0){
            if (opponent.trainer().equals(trainerId)) {
                encounterOpponentStorage.setSelfOpponent(opponent);
            } else if (opponent.isAttacker() == encounterOpponentStorage.isAttacker()){
                encounterOpponentStorage.setCoopOpponent(opponent);
            } else {
                encounterOpponentStorage.getEnemyOpponents().removeIf(o -> o._id().equals(opponent._id()));
                encounterOpponentStorage.addEnemyOpponent(opponent);
            }
            opponentsUpdate.put(opponent._id() + "Results", opponent);
        }

        // this magic number is two time the size of oppenents in this encounter
        if (opponentsUpdate.size() >= 2 * encounterOpponentStorage.getEncounterSize()) {
            writeBattleDescription(opponentsUpdate);
        }
    }

    private void writeBattleDescription(HashMap<String, Opponent> forDescription) {
        updateDescription(EMPTY_STRING, true);
        List<String> opponentsInStorage = encounterOpponentStorage.getOpponentsInStorage();
        for (String opponentId:opponentsInStorage){
            Opponent o = forDescription.get(opponentId + "Move");
            Move move = o.move();
            if (move instanceof AbilityMove abilityMove) {
                if (o.trainer().equals(trainerStorageProvider.get().getTrainer()._id())) {
                    updateDescription(resources.getString("YOU.USED")+ " " + abilityDtos.get(abilityMove.ability()-1).name() + ". ", false);
                } else if (o.isAttacker() == encounterOpponentStorage.isAttacker()) {
                    updateDescription(resources.getString("MATE.USED")+ " " + abilityDtos.get(abilityMove.ability()-1).name() + ". ", false);
                } else {
                    updateDescription(resources.getString("ENEMY.USED")+ " " + abilityDtos.get(abilityMove.ability()-1).name() + ". ", false);
                }
            } else {
                // else for change monster move
                // here for the situation that change Monster Move
                System.out.println("Change Monster");
            }

            Opponent oResults = forDescription.get(opponentId + "Results");
            System.out.println("Opponent Results: " + oResults);
            for (Result r : oResults.results()) {
                System.out.println("Result: " + r);
                switch (r.type()){
                    case "ability-success" -> {
                        updateDescription(abilityDtos.get(r.ability() - 1).name() + " " + resources.getString("IS") + r.effectiveness() + ".\n", false);
                        if (o.monster() != null) {
                            updateMonsterValues(o.trainer(), o.monster(), o);
                        }
                    }
                    case "target-defeated" -> updateDescription(resources.getString("TARGET.DEFEATED"), false);
                    // @Tobias: Here you can add the other cases
                    default -> updateDescription(resources.getString("NOTHING.HAPPENED"), false);
                }
            }
        }
    }

    public void showAbilities() {
        battleMenuVBox.getChildren().clear();
        Monster monster = encounterOpponentStorage.getCurrentTrainerMonster();
        subControllers.add(abilitiesMenuController);
        if (encounterOpponentStorage.getSelfOpponent().monster() != null) {
            abilitiesMenuController.init(monster, presetsService, battleMenuVBox, this);
        } else {
            abilitiesMenuController.init(null, presetsService, battleMenuVBox, this);
        }
        battleMenuVBox.getChildren().add(abilitiesMenuController.render());
    }

    public void goBackToBattleMenu() {
        battleMenuVBox.getChildren().clear();
        battleMenuController.init(this, battleMenuVBox, encounterOpponentStorage, app);
        battleMenuVBox.getChildren().add(battleMenuController.render());
    }

    public void fleeFromBattle(Event event) {
        SequentialTransition fleeAnimation = buildFleeAnimation();
        PauseTransition firstPause = new PauseTransition(Duration.millis(500));
        battleDialogText.setText(resources.getString("ENCOUNTER_DESCRIPTION_FLEE"));

        firstPause.setOnFinished(evt -> {
            //setVisible the image of the own monster
            //myMonster.setVisible(false);
            fleeAnimation.play();
        });
        fleeAnimation.setOnFinished(evt -> disposables.add(encounterOpponentsService.deleteOpponent(
                encounterOpponentStorage.getRegionId(),
                encounterOpponentStorage.getEncounterId(),
                encounterOpponentStorage.getSelfOpponent()._id()
        ).observeOn(FX_SCHEDULER).subscribe(
                result -> {
                    destroy();
                    app.show(ingameControllerProvider.get());
                }, error -> {
                    showError(error.getMessage());
                    error.printStackTrace();
                })));
        firstPause.play();
    }

    private SequentialTransition buildFleeAnimation() {
        SequentialTransition transition = new SequentialTransition();

        Image[] images = ImageProcessor.cropTrainerImages(trainerStorageProvider.get().getTrainerSpriteChunk(), TRAINER_DIRECTION_DOWN, true);

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

    private VBox buildFleePopup() {
        // base VBox
        VBox fleeVBox = new VBox();
        fleeVBox.setId("fleePopup");
        fleeVBox.setMaxWidth(fleePopupWidth);
        fleeVBox.setMaxHeight(fleePopupHeight);
        fleeVBox.getStyleClass().add("dialogTextFlow");
        fleeVBox.setAlignment(Pos.CENTER);

        // flee TextFlow
        TextFlow fleeTextFlow = new TextFlow();
        fleeTextFlow.setMaxWidth(fleePopupWidth);
        fleeTextFlow.setMaxHeight(fleeTextHeight);
        fleeTextFlow.setPrefWidth(fleePopupWidth);
        fleeTextFlow.setPrefHeight(fleeTextHeight);
        fleeTextFlow.setPadding(fleeTextInsets);
        fleeTextFlow.setTextAlignment(TextAlignment.CENTER);

        // flee Text
        Text fleeText = new Text(this.resources.getString("ENCOUNTER_FLEE_TEXT"));
        fleeTextFlow.getChildren().add(fleeText);

        // buttons HBox
        HBox buttonHBox = new HBox();
        buttonHBox.setMaxWidth(fleePopupWidth);
        buttonHBox.setMaxHeight(fleeButtonsHBoxHeight);
        buttonHBox.setPrefWidth(fleePopupWidth);
        buttonHBox.setPrefHeight(fleeButtonsHBoxHeight);
        buttonHBox.setPadding(fleeButtonsHBoxInsets);
        buttonHBox.setAlignment(Pos.TOP_CENTER);
        buttonHBox.setSpacing(buttonsHBoxSpacing);

        // yes Button
        Button yesButton = new Button(this.resources.getString("ENCOUNTER_FLEE_CONFIRM_BUTTON"));
        yesButton.setMaxWidth(fleeButtonWidth);
        yesButton.setMinHeight(fleeButtonHeight);
        yesButton.setPrefWidth(fleeButtonWidth);
        yesButton.setPrefHeight(fleeButtonHeight);
        yesButton.getStyleClass().add("hBoxRed");
        //remove the fleeVox from the root BorderPane
        //rootStackPane.getChildren().remove(fleeVBox);
        yesButton.setOnAction(this::fleeFromBattle);

        // no Button
        Button noButton = new Button(this.resources.getString("ENCOUNTER_FLEE_CANCEL_BUTTON"));
        noButton.setMaxWidth(fleeButtonWidth);
        noButton.setMinHeight(fleeButtonHeight);
        noButton.setPrefWidth(fleeButtonWidth);
        noButton.setPrefHeight(fleeButtonHeight);
        noButton.getStyleClass().add("hBoxYellow");
        //noButton.setOnAction(event -> rootStackPane.getChildren().remove(fleeVBox));

        // add buttons to hbox
        buttonHBox.getChildren().addAll(yesButton, noButton);

        // add textFlow and buttonHBox to VBox
        fleeVBox.getChildren().addAll(fleeTextFlow, buttonHBox);

        return fleeVBox;
    }

    public void updateDescription(String information, boolean isUpdated) {
        if (isUpdated) {
            battleDialogText.setText(information);
        } else {
            battleDialogText.setText(battleDialogText.getText() + information);
        }
    }


    private void updateMonsterValues(String trainerId, String monsterId, Opponent opponent) {
        EncounterOpponentController e = encounterOpponentControllerHashMap.get(opponent._id());
        if(monsterId == null) {
            if(e.getEnemy()){
                e.setHealthBarValue(0);
            } else {
                e.setHealthBarValue(0)
                        .setHealthLabel(0 + " HP");
            }
        } else {
            if (e.getEnemy()) {
                showWildMonster(e, opponent, false);
            } else showTeamMonster(e, opponent, trainerId.equals(trainerStorageProvider.get().getTrainer()._id()));
        }

    }


    public void resetOppoenentUpdate() {
        opponentsUpdate.clear();
    }

    public void resetRepeatedTimes() {
        this.repeatedTimes = 0;
    }
}
