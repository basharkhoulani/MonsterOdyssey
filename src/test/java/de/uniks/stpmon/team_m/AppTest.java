package de.uniks.stpmon.team_m;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

class AppTest extends ApplicationTest {


    @Override
    public void start(Stage stage) throws Exception {
        new App().start(stage);
    }

    @Test
    void testLoading() {
        final Label loading = lookup("Loading...").query();
        assertNotNull(loading);
    }

    @Test
    void testMainMenuStartGameButton() {
        final Button startGameButton = lookup("#startGameButton").query();
        assertNotNull(startGameButton);
        assertTrue(startGameButton.isDisabled());
        final VBox regionRadioButtonList = lookup("#regionRadioButtonList").query();
        assertNotNull(regionRadioButtonList);
        final RadioButton radioButton = regionRadioButtonList.getChildren().stream()
                .filter(node -> node instanceof RadioButton)
                .map(node -> (RadioButton) node)
                .findFirst()
                .orElse(null);
        assertNotNull(radioButton);
        clickOn(radioButton);
        assertFalse(startGameButton.isDisabled());
    }

}