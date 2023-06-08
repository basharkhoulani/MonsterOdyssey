package de.uniks.stpmon.team_m;

import de.uniks.stpmon.team_m.dto.Region;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class AppTest extends ApplicationTest {

    private Stage stage;
    private final App app = new App(null);
    private final TestComponent component = (TestComponent) DaggerTestComponent.builder().mainApp(app).build();

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        app.start(this.stage);
        app.show(component.loginController());
        stage.requestFocus();
    }

    @Test
    void criticalPathV1() {
        // test Login Screen
        assertEquals("Monster Odyssey - Sign Up & In", stage.getTitle());

        // test Sign In To MainMenu
        clickOn("#usernameField");
        write("t\t");
        write("testtest");
        clickOn("#signInButton");
        assertEquals("Monster Odyssey - Main Menu", stage.getTitle());

        // test Main Menu to Settings
        clickOn("settings");
        assertEquals("Monster Odyssey - Account Setting", stage.getTitle());

        // test delete Account and the cancel Button
        clickOn("#deleteAccountButton");
        final DialogPane dialogPaneDelete = lookup(".dialog-pane").query();
        assertNotNull(dialogPaneDelete);
        final Label deleteLabel = dialogPaneDelete.getChildren().stream()
                .filter(node -> node instanceof Label)
                .map(node -> (Label) node)
                .findFirst()
                .orElse(null);
        assertNotNull(deleteLabel);
        assertEquals("Are you sure?", deleteLabel.getText());

        final Button cancelBtn = (Button) dialogPaneDelete.lookupButton(ButtonType.CANCEL);
        assertNotNull(cancelBtn);
        clickOn(cancelBtn);
        assertEquals("Monster Odyssey - Account Setting", stage.getTitle());

        // test Setting To Main Menu
        clickOn("Cancel");
        assertEquals("Monster Odyssey - Main Menu", stage.getTitle());

        //test Delete Account Popup to Login screen
        clickOn("#settingsButton");
        assertEquals("Monster Odyssey - Account Setting", stage.getTitle());
        clickOn("#deleteAccountButton");

        final DialogPane dlgPaneDelete = lookup(".dialog-pane").query();
        assertNotNull(dlgPaneDelete);
        final Button okButton = (Button) dlgPaneDelete.lookupButton(ButtonType.OK);
        clickOn(okButton);
        assertEquals("Monster Odyssey - Sign Up & In", stage.getTitle());
        final Label infoLabel = lookup("#informationLabel").query();
        assertEquals("Account successfully deleted", infoLabel.getText());

        clickOn("#usernameField");
        write("t\t");
        write("testtest");
        clickOn("#signUpButton");

        // test Main Menu to New Friends
        clickOn("#findNewFriendsButton");
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

        // test Main Menu to Messages
        clickOn("Messages");
        assertEquals("Monster Odyssey - Messages", stage.getTitle());
        final Button sendButton = lookup("#sendButton").query();
        assertNotNull(sendButton);

        // test New Group
        clickOn("New Group");
        final Label selectGroupMembersLabel = lookup("Select Groupmembers").query();
        assertNotNull(selectGroupMembersLabel);
        assertEquals("Monster Odyssey - New Group", stage.getTitle());
        final Button saveGroupButton = lookup("Save Group").query();
        assertNotNull(saveGroupButton);

        // test Edit Group
        clickOn("Go back");
        clickOn("Main Menu");
    }

    @Test
    void criticalPathV2() {
        // test Login Screen
        assertEquals("Monster Odyssey - Sign Up & In", stage.getTitle());

        // test Sign In To MainMenu
        clickOn("#usernameField");
        write("t\t");
        write("testtest");
        clickOn("#signInButton");

        // Main Menu to Ingame
        final Button startGameButton = lookup("Start Game").query();
        assertNotNull(startGameButton);
        final ListView<Region> regionListView = lookup("#regionListView").query();
        regionListView.getSelectionModel().selectFirst();
        assertNotNull(regionListView);
        waitForFxEvents();
        assertFalse(startGameButton.isDisabled());
        clickOn(startGameButton);

        // Intro
        assertEquals("Monster Odyssey - Ingame", stage.getTitle());

        clickOn("Next");
        clickOn("Next");
        clickOn("Next");
        clickOn("#nameField");
        write("Ash");
        clickOn("OK");
        clickOn("Next");
        clickOn("Next");
        clickOn("Next");

        // test Ingame Help Symbol
        clickOn("#helpSymbol");
        final DialogPane dialogPane = lookup(".dialog-pane").query();
        assertNotNull(dialogPane);
        final Label helpLabel = dialogPane.getChildren().stream()
                .filter(node -> node instanceof Label)
                .map(node -> (Label) node)
                .findFirst()
                .orElse(null);
        assertNotNull(helpLabel);
        clickOn("OK");


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

        // test Ingame Unpause With Key Code P
        type(KeyCode.P);
        // test Ingame Unpause With Button
        type(KeyCode.P);
        clickOn("Resume Game");


        // test Ingame Back To Main Menu
        type(KeyCode.P);
        clickOn("Save Game & Leave");
        assertEquals("Monster Odyssey - Main Menu", stage.getTitle());
    }
}