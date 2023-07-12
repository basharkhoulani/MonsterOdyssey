package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.dto.Opponent;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;

public class EncounterOpponentControllerTest extends ApplicationTest {
    App app = new App(null);
    private EncounterOpponentController encounterOpponentController;

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
        app.start(stage);
        encounterOpponentController = new EncounterOpponentController(false, false, false, false);
        ResourceBundle bundle = ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
        encounterOpponentController.setValues(bundle, null, null, encounterOpponentController, app);
        encounterOpponentController.init();
        app.show(encounterOpponentController);
        stage.requestFocus();
    }

    @Test
    public void renderTest() {
        assertFalse(encounterOpponentController.experienceBar.isDisabled());
        assertFalse(encounterOpponentController.healthLabel.isDisabled());
        assertFalse(encounterOpponentController.trainerImageView.isDisabled());
        assertEquals(encounterOpponentController.opponentHBox.getChildren().get(0), encounterOpponentController.monsterInfoBox);
        assertEquals(encounterOpponentController.opponentHBox.getChildren().get(1), encounterOpponentController.splitterVBox);
        assertEquals(encounterOpponentController.opponentHBox.getChildren().get(2), encounterOpponentController.trainerMonsterVBox);
    }

    @Test
    public void renderForWildTest() {
        encounterOpponentController = new EncounterOpponentController(true, true, true, false);
        ResourceBundle bundle = ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
        encounterOpponentController.setValues(bundle, null, null, encounterOpponentController, app);
        app.show(encounterOpponentController);
        assertTrue(encounterOpponentController.experienceBar.isDisabled());
        assertTrue(encounterOpponentController.healthLabel.isDisabled());
        assertTrue(encounterOpponentController.trainerImageView.isDisabled());
        assertEquals(encounterOpponentController.opponentHBox.getChildren().get(0), encounterOpponentController.trainerMonsterVBox);
        assertEquals(encounterOpponentController.opponentHBox.getChildren().get(1), encounterOpponentController.splitterVBox);
        assertEquals(encounterOpponentController.opponentHBox.getChildren().get(2), encounterOpponentController.monsterInfoBox);
    }

    @Test
    public void renderForEnemyTrainer() {
        encounterOpponentController = new EncounterOpponentController(true, false, true, false);
        ResourceBundle bundle = ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
        encounterOpponentController.setValues(bundle, null, null, encounterOpponentController, app);
        app.show(encounterOpponentController);
        assertFalse(encounterOpponentController.trainerImageView.isDisabled());
    }

    @Test
    public void setExperienceBarValue() {
        encounterOpponentController.setExperienceBarValue(0.5);
        // Perform assertions
        ProgressBar experienceBar = encounterOpponentController.experienceBar;
        assertEquals(0.5, experienceBar.getProgress());
    }

    @Test
    void setHealthBarValue() {
        encounterOpponentController.setHealthBarValue(0.75);
        // Perform assertions
        ProgressBar healthBar = encounterOpponentController.HealthBar;
        assertEquals(0.75, healthBar.getProgress());
    }

    @Test
    void setHealthLabel() {
        Platform.runLater(() -> {
            encounterOpponentController.setHealthLabel("100/150");
            // Perform assertions
            assertEquals("100/150", encounterOpponentController.healthLabel.getText());
        });
    }

    @Test
    void setMonsterNameLabel() {
        Platform.runLater(() -> {
            encounterOpponentController.setMonsterNameLabel("Pikachu");
            // Perform assertions
            assertEquals("Pikachu", encounterOpponentController.monsterNameLabel.getText());
        });
    }

    @Test
    void setMonsterImage() {
        Platform.runLater(() -> {
            Image image = new Image(Objects.requireNonNull(Main.class.getResource("images/Monster.png")).toString());
            encounterOpponentController.setMonsterImage(image);
            // Perform assertions
            ImageView monsterImageView = encounterOpponentController.monsterImageView;
            assertEquals(image, monsterImageView.getImage());
        });
    }

    @Test
    void setTrainerImage() {
        Image image = new Image(Objects.requireNonNull(Main.class.getResource("images/Monster.png")).toString());
        encounterOpponentController.setTrainerImage(image);
        // Perform assertions
        ImageView trainerImageView = encounterOpponentController.trainerImageView;
        assertEquals(image, trainerImageView.getImage());
    }

    @Test
    void onTarget() {
        encounterOpponentController.onTarget();
        // Perform assertions on the visual effects
        assertTrue(encounterOpponentController.isTargeted);
    }

    @Test
    void unTarget() {
        encounterOpponentController.unTarget();
        // Perform assertions on the visual effects
        assertFalse(encounterOpponentController.isTargeted);
    }

    @Test
    void getCurrentTarget() {
        assertNull(encounterOpponentController.getCurrentTarget());
    }

    @Test
    void setCurrentTarget() {
        Opponent target = new Opponent("1", "Pikachu", "42", "1", "100", true, true, "1", null, null, 0);
        encounterOpponentController.setCurrentTarget(target);
        // Perform assertions
        assertEquals(target, encounterOpponentController.getCurrentTarget());
    }

    @Test
    void getTrainerImageView() {
        ImageView trainerImageView = encounterOpponentController.getTrainerImageView();
        assertNotNull(trainerImageView);
    }

    @Test
    void setLevelLabel() {
        Platform.runLater(() -> {
            encounterOpponentController.setLevelLabel("1");
            // Perform assertions
            assertEquals("1", encounterOpponentController.levelLabel.getText());
        });
    }


    @Test
    void getMonsterName() {
        Platform.runLater(() -> {
            encounterOpponentController.setMonsterNameLabel("Pikachu");
            String monsterName = encounterOpponentController.getMonsterName();
            assertEquals("Pikachu", monsterName);
        });
    }

    @Test
    void getEnemy() {
        boolean isEnemy = encounterOpponentController.getEnemy();
        assertFalse(isEnemy);
    }
}
