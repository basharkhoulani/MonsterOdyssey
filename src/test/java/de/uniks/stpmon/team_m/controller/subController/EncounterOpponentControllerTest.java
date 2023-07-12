package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.Locale;
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
}
