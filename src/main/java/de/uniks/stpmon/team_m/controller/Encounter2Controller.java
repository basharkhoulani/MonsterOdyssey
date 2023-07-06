package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.Constants;
import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.controller.subController.BattleMenuController;
import de.uniks.stpmon.team_m.controller.subController.EncounterOpponentController;
import de.uniks.stpmon.team_m.dto.Opponent;
import de.uniks.stpmon.team_m.service.EncounterOpponentsService;
import de.uniks.stpmon.team_m.utils.EncounterOpponentStorage;
import de.uniks.stpmon.team_m.utils.ImageProcessor;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.HashMap;

@Singleton
public class Encounter2Controller extends Controller {
    // Sample data
    int opponentsSize = 2;
    boolean isWild = true;
    String enemyMonsterName = "Flamuntel";
    double enemyHealthBarValue = 0.93;
    int enemyLevel = 8;
    String teamMonsterName = "Envias";
    double teamMonsterHeathBarValue = 1.0;
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

    @Inject
    public Provider<TrainerStorage> trainerStorageProvider;

    @Inject
    public IngameController ingameController;

    @Inject
    public EncounterOpponentsService encounterOpponentsService;


    private EncounterOpponentController enemy1Controller;
    private EncounterOpponentController enemy2Controller;
    private EncounterOpponentController ownTrainerController;
    private EncounterOpponentController coopTrainerController;
    private int currentImageIndex;
    private HashMap<Opponent, EncounterOpponentController> opponentControllerHashMap = new HashMap<>();

    @Inject
    public Encounter2Controller() {
    }

    @Override
    public void init() {
        super.init();
        //opponentsSize = encounterOpponentStorage.getEncounterSize();
        //isWild = encounterOpponentStorage.isWild();
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
        } else if (opponentsSize == 3) {
            renderFor1vs2(enemy1Parent, ownTrainerParent);
        } else {
            renderFor2vs2(enemy1Parent, ownTrainerParent);
        }
        battleMenuController.init(this, actionButtonVBox, encounterOpponentStorage);
        actionButtonVBox.getChildren().add(battleMenuController.render());
        if (isWild && opponentsSize == 2) {
            battleMenuController.showFleeButton();
            battleMenuController.onFleeButtonClick = this::onFleeButtonClick;
        } else {
            battleMenuController.hideFleeButton();
        }
        return parent;
    }

    private void renderForDuel(Parent enemy1Parent, Parent ownTrainerParent) {
        enemyHBox.setPadding(new Insets(0, 450, 0, 0));
        enemyHBox.getChildren().add(enemy1Parent);
        enemy1Controller.setCurrentTarget(encounterOpponentStorage.getSelfOpponent());
        // TODO: setup controllers with data

        teamHBox.setPadding(new Insets(0, 0, 0, 450));
        teamHBox.getChildren().add(ownTrainerParent);
        ownTrainerController.setCurrentTarget(encounterOpponentStorage.getEnemyOpponent());
        // TODO: setup controllers with data

    }

    private void renderFor1vs2(Parent enemy1Parent, Parent ownTrainerParent) {
        enemy2Controller = new EncounterOpponentController(true, false, true);
        enemy2Controller.init();
        enemyHBox.getChildren().add(enemy1Parent);
        VBox enemy2Parent = (VBox) enemy2Controller.render();
        HBox.setHgrow(enemy2Parent, javafx.scene.layout.Priority.ALWAYS);
        enemyHBox.getChildren().add(enemy2Parent);
        // TODO: setup controllers with data

        teamHBox.setPadding(new Insets(0, 0, 0, 450));
        teamHBox.getChildren().add(ownTrainerParent);
        // TODO: setup controllers with data

        targetOpponent(encounterOpponentStorage.getEnemyOpponent());
    }

    private void targetOpponent(Opponent opponent) {
        if (ownTrainerController.getCurrentTarget() != opponent) {
            ownTrainerController.setCurrentTarget(opponent);
        }
    }


    private void renderFor2vs2(Parent enemy1Parent, Parent ownTrainerParent) {
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

    public void onFleeButtonClick() {
        battleDialogText.setText("Back off " + teamMonsterName + "!");

        SequentialTransition fleeAnimation = buildFleeAnimation();
        PauseTransition firstPause = new PauseTransition(Duration.millis(500));
        PauseTransition secondPause = new PauseTransition(Duration.seconds(1));

        firstPause.setOnFinished(event -> {
            ownTrainerController.setMonsterImage(null);
            fleeAnimation.play();
        });

        fleeAnimation.setOnFinished(evt -> {
            secondPause.play();
            battleDialogText.setText("You escaped!");
            disposables.add(encounterOpponentsService.deleteOpponent(
                    encounterOpponentStorage.getRegionId(),
                    encounterOpponentStorage.getEncounterId(),
                    encounterOpponentStorage.getEnemyOpponent()._id()
            ).observeOn(FX_SCHEDULER).subscribe(
                    result -> {
                        //app.show(ingameController);
                        System.out.println("Deleted opponent: " + encounterOpponentStorage.getEnemyOpponent());
                    }, error -> {
                        showError(error.getMessage());
                        error.printStackTrace();
                    }));
        });

        secondPause.setOnFinished(event -> {
            //app.show(ingameController);
            System.out.println("Changing to ingame controller");
        });

        firstPause.play();
    }

    private SequentialTransition buildFleeAnimation() {
        SequentialTransition transition = new SequentialTransition();

        Image trainerChunk = new Image(String.valueOf(Main.class.getResource("charactermodels/Premade_Character_01.png")));
        Image[] images = ImageProcessor.cropTrainerImages(trainerChunk, Constants.TRAINER_DIRECTION_DOWN, true);

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
