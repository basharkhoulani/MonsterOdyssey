package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class WelcomeSceneControllerTest extends ApplicationTest {


    @Spy
    App app = new App(null);
    @InjectMocks
    WelcomeSceneController welcomeSceneController;

    @Override
    public void start(Stage stage) {
        app.start(stage);
        app.show(welcomeSceneController);
        stage.requestFocus();
    }

    @Test
    void welcomeScene() {
        // Scene 1
        Label firstMessage = lookup("#firstMessage").query();
        assertEquals("Welcome to Monster Odyssey!", firstMessage.getText());

        Label secondMessage = lookup("#secondMessage").query();
        assertEquals("Welcome Aboard!", secondMessage.getText());

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

    }

}
