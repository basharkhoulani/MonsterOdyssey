package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.controller.subController.CharacterSelectionController;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
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

import static de.uniks.stpmon.team_m.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WelcomeSceneControllerTest extends ApplicationTest {


    @Spy
    App app = new App(null);
    @InjectMocks
    WelcomeSceneController welcomeSceneController;
    @Mock
    Provider<CharacterSelectionController> characterSelectionControllerProvider;
    @Mock
    Provider<IngameController> ingameControllerProvider;

    @Override
    public void start(Stage stage) {
        ResourceBundle bundle = ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
        welcomeSceneController.setValues(bundle,null,null,welcomeSceneController,app);
        final CharacterSelectionController characterSelectionController = mock(CharacterSelectionController.class);
        when(characterSelectionControllerProvider.get()).thenReturn(characterSelectionController);
        doNothing().when(app).show(characterSelectionController);

        final IngameController ingameController = mock(IngameController.class);
        when(ingameControllerProvider.get()).thenReturn(ingameController);
        doNothing().when(app).show(ingameController);

        app.start(stage);
        app.show(welcomeSceneController);
        stage.requestFocus();
    }

    @Test
    void welcomeSceneTest() {
        // Scene 1
        Label firstMessage = lookup("#firstMessage").query();
        assertEquals("Welcome to Monster Odyssey!", firstMessage.getText());

        Label secondMessage = lookup("#secondMessage").query();
        assertEquals("Welcome Aboard!", secondMessage.getText());

        clickOn("Next");
        clickOn("Previous");
        clickOn("Next");

        // Scene 2
        Label thirdMessage = lookup("#firstMessage").query();
        assertEquals("We are the crew of this ship.", thirdMessage.getText());

        Label fourthMessage = lookup("#secondMessage").query();
        assertEquals("My name is James.", fourthMessage.getText());

        Label fifthMessage = lookup("#thirdMessage").query();
        assertEquals("And my name is Henry!", fifthMessage.getText());

        clickOn("Next");

        // Scene 3
        Label sixthMessage = lookup("#firstMessage").query();
        assertEquals("Now then, we'll need to look up your application.", sixthMessage.getText());

        Label seventhMessage = lookup("#secondMessage").query();
        assertEquals("Can we have your name?", seventhMessage.getText());

        clickOn("Next");

        // Scene 4
        final DialogPane dialogPane = lookup(".dialog-pane").query();
        assertNotNull(dialogPane);
        clickOn("#nameField");
        write("Ash");
        clickOn("OK");

        // Scene 5
        Label eightMessage = lookup("#firstMessage").query();
        assertEquals("Hello. Next, we 'd like to take a picture of you.", eightMessage.getText());

        Label ninthMessage = lookup("#secondMessage").query();
        assertEquals("Take as much time as you want to get dressed up for it.", ninthMessage.getText());

        clickOn("Next");

        // Scene 6 CharacterSelection
        verify(app).show(characterSelectionControllerProvider.get());

        clickOn("Next");

        // Scene 7
        verify(app).show(welcomeSceneController);
        Label tenthMessage = lookup("#firstMessage").query();
        assertEquals("Now it's time to start your Journey", tenthMessage.getText());

        Label eleventhMessage = lookup("#secondMessage").query();
        assertEquals("See you next time!", eleventhMessage.getText());

        clickOn("Next");

        // Change to Ingame
        verify(app).show(ingameControllerProvider.get());
    }
}
