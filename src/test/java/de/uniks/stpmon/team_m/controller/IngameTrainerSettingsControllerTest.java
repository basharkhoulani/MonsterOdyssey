package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.controller.subController.IngameTrainerSettingsController;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class IngameTrainerSettingsControllerTest extends ApplicationTest {
    @Spy
    App app = new App(null);
    @InjectMocks
    IngameTrainerSettingsController trainerSettingsController;

    @Override
    public void start(Stage stage) {
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
