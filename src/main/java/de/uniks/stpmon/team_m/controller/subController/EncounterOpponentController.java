package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.dto.Opponent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class EncounterOpponentController extends Controller {
    private final Boolean isEnemy;
    private final Boolean isWild;
    private final Boolean invertX;
    public final Boolean isMultipleEnemyEncounter;

    @FXML
    public HBox opponentHBox;

    @FXML
    public VBox monsterInfoBox;
    @FXML
    public ProgressBar experienceBar;
    @FXML
    public Label levelLabel;
    @FXML
    public ProgressBar HealthBar;
    @FXML
    public HBox healthHBox;
    @FXML
    public Label healthLabel;
    @FXML
    public HBox monsterNameHBox;
    @FXML
    public Label monsterNameLabel;
    @FXML
    public VBox trainerMonsterVBox;
    @FXML
    public VBox monsterImageViewVBox;
    @FXML
    public ImageView monsterImageView;
    @FXML
    public ImageView trainerImageView;
    @FXML
    public VBox splitterVBox;
    @FXML
    public HBox trainerMonsterHBox;

    private int monsterMaxHealth = 100;
    private Opponent currentTarget;

    public EncounterOpponentController(Boolean isEnemy, Boolean isWild, Boolean invertX, Boolean isMultipleEnemyEncounter) {
        this.isEnemy = isEnemy;
        this.isWild = isWild;
        this.invertX = invertX;
        this.isMultipleEnemyEncounter = isMultipleEnemyEncounter;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        if (isEnemy) {
            experienceBar.setVisible(false);
            experienceBar.setDisable(true);
            experienceBar.setMinWidth(0);
            experienceBar.setMaxWidth(0);
            healthHBox.setVisible(false);
            healthLabel.setVisible(false);
            healthLabel.setDisable(true);
            monsterNameHBox.getStyleClass().clear();
            monsterNameHBox.getStyleClass().add("hBoxRed");
            trainerMonsterVBox.getChildren().clear();
            if (isWild) {
                trainerMonsterVBox.setAlignment(Pos.BOTTOM_CENTER);
                trainerImageView.setDisable(true);
                trainerImageView.setVisible(false);
                trainerMonsterVBox.getChildren().add(monsterImageViewVBox);
            } else {
                trainerMonsterVBox.setAlignment(Pos.CENTER);
                trainerMonsterVBox.getChildren().add(0, trainerImageView);
                trainerMonsterVBox.getChildren().add(1, trainerMonsterHBox);
            }
        }
        if (invertX) {
            opponentHBox.getChildren().clear();
            opponentHBox.getChildren().add(0, trainerMonsterVBox);
            opponentHBox.getChildren().add(1, splitterVBox);
            opponentHBox.getChildren().add(2, monsterInfoBox);
        }
        return parent;
    }

    public Boolean getEnemy() {
        return isEnemy;
    }

    public Boolean getWild() {
        return isWild;
    }

    public Boolean getInvertX() {
        return invertX;
    }

    public EncounterOpponentController setExperienceBarValue(double value) {
        if (!experienceBar.isDisabled()) {
            experienceBar.setProgress(value);
        }
        return this;
    }

    public EncounterOpponentController setLevelLabel(String value) {
        levelLabel.setText(value);
        return this;
    }

    public EncounterOpponentController setHealthBarValue(double value) {
        HealthBar.setProgress(value);
        return this;
    }

    public EncounterOpponentController setHealthLabel(String value) {
        if (!healthLabel.isDisabled()) {
            healthLabel.setText(value);
        }
        return this;
    }

    public EncounterOpponentController setMonsterNameLabel(String value) {
        monsterNameLabel.setText(value);
        return this;
    }

    public EncounterOpponentController setMonsterImage(Image image) {
        monsterImageView.setImage(image);
        return this;
    }

    public EncounterOpponentController setTrainerImage(Image image) {
        if (!trainerImageView.isDisabled()) {
            trainerImageView.setImage(image);
        }
        return this;
    }

    public EncounterOpponentController onTarget() {
        monsterNameHBox.getStyleClass().clear();
        monsterNameHBox.getStyleClass().add("hBoxGreen");
        monsterImageViewVBox.setStyle("-fx-padding: 16px; -fx-border-color: red; -fx-border-radius: 100;");
        return this;
    }

    public Opponent getCurrentTarget() {
        return currentTarget;
    }

    public void setCurrentTarget(Opponent currentTarget) {
        this.currentTarget = currentTarget;
    }

    public ImageView getTrainerImageView() {
        return trainerImageView;
    }
}
