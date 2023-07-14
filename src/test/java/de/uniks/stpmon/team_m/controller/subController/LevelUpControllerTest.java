package de.uniks.stpmon.team_m.controller.subController;


import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.controller.EncounterController;
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

/*
@ExtendWith(MockitoExtension.class)
public class LevelUpControllerTest extends ApplicationTest {
    @Spy
    final
    App app = new App(null);
    @InjectMocks
    LevelUpController levelUpController;
    @Mock
    Provider<EncounterController> encounterControllerProvider;


    @Override
    public void start(Stage stage) {
        ResourceBundle bundle = ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
        levelUpController.setValues(bundle, null, null, levelUpController, app);
        final EncounterController encounterController = mock(EncounterController.class);
        when(encounterControllerProvider.get()).thenReturn(encounterController);
        doNothing().when(app).show(encounterController);

        app.start(stage);
        app.show(levelUpController);
        stage.requestFocus();
    }


    @Test
    public void levelUpPopUpTest() {

        //final Button okButton = lookup("#okButton").query();
        //clickOn(okButton);
    }


}

 */