package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.controller.subController.AvatarSelectionController;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.UsersService;
import de.uniks.stpmon.team_m.utils.UserStorage;
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

import static de.uniks.stpmon.team_m.Constants.USER_STATUS_ONLINE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

@ExtendWith(MockitoExtension.class)
class AccountSettingControllerTest extends ApplicationTest {

    @Mock
    Provider<MainMenuController> mainMenuControllerProvider;
    @Mock
    Provider<LoginController> loginControllerProvider;
    @Mock
    Provider<AvatarSelectionController> avatarSelectionControllerProvider;
    @Mock
    UsersService usersService;
    @InjectMocks
    AccountSettingController accountSettingController;
    @Mock
    Provider<UserStorage> userStorageProvider;
    @Spy
    final
    App app = new App(null);

    @Override
    public void start(Stage stage) {
        ResourceBundle bundle = ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
        accountSettingController.setValues(bundle, null, null, accountSettingController, app);
        UserStorage userStorage = mock(UserStorage.class);
        when(userStorageProvider.get()).thenReturn(userStorage);
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
    void saveUsernameSuccessful() {
        final Label infoLabel = lookup("#informationLabel").query();
        final TextField usernameField = lookup("#usernameField").query();

        when(usersService.updateUser(anyString(), isNull(), isNull(), isNull(), isNull()))
                .thenReturn(Observable.just(new User(
                        "423f8d731c386bcd2204da39",
                        "UserPatch",
                        USER_STATUS_ONLINE,
                        null,
                        null
                )));

        when(userStorageProvider.get().getName()).thenReturn("UserPatch");

        clickOn("#usernameEditButton");
        clickOn(usernameField);
        write("UserPatch");
        clickOn("#saveUsernameButton");

        verify(usersService).updateUser("UserPatch", null, null, null, null);
        assertEquals("Your username has been changed successfully.", infoLabel.getText());
        assertEquals("UserPatch", usernameField.getPromptText());
    }

    @Test
    void changeUsernameTake() {
        final TextField usernameField = lookup("#usernameField").query();
        final Label usernameErrorLabel = lookup("#usernameErrorLabel").query();

        when(usersService.updateUser(anyString(), isNull(), isNull(), isNull(), isNull())).thenReturn(Observable.error(new Exception("HTTP 409")));

        clickOn("#usernameEditButton");
        clickOn(usernameField);
        write("UserPatch");
        clickOn("#saveUsernameButton");
        verify(usersService).updateUser("UserPatch", null, null, null, null);

        assertEquals("Username is already taken!", usernameErrorLabel.getText());
    }

    @Test
    void changeUsernameOtherError() {
        final TextField usernameField = lookup("#usernameField").query();
        final Label usernameErrorLabel = lookup("#usernameErrorLabel").query();

        when(usersService.updateUser(anyString(), isNull(), isNull(), isNull(), isNull())).thenReturn(Observable.error(new Exception("Test")));

        clickOn("#usernameEditButton");
        clickOn(usernameField);
        write("UserPatch");
        clickOn("#saveUsernameButton");
        verify(usersService).updateUser("UserPatch", null, null, null, null);

        assertEquals("Something went terribly wrong!", usernameErrorLabel.getText());
    }

    @Test
    void showPassword() {
        clickOn("#passwordEditButton");
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
        assertEquals("Your Password has been changed successfully.", infoLabel.getText());
    }

    @Test
    void changePasswordOtherError() {
        final TextField passwordField = lookup("#passwordField").query();
        final Label passwordErrorLabel = lookup("#passwordErrorLabel").query();

        when(usersService.updateUser(isNull(), isNull(), isNull(), isNull(), anyString())).thenReturn(Observable.error(new Exception("Test")));

        clickOn("#passwordEditButton");
        clickOn(passwordField);
        write("UserPatch");
        clickOn("#savePasswordButton");
        verify(usersService).updateUser(null, null, null, null, "UserPatch");
        assertEquals("Something went terribly wrong!", passwordErrorLabel.getText());
    }

    @Test
    void deleteAccount() {
        final LoginController loginController = mock(LoginController.class);
        when(loginControllerProvider.get()).thenReturn(loginController);
        doNothing().when(app).show(loginController);
        when(usersService.deleteUser())
                .thenReturn(Observable.just(new User(
                        "423f8d731c386bcd2204da39",
                        "UserDelete",
                        USER_STATUS_ONLINE,
                        null,
                        null)));

        clickOn("#deleteAccountButton");
        clickOn(ButtonType.OK.getText());
        verify(usersService).deleteUser();
        verify(app).show(loginController);
    }

    @Test
    void deleteAccountError() {
        when(usersService.deleteUser()).thenReturn(Observable.error(new Exception("Test")));
        clickOn("#deleteAccountButton");
        clickOn(ButtonType.OK.getText());
        clickOn(ButtonType.OK.getText());
        assertEquals("Monster Odyssey - Account Setting", app.getStage().getTitle());
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

    @Test
    void editAvatar() {
        AvatarSelectionController avatarSelectionController = new AvatarSelectionController();
        when(avatarSelectionControllerProvider.get()).thenReturn(avatarSelectionController);
        when(usersService.updateUser(isNull(), isNull(), anyString(), isNull(), isNull()))
                .thenReturn(Observable.just(new User(
                        "423f8d731c386bcd2204da39",
                        "New Avatar",
                        null,
                        "data:image/png;base64, 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",
                        null
                )));
        final Label infoLabel = lookup("#informationLabel").query();
        final Button editAvatarButton = lookup("#editAvatarButton").query();
        final Button saveAvatarButton = lookup("#saveAvatarButton").query();
        clickOn(editAvatarButton);
        waitForFxEvents();
        clickOn("#avatar2ImageView");
        clickOn(ButtonType.OK.getText());
        waitForFxEvents();
        clickOn(saveAvatarButton);
        waitForFxEvents();
        assertEquals("Your Avatar has been changed successfully.", infoLabel.getText());
    }

    @Test
    void saveAvatar() {

    }

    @Test
    void changeLanguage() {
        final Button changeLanguageButton = lookup("#changeLanguageButton").query();
        clickOn(changeLanguageButton);

        final DialogPane dialogPane = lookup(".dialog-pane").query();
        assertTrue(dialogPane.isVisible());
    }
}