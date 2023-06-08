package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.AppTest;
import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.controller.subController.IngameTrainerSettingsController;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Provider;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IngameControllerTest extends ApplicationTest {

    @Spy
    App app = new App(null);

    @Mock
    Provider<IngameTrainerSettingsController> trainerSettingsControllerProvider;

    @Mock
    Provider<MainMenuController> mainMenuControllerProvider;

    @Mock
    TrainerStorage trainerStorage;

    @InjectMocks
    IngameController ingameController;

    @Override
    public void start(Stage stage) {
        ResourceBundle bundle = ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
        ingameController.setValues(bundle,null,null,ingameController,app);
        final IngameTrainerSettingsController trainerSettingsController = mock(IngameTrainerSettingsController.class);
        Mockito.when(trainerSettingsControllerProvider.get()).thenReturn(trainerSettingsController);
        Mockito.when(trainerStorage.getTrainerSpriteChunk()).thenReturn(new Image(Objects.requireNonNull(AppTest.class.getResource("images/Monster.png")).toString()));
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
        clickOn("OK");
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

        // test Ingame Back To Main Menu
        type(KeyCode.P);
        clickOn("Save Game & Leave");
        verify(app).show(mainMenuController);
    }
}
