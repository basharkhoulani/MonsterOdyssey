package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.controller.subController.ChangeLanguageController;
import de.uniks.stpmon.team_m.dto.LoginResult;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.AuthenticationService;
import de.uniks.stpmon.team_m.service.UsersService;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.control.*;
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
import java.util.prefs.Preferences;

import static de.uniks.stpmon.team_m.Constants.USER_STATUS_ONLINE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest extends ApplicationTest {
    @Mock
    AuthenticationService authenticationService;
    @Mock
    UsersService usersService;
    @Mock
    Provider<ChangeLanguageController> changeLanguageControllerProvider;
    @Spy
    final
    App app = new App(null);

    @InjectMocks
    LoginController loginController;

    @Override
    public void start(Stage stage) {
        ResourceBundle bundle = ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
        Preferences preferences = mock(Preferences.class);
        loginController.setValues(bundle, preferences, null, loginController, app);
        app.start(stage);
        app.show(loginController);
        stage.requestFocus();
    }

    @Test
    void signInSuccessful() {
        //successfully Sign In
        when(authenticationService.login(anyString(), anyString(), eq(false))).thenReturn(Observable.just(new LoginResult(
                "423f8d731c386bcd2204da39",
                "1",
                "online",
                null,
                null,
                "a1a2",
                "a3a4")));

        clickOn("#usernameField");
        write("1\t");
        write("12345678");
        clickOn("#signInButton");

        verify(authenticationService).login("1", "12345678", false);
    }

    @Test
    void signInWrongUsername() {
        //Sign In with incorrect username or password
        when(authenticationService.login(anyString(), anyString(), anyBoolean())).thenReturn(Observable.error(new Exception("HTTP 401")));
        clickOn("#usernameField");
        write("Bob\t");
        write("12345678");
        clickOn("#signInButton");
        verify(authenticationService).login("Bob", "12345678", false);
        final Label errorLabel = lookup("#passwordErrorLabel").query();
        assertEquals("Wrong Password or Username! Try again!", errorLabel.getText());
    }

    @Test
    void signInOtherError() {
        //Sign In with other errors
        when(authenticationService.login(anyString(), anyString(), anyBoolean())).thenReturn(Observable.error(new Exception("Test")));
        clickOn("#usernameField");
        write("Bob\t");
        write("12345678");
        clickOn("#signInButton");
        verify(authenticationService).login("Bob", "12345678", false);
        final Label errorLabel = lookup("#passwordErrorLabel").query();
        assertEquals("Something went terribly wrong!", errorLabel.getText());
    }

    @Test
    void signUpSuccessful() {
        //successfully Sign Up
        when(usersService.createUser(anyString(), isNull(), anyString())).thenReturn(Observable.just(new User(
                "423f8d731c386bcd2204da39",
                "1",
                USER_STATUS_ONLINE,
                null,
                null
        )));

        when(authenticationService.login(anyString(), anyString(), eq(false))).thenReturn(Observable.just(new LoginResult(
                "1",
                "1",
                USER_STATUS_ONLINE,
                null,
                null,
                "a1a2",
                "a3a4")));

        clickOn("#usernameField");
        write("1\t");
        write("12345678");
        clickOn("#signUpButton");

        verify(usersService).createUser("1", null, "12345678");
        verify(authenticationService).login("1", "12345678", false);
    }

    @Test
    void signUpUsernameTaken() {
        when(usersService.createUser(anyString(), isNull(), anyString())).thenReturn(Observable.error(new Exception("HTTP 409")));

        clickOn("#usernameField");
        write("1\t");
        write("12345678");
        clickOn("#signUpButton");
        verify(usersService).createUser("1", null, "12345678");
        final Label errorLabel = lookup("#passwordErrorLabel").query();
        assertEquals("Username is already taken!", errorLabel.getText());
    }

    @Test
    void signUpOtherError() {
        //Sign In with other errors
        when(usersService.createUser(anyString(), isNull(), anyString())).thenReturn(Observable.error(new Exception("Test")));
        clickOn("#usernameField");
        write("Bob\t");
        write("12345678");
        clickOn("#signUpButton");
        verify(usersService).createUser("Bob", null, "12345678");
        final Label errorLabel = lookup("#passwordErrorLabel").query();
        assertEquals("Something went terribly wrong!", errorLabel.getText());
    }


    @Test
    void showHidePassword() {
        final PasswordField passwordField = lookup("#passwordField").query();
        write("\t");
        write("password");
        clickOn("#hideButton");
        assertEquals("class de.uniks.stpmon.team_m.utils.PasswordFieldSkin", passwordField.getSkin().getClass().toString());
    }

    @Test
    void disableButton() {
        final Button signUpButton = lookup("#signUpButton").query();
        final Button signInButton = lookup("#signInButton").query();
        assertNotNull(signUpButton);
        assertNotNull(signInButton);

        assertTrue(signInButton.isDisabled());
        assertTrue(signUpButton.isDisabled());

        // test Sign In To MainMenu
        clickOn("#usernameField");
        write("t\t");
        write("testtest");

        assertFalse(signInButton.isDisabled());
        assertFalse(signUpButton.isDisabled());
    }

    @Test
    void changeLanguage() {
        ChangeLanguageController changeLanguageController = new ChangeLanguageController();
        when(changeLanguageControllerProvider.get()).thenReturn(changeLanguageController);
        final Button languageSettings = lookup("#languageSettings").query();
        clickOn(languageSettings);

        final DialogPane dialogPane = lookup(".dialog-pane").query();
        assertTrue(dialogPane.isVisible());
    }
}