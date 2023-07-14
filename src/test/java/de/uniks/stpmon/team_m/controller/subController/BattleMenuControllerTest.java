package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.controller.EncounterController;
import de.uniks.stpmon.team_m.utils.EncounterOpponentStorage;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BattleMenuControllerTest extends ApplicationTest {

    @Spy
    App app = new App(null);

    @InjectMocks
    BattleMenuController battleMenuController;
    @Mock
    EncounterOpponentStorage encounterOpponentStorage;
    @Mock
    EncounterController encounterController;

    @Override
    public void start(Stage stage){
        ResourceBundle bundle = ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
        battleMenuController.setValues(bundle, null, null, battleMenuController, app);

        battleMenuController.init(encounterController, encounterOpponentStorage, app);
        app.start(stage);
        app.show(battleMenuController);
        stage.requestFocus();
    }

    @Test
    void controllerTest(){
        final Button abilitiesButton = lookup("#abilitiesButton").query();
        final Button changeMonsterButton = lookup("#changeMonsterButton").query();
        final Button currentInfoButton = lookup("#currentInfoButton").query();
        final Button fleeButton = lookup("#fleeButton").query();

        assertTrue(abilitiesButton.isVisible());
        assertTrue(changeMonsterButton.isVisible());
        assertTrue(currentInfoButton.isVisible());
        assertFalse(fleeButton.isVisible());
    }

    @Test
    void showAbilitiesTest(){
        final Button abilitiesButton = lookup("#abilitiesButton").query();
        doNothing().when(encounterController).showAbilities();
        clickOn(abilitiesButton);
        verify(encounterController).showAbilities();
    }


}