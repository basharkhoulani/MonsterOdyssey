package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.controller.subController.IngameTrainerSettingsController;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import de.uniks.stpmon.team_m.dto.NPCInfo;
import de.uniks.stpmon.team_m.dto.Trainer;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Provider;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        trainerSettingsController.setValues(bundle, null, null, trainerSettingsController, app);
        when(trainerStorageProvider.get().getTrainer()).thenReturn(new Trainer("2023-05-22T17:51:46.772Z",
                "2023-05-22T17:51:46.772Z",
                "646bac223b4804b87c0b8054",
                "646bab5cecf584e1be02598a",
                "646bac8c1a74032c70fffe24",
                "Hans",
                "Premade_Character_01.png",
                0,
                "646bacc568933551792bf3d5",
                0,
                0,
                0,
                new NPCInfo(false)));
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
