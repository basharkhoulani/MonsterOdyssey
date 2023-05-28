package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.TestComponent;
import de.uniks.stpmon.team_m.dto.Region;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;


import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;


public class WelcomeSceneControllerTest extends ApplicationTest{
    private Stage stage;
    private final App app = new App(null);
    private final TestComponent component = (TestComponent) de.uniks.stpmon.team_m.DaggerTestComponent.builder().mainApp(app).build();

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        app.start(this.stage);
        app.show(component.loginController());
        stage.requestFocus();
    }

    @Test
    void welcomeScene() {
        assertEquals("Monster Odyssey - Sign Up & In", stage.getTitle());
        write("t\t");
        write("testtest");
        clickOn("Sign In");
        assertEquals("Monster Odyssey - Main Menu", stage.getTitle());


        final Button startGameButton = lookup("Start Game").query();
        final ObservableList<Region> items = FXCollections
                .observableArrayList(new Region("TestRegion", "NamedRegion", null, null, null, null));
        final ListView<Region> regionListView = lookup("#regionListView").query();
        regionListView.setItems(items);
        regionListView.getSelectionModel().selectFirst();
        assertNotNull(regionListView);
        waitForFxEvents();
        assertFalse(startGameButton.isDisabled());
        clickOn(startGameButton);
        assertEquals("Monster Odyssey - Ingame", stage.getTitle());

        // Scene 1
        Label firstMessage = lookup("#firstMessage").query();
        assertEquals("Welcome to Monster Odyssey!", firstMessage.getText());

        Label secondMessage =  lookup("#secondMessage").query();
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
