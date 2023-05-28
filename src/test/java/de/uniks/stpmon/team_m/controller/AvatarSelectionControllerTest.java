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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void selectAvatarTest() {
        RadioButton avatar1RadioButton = lookup("#avatar1RadioButton").query();
        TextField uploadTextField = lookup("#uploadTextField").query();
        RadioButton avatar2RadioButton = lookup("#avatar2RadioButton").query();
        RadioButton avatar3RadioButton = lookup("#avatar3RadioButton").query();
        RadioButton avatar4RadioButton = lookup("#avatar4RadioButton").query();
        clickOn(uploadTextField);
        write("/pictures/avatar.png");
        clickOn(avatar1RadioButton);
        assertTrue(avatar1RadioButton.isSelected());
        assertTrue(uploadTextField.getText().isEmpty());
        clickOn(avatar2RadioButton);
        assertTrue(avatar2RadioButton.isSelected());
        assertFalse(avatar1RadioButton.isSelected());
        clickOn(avatar3RadioButton);
        assertTrue(avatar3RadioButton.isSelected());
        assertFalse(avatar2RadioButton.isSelected());
        clickOn(avatar4RadioButton);
        assertTrue(avatar4RadioButton.isSelected());
        assertFalse(avatar3RadioButton.isSelected());
        clickOn(uploadTextField);
        assertFalse(avatar4RadioButton.isSelected());
    }
}
