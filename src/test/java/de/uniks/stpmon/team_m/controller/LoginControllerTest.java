package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.dto.LoginResult;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.AuthenticationService;
import de.uniks.stpmon.team_m.service.UsersService;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Provider;

import static de.uniks.stpmon.team_m.Constants.USER_STATUS_ONLINE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest extends ApplicationTest {

    @Mock
    Provider<MainMenuController> mainMenuControllerProvider;
    @Mock
    AuthenticationService authenticationService;
    @Mock
    UsersService usersService;

    @Spy
    App app = new App(null);

    @InjectMocks
    LoginController loginController;

    @Override
    public void start(Stage stage) {
        app.start(stage);
        app.show(loginController);
        stage.requestFocus();
    }

    @Test
    void signIn() {
        //successfully Sign In
        when(authenticationService.login(anyString(), anyString(), eq(false))).thenReturn(Observable.just(new LoginResult(
                "1",
                "1",
                "online",
                null,
                null,
                "a1a2",
                "a3a4")));

        final MainMenuController mainMenuController = mock(MainMenuController.class);
        when(mainMenuControllerProvider.get()).thenReturn(mainMenuController);
        doNothing().when(app).show(mainMenuController);

        write("1\t");
        write("12345678");
        clickOn("#signInButton");

        verify(authenticationService).login("1", "12345678", false);
        verify(app).show(mainMenuController);
    }

    @Test
    void signUp() {
        //successfully Sign Up
        when(usersService.createUser(anyString(), isNull(), anyString())).thenReturn(Observable.just(new User(
                "1",
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

        final MainMenuController mainMenuController = mock(MainMenuController.class);
        when(mainMenuControllerProvider.get()).thenReturn(mainMenuController);
        doNothing().when(app).show(mainMenuController);

        write("1\t");
        write("12345678");
        clickOn("#signUpButton");

        verify(usersService).createUser("1", null, "12345678");
        verify(authenticationService).login("1", "12345678", false);
        verify(app).show(mainMenuController);
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
        final Button signUpButton = lookup("Sign Up").query();
        final Button signInButton = lookup("Sign In").query();
        assertNotNull(signUpButton);
        assertNotNull(signInButton);

        assertTrue(signInButton.isDisabled());
        assertTrue(signUpButton.isDisabled());

        // test Sign In To MainMenu
        write("t\t");
        write("testtest");

        assertFalse(signInButton.isDisabled());
        assertFalse(signUpButton.isDisabled());
    }
}