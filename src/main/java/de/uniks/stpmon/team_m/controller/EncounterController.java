package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.Constants;
import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.controller.subController.AbilitiesMenuController;
import de.uniks.stpmon.team_m.controller.subController.BattleMenuController;
import de.uniks.stpmon.team_m.controller.subController.EncounterOpponentController;
import de.uniks.stpmon.team_m.controller.subController.*;
import de.uniks.stpmon.team_m.dto.*;
import de.uniks.stpmon.team_m.service.*;
import de.uniks.stpmon.team_m.utils.AnimationBuilder;
import de.uniks.stpmon.team_m.utils.EncounterOpponentStorage;
import de.uniks.stpmon.team_m.utils.ImageProcessor;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import de.uniks.stpmon.team_m.ws.EventListener;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

import javax.inject.Inject;
import javax.inject.Provider;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static de.uniks.stpmon.team_m.Constants.*;


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
    EncounterResultController encounterResultController;
    @Inject
    AbilitiesMenuController abilitiesMenuController;
    @Inject
    Provider<TrainerStorage> trainerStorageProvider;
    @Inject
    Provider<MonstersDetailController> monstersDetailControllerProvider;
    @Inject
    Provider<ChangeMonsterListController> changeMonsterListControllerProvider;
    IngameController ingameController;
    @Inject
    Provider<LevelUpController> levelUpControllerProvider;
    @Inject
    Provider<EncounterController> encounterControllerProvider;

    private EncounterOpponentController enemy1Controller;
    private EncounterOpponentController enemy2Controller;
    private EncounterOpponentController ownTrainerController;
    private EncounterOpponentController coopTrainerController;

    private int opponentsSize;
    private String regionId;
    private String encounterId;
    private String trainerId;
    private final List<Controller> subControllers = new ArrayList<>();
    private final List<AbilityDto> abilityDtos = new ArrayList<>();
    private final HashMap<String, Opponent> opponentsUpdate = new HashMap<>();
    private final HashMap<String, Opponent> opponentsDelete = new HashMap<>();
    private final HashMap<Monster, MonsterTypeDto> monstersInEncounter = new HashMap<>();
    private int repeatedTimes = 0;
    private boolean inEncounter = true;
    private boolean resultLevelUP = false;
    private boolean resultEvolved = false;
    private Monster oldMonster;
    private final ArrayList<Integer> newAbilities = new ArrayList<>();

    private HashMap<String, EncounterOpponentController> encounterOpponentControllerHashMap = new HashMap<>();

    @Inject
    public EncounterController() {
    }

    public void init() {
        super.init();
        opponentsSize = encounterOpponentStorage.getEncounterSize();
        regionId = trainerStorageProvider.get().getRegion()._id();
        encounterId = encounterOpponentStorage.getEncounterId();
        trainerId = trainerStorageProvider.get().getTrainer()._id();
        listenToOpponents(encounterId);
        battleMenuController.setValues(resources, preferences, resourceBundleProvider, battleMenuController, app);
        battleMenuController.init();
        subControllers.add(battleMenuController);
        subControllers.add(abilitiesMenuController);
        encounterOpponentControllerHashMap = new HashMap<>();
        disposables.add(monstersService.getMonsters(regionId, trainerId).observeOn(FX_SCHEDULER).subscribe(monsters ->
                monsters.forEach(monster -> disposables.add(presetsService.getMonster(monster.type()).observeOn(FX_SCHEDULER).subscribe(monsterTypeDto ->
                                monstersInEncounter.put(monster, monsterTypeDto),
                        Throwable::printStackTrace))), Throwable::printStackTrace));
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

    public Parent render() {
        Parent parent = super.render();
        // init battle menu
        battleMenuController.init(this, battleMenuVBox, encounterOpponentStorage, app);
        battleMenuVBox.getChildren().add(battleMenuController.render());
        battleMenuController.onFleeButtonClick = this::onFleeButtonClick;

        // Init opponent controller for own trainer
        ownTrainerController = new EncounterOpponentController(false, false, true, false, encounterOpponentStorage, encounterOpponentStorage.getSelfOpponent());
        ownTrainerController.init();
        encounterOpponentControllerHashMap.put(encounterOpponentStorage.getSelfOpponent()._id(), ownTrainerController);
        Parent ownTrainerParent = ownTrainerController.render();
        //showMyMonster
        showTeamMonster(ownTrainerController, encounterOpponentStorage.getSelfOpponent(), true);
        // showMySprite
        ImageView sprite = ownTrainerController.getTrainerImageView();
        setTrainerSpriteImageView(trainerStorageProvider.get().getTrainer(), sprite, 1);

        disposables.add(monstersService.getMonster(regionId, trainerId, encounterOpponentStorage.getSelfOpponent().monster()).observeOn(FX_SCHEDULER).subscribe(monster -> {
            oldMonster = monster;
            encounterOpponentStorage.addCurrentMonster(monster);
        }));
        disposables.add(presetsService.getAbilities().observeOn(FX_SCHEDULER).subscribe(abilityDtos::addAll, Throwable::printStackTrace));

        disposables.add(regionEncountersService.getEncounter(regionId, encounterId).observeOn(FX_SCHEDULER).subscribe(encounter -> {
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
        }, Throwable::printStackTrace));
        return parent;
    }

    public void onFleeButtonClick() {
        rootStackPane.getChildren().add(this.buildFleePopup());
    }

    private void renderForWild(Parent ownTrainerParent) {
        // Wild situation

        // Wild monster (init opponent controller for enemy)
        enemy1Controller = new EncounterOpponentController(true, true, false, false, encounterOpponentStorage, encounterOpponentStorage.getEnemyOpponents().get(0));
        enemy1Controller.init();
        encounterOpponentControllerHashMap.put(encounterOpponentStorage.getEnemyOpponents().get(0)._id(), enemy1Controller);
        enemyHBox.setPadding(new Insets(0, 360, 0, 0));
        Parent enemy1Parent = enemy1Controller.render();
        enemyHBox.getChildren().add(enemy1Parent);
        targetOpponent(encounterOpponentStorage.getEnemyOpponents().get(0));
        showEnemyMonster(enemy1Controller, encounterOpponentStorage.getEnemyOpponents().get(0), true);

        // Own trainer
        teamHBox.setPadding(new Insets(0, 0, 0, 360));
        teamHBox.getChildren().add(ownTrainerParent);
    }

    private void renderFor1vs1(Parent ownTrainerParent) {
        // 1 vs 1 situation

        // Enemy as a trainer (init opponent controller for the enemy)
        enemy1Controller = new EncounterOpponentController(true, false, false, false, encounterOpponentStorage, encounterOpponentStorage.getEnemyOpponents().get(0));
        enemy1Controller.init();
        encounterOpponentControllerHashMap.put(encounterOpponentStorage.getEnemyOpponents().get(0)._id(), enemy1Controller);
        Parent enemy1Parent = enemy1Controller.render();
        enemyHBox.setPadding(new Insets(0, 360, 0, 0));
        enemyHBox.getChildren().add(enemy1Parent);
        targetOpponent(encounterOpponentStorage.getEnemyOpponents().get(0));
        showEnemyInfo(enemy1Controller, encounterOpponentStorage.getEnemyOpponents().get(0));

        // Own trainer
        teamHBox.setPadding(new Insets(0, 0, 0, 360));
        teamHBox.getChildren().add(ownTrainerParent);
    }

    private void renderFor1vs2(Parent ownTrainerParent) {
        // 1 vs 2 situation

        // 1st enemy as a trainer
        enemy1Controller = new EncounterOpponentController(true, false, false, true, encounterOpponentStorage, encounterOpponentStorage.getEnemyOpponents().get(0));
        enemy1Controller.init();
        enemy1Controller.onTargetChange = this::onTargetChange;
        encounterOpponentControllerHashMap.put(encounterOpponentStorage.getEnemyOpponents().get(0)._id(), enemy1Controller);
        VBox enemy1Parent = (VBox) enemy1Controller.render();
        enemyHBox.getChildren().add(enemy1Parent);
        targetOpponent(encounterOpponentStorage.getEnemyOpponents().get(0));
        showEnemyInfo(enemy1Controller, encounterOpponentStorage.getEnemyOpponents().get(0));

        // 2nd enemy as a trainer
        enemy2Controller = new EncounterOpponentController(true, false, true, true, encounterOpponentStorage, encounterOpponentStorage.getEnemyOpponents().get(1));
        enemy2Controller.init();
        enemy2Controller.onTargetChange = this::onTargetChange;
        encounterOpponentControllerHashMap.put(encounterOpponentStorage.getEnemyOpponents().get(1)._id(), enemy2Controller);
        VBox enemy2Parent = (VBox) enemy2Controller.render();
        enemyHBox.getChildren().add(enemy2Parent);
        showEnemyInfo(enemy2Controller, encounterOpponentStorage.getEnemyOpponents().get(1));

        // Own trainer
        teamHBox.setPadding(new Insets(0, 0, 0, 360));
        teamHBox.getChildren().add(ownTrainerParent);
    }

    private void renderFor2vs2(Parent ownTrainerParent) {
        // 2 vs 2 situation

        // 1st enemy as a trainer
        enemy1Controller = new EncounterOpponentController(true, false, false, true, encounterOpponentStorage, encounterOpponentStorage.getEnemyOpponents().get(0));
        enemy1Controller.init();
        enemy1Controller.onTargetChange = this::onTargetChange;
        encounterOpponentControllerHashMap.put(encounterOpponentStorage.getEnemyOpponents().get(0)._id(), enemy1Controller);
        VBox enemy1Parent = (VBox) enemy1Controller.render();
        enemyHBox.getChildren().add(enemy1Parent);
        targetOpponent(encounterOpponentStorage.getEnemyOpponents().get(0));
        showEnemyInfo(enemy1Controller, encounterOpponentStorage.getEnemyOpponents().get(0));

        // 2nd enemy as a trainer
        enemy2Controller = new EncounterOpponentController(true, false, true, true, encounterOpponentStorage, encounterOpponentStorage.getEnemyOpponents().get(1));
        enemy2Controller.init();
        enemy2Controller.onTargetChange = this::onTargetChange;
        encounterOpponentControllerHashMap.put(encounterOpponentStorage.getEnemyOpponents().get(1)._id(), enemy2Controller);
        VBox enemy2Parent = (VBox) enemy2Controller.render();
        enemyHBox.getChildren().add(enemy2Parent);
        showEnemyInfo(enemy2Controller, encounterOpponentStorage.getEnemyOpponents().get(1));

        // Coop Trainer
        coopTrainerController = new EncounterOpponentController(false, false, false, false, encounterOpponentStorage, encounterOpponentStorage.getCoopOpponent());
        coopTrainerController.init();
        encounterOpponentControllerHashMap.put(encounterOpponentStorage.getCoopOpponent()._id(), coopTrainerController);
        Parent coopTrainerParent = coopTrainerController.render();
        teamHBox.getChildren().add(coopTrainerParent);
        showCoopImage(coopTrainerController, encounterOpponentStorage.getCoopOpponent());
        showTeamMonster(coopTrainerController, encounterOpponentStorage.getCoopOpponent(), false);

        // Own trainer
        teamHBox.getChildren().add(ownTrainerParent);
    }

    private void onTargetChange() {
        if (encounterOpponentStorage.getLeastTargetOpponent() != null && encounterOpponentControllerHashMap.containsKey(encounterOpponentStorage.getLeastTargetOpponent()._id())) {
            encounterOpponentControllerHashMap.get(encounterOpponentStorage.getLeastTargetOpponent()._id()).unTarget();
        }
        if (encounterOpponentControllerHashMap.containsKey(encounterOpponentStorage.getTargetOpponent()._id())) {
            encounterOpponentControllerHashMap.get(encounterOpponentStorage.getTargetOpponent()._id()).onTarget();
        }
    }

    private void targetOpponent(Opponent opponent) {
        encounterOpponentStorage.setTargetOpponent(opponent);
        if (encounterOpponentStorage.getTargetOpponent() != opponent
                && encounterOpponentControllerHashMap.containsKey(opponent._id())
                && encounterOpponentControllerHashMap.get(opponent._id()).isMultipleEnemyEncounter
        ) {
            onTargetChange();
        }
    }

    // Hier soll allen Serveranfragen kommen
    private void showEnemyMonster(EncounterOpponentController encounterOpponentController, Opponent opponent, boolean isInit) {
        disposables.add(monstersService.getMonster(regionId, opponent.trainer(), opponent.monster()).observeOn(FX_SCHEDULER).subscribe(monster -> {
            encounterOpponentController.setLevelLabel("LVL " + monster.level()).setHealthBarValue((double) monster.currentAttributes().health() / monster.attributes().health());

            listenToMonster(opponent.trainer(), monster._id(), encounterOpponentController);
            //write monster name
            disposables.add(presetsService.getMonster(monster.type()).observeOn(FX_SCHEDULER).subscribe(m -> {
                encounterOpponentController.setMonsterNameLabel(m.name());
                encounterOpponentStorage.addCurrentMonsterType(m);
                if (encounterOpponentStorage.isWild() && isInit) {
                    battleDialogText.setText(resources.getString("ENCOUNTER_DESCRIPTION_BEGIN") + " " + m.name());
                }
            }, Throwable::printStackTrace));
            disposables.add(presetsService.getMonsterImage(monster.type()).observeOn(FX_SCHEDULER).subscribe(mImage -> {
                Image enemyMonsterImage = ImageProcessor.resonseBodyToJavaFXImage(mImage);
                encounterOpponentController.setMonsterImage(enemyMonsterImage);
            }, Throwable::printStackTrace));
        }, Throwable::printStackTrace));
    }

    private void showEnemyInfo(EncounterOpponentController encounterOpponentController, Opponent opponent) {
        if (opponent != null) {
            // Monster
            showEnemyMonster(encounterOpponentController, opponent, true);

            // Trainer Sprite
            disposables.add(trainersService.getTrainer(regionId, opponent.trainer()).observeOn(FX_SCHEDULER).subscribe(trainer -> {
                battleDialogText.setText(resources.getString("ENCOUNTER_DESCRIPTION_BEGIN") + " " + trainer.name());
                ImageView opponentTrainer = encounterOpponentController.getTrainerImageView();
                setTrainerSpriteImageView(trainer, opponentTrainer, TRAINER_DIRECTION_DOWN);
            }, Throwable::printStackTrace));
        }
    }

    private void showTeamMonster(EncounterOpponentController encounterOpponentController, Opponent opponent, boolean isSelf) {
        // Monster
        disposables.add(monstersService.getMonster(regionId, opponent.trainer(), opponent.monster()).observeOn(FX_SCHEDULER).subscribe(monster -> {
            if (isSelf) {
                encounterOpponentStorage.setCurrentTrainerMonster(monster);
            }
            encounterOpponentStorage.addCurrentMonster(monster);
            listenToMonster(opponent.trainer(), opponent.monster(), encounterOpponentController);
            encounterOpponentController.setLevelLabel("LVL " + monster.level())
                    .setExperienceBarValue((double) monster.experience() / requiredExperience(monster.level() + 1))
                    .setHealthBarValue((double) monster.currentAttributes().health() / monster.attributes().health())
                    .setHealthLabel(monster.currentAttributes().health() + "/" + monster.attributes().health() + " HP");
            //write monster name
            disposables.add(presetsService.getMonster(monster.type()).observeOn(FX_SCHEDULER).subscribe(m -> {
                encounterOpponentController.setMonsterNameLabel(m.name());
                if (isSelf) {
                    encounterOpponentStorage.setCurrentTrainerMonsterType(m);
                }
                encounterOpponentStorage.addCurrentMonsterType(m);
            }, Throwable::printStackTrace));
            disposables.add(presetsService.getMonsterImage(monster.type()).observeOn(FX_SCHEDULER).subscribe(mImage -> {
                Image myMonsterImage = ImageProcessor.resonseBodyToJavaFXImage(mImage);
                encounterOpponentController.setMonsterImage(myMonsterImage);
            }, Throwable::printStackTrace));
        }, Throwable::printStackTrace));
    }

    private void showCoopImage(EncounterOpponentController encounterOpponentController, Opponent opponent) {
        disposables.add(trainersService.getTrainer(regionId, opponent.trainer()).observeOn(FX_SCHEDULER).subscribe(trainer -> {
            ImageView opponentTrainer = encounterOpponentController.getTrainerImageView();
            setTrainerSpriteImageView(trainer, opponentTrainer, 3);
        }, Throwable::printStackTrace));
    }

    public int requiredExperience(int currentLevel) {
        return (int) (Math.pow(currentLevel, 3) - Math.pow(currentLevel - 1, 3));
    }

    public void showMonsterDetailsInEncounter() {
        VBox monsterDetailVBox = new VBox();
        monsterDetailVBox.setAlignment(Pos.CENTER);
        MonstersDetailController monstersDetailController = monstersDetailControllerProvider.get();
        Monster monster = encounterOpponentStorage.getCurrentTrainerMonster();
        MonsterTypeDto monsterTypeDto = encounterOpponentStorage.getCurrentTrainerMonsterType();

        StringBuilder type = new StringBuilder();
        for (String s : monsterTypeDto.type()) {
            type.append(s);
        }
        monstersDetailController.initFromBattleMenu(this, monsterDetailVBox, monster, monsterTypeDto, ownTrainerController.monsterImageView.getImage(), resources, presetsService, type.toString());
        monsterDetailVBox.getChildren().add(monstersDetailController.render());
        rootStackPane.getChildren().add(monsterDetailVBox);
        monsterDetailVBox.requestFocus();
    }

    public void showChangeMonsterList() {
        VBox monsterListVBox = new VBox();
        monsterListVBox.setMinWidth(600);
        monsterListVBox.setMinHeight(410);
        monsterListVBox.setAlignment(Pos.CENTER);
        ChangeMonsterListController changeMonsterListController = changeMonsterListControllerProvider.get();
        changeMonsterListController.init(this, monsterListVBox, ingameController);
        monsterListVBox.getChildren().add(changeMonsterListController.render());
        rootStackPane.getChildren().add(monsterListVBox);
        monsterListVBox.requestFocus();
    }

    @Override
    public void destroy() {
        super.destroy();
        for (var controller : subControllers) {
            if (controller != null) {
                controller.destroy();
            }
        }
        if (encounterOpponentControllerHashMap != null) {
            encounterOpponentControllerHashMap.values().forEach(Controller::destroy);
        }

    }

    public void listenToOpponents(String encounterId) {
        disposables.add(eventListener.get().listen("encounters." + encounterId + ".trainers.*.opponents.*.*", Opponent.class).observeOn(FX_SCHEDULER).subscribe(event -> {
            final Opponent opponent = event.data();
            if (event.suffix().contains("updated")) {
                inEncounter = true;
                updateOpponent(opponent);
                if (opponent.results().size() > 0) {
                    opponent.results().forEach(result -> {
                        if (Objects.equals(result.type(), "monster-levelup")) {
                            if (!resultLevelUP) {
                                resultLevelUP = true;
                            }
                        } else if (Objects.equals(result.type(), "monster-learned")) {
                            System.out.println(result);
                            newAbilities.add(result.ability());
                        } else if (Objects.equals(result.type(), "monster-evolved")) {
                            if (!resultEvolved) {
                                resultEvolved = true;
                            }
                        } else if (Objects.equals(result.type(), "target-defeated")) {
                            if (Objects.equals(opponent._id(), encounterOpponentStorage.getSelfOpponent()._id())) {
                                enemyMonsterDefeated();
                            } else {
                                yourMonsterDefeated();
                            }
                        }
                        if (Objects.equals(result.type(), "status-added")) {
                            EncounterOpponentController encounterOpponentController = encounterOpponentControllerHashMap.get(opponent._id());
                            encounterOpponentController.showStatus(result.status(), true);
                        }
                        if (Objects.equals(result.type(), "status-removed")) {
                            EncounterOpponentController encounterOpponentController = encounterOpponentControllerHashMap.get(opponent._id());
                            encounterOpponentController.showStatus(result.status(), false);
                        }
                    });
                }
            } else if (event.suffix().contains("deleted")) {
                opponentsDelete.put(opponent._id(), opponent);
                if (opponentsDelete.size() >= encounterOpponentStorage.getEncounterSize() && !inEncounter) {
                    showResult();
                }
            }
        }, Throwable::printStackTrace));
    }

    // manage the response of all the opponents (include the move and all results after the move, there are 2 Updates per opponent)
    private void updateOpponent(Opponent opponent) {
        // For komplexer Situation for example with more opponents should be considered in the future
        String trainerId = trainerStorageProvider.get().getTrainer()._id();
        if (opponent.move() != null) {
            opponentsUpdate.put(opponent._id() + "Move", opponent);
        } else if (opponent.results().size() != 0) {
            if (opponent.trainer().equals(trainerId)) {
                encounterOpponentStorage.setSelfOpponent(opponent);
            } else {
                encounterOpponentStorage.getEnemyOpponents().removeIf(o -> o._id().equals(opponent._id()));
                encounterOpponentStorage.addEnemyOpponent(opponent);
                showEnemyMonster(encounterOpponentControllerHashMap.get(opponent._id()), opponent, false);
            }
            opponentsUpdate.put(opponent._id() + "Results", opponent);
        }

        // this magic number is two time the size of oppenents in this encounter
        if (opponentsUpdate.size() >= 2 * encounterOpponentStorage.getEncounterSize()) {
            if (repeatedTimes == 0) {
                writeBattleDescription(opponentsUpdate);
            }
            repeatedTimes++;
            inEncounter = false;
        }
    }

    private void writeBattleDescription(HashMap<String, Opponent> forDescription) {
        updateDescription(EMPTY_STRING, true);
        List<String> opponentsInStorage = encounterOpponentStorage.getOpponentsInStorage();
        for (String opponentId : opponentsInStorage) {
            Opponent o = forDescription.get(opponentId + "Move");
            Move move = o.move();
            if (move instanceof AbilityMove abilityMove) {
                if (o.trainer().equals(trainerStorageProvider.get().getTrainer()._id())) {
                    updateDescription(resources.getString("YOU.USED") + " " + abilityDtos.get(abilityMove.ability() - 1).name() + ". ", false);
                } else {
                    updateDescription(resources.getString("ENEMY.USED") + " " + abilityDtos.get(abilityMove.ability() - 1).name() + ". ", false);
                }
            }
            if (move instanceof ChangeMonsterMove) {
                if (o.trainer().equals(trainerStorageProvider.get().getTrainer()._id())) {
                    updateDescription(resources.getString("YOU.CHANGED.MONSTER") + ". ", false);
                    showTeamMonster(encounterOpponentControllerHashMap.get(o._id()), o, true);
                    listenToMonster(o.trainer(), o.monster(), encounterOpponentControllerHashMap.get(o._id()));
                } else if (encounterOpponentStorage.getEnemyOpponents().contains(o)) {
                    updateDescription(resources.getString("ENEMY.CHANGED.MONSTER") + ". ", false);
                    showEnemyMonster(encounterOpponentControllerHashMap.get(o._id()), o, false);
                    listenToMonster(o.trainer(), o.monster(), encounterOpponentControllerHashMap.get(o._id()));
                } else {
                    updateDescription(resources.getString("ALLY.CHANGED.MONSTER") + ". ", false);
                    showTeamMonster(encounterOpponentControllerHashMap.get(o._id()), o, false);
                    listenToMonster(o.trainer(), o.monster(), encounterOpponentControllerHashMap.get(o._id()));
                }

            }

            Opponent oResults = forDescription.get(opponentId + "Results");

            for (Result r : oResults.results()) {
                if (r.type().equals("ability-success")) {
                    updateDescription(abilityDtos.get(r.ability() - 1).name() + " " + resources.getString("IS") + " " + r.effectiveness() + ".\n", false);
                } else {
                    updateDescription("", false);
                }
            }
        }
        inEncounter = false;
    }

    public void updateDescription(String information, boolean isUpdated) {
        if (isUpdated) {
            battleDialogText.setText(information);
        } else {
            if (battleDialogText.getText().contains(information)) {
                return;
            }
            battleDialogText.setText(battleDialogText.getText() + information);
        }
    }

    private void listenToMonster(String trainerId, String monsterId, EncounterOpponentController encounterOpponentController) {
        disposables.add(eventListener.get().listen("trainers." + trainerId + ".monsters." + monsterId + ".*", Monster.class).observeOn(FX_SCHEDULER).subscribe(event -> {
            final Monster monster = event.data();
            if (event.suffix().contains("updated")) {
                float currentHealth = (float) (Math.round(monster.currentAttributes().health() * 10) / 10);
                float maxHealth = (float) (Math.round(monster.attributes().health() * 10) / 10);
                if (!GraphicsEnvironment.isHeadless()) {
                    AnimationBuilder.buildShakeAnimation(encounterOpponentController.monsterImageView, 50, 3, 1).play();
                    AnimationBuilder.buildProgressBarAnimation(encounterOpponentController.HealthBar, 1500, monster.currentAttributes().health() / monster.attributes().health()).play();
                } else {
                    encounterOpponentController.setHealthBarValue(monster.currentAttributes().health() / monster.attributes().health());
                }
                encounterOpponentController
                        .setHealthLabel(currentHealth + "/" + maxHealth)
                        .setLevelLabel("LVL " + monster.level())
                        .setExperienceBarValue((double) monster.experience() / requiredExperience(monster.level()));
                if (trainerId.equals(trainerStorageProvider.get().getTrainer()._id())) {
                    encounterOpponentStorage.setCurrentTrainerMonster(monster);
                    disposables.add(presetsService.getMonster(monster.type()).observeOn(FX_SCHEDULER).subscribe(monsterTypeDto -> {
                        encounterOpponentStorage.setCurrentTrainerMonsterType(monsterTypeDto);
                        monstersInEncounter.remove(monster);
                        monstersInEncounter.put(monster, monsterTypeDto);
                    }, Throwable::printStackTrace));
                }
            }
        }, Throwable::printStackTrace));
    }

    private void showResult() {
        List<String> opponentsInStorage = encounterOpponentStorage.getOpponentsInStorage();
        for (String id : opponentsInStorage) {
            Opponent o = opponentsDelete.get(id);
            if (o.monster() == null) {
                if (o.trainer().equals(trainerStorageProvider.get().getTrainer()._id())) {
                    showResultPopUp(resources.getString("YOU.FAILED"));
                } else {
                    showResultPopUp(resources.getString("YOU.WON"));
                }
            }
        }
    }

    private void showResultPopUp(String string) {
        VBox resultBox = new VBox();
        resultBox.setAlignment(Pos.CENTER);
        encounterResultController.init(app);
        resultBox.getChildren().add(encounterResultController.render());
        rootStackPane.getChildren().add(resultBox);
        resultBox.requestFocus();
        buttonsDisableEncounter(true);
        encounterResultController.setInformationText(string);
    }

    public void showIngameController() {
        destroy();
        if (trainerStorageProvider.get().getMonsters() != null) {
            trainerStorageProvider.get().getTrainer().team().stream().flatMap(teamMonsterId -> trainerStorageProvider.get().getMonsters().stream().filter(trainerMonster -> teamMonsterId.equals(trainerMonster._id())).filter(trainerMonster -> (double) trainerMonster.currentAttributes().health() / trainerMonster.attributes().health() <= 0.2)).forEach(trainerMonster -> this.ingameController.showLowHealthNotification());
        }
        app.show(ingameControllerProvider.get());
    }

    public void showAbilities() {
        battleMenuVBox.getChildren().clear();
        Monster monster = encounterOpponentStorage.getCurrentTrainerMonster();
        subControllers.add(abilitiesMenuController);
        if (encounterOpponentStorage.getSelfOpponent().monster() != null) {
            abilitiesMenuController.init(monster, presetsService, this);
        } else {
            abilitiesMenuController.init(null, presetsService, this);
        }
        battleMenuVBox.getChildren().add(abilitiesMenuController.render());
    }

    public void goBackToBattleMenu() {
        battleMenuVBox.getChildren().clear();
        battleMenuController.init(this, battleMenuVBox, encounterOpponentStorage, app);
        battleMenuVBox.getChildren().add(battleMenuController.render());
    }

    public void enemyMonsterDefeated() {
        battleDialogText.setText(resources.getString("TARGET.DEFEATED"));
        PauseTransition pause = new PauseTransition(Duration.millis(1000));
        pause.setOnFinished(evt -> {
            enemy1Controller.monsterImageView.setVisible(false);
            if (resultLevelUP) {
                showLevelUpPopUp();
            } else {
                continueBattle();
            }
        });
        pause.play();
    }

    public void continueBattle() {
        final boolean[] enemyHasAnotherMonster = {false};
        encounterOpponentStorage.getEnemyOpponents().forEach(opponent -> {
            if (opponent.monster() != null) {
                enemyHasAnotherMonster[0] = true;
            }
        });
        if (!enemyHasAnotherMonster[0] || encounterOpponentStorage.isWild()) {
            SequentialTransition fleeAnimation = AnimationBuilder.buildFleeAnimation(trainerStorageProvider.get().getTrainerSpriteChunk(), ownTrainerController);
            PauseTransition pause = new PauseTransition(Duration.millis(1000));
            pause.setOnFinished(evt -> {
                ownTrainerController.monsterImageView.setVisible(false);
                fleeAnimation.play();
            });
            pause.play();
            fleeAnimation.setOnFinished(evt -> showResultPopUp(resources.getString("YOU.WON")));
        }
    }

    public void yourMonsterDefeated() {
        final boolean[] hasAnotherMonster = {false};
        PauseTransition pause = new PauseTransition(Duration.millis(2000));
        pause.setOnFinished(evt -> {
            ownTrainerController.monsterImageView.setVisible(false);
            System.out.println(monstersInEncounter);
            monstersInEncounter.forEach((monster, monsterTypeDto) -> {
                if (monster.currentAttributes().health() > 0) {
                    hasAnotherMonster[0] = true;
                    disposables.add(encounterOpponentsService.updateOpponent(regionId, encounterId, encounterOpponentStorage.getSelfOpponent()._id(), monster._id(), null).observeOn(FX_SCHEDULER).subscribe(
                            opponent -> {
                                resetRepeatedTimes();
                                goBackToBattleMenu();
                                encounterOpponentStorage.setCurrentTrainerMonster(monster);
                                encounterOpponentStorage.setCurrentTrainerMonsterType(monsterTypeDto);
                                encounterOpponentStorage.setSelfOpponent(opponent);
                                showTeamMonster(ownTrainerController, encounterOpponentStorage.getSelfOpponent(), true);
                            }, Throwable::printStackTrace));
                }
            });
        });
        pause.play();
        PauseTransition pause2 = new PauseTransition(Duration.millis(2000));
        pause2.setOnFinished(evt -> {
            if (!hasAnotherMonster[0]) {
                showResultPopUp(resources.getString("YOU.FAILED"));
            }
        });
        pause2.play();
    }

    public void fleeFromBattle() {
        SequentialTransition fleeAnimation = AnimationBuilder.buildFleeAnimation(trainerStorageProvider.get().getTrainerSpriteChunk(), ownTrainerController);
        PauseTransition firstPause = new PauseTransition(Duration.millis(500));
        battleDialogText.setText(resources.getString("ENCOUNTER_DESCRIPTION_FLEE"));
        firstPause.setOnFinished(evt -> {
            ownTrainerController.monsterImageView.setVisible(false);
            fleeAnimation.play();
        });
        fleeAnimation.setOnFinished(evt -> disposables.add(encounterOpponentsService.deleteOpponent(encounterOpponentStorage.getRegionId(), encounterOpponentStorage.getEncounterId(), encounterOpponentStorage.getSelfOpponent()._id()).observeOn(FX_SCHEDULER)
                .subscribe(result -> showIngameController(), error -> {
                    showError(error.getMessage());
                    error.printStackTrace();
                })));
        firstPause.play();
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
        yesButton.setId("fleePopupYesButton");
        yesButton.setMaxWidth(fleeButtonWidth);
        yesButton.setMinHeight(fleeButtonHeight);
        yesButton.setPrefWidth(fleeButtonWidth);
        yesButton.setPrefHeight(fleeButtonHeight);
        yesButton.getStyleClass().add("hBoxRed");
        yesButton.setOnAction(event -> {
            rootStackPane.getChildren().remove(fleeVBox);
            this.fleeFromBattle();
        });

        // no Button
        Button noButton = new Button(this.resources.getString("ENCOUNTER_FLEE_CANCEL_BUTTON"));
        noButton.setId("fleePopupNoButton");
        noButton.setMaxWidth(fleeButtonWidth);
        noButton.setMinHeight(fleeButtonHeight);
        noButton.setPrefWidth(fleeButtonWidth);
        noButton.setPrefHeight(fleeButtonHeight);
        noButton.getStyleClass().add("hBoxYellow");
        noButton.setOnAction(event -> rootStackPane.getChildren().remove(fleeVBox));

        // add buttons to hbox
        buttonHBox.getChildren().addAll(yesButton, noButton);

        // add textFlow and buttonHBox to VBox
        fleeVBox.getChildren().addAll(fleeTextFlow, buttonHBox);

        return fleeVBox;
    }

    public void resetOpponentUpdate() {
        opponentsUpdate.clear();
    }

    public void resetRepeatedTimes() {
        this.repeatedTimes = 0;
    }

    public void buttonsDisableEncounter(boolean isDisable) {
        battleMenuController.buttonDisable(isDisable);
    }

    public void showLevelUpPopUp() {
        resultLevelUP = false;
        LevelUpController levelUpController = levelUpControllerProvider.get();
        VBox popUpVBox = new VBox();
        popUpVBox.getStyleClass().add("miniMapContainer");
        disposables.add(monstersService.getMonster(regionId, trainerId, encounterOpponentStorage.getCurrentTrainerMonster()._id()).observeOn(FX_SCHEDULER).subscribe(monster -> {
            levelUpController.init(popUpVBox, rootStackPane, this, monster, encounterOpponentStorage.getCurrentTrainerMonsterType(), oldMonster, newAbilities, abilityDtos);
            popUpVBox.getChildren().add(levelUpController.render());
            rootStackPane.getChildren().add(popUpVBox);
            newAbilities.clear();
        }, Throwable::printStackTrace));
    }

    public void changeMonster(Monster monster, MonsterTypeDto monsterTypeDto) {
        Move move = new ChangeMonsterMove("change-monster", monster._id());
        disposables.add(encounterOpponentsService.updateOpponent(regionId, encounterId, encounterOpponentStorage.getSelfOpponent()._id(), null, move).observeOn(FX_SCHEDULER).subscribe(
                opponent -> {
                    resetRepeatedTimes();
                    goBackToBattleMenu();
                    encounterOpponentStorage.setCurrentTrainerMonster(monster);
                    encounterOpponentStorage.setCurrentTrainerMonsterType(monsterTypeDto);
                    encounterOpponentStorage.setSelfOpponent(opponent);
                }, Throwable::printStackTrace));
    }

}
    
