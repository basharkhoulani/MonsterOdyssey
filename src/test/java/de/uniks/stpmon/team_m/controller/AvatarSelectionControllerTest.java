package de.uniks.stpmon.team_m.controller;


import de.uniks.stpmon.team_m.App;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import static de.uniks.stpmon.team_m.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void selectedAvatarTest() {
        final RadioButton avatar1RadioButton = lookup("#avatar1RadioButton").query();
        final RadioButton avatar2RadioButton = lookup("#avatar2RadioButton").query();
        final RadioButton avatar3RadioButton = lookup("#avatar3RadioButton").query();
        final RadioButton avatar4RadioButton = lookup("#avatar4RadioButton").query();
        clickOn(avatar1RadioButton);
        assertEquals(avatarSelectionController.selectedAvatar, AVATAR_1);
        clickOn(avatar2RadioButton);
        assertEquals(avatarSelectionController.selectedAvatar, AVATAR_2);
        clickOn(avatar3RadioButton);
        assertEquals(avatarSelectionController.selectedAvatar, AVATAR_3);
        clickOn(avatar4RadioButton);
        assertEquals(avatarSelectionController.selectedAvatar, AVATAR_4);
    }
}
