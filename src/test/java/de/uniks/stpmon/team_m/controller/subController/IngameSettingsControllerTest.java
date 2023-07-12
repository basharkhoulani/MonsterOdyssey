package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;
import java.util.Locale;
import java.util.ResourceBundle;

@ExtendWith(MockitoExtension.class)
public class IngameSettingsControllerTest extends ApplicationTest {
    @Spy
    App app = new App(null);
    @InjectMocks
    IngameSettingsController ingameSettingsController;

    @Override
    public void start(Stage stage) {
        ResourceBundle bundle = ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
        ingameSettingsController.setValues(bundle, null, null, ingameSettingsController, app);
        app.start(stage);
        app.show(ingameSettingsController);
        stage.requestFocus();
    }
    @Test
    public void testSettings() {
        lookup("#audioSettingsButton").queryButton();
        lookup("#keybindingsButton").queryButton();
        lookup("#trainerSettingsButton").queryButton();
        lookup("#goBackButton").queryButton();
    }
}
