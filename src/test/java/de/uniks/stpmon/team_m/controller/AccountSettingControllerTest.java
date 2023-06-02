package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
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

import static de.uniks.stpmon.team_m.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    App app = new App(null);

    @Override
    public void start(Stage stage) {
        UserStorage userStorage = mock(UserStorage.class);
        when(userStorageProvider.get()).thenReturn(userStorage);
        app.start(stage);
        app.show(accountSettingController);
        stage.requestFocus();
    }

    @Test
    void changeAvatar() {
        when(avatarSelectionControllerProvider.get()).thenReturn(new AvatarSelectionController());
        final Button editAvatarButton = lookup("#editAvatarButton").query();
        clickOn(editAvatarButton);

        final DialogPane dialogPane = lookup(".dialog-pane").query();
        assertTrue(dialogPane.isVisible());

        final RadioButton radioButton1 = lookup("#avatar1RadioButton").query();
        final RadioButton radioButton2 = lookup("#avatar2RadioButton").query();
        final RadioButton radioButton3 = lookup("#avatar3RadioButton").query();
        final RadioButton radioButton4 = lookup("#avatar4RadioButton").query();
        assertFalse(radioButton1.isSelected());
        assertFalse(radioButton2.isSelected());
        assertFalse(radioButton3.isSelected());
        assertFalse(radioButton4.isSelected());
        clickOn(radioButton1);
        assertTrue(radioButton1.isSelected());
        assertFalse(radioButton2.isSelected());
        assertFalse(radioButton3.isSelected());
        assertFalse(radioButton4.isSelected());
        clickOn(radioButton2);
        assertFalse(radioButton1.isSelected());
        assertTrue(radioButton2.isSelected());
        assertFalse(radioButton3.isSelected());
        assertFalse(radioButton4.isSelected());
        clickOn(radioButton3);
        assertFalse(radioButton1.isSelected());
        assertFalse(radioButton2.isSelected());
        assertTrue(radioButton3.isSelected());
        assertFalse(radioButton4.isSelected());
        clickOn(radioButton4);
        assertFalse(radioButton1.isSelected());
        assertFalse(radioButton2.isSelected());
        assertFalse(radioButton3.isSelected());
        assertTrue(radioButton4.isSelected());

        final Button okButton = lookup("Ok").query();
        clickOn(okButton);
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
        assertEquals(USERNAME_SUCCESS_CHANGED, infoLabel.getText());
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
        assertEquals(PASSWORD_SUCCESS_CHANGED, infoLabel.getText());
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
        verify(loginController).setInformation("Account successfully deleted");
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
    void changeLanguage() {
        final Button changeLanguageButton = lookup("#changeLanguageButton").query();
        clickOn(changeLanguageButton);

        final DialogPane dialogPane = lookup(".dialog-pane").query();
        assertTrue(dialogPane.isVisible());

        final RadioButton radioButtonLanguageEnglish = lookup("#radioButtonLanguageEnglish").query();
        final RadioButton radioButtonLanguageGerman = lookup("#radioButtonLanguageGerman").query();
        final RadioButton radioButtonLanguageChinese = lookup("#radioButtonLanguageChinese").query();
        final Button applyLanguageButton = lookup("#applyLanguageButton").query();

        assertTrue(radioButtonLanguageEnglish.isSelected());
        assertFalse(radioButtonLanguageGerman.isSelected());
        assertFalse(radioButtonLanguageChinese.isSelected());

        clickOn(radioButtonLanguageGerman);
        assertTrue(radioButtonLanguageGerman.isSelected());
        assertFalse(radioButtonLanguageEnglish.isSelected());
        assertFalse(radioButtonLanguageChinese.isSelected());

        clickOn(radioButtonLanguageChinese);
        assertTrue(radioButtonLanguageChinese.isSelected());
        assertFalse(radioButtonLanguageEnglish.isSelected());
        assertFalse(radioButtonLanguageGerman.isSelected());

        clickOn(radioButtonLanguageEnglish);
        assertTrue(radioButtonLanguageEnglish.isSelected());
        assertFalse(radioButtonLanguageGerman.isSelected());
        assertFalse(radioButtonLanguageChinese.isSelected());

        clickOn(applyLanguageButton);
        verify(app).show(accountSettingController);
    }
}