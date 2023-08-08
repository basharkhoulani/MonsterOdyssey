package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.controller.subController.CharacterSelectionController;
import de.uniks.stpmon.team_m.dto.*;
import de.uniks.stpmon.team_m.service.TrainersService;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Provider;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WelcomeSceneControllerTest extends ApplicationTest {


    @Spy
    final
    App app = new App(null);
    @InjectMocks
    WelcomeSceneController welcomeSceneController;
    @Mock
    Provider<CharacterSelectionController> characterSelectionControllerProvider;
    @Spy
    TrainerStorage trainerStorage;
    @Mock
    Provider<TrainersService> trainersServiceProvider;


    @Override
    public void start(Stage stage) {
        ResourceBundle bundle = ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
        Preferences preferences = mock(Preferences.class);
        welcomeSceneController.setValues(bundle, preferences, null, welcomeSceneController, app);
        final CharacterSelectionController characterSelectionController = mock(CharacterSelectionController.class);
        when(characterSelectionControllerProvider.get()).thenReturn(characterSelectionController);
        doNothing().when(app).show(characterSelectionController);
        when(trainerStorage.getRegion()).thenReturn(new Region(
                "2023-05-22T17:51:46.772Z",
                "2023-05-22T17:51:46.772Z",
                "646bc436cfee07c0e408466f",
                "Albertina",
                new Spawn("646bc3c0a9ac1b375fb41d93", 1, 1),
                new Map(-1,
                        true,
                        1,
                        1,
                        "orthogonal",
                        "right-down",
                        "1.6.1",
                        "map",
                        "1.6",
                        32,
                        32,
                        List.of(),
                        16,
                        16,
                        List.of(),
                        List.of())));
        final TrainersService trainersService = mock(TrainersService.class);
        when(trainersServiceProvider.get()).thenReturn(trainersService);
        when(trainersService.createTrainer(any(), any(), any())).thenReturn(Observable.just(new Trainer(
                "2023-05-22T17:51:46.772Z",
                "2023-05-22T17:51:46.772Z",
                "646bac223b4804b87c0b8054",
                "646bab5cecf584e1be02598a",
                "646bac8c1a74032c70fffe24",
                "Hans",
                "Premade_Character_01.png",
                0,
                null,
                null,
                List.of(),
                "646bacc568933551792bf3d5",
                0,
                0,
                0,
                new NPCInfo(false, false, false, false, null, null, null))));


        app.start(stage);
        app.show(welcomeSceneController);
        stage.requestFocus();
    }

    @Test
    void welcomeSceneTest() {
        // Scene 1
        Label firstMessage = lookup("#firstMessage").query();
        assertEquals("Welcome to Monster Odyssey!", firstMessage.getText());

        Label secondMessage = lookup("#secondMessage").query();
        assertEquals("Welcome Aboard!", secondMessage.getText());

        clickOn("Next");
        clickOn("Previous");
        clickOn("Next");

        // Scene 2
        Label thirdMessage = lookup("#firstMessage").query();
        assertEquals("We are the crew of this ship.", thirdMessage.getText());

        Label fourthMessage = lookup("#secondMessage").query();
        assertEquals("My name is James.", fourthMessage.getText());

        Label fifthMessage = lookup("#thirdMessage").query();
        assertEquals("And my name is Henry!", fifthMessage.getText());

        clickOn("Next");

        // Scene 3
        Label sixthMessage = lookup("#firstMessage").query();
        assertEquals("Now then, we'll need to look up your application.", sixthMessage.getText());

        Label seventhMessage = lookup("#secondMessage").query();
        assertEquals("Can we have your name?", seventhMessage.getText());

        clickOn("Next");

        // Scene 4
        final DialogPane dialogPane = lookup(".dialog-pane").query();
        assertNotNull(dialogPane);
        clickOn("#nameField");
        write("Ash");
        clickOn("OK");

        // Scene 5
        Label eightMessage = lookup("#firstMessage").query();
        assertEquals("Hello. Next, we 'd like to take a picture of you.", eightMessage.getText());

        Label ninthMessage = lookup("#secondMessage").query();
        assertEquals("Take as much time as you want to get dressed up for it.", ninthMessage.getText());

        clickOn("Next");

        // Scene 6 CharacterSelection
        verify(app).show(characterSelectionControllerProvider.get());

        when(trainerStorage.getRegion()).thenReturn(new Region("123", "456", "789", "test", new Spawn("adsad", 0, 0), null));

        clickOn("Next");

        // Scene 7
        verify(app).show(welcomeSceneController);
        Label tenthMessage = lookup("#firstMessage").query();
        assertEquals("If you are one of the tough ones, try the Hardcore mode.", tenthMessage.getText());

        Label eleventhMessage = lookup("#secondMessage").query();
        assertEquals("Or you can try it in normal mode for now, as you like!", eleventhMessage.getText());

        clickOn("Next");
    }
}
