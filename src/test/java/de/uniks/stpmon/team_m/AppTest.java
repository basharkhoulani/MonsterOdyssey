package de.uniks.stpmon.team_m;

import javafx.scene.control.*;
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
        stage.requestFocus();
    }

    void writeUsernameAndPassword(){
        write("Username\t");
        write("Username123");
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
    void testSignInToMainMenu() {
        final TextField usernameField = lookup("#usernameField").query();
        assertNotNull(usernameField);
        final TextField passwordField = lookup("#passwordField").query();
        assertNotNull(passwordField);
        final Button signInButton = lookup("Sign In").query();
        assertNotNull(signInButton);
        writeUsernameAndPassword();
        clickOn(signInButton);
        assertEquals("Monster Odyssey - Main Menu", stage.getTitle());
    }

    @Test
    void testMainMenuStartGameButton() {
        testSignInToMainMenu();
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

    @Test
    void testMainMenuToLoginScreen() {
        assertEquals("Monster Odyssey - Sign Up & In", stage.getTitle());
        testSignInToMainMenu();
        assertEquals("Monster Odyssey - Main Menu", stage.getTitle());
        final Button logoutButton = lookup("#logoutButton").query();
        assertNotNull(logoutButton);
        clickOn(logoutButton);
        assertEquals("Monster Odyssey - Sign Up & In", stage.getTitle());
    }

    @Test
    void testMainMenuToSetting(){
        assertEquals("Monster Odyssey - Sign Up & In", stage.getTitle());
        testSignInToMainMenu();
        assertEquals("Monster Odyssey - Main Menu", stage.getTitle());
        final Button settingButton = lookup("#settingsButton").query();
        assertNotNull(settingButton);
        clickOn(settingButton);
        assertEquals("Monster Odyssey - Account Setting", stage.getTitle());
    }

    @Test
    void testSettingToMainMenu(){
        assertEquals("Monster Odyssey - Sign Up & In", stage.getTitle());
        testSignInToMainMenu();
        assertEquals("Monster Odyssey - Main Menu", stage.getTitle());
        final Button settingButton = lookup("#settingsButton").query();
        assertNotNull(settingButton);
        clickOn(settingButton);
        assertEquals("Monster Odyssey - Account Setting", stage.getTitle());
        assertEquals("Monster Odyssey - Account Setting", stage.getTitle());
        final Button cancelButton = lookup("Cancel").query();
        assertNotNull(cancelButton);
        clickOn(cancelButton);
        assertEquals("Monster Odyssey - Main Menu", stage.getTitle());
    }

    @Test
    void testMainMenuToIngame() {
        testMainMenuStartGameButton();
        final Button startGameButton = lookup("Start Game").query();
        assertNotNull(startGameButton);
        clickOn(startGameButton);
        assertEquals("Monster Odyssey - Ingame", stage.getTitle());
        final Button helpSymbol = lookup("#helpSymbol").query();
        assertNotNull(helpSymbol);
    }
    
    @Test
    void testMainMenuToNewFriend() {
        testSignInToMainMenu();
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
    }
    
    @Test
    void testIngameHelpSymbol() {
        testMainMenuToIngame();
        final Button helpSymbol = lookup("#helpSymbol").query();
        assertNotNull(helpSymbol);
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
     }

}