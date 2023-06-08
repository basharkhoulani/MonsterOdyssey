package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.AppTest;
import de.uniks.stpmon.team_m.controller.subController.IngameTrainerSettingsController;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Provider;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class IngameTrainerSettingsControllerTest extends ApplicationTest {
    @Spy
    App app = new App(null);

    @Mock
    Provider<TrainerStorage> trainerStorageProvider;

    @InjectMocks
    IngameTrainerSettingsController trainerSettingsController;

    @Override
    public void start(Stage stage) {
        ResourceBundle bundle = ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
        final TrainerStorage trainerStorage = mock(TrainerStorage.class);
        Mockito.when(trainerStorageProvider.get()).thenReturn(trainerStorage);
        String path = "Premade_Character_01.png";
        Mockito.when(trainerStorageProvider.get().getTrainerSprite()).thenReturn(path);
        trainerSettingsController.setValues(bundle,null,null,trainerSettingsController,app);
        app.start(stage);
        app.show(trainerSettingsController);
        stage.requestFocus();
    }

    @Test
    public void Cancel() {

        Button cancelButton = lookup("#cancelButton").queryButton();
        Button deleteTrainerButton = lookup("#deleteTrainerButton").queryButton();
        assertEquals(cancelButton.getText(), "Cancel");
        assertEquals(deleteTrainerButton.getText(), "Delete your trainer");
        clickOn(cancelButton);
    }
}
