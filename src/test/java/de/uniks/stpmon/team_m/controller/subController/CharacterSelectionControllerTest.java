package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.controller.WelcomeSceneController;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.scene.control.RadioButton;
import javax.inject.Provider;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CharacterSelectionControllerTest extends ApplicationTest {
    @InjectMocks
    CharacterSelectionController characterSelectionController;
    @Mock
    Provider<WelcomeSceneController> welcomeSceneControllerProvider;
    @Spy
    App app = new App(null);

    @Override
    public void start(Stage stage) {
        final WelcomeSceneController welcomeSceneController = mock(WelcomeSceneController.class);
        when(welcomeSceneControllerProvider.get()).thenReturn(welcomeSceneController);
        doNothing().when(app).show(welcomeSceneController);

        app.start(stage);
        app.show(characterSelectionController);
        stage.requestFocus();
    }

    @Test
    void selectCharacterTest() {
        clickOn("Previous");
        verify(app).show(welcomeSceneControllerProvider.get());
        clickOn("Next");

        RadioButton radioButton1 = lookup("#character1RadioButton").query();
        RadioButton radioButton2 = lookup("#character2RadioButton").query();

        assertTrue(radioButton1.isSelected());
        assertFalse(radioButton2.isSelected());
        clickOn(radioButton2);
        assertTrue(radioButton2.isSelected());
        assertFalse(radioButton1.isSelected());
        clickOn(radioButton1);

    }

}
