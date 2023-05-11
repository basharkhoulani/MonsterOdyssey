package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.dto.LoginResult;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.AuthenticationService;
import de.uniks.stpmon.team_m.service.UserStorage;
import de.uniks.stpmon.team_m.service.UsersService;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Provider;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewFriendControllerTest extends ApplicationTest {

    @Mock
    Provider<MainMenuController> mainMenuControllerProvider;
    @Mock
    UsersService usersService;

    @Spy
    App app = new App(null);
    @Spy
    UserStorage userStorage;
    @InjectMocks
    NewFriendController newFriendController;

    @Override
    public void start(Stage stage) throws Exception {
        app.start(stage);
        app.show(newFriendController);
        stage.requestFocus();
    }

    @Test
    void getTitleTest() {
        assertEquals("Add a new friend", newFriendController.getTitle());
    }

    @Test
    void backTest() {
        //successfully switch to MainMenu
        final MainMenuController mainMenuController = mock(MainMenuController.class);
        when(mainMenuControllerProvider.get()).thenReturn(mainMenuController);
        doNothing().when(app).show(mainMenuController);

        clickOn("#mainMenuButton");

        verify(app).show(mainMenuController);
    }

}