package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.Constants;
import de.uniks.stpmon.team_m.controller.subController.AbilitiesMenuController;
import de.uniks.stpmon.team_m.controller.subController.BattleMenuController;
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
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
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
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import static de.uniks.stpmon.team_m.Constants.*;


public class EncounterController extends Controller {
    @FXML
    public Label opponentLevel;
    @FXML
    public ProgressBar opponentHealthBar;
    @FXML
    public Label opponentMonsterName;
    @FXML
    public ImageView opponentTrainer;
    @FXML
    public ImageView opponentMonster;
    @FXML
    public ImageView myMonster;
    @FXML
    public ImageView mySprite;
    @FXML
    public ProgressBar myLevelBar;
    @FXML
    public Label myLevel;
    @FXML
    public ProgressBar myHealthBar;
    @FXML
    public Label myHealth;
    @FXML
    public Label myMonsterName;
    @FXML
    public HBox battleMenu;
    @FXML
    public Text battleDescription;
    @FXML
    public Button goBack;
    @FXML
    public StackPane rootStackPane;

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

    private String regionId;
    private String encounterId;
    private String trainerId;
    private Image myMonsterImage;
    private Image enemyMonsterImage;
    private List<Controller> subControllers = new ArrayList<>();
    private int currentImageIndex = 0;
    private List<AbilityDto> abilityDtos = new ArrayList<>();
    private List<Opponent> opponentsUpdate = new ArrayList<>();
    private int repeatedTimes = 0;

    @Inject
    public EncounterController() {
    }

    public void init() {
        super.init();
        regionId = encounterOpponentStorage.getRegionId();
        encounterId = encounterOpponentStorage.getEncounterId();
        trainerId = trainerStorageProvider.get().getTrainer()._id();
        listenToOpponents(encounterId);
        battleMenuController.init();
        subControllers.addAll(List.of(battleMenuController, abilitiesMenuController));
    }

    public String getTitle() {
        return resources.getString("ENCOUNTER");
    }

    public Parent render() {
        final Parent parent = super.render();
        disposables.add(regionEncountersService.getEncounter(regionId,encounterId)
                .observeOn(FX_SCHEDULER).subscribe(encounter -> {
                    encounterOpponentStorage.setWild(encounter.isWild());
                    // Sprite and all relevante Information
                    showTrainer();
                    showMonster();
                }, Throwable::printStackTrace));

        // render for subcontroller
        battleMenuController.init(this, battleMenu, encounterOpponentStorage, app);
        battleMenu.getChildren().add(battleMenuController.render());
        battleMenuController.onFleeButtonClick = this::onFleeButtonClick;

        listenToOpponents(encounterOpponentStorage.getEncounterId());

        disposables.add(presetsService.getAbilities()
                .observeOn(FX_SCHEDULER).subscribe(as -> this.abilityDtos = as, Throwable::printStackTrace));

        return parent;
    }


    private void showTrainer(){
        setTrainerSpriteImageView(trainerStorageProvider.get().getTrainer(), mySprite,1);
        if(!encounterOpponentStorage.isWild()){
            String enemyTrainerId = encounterOpponentStorage.getEnemyOpponent().trainer();
            battleMenuController.showFleeButton(false);
            disposables.add(trainersService.getTrainer(regionId, enemyTrainerId)
                    .observeOn(FX_SCHEDULER).subscribe(trainer -> {
                        encounterOpponentStorage.setOpponentTrainer(trainer);
                        battleDescription.setText(resources.getString("ENCOUNTER_DESCRIPTION_BEGIN") + " " + trainer.name());
                        setTrainerSpriteImageView(trainer, opponentTrainer,3);
                    }, Throwable::printStackTrace));
        } else {
            battleMenuController.showFleeButton(true);
        }
    }

    private void showMonster() {
        // self monster
        disposables.add(monstersService.getMonster(regionId, trainerId, encounterOpponentStorage.getSelfOpponent().monster())
                .observeOn(FX_SCHEDULER).subscribe(monster -> {
                    encounterOpponentStorage.setCurrentTrainerMonster(monster);
                    myLevelBar.setProgress((double) monster.experience() / requiredExperience(monster.level() + 1));
                    myLevel.setText(monster.level() + " LVL");
                    myHealthBar.setProgress((double) monster.currentAttributes().health() / monster.attributes().health());
                    myHealth.setText(monster.currentAttributes().health() + "/" + monster.attributes().health() + " HP");
                    //write monster name
                    disposables.add(presetsService.getMonster(monster.type())
                            .observeOn(FX_SCHEDULER).subscribe(m -> {
                                myMonsterName.setText(m.name());
                                encounterOpponentStorage.setCurrentTrainerMonsterType(m);
                            }, Throwable::printStackTrace));
                    disposables.add(presetsService.getMonsterImage(monster.type())
                            .observeOn(FX_SCHEDULER).subscribe(mImage -> {
                                myMonsterImage = ImageProcessor.resonseBodyToJavaFXImage(mImage);
                                myMonster.setImage(myMonsterImage);
                            }, Throwable::printStackTrace));
                        }, Throwable::printStackTrace));

        // enemy monster
        disposables.add(monstersService.getMonster(regionId, encounterOpponentStorage.getEnemyOpponent().trainer(), encounterOpponentStorage.getEnemyOpponent().monster())
                .observeOn(FX_SCHEDULER).subscribe(monster -> {
                    encounterOpponentStorage.setCurrentEnemyMonster(monster);
                    opponentLevel.setText(monster.level() + " LVL");
                    opponentHealthBar.setProgress((double) monster.currentAttributes().health() / monster.attributes().health());
                    disposables.add(presetsService.getMonster(monster.type())
                            .observeOn(FX_SCHEDULER).subscribe(m -> {
                                opponentMonsterName.setText(m.name());
                                encounterOpponentStorage.setCurrentEnemyMonsterType(m);
                                if(encounterOpponentStorage.isWild()){
                                    battleDescription.setText(resources.getString("ENCOUNTER_DESCRIPTION_BEGIN") + " " + m.name());
                                }
                            }, Throwable::printStackTrace));
                    disposables.add(presetsService.getMonsterImage(monster.type())
                            .observeOn(FX_SCHEDULER).subscribe(mImage -> {
                                enemyMonsterImage = ImageProcessor.resonseBodyToJavaFXImage(mImage);
                                opponentMonster.setImage(enemyMonsterImage);
                            }, Throwable::printStackTrace));
                        }, Throwable::printStackTrace));
    }

    public int requiredExperience(int currentLevel) {
        return (int) (Math.pow(currentLevel, 3) - Math.pow(currentLevel - 1, 3));
    }


    @Override
    public void destroy() {
        super.destroy();
        subControllers.forEach(Controller::destroy);
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

    private void updateOpponent(Opponent opponent) {
        // For komplexer Situation for example with more opponents should be considered in the future
        if(opponent.trainer().equals(trainerStorageProvider.get().getTrainer()._id())){
            if(opponent.move() != null) {
                opponentsUpdate.add(0, opponent);
                // else for change monster move
            } else {
                if(opponent.results().size() != 0){
                    opponentsUpdate.add(1, opponent);
                }
            }
        } else {
            if(opponent.move() != null) {
                Move move = opponent.move();
                if(move instanceof AbilityMove){
                    opponentsUpdate.add(opponent);
                }
                // else for change monster move
            } else {
                if(opponent.results().size() != 0){
                    opponentsUpdate.add(opponent);
                }
            }
        }
        LinkedHashSet<Opponent> opponentsHashSet = new LinkedHashSet<>(opponentsUpdate);
        ArrayList<Opponent> forDescription = new ArrayList<>(opponentsHashSet);

        // this magic number is two time the size of oppenents in this encounter
        if(forDescription.size() >= 4){
            if(repeatedTimes == 0){
                writeBattleDescription(forDescription);
            }
            repeatedTimes++;
        }
    }

    private void writeBattleDescription(ArrayList<Opponent> forDescription) {
        for(Opponent o: forDescription) {
            if (o.move() != null) {
                Move move = o.move();
                if (move instanceof AbilityMove abilityMove) {
                    if(o.trainer().equals(trainerStorageProvider.get().getTrainer()._id())){
                        updateDescription(resources.getString("YOU.USED") + abilityDtos.get((abilityMove).ability() - 1).name() + ". ", false);
                    } else {
                        updateDescription(resources.getString("ENEMY.USED") + abilityDtos.get((abilityMove).ability() - 1).name() + ". ", false);
                    }
                } // else for change monster move
            } else {
                if (o.results().size() != 0) {
                    if(o.trainer().equals(trainerStorageProvider.get().getTrainer()._id())){
                        encounterOpponentStorage.setSelfOpponent(o);
                    } else {
                        encounterOpponentStorage.setEnemyOpponent(o);
                    }
                    for (Result r : o.results()) {
                        switch (r.type()) {
                            case "ability-success" -> {
                                updateDescription(abilityDtos.get(r.ability() - 1).name() + " " + resources.getString("IS") + r.effectiveness() + ".\n", false);
                                if (o.monster() != null) {
                                    updateMonsterValues(o.trainer(), o.monster());
                                }
                            }
                            case "target-defeated" -> updateDescription(resources.getString("TARGET.DEFEATED"), false);
                        }
                    }
                }
            }
        }
    }

    public void showIngameController() {
        destroy();
        app.show(ingameControllerProvider.get());
    }

    public void showAbilities() {
        battleMenu.getChildren().clear();
        Monster monster = encounterOpponentStorage.getCurrentTrainerMonster();
        if(encounterOpponentStorage.getSelfOpponent().monster() != null){
            abilitiesMenuController.init(monster, presetsService, battleMenu, this);
        } else {
            abilitiesMenuController.init(null, presetsService, battleMenu, this);
        }
        battleMenu.getChildren().add(abilitiesMenuController.render());
    }

    public void goBackToBattleMenu() {
        battleMenu.getChildren().clear();
        battleMenuController.init(this, battleMenu, encounterOpponentStorage, app);
        battleMenu.getChildren().add(battleMenuController.render());
    }

    public void onFleeButtonClick() {
        rootStackPane.getChildren().add(this.buildFleePopup());
    }

    public void fleeFromBattle() {
        SequentialTransition fleeAnimation = buildFleeAnimation();
        PauseTransition firstPause = new PauseTransition(Duration.millis(500));
        battleDescription.setText(resources.getString("ENCOUNTER_DESCRIPTION_FLEE"));

        firstPause.setOnFinished(evt -> {
            myMonster.setVisible(false);
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

        Image[] images = ImageProcessor.cropTrainerImages(trainerStorageProvider.get().getTrainerSpriteChunk(), 3, true);

        KeyFrame animationFrame = new KeyFrame(Duration.millis(Constants.DELAY), event -> {
            mySprite.setImage(images[currentImageIndex]);
            currentImageIndex = (currentImageIndex + 1) % 6;
        });
        KeyFrame movementFrame = new KeyFrame(Duration.millis(Constants.DELAY), evt -> {
            TranslateTransition translateTransition = new TranslateTransition();
            translateTransition.setNode(mySprite);
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
        yesButton.setOnAction(event -> {
            rootStackPane.getChildren().remove(fleeVBox);
            this.fleeFromBattle(event);
        });

        // no Button
        Button noButton = new Button(this.resources.getString("ENCOUNTER_FLEE_CANCEL_BUTTON"));
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

        return  fleeVBox;
    }

    public void updateDescription(String information, boolean isUpdated) {
        if(isUpdated){
            battleDescription.setText(information);
        } else {
            if (battleDescription.getText().contains(information)){
                return;
            }
            battleDescription.setText(battleDescription.getText() + information);
        }
    }

    private void updateMonsterValues(String trainerId, String monsterId) {
        boolean isMe = trainerId.equals(trainerStorageProvider.get().getTrainer()._id());
        disposables.add(monstersService.getMonster(regionId, trainerId, monsterId)
                .observeOn(FX_SCHEDULER).subscribe(monster -> {
                    if (isMe){
                        encounterOpponentStorage.setCurrentTrainerMonster(monster);
                        myLevelBar.setProgress((double) monster.experience() / requiredExperience(monster.level() + 1));
                        myLevel.setText(monster.level() + " LVL");
                        myHealthBar.setProgress((double) monster.currentAttributes().health() / monster.attributes().health());
                        myHealth.setText(monster.currentAttributes().health() + "/" + monster.attributes().health() + " HP");
                    } else {
                        encounterOpponentStorage.setCurrentEnemyMonster(monster);
                        opponentLevel.setText(monster.level() + " LVL");
                        opponentHealthBar.setProgress((double) monster.currentAttributes().health() / monster.attributes().health());
                    }
                }, Throwable::printStackTrace));

    }

    public void resetOppoenentUpdate(){
        opponentsUpdate.clear();
    }

    public void resetRepeatedTimes() {
        this.repeatedTimes = 0;
    }
}
    
