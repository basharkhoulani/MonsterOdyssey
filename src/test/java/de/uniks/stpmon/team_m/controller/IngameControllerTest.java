package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Provider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IngameControllerTest extends ApplicationTest {

    @Spy
    App app = new App(null);

    @Mock
    Provider<MainMenuController> mainMenuControllerProvider;

    @InjectMocks
    IngameController ingameController;

    @Override
    public void start(Stage stage) {
        app.start(stage);
        app.show(ingameController);
        stage.requestFocus();
    }

    @Test
    void showHelp() {
        clickOn("#helpSymbol");
        final DialogPane dialogPane = lookup(".dialog-pane").query();
        assertNotNull(dialogPane);
        final Label helpLabel = dialogPane.getChildren().stream()
                .filter(node -> node instanceof Label)
                .map(node -> (Label) node)
                .findFirst()
                .orElse(null);
        assertNotNull(helpLabel);
        assertEquals("""
                Click 'p' on your keyboard for pause menu.
                     \u2191\s
                 \u2190 \u2193 \u2192  to move your character.\s
                Click Space for interact and attack.""", helpLabel.getText());
        clickOn("OK");
        final Label gameTitle = lookup("Monster Odyssey").query();
        assertNotNull(gameTitle);
    }

    @Test
    void pauseGame() {
        MainMenuController mainMenuController = mock(MainMenuController.class);
        when(mainMenuControllerProvider.get()).thenReturn(mainMenuController);
        doNothing().when(app).show(any());
        // test Ingame Pause
        type(KeyCode.P);
        final DialogPane dialogPanePause = lookup(".dialog-pane").query();
        assertNotNull(dialogPanePause);
        final Label pauseLabel = dialogPanePause.getChildren().stream()
                .filter(node -> node instanceof Label)
                .map(node -> (Label) node)
                .findFirst()
                .orElse(null);
        assertNotNull(pauseLabel);
        assertEquals("What do you want to do?", pauseLabel.getText());

        // test Ingame Unpause With Key Code P
        type(KeyCode.E);
        type(KeyCode.P);
        // test Ingame Unpause With Button
        type(KeyCode.P);
        clickOn("Resume Game");
        final Label gameTitleUnpauseButton = lookup("Monster Odyssey").query();
        assertNotNull(gameTitleUnpauseButton);

        // test Ingame Back To Main Menu
        type(KeyCode.P);
        clickOn("Save Game & Leave");
        verify(app).show(mainMenuController);
    }
}
