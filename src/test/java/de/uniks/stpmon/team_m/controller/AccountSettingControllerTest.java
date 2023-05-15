package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.UserStorage;
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

import static de.uniks.stpmon.team_m.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountSettingControllerTest extends ApplicationTest {

    @Mock
    Provider<MainMenuController> mainMenuControllerProvider;
    @Mock
    UsersService usersService;
    @InjectMocks
    AccountSettingController accountSettingController;
    @Mock
    UserStorage userStorage;

    @Spy
    App app = new App(null);

    @Override
    public void start(Stage stage) {
        app.start(stage);
        app.show(accountSettingController);
        stage.requestFocus();
    }

    @Test
    void editUsername() {
        final Button editButton = lookup("#usernameEditButton").query();
        final TextField usernameField = lookup("#usernameField").query();
        assertTrue(usernameField.isDisabled());

        clickOn(editButton);
        assertFalse(usernameField.isDisabled());

        clickOn(editButton);
        assertTrue(usernameField.isDisabled());
    }

    @Test
    void saveUsername() {
        final Label infoLabel = lookup("#informationLabel").query();
        final TextField usernameField = lookup("#usernameField").query();

        when(usersService.updateUser(anyString(), isNull(), isNull(), isNull(), isNull()))
                .thenReturn(Observable.just(new User(
                        "1",
                        "UserPatch",
                        USER_STATUS_ONLINE,
                        null,
                        null
                )));

        when(userStorage.getName()).thenReturn("UserPatch");

        clickOn("#usernameEditButton");
        clickOn(usernameField);
        write("UserPatch");
        clickOn("#saveUsernameButton");

        verify(usersService).updateUser("UserPatch", null, null, null, null);
        assertEquals(USERNAME_SUCCESS_CHANGED, infoLabel.getText());
        assertEquals("UserPatch", usernameField.getPromptText());
    }

    @Test
    void showPassword() {
        final PasswordField passwordField = lookup("#passwordField").query();
        clickOn(passwordField);
        write("password");
        clickOn("#showPasswordButton");
        assertEquals("class de.uniks.stpmon.team_m.utils.PasswordFieldSkin", passwordField.getSkin().getClass().toString());
    }

    @Test
    void editPassword() {
        final Button editButton = lookup("#passwordEditButton").query();
        final PasswordField passwordField = lookup("#passwordField").query();
        final Button showPasswordBtn = lookup("#showPasswordButton").query();
        assertTrue(passwordField.isDisabled());
        assertTrue(showPasswordBtn.isDisabled());

        clickOn(editButton);
        assertFalse(passwordField.isDisabled());
        assertFalse(showPasswordBtn.isDisabled());

        clickOn(editButton);
        assertTrue(passwordField.isDisabled());
        assertTrue(showPasswordBtn.isDisabled());
    }

    @Test
    void savePassword() {
        final Label infoLabel = lookup("#informationLabel").query();
        final PasswordField passwordField = lookup("#passwordField").query();

        when(usersService.updateUser(isNull(), isNull(), isNull(), isNull(), anyString()))
                .thenReturn(Observable.just(new User(
                        "1",
                        "UserPatch",
                        USER_STATUS_ONLINE,
                        null,
                        null
                )));

        clickOn("#passwordEditButton");
        clickOn(passwordField);
        write("UserPatch");
        clickOn("#savePasswordButton");

        verify(usersService).updateUser(null, null, null, null, "UserPatch");
        assertEquals(PASSWORD_SUCCESS_CHANGED, infoLabel.getText());
    }

    @Test
    void deleteAccount() {
    }

    @Test
    void cancel() {
        final MainMenuController mainMenuController = mock(MainMenuController.class);
        when(mainMenuControllerProvider.get()).thenReturn(mainMenuController);
        doNothing().when(app).show(mainMenuController);

        clickOn("#cancelButton");

        verify(app).show(mainMenuController);
    }

    @Test
    void showDeletePopUp() {
        final Button deleteAccBtn = lookup("#deleteAccountButton").query();
        clickOn(deleteAccBtn);
        final DialogPane dialogPaneDelete = lookup(".dialog-pane").query();
        assertNotNull(dialogPaneDelete);
        final Label deleteLabel = dialogPaneDelete.getChildren().stream()
                .filter(node -> node instanceof Label)
                .map(node -> (Label) node)
                .findFirst()
                .orElse(null);
        assertNotNull(deleteLabel);
        assertEquals("Are you sure?", deleteLabel.getText());
    }
}