package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.EncounterController;
import de.uniks.stpmon.team_m.dto.Opponent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.awt.*;
import java.util.Objects;

import static de.uniks.stpmon.team_m.Constants.*;

public class EncounterOpponentController extends Controller {
    private Boolean isEnemy;
    private Boolean isWild;
    private Boolean invertX;
    public Boolean isMultipleEnemyEncounter;
    public Boolean isTargeted = false;
    private Boolean isSelf;
    private Opponent currentOpponent;

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
    @FXML
    public ImageView starImageView;
    @FXML
    public ImageView heartImageView;
    @FXML
    public Button currentMonsterButton;

    private Opponent currentTarget;
    public Runnable onTargetChange;
    EncounterController encounterController;

    @Inject
    public EncounterOpponentController(){
    }

    public void init(Opponent currentOpponent, Boolean isEnemy, Boolean isWild, Boolean invertX, Boolean isMultipleEnemyEncounter, Boolean isSelf, EncounterController encounterController) {
        super.init();
        this.isEnemy = isEnemy;
        this.isWild = isWild;
        this.invertX = invertX;
        this.isMultipleEnemyEncounter = isMultipleEnemyEncounter;
        this.isSelf = isSelf;
        this.currentOpponent = currentOpponent;
        this.encounterController = encounterController;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        if (!GraphicsEnvironment.isHeadless()) {
            starImageView.setImage(new Image(Objects.requireNonNull(App.class.getResource(STAR_ICON)).toString()));
            heartImageView.setImage(new Image(Objects.requireNonNull(App.class.getResource(HEART_ICON)).toString()));
        }

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
        monsterImageViewVBox.setOnMouseClicked(event -> {
            if (isEnemy && isMultipleEnemyEncounter && onTargetChange != null) {
                onTargetChange.run();
            }
        });

        if(!isSelf){
            currentMonsterButton.setVisible(false);
            currentMonsterButton.setDisable(true);
        } else {
            currentMonsterButton.setOnAction(event -> {
                showMonsterInformation(currentOpponent, this);
            });
        }
        return parent;
    }

    private void showMonsterInformation(Opponent currentOpponent, EncounterOpponentController encounterOpponentController) {
        this.encounterController.showMonsterDetailsInEncounter(currentOpponent, encounterOpponentController);
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
        if (!GraphicsEnvironment.isHeadless()) {
            monsterImageView.setImage(image);
        }
        return this;
    }

    public Image getMonsterImage() {
        return monsterImageView.getImage();
    }

    public EncounterOpponentController setTrainerImage(Image image) {
        if (!GraphicsEnvironment.isHeadless()) {
            if (!trainerImageView.isDisabled()) {
                trainerImageView.setImage(image);
            }
        }
        return this;
    }

    public EncounterOpponentController onTarget() {
        monsterNameHBox.getStyleClass().clear();
        monsterNameHBox.getStyleClass().add("hBoxGreen");
        monsterImageViewVBox.setStyle("-fx-padding: 16px; -fx-border-color: red; -fx-border-radius: 100;");
        isTargeted = true;
        return this;
    }

    public EncounterOpponentController unTarget() {
        monsterNameHBox.getStyleClass().clear();
        if (isEnemy) {
            monsterNameHBox.getStyleClass().add("hBoxRed");
        } else {
            monsterNameHBox.getStyleClass().add("hBoxYellow");
        }
        monsterImageViewVBox.setStyle("-fx-padding: 0px; -fx-border-color: transparent; -fx-border-radius: 0;");
        isTargeted = false;
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
