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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BattleMenuControllerTest extends ApplicationTest {

    @Spy
    final
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
        final Button itemButton = lookup("#itemButton").query();
        final Button fleeButton = lookup("#fleeButton").query();

        assertTrue(abilitiesButton.isVisible());
        assertTrue(changeMonsterButton.isVisible());
        assertTrue(itemButton.isVisible());
        assertFalse(fleeButton.isVisible());
    }

    @Test
    void showAbilitiesTest(){
        final Button abilitiesButton = lookup("#abilitiesButton").query();
        doNothing().when(encounterController).showAbilities();
        clickOn(abilitiesButton);
        verify(encounterController).showAbilities();
    }

    @Test
    void changeMonsterTest(){
        final Button changeMonsterButton = lookup("#changeMonsterButton").query();
        doNothing().when(encounterController).showChangeMonsterList();
        clickOn(changeMonsterButton);
        verify(encounterController).showChangeMonsterList();
    }



}