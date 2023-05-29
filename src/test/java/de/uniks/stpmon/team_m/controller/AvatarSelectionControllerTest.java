package de.uniks.stpmon.team_m.controller;


import de.uniks.stpmon.team_m.App;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

@ExtendWith(MockitoExtension.class)
public class AvatarSelectionControllerTest extends ApplicationTest {
    @Spy
    App app = new App(null);
    @InjectMocks
    AvatarSelectionController avatarSelectionController;

    @Override
    public void start(Stage stage) {
        app.start(stage);
        app.show(avatarSelectionController);
        stage.requestFocus();
    }


    @Test
    void selectFileTest() {
        //The FileChooser is not a JavaFX control, so it is not located by the FxRobot and can not be tested.
    }
}
