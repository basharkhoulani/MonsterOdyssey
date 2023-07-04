package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.controller.subController.EncounterEnemyInfoBoxController;
import de.uniks.stpmon.team_m.controller.subController.EncounterTeamInfoBoxController;
import de.uniks.stpmon.team_m.controller.subController.EncounterTrainerMonsterImageBoxController;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.LinkedList;

public class Encounter2Controller extends Controller {
    // Sample data
    int opponentsSize = 3;
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


    private LinkedList<EncounterEnemyInfoBoxController> enemyInfoBoxControllers;
    private LinkedList<EncounterTrainerMonsterImageBoxController> enemyTrainerMonsterControllers;
    private LinkedList<EncounterTeamInfoBoxController> teamInfoBoxControllers;
    private LinkedList<EncounterTrainerMonsterImageBoxController> teamTrainerMonsterControllers;

    @Override
    public void init() {
        super.init();
        this.enemyInfoBoxControllers = new LinkedList<>();
        this.enemyTrainerMonsterControllers = new LinkedList<>();
        this.teamTrainerMonsterControllers = new LinkedList<>();
        this.teamInfoBoxControllers = new LinkedList<>();
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        if (opponentsSize == 2) {
            VBox enemyVBox = buildTrainerBox(false, true);
            enemyHBox.setPadding(new Insets(0, 400, 0, 0));
            enemyHBox.getChildren().add(enemyVBox);
            // TODO: setup controllers with data
            enemyInfoBoxControllers.getFirst().setOpponentMonsterName(enemyMonsterName);
            enemyInfoBoxControllers.getFirst().setOpponentHealthBarValue(enemyHealthBarValue);
            enemyInfoBoxControllers.getFirst().setOpponentLevel(enemyLevel);


            VBox teamVBox = buildTrainerBox(false, false);
            teamHBox.setPadding(new Insets(0, 0, 0, 400));
            teamHBox.getChildren().add(teamVBox);
            // TODO: setup controllers with data

            teamInfoBoxControllers.getFirst().setMyMonsterName(teamMonsterName);
            teamInfoBoxControllers.getFirst().setLevel(teamLevel);
            teamInfoBoxControllers.getFirst().setHealthBar(teamMonsterHeathBarValue);
            teamInfoBoxControllers.getFirst().setHealth(teamHeath);
            teamInfoBoxControllers.getFirst().setLevelBarValue(teamLevelBarValue);

        }
        else if (opponentsSize == 3) {
            VBox enemy1VBox = buildTrainerBox(false, true);
            VBox enemy2VBox = buildTrainerBox(true, true);
            enemy1VBox.setPadding(new Insets(0, 8, 0, 0));
            enemy2VBox.setPadding(new Insets(0, 0, 0, 8));
            enemyHBox.getChildren().add(enemy1VBox);
            enemyHBox.getChildren().add(enemy2VBox);
            // TODO: setup controllers with data

            VBox teamVBox = buildTrainerBox(false, false);
            teamHBox.setPadding(new Insets(0, 0, 0, 400));
            teamHBox.getChildren().add(teamVBox);
            // TODO: setup controllers with data
        }
        else {
            // 2 vs 2 situation
            VBox enemy1VBox = buildTrainerBox(false, true);
            VBox enemy2VBox = buildTrainerBox(true, true);
            enemy1VBox.setPadding(new Insets(0, 8, 0, 0));
            enemy2VBox.setPadding(new Insets(0, 0, 0, 8));
            enemyHBox.getChildren().add(enemy1VBox);
            enemyHBox.getChildren().add(enemy2VBox);
            // TODO: setup controllers with data

            VBox team1VBox = buildTrainerBox(true, false);
            VBox team2VBox = buildTrainerBox(false, false);
            team1VBox.setPadding(new Insets(0, 8, 0, 0));
            team2VBox.setPadding(new Insets(0, 0, 0, 8));
            teamHBox.getChildren().add(team1VBox);
            teamHBox.getChildren().add(team2VBox);
            // TODO: setup controllers with data
        }
        return parent;
    }

    private VBox buildTrainerBox(boolean invertX, boolean isEnemy) {
        VBox root = new VBox();
        EncounterTrainerMonsterImageBoxController trainerMonsterController = new EncounterTrainerMonsterImageBoxController();
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        if (isEnemy) {
            EncounterEnemyInfoBoxController controller = new EncounterEnemyInfoBoxController();
            enemyInfoBoxControllers.add(controller);
            enemyTrainerMonsterControllers.add(trainerMonsterController);

            Parent enemyInfoBox = controller.render();
            Parent enemyTrainerMonsterBox = trainerMonsterController.render();
            if (!invertX) {
                hBox.getChildren().add(enemyInfoBox);
                hBox.getChildren().add(enemyTrainerMonsterBox);
            }
            else {
                hBox.getChildren().add(enemyTrainerMonsterBox);
                hBox.getChildren().add(enemyInfoBox);
            }
        }
        else {
            EncounterTeamInfoBoxController controller = new EncounterTeamInfoBoxController();
            teamInfoBoxControllers.add(controller);
            teamTrainerMonsterControllers.add(trainerMonsterController);

            Parent teamInfoBox = controller.render();
            Parent teamTrainerMonsterBox = trainerMonsterController.render();
            if (!invertX) {
                hBox.getChildren().add(teamTrainerMonsterBox);
                hBox.getChildren().add(teamInfoBox);
            }
            else {
                hBox.getChildren().add(teamInfoBox);
                hBox.getChildren().add(teamTrainerMonsterBox);
            }
        }
        root.getChildren().add(hBox);
        return root;
    }
}
