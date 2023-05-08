package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.dto.LoginResult;
import de.uniks.stpmon.team_m.service.AuthenticationService;
import de.uniks.stpmon.team_m.service.TokenStorage;
import de.uniks.stpmon.team_m.service.UserStorage;
import de.uniks.stpmon.team_m.service.UsersService;
import io.reactivex.rxjava3.core.Observable;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Inject;
import javax.inject.Provider;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest extends ApplicationTest {

    @Mock
    Provider<MainMenuController> mainMenuControllerProvider;
    @Mock
    AuthenticationService authenticationService;
    @Mock
    TokenStorage tokenStorage;
    @Mock
    UserStorage user;
    @Mock
    UsersService usersService;

    @Spy
    App app = new App(null);

    @InjectMocks
    LoginController loginController;

    @Override
    public void start(Stage stage) throws Exception {
        app.start(stage);
        app.show(loginController);
        stage.requestFocus();
    }

    @Test
    void signIn() {
        //successfully login
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

        verify(app).show(mainMenuController);
    }
}