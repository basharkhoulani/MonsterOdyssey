package de.uniks.stpmon.team_m;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class AppTest extends ApplicationTest {


    @Override
    public void start(Stage stage) throws Exception {
        new App().start(stage);
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
    void testSigninToMainMenu () {
        final TextField usernameField = lookup("#usernameField").query();
        assertNotNull(usernameField);
        final TextField passwordField = lookup("#passwordField").query();
        assertNotNull(passwordField);
        final Button signInButton = lookup("Sign In").query();
        assertNotNull(signInButton);

        clickOn(usernameField);
        write("testtest");
        clickOn(passwordField);
        write("testtest");
        clickOn(signInButton);
        
    }

}