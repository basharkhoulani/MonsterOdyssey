package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;

class EncounterControllerTest extends ApplicationTest {

    @InjectMocks
    EncounterController encounterController;
    @Spy
    final
    App app = new App(null);

    public void start(Stage stage) {
        ResourceBundle bundle = ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
        encounterController.setValues(bundle, null, null, encounterController, app);
        app.start(stage);
        app.show(encounterController);
        stage.requestFocus();
    }

}