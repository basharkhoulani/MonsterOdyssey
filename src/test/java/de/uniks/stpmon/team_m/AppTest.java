package de.uniks.stpmon.team_m;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

class AppTest extends ApplicationTest {

    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        new App().start(this.stage);
    }

    @Test
    void testLoginScreen() {
        final Label welcomeLabel = lookup("Welcome to").query();
        final Button signUpButton = lookup("Sign Up").query();
        final Button signInButton = lookup("Sign In").query();

        assertNotNull(welcomeLabel);
        assertNotNull(signUpButton);
        assertNotNull(signInButton);

    }

    @Test
    void testSigninToMainMenu() {
        final TextField usernameField = lookup("#usernameField").query();
        assertNotNull(usernameField);
        final TextField passwordField = lookup("#passwordField").query();
        assertNotNull(passwordField);
        final Button signInButton = lookup("Sign In").query();
        assertNotNull(signInButton);

        clickOn(signInButton);

        assertEquals("Monster Odyssey - Main Menu", stage.getTitle());
    }

    @Test
    void testMainMenuStartGameButton() {
        signInBasicFn();
        final Button startGameButton = lookup("Start Game").query();
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

    void signInBasicFn() {
        final TextField usernameField = lookup("#usernameField").query();
        assertNotNull(usernameField);
        final TextField passwordField = lookup("#passwordField").query();
        assertNotNull(passwordField);
        final Button signInButton = lookup("Sign In").query();
        assertNotNull(signInButton);

        clickOn(signInButton);
    }

}