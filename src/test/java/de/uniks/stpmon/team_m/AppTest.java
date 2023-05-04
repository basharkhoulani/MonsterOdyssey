package de.uniks.stpmon.team_m;

import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

class AppTest extends ApplicationTest {

    private Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        new App().start(this.stage);
        stage.requestFocus();
    }

    @Test
    void testLoginView() {

        // test Login Screen
        assertEquals("Monster Odyssey - Sign Up & In", stage.getTitle());
        final Label welcomeLabel = lookup("Welcome to").query();
        final Button signUpButton = lookup("Sign Up").query();
        final Button signInButton = lookup("Sign In").query();
        assertNotNull(welcomeLabel);
        assertNotNull(signUpButton);
        assertNotNull(signInButton);

        assertTrue(signInButton.isDisabled());
        assertTrue(signUpButton.isDisabled());

        // test Show Password In SignIn
        final PasswordField passwordField = lookup("#passwordField").query();
        clickOn(passwordField);
        write("password");
        final Button showPasswordBtn = lookup("#hideButton").query();
        clickOn(showPasswordBtn);
        assertEquals("class de.uniks.stpmon.team_m.utils.PasswordFieldSkin", passwordField.getSkin().getClass().toString());

        // test Sign In To MainMenu
        final TextField usernameField = lookup("#usernameField").query();
        clickOn(usernameField);
        write("t");
        clickOn(signInButton);
        assertEquals("Monster Odyssey - Main Menu", stage.getTitle());

    }

    @Test
    void testMainMenuView() {
        signInToMainMenu();

        // test Main Menu start game button
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

        // test back to Sign In
        clickOn("Logout");
        assertEquals("Monster Odyssey - Sign Up & In", stage.getTitle());
    }

    @Test
    void testIngameView() {
        signInToMainMenu();

        // Main Menu to Ingame
        final VBox regionRadioButtonList = lookup("#regionRadioButtonList").query();
        final RadioButton radioButton = regionRadioButtonList.getChildren().stream()
                .filter(node -> node instanceof RadioButton)
                .map(node -> (RadioButton) node)
                .findFirst()
                .orElse(null);
        assertNotNull(radioButton);
        clickOn(radioButton);
        clickOn("Start Game");
        assertEquals("Monster Odyssey - Ingame", stage.getTitle());
        final Button helpSymbol = lookup("#helpSymbol").query();
        assertNotNull(helpSymbol);

        // test Ingame Help Symbol
        clickOn(helpSymbol);
        final DialogPane dialogPane = lookup(".dialog-pane").query();
        assertNotNull(dialogPane);
        final Label helpLabel = dialogPane.getChildren().stream()
                .filter(node -> node instanceof Label)
                .map(node -> (Label) node)
                .findFirst()
                .orElse(null);
        assertNotNull(helpLabel);
        assertEquals("Click 'p' on your keyboard for pause menu.", helpLabel.getText());
        final ButtonType buttonType = dialogPane.getButtonTypes().stream()
                .filter(type -> type.getButtonData() == ButtonBar.ButtonData.OK_DONE)
                .findFirst()
                .orElse(null);
        assertNotNull(buttonType);
        final Button button = (Button) dialogPane.lookupButton(buttonType);
        assertNotNull(button);
        clickOn(button);
        final Label gameTitle = lookup("Monster Odyssey").query();
        assertNotNull(gameTitle);

        // test Ingame Pause
        type(KeyCode.P);
        final DialogPane dialogPanePause = lookup(".dialog-pane").query();
        assertNotNull(dialogPanePause);
        final Label pauseLabel = dialogPanePause.getChildren().stream()
                .filter(node -> node instanceof Label)
                .map(node -> (Label) node)
                .findFirst()
                .orElse(null);
        assertNotNull(pauseLabel);
        assertEquals("What do you want to do?", pauseLabel.getText());
        final ButtonType buttonTypePause = dialogPanePause.getButtonTypes().stream()
                .findFirst()
                .orElse(null);
        assertNotNull(buttonTypePause);
        final Button buttonPause = (Button) dialogPanePause.lookupButton(buttonTypePause);
        assertNotNull(buttonPause);

        // test Ingame Unpause With Key Code P
        type(KeyCode.P);
        final Label gameTitleUnpauseP = lookup("Monster Odyssey").query();
        assertNotNull(gameTitleUnpauseP);

        // test Ingame Unpause With Button
        type(KeyCode.P);
        clickOn("Resume Game");
        final Label gameTitleUnpauseButton = lookup("Monster Odyssey").query();
        assertNotNull(gameTitleUnpauseButton);
    }

    @Test
    void testSettingsView() {
        signInToMainMenu();

        // test Main Menu to Settings
        final Button settingButton = lookup("#settingsButton").query();
        assertNotNull(settingButton);
        clickOn(settingButton);
        assertEquals("Monster Odyssey - Account Setting", stage.getTitle());

        // test Show Password In Setting
        final Button passwordEditBtn = lookup("#passwordEditButton").query();
        clickOn(passwordEditBtn);
        final PasswordField passwordField = lookup("#passwordField").query();
        clickOn(passwordField);
        write("password");
        final Button showPasswordBtn = lookup("#showPasswordButton").query();
        clickOn(showPasswordBtn);
        assertEquals("class de.uniks.stpmon.team_m.utils.PasswordFieldSkin", passwordField.getSkin().getClass().toString());

        // test Setting To Main Menu
        final Button cancelButton = lookup("Cancel").query();
        assertNotNull(cancelButton);
        clickOn(cancelButton);
        assertEquals("Monster Odyssey - Main Menu", stage.getTitle());
    }

    @Test
    void testNewFriendView() {
        signInToMainMenu();
        clickOn("Find New Friends");
        assertEquals("Monster Odyssey - Add a new friend", stage.getTitle());
        final Button mainMenuButton = lookup("#mainMenuButton").query();
        assertNotNull(mainMenuButton);
        final Button addFriendButton = lookup("#addFriendButton").query();
        assertNotNull(addFriendButton);
        final Button messageButton = lookup("#messageButton").query();
        assertNotNull(messageButton);
        final TextField searchTextField = lookup("#searchTextField").query();
        assertNotNull(searchTextField);
        clickOn("Main Menu");
        assertEquals("Monster Odyssey - Main Menu", stage.getTitle());
    }

    @Test
    void testMessagesView() {
        signInToMainMenu();

        // test Main Menu to Messages
        final Button messagesButton = lookup("Messages").query();
        assertNotNull(messagesButton);
        clickOn(messagesButton);
        assertEquals("Monster Odyssey - Messages", stage.getTitle());
        final Button sendButton = lookup("#sendButton").query();
        assertNotNull(sendButton);

        // test Messages To Main Menu
        final Button mainMenuButton = lookup("#mainMenuButton").query();
        assertNotNull(mainMenuButton);
        clickOn(mainMenuButton);
        assertEquals("Monster Odyssey - Main Menu", stage.getTitle());
        clickOn("Messages");

        // test Messages To New Friends
        final Button findNewFriendsButton = lookup("#findNewFriendsButton").query();
        assertNotNull(findNewFriendsButton);
        clickOn(findNewFriendsButton);
        assertEquals("Monster Odyssey - Add a new friend", stage.getTitle());

    }

    @Test
    void testGroupView() {
        signInToMainMenu();
        clickOn("Messages");

        // test Messages to New Group
        final Button newGroupButton = lookup("#newGroupButton").query();
        assertNotNull(newGroupButton);
        clickOn(newGroupButton);

        // test New Group
        assertEquals("Monster Odyssey - New Group", stage.getTitle());
        final Button saveGroupButton = lookup("Save Group").query();
        assertNotNull(saveGroupButton);
        final Text selectGroupMembersText = lookup("Select Groupmembers").queryText();
        assertNotNull(selectGroupMembersText);
        clickOn("Go back");
        assertEquals("Monster Odyssey - Messages", stage.getTitle());

        // test Edit Group
        final Button settingsButton = lookup("#settingsButton").query();
        clickOn(settingsButton);
        assertEquals("Monster Odyssey - Edit Group", stage.getTitle());
        final Button deleteButton = lookup("#deleteGroupButton").query();
        assertNotNull(deleteButton);
    }

    private void signInToMainMenu() {
        final TextField usernameField = lookup("#usernameField").query();
        final TextField passwordField = lookup("#passwordField").query();
        final Button signInButton = lookup("Sign In").query();
        clickOn(usernameField);
        write("t");
        clickOn(passwordField);
        write("testtest");
        clickOn(signInButton);
    }
}