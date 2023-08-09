package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.controller.EncounterController;
import de.uniks.stpmon.team_m.controller.IngameController;
import de.uniks.stpmon.team_m.dto.NPCInfo;
import de.uniks.stpmon.team_m.dto.Trainer;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Provider;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EncounterResultControllerTest extends ApplicationTest {

    @Spy
    final
    App app = new App(null);

    @InjectMocks
    EncounterResultController encounterResultController;
    @Mock
    Provider<EncounterController> encounterControllerProvider;
    @Mock
    Provider<TrainerStorage> trainerStorageProvider;
    @Mock
    Provider<IngameController> ingameControllerProvider;

    @Override
    public void start(Stage stage){
        ResourceBundle bundle = ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
        encounterResultController.setValues(bundle, null, null, encounterResultController, app);
        encounterResultController.init(app);
        app.start(stage);
        app.show(encounterResultController);
        stage.requestFocus();
    }

    @Test
    void clickOK() {
        final Button okButton = lookup("#okButton").query();
        final EncounterController encounterController = mock(EncounterController.class);
        when(encounterControllerProvider.get()).thenReturn(encounterController);
        final IngameController ingameController = mock(IngameController.class);
        when(ingameControllerProvider.get()).thenReturn(ingameController);
        final TrainerStorage trainerStorage = mock(TrainerStorage.class);
        when(trainerStorageProvider.get()).thenReturn(trainerStorage);
        Trainer trainer = new Trainer(
                "2023-05-22T17:51:46.772Z",
                "2023-05-22T17:51:46.772Z",
                "646bac223b4804b87c0b8054",
                "646bab5cecf584e1be02598a",
                "646bac8c1a74032c70fffe24",
                "Hans",
                "Premade_Character_01.png",
                0,
                null,
                null,
                List.of(),
                "646bacc568933551792bf3d5",
                0,
                0,
                0,
                new NPCInfo(false, false, false, false, null, null, null), null);

        when(trainerStorage.getTrainer()).thenReturn(trainer);

        doNothing().when(encounterController).destroy();
        doNothing().when(app).show(ingameController);

        clickOn(okButton);

        verify(app).show(ingameControllerProvider.get());
    }
}