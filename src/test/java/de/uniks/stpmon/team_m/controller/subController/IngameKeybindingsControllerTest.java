package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class IngameKeybindingsControllerTest extends ApplicationTest {
    @Spy
    App app = new App(null);
    @InjectMocks
    IngameKeybindingsController ingameKeybindingsController;
    @Override
    public void start(Stage stage) {
        ResourceBundle bundle = ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
        Preferences preferences = mock(Preferences.class);
        ingameKeybindingsController.setValues(bundle, preferences, null, ingameKeybindingsController, app);
        app.start(stage);
        app.show(ingameKeybindingsController);
        stage.requestFocus();
    }
    @Test
    public void testKeybindings() {
        final Button walkUpButton = lookup("#walkUpButton").queryButton();
        final Button walkDownButton = lookup("#walkDownButton").queryButton();
        final Button walkLeftButton = lookup("#walkLeftButton").queryButton();
        final Button walkRightButton = lookup("#walkRightButton").queryButton();
        final Button interactionButton = lookup("#interactionButton").queryButton();
        final Button pauseMenuButton = lookup("#pauseMenuButton").queryButton();
        lookup("#checkButton").queryButton();
        lookup("#defaultButton").queryButton();
        lookup("#goBackButton").queryButton();
        final Label informationLabel = lookup("#informationLabel").query();
        assertEquals("Click on a button to change the keybinding respectively", informationLabel.getText());

        clickOn("#walkLeftButton");
        assertTrue(walkUpButton.isDisabled());
        assertTrue(walkDownButton.isDisabled());
        assertTrue(walkRightButton.isDisabled());
        assertTrue(interactionButton.isDisabled());
        assertTrue(pauseMenuButton.isDisabled());
        assertFalse(walkLeftButton.isDisabled());
        assertEquals("Waiting for input...", informationLabel.getText());
        assertEquals("...", walkLeftButton.getText());

        press(KeyCode.K);
        release(KeyCode.K);
        assertEquals("K", walkLeftButton.getText());
        assertEquals("Click the Check Button to confirm the change!", informationLabel.getText());
        assertFalse(walkUpButton.isDisabled());
        assertFalse(walkDownButton.isDisabled());
        assertFalse(walkRightButton.isDisabled());
        assertFalse(interactionButton.isDisabled());
        assertFalse(pauseMenuButton.isDisabled());
        assertFalse(walkLeftButton.isDisabled());

        clickOn("#checkButton");
        assertEquals("Keybindings successfully changed!", informationLabel.getText());

        clickOn("#walkRightButton");
        press(KeyCode.K);
        release(KeyCode.K);
        assertEquals("K", walkLeftButton.getText());
        assertNotEquals("K",walkRightButton.getText());
        assertEquals("Key is already used!", informationLabel.getText());

        clickOn("#walkDownButton");
        press(KeyCode.T);
        release(KeyCode.T);
        assertEquals("T", walkDownButton.getText());

        clickOn("#walkUpButton");
        press(KeyCode.M);
        release(KeyCode.M);
        assertEquals("M", walkUpButton.getText());

        clickOn("#interactionButton");
        press(KeyCode.DIGIT3);
        release(KeyCode.DIGIT3);
        assertEquals("3", interactionButton.getText());

        clickOn("#pauseMenuButton");
        press(KeyCode.UP);
        release(KeyCode.UP);
        assertEquals("UP", pauseMenuButton.getText());

        clickOn("#defaultButton");
        assertEquals("Keybindings successfully changed to default!", informationLabel.getText());
        assertEquals("A", walkLeftButton.getText());
        assertEquals("D", walkRightButton.getText());
        assertEquals("S", walkDownButton.getText());
        assertEquals("W", walkUpButton.getText());
        assertEquals("E", interactionButton.getText());
        assertEquals("ESCAPE", pauseMenuButton.getText());
    }
}
