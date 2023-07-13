package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.controller.EncounterController;
import de.uniks.stpmon.team_m.controller.IngameController;
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

import java.util.Locale;
import java.util.ResourceBundle;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EncounterResultControllerTest extends ApplicationTest {

    @Spy
    App app = new App(null);

    @InjectMocks
    EncounterResultController encounterResultController;
    @Mock
    Provider<EncounterController> encounterControllerProvider;
    @Mock
    Provider<IngameController> ingameControllerProvider;

    @Override
    public void start(Stage stage){
        ResourceBundle bundle = ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
        encounterResultController.setValues(bundle, null, null, encounterResultController, app);
        encounterResultController.init(app);
        app.start(stage);
        app.show(encounterResultController);
    }

    @Test
    void clickOK() {
        final Button okButton = lookup("#okButton").query();
        EncounterController encounterController = mock(EncounterController.class);
        when(encounterControllerProvider.get()).thenReturn(encounterController);
        IngameController ingameController = mock(IngameController.class);
        when(ingameControllerProvider.get()).thenReturn(ingameController);

        doNothing().when(encounterController).destroy();
        doNothing().when(app).show(ingameController);

        clickOn(okButton);

        verify(app).show(ingameControllerProvider.get());
    }
}