package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.dto.*;
import de.uniks.stpmon.team_m.service.AreasService;
import de.uniks.stpmon.team_m.service.MessageService;
import de.uniks.stpmon.team_m.udp.UDPEventListener;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Provider;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IngameControllerTest extends ApplicationTest {

    @Spy
    App app = new App(null);
    @Mock
    Provider<MainMenuController> mainMenuControllerProvider;
    @Mock
    Provider<TrainerStorage> trainerStorageProvider;
    @Mock
    AreasService areasService;
    @Mock
    Provider<UDPEventListener> udpEventListenerProvider;

    // Leave this mock!! it ensures that tests run fine
    @Mock
    TrainerStorage trainerStorage;

    @Mock
    MessageService messageService;
    @InjectMocks
    IngameController ingameController;

    @Override
    public void start(Stage stage) {
        ResourceBundle bundle = ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
        ingameController.setValues(bundle, null, null, ingameController, app);

        UDPEventListener udpEventListener = mock(UDPEventListener.class);
        Mockito.when(udpEventListenerProvider.get()).thenReturn(udpEventListener);
        when(udpEventListener.listen(any(), any())).thenReturn(Observable.just(new Event<>("areas.*.trainers.*.moved", new MoveTrainerDto("646bac223b4804b87c0b8054", "64610ec8420b3d786212aea3", 0, 0, 2))));
        final TrainerStorage trainerStorage = mock(TrainerStorage.class);
        Mockito.when(trainerStorageProvider.get()).thenReturn(trainerStorage);
        Mockito.when(trainerStorage.getTrainer()).thenReturn(new Trainer(
                "2023-05-22T17:51:46.772Z",
                "2023-05-22T17:51:46.772Z",
                "646bac223b4804b87c0b8054",
                "646bab5cecf584e1be02598a",
                "646bac8c1a74032c70fffe24",
                "Hans",
                "Premade_Character_01.png",
                0,
                "646bacc568933551792bf3d5",
                0,
                0,
                0,
                new NPCInfo(false)
        ));
        when(trainerStorageProvider.get().getRegion()).thenReturn(
                new Region(
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
        when(areasService.getArea(any(), any())).thenReturn(Observable.just(
                new Area(
                        "2023-05-22T17:51:46.772Z",
                        "2023-05-22T17:51:46.772Z",
                        "646bc3c0a9ac1b375fb41d93",
                        "646bc436cfee07c0e408466f",
                        "Albertina",
                        new Map(
                                -1,
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
                                List.of()))

        ));
        ingameController.setValues(bundle, null, null, ingameController, app);
        app.start(stage);
        app.show(ingameController);
        stage.requestFocus();
    }

    @Test
    void showHelp() {
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
    }

    @Test
    void pauseGame() {
        MainMenuController mainMenuController = mock(MainMenuController.class);
        when(mainMenuControllerProvider.get()).thenReturn(mainMenuController);
        doNothing().when(app).show(any());
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
        type(KeyCode.E);
        type(KeyCode.P);
        // test Ingame Unpause With Button
        type(KeyCode.P);
        clickOn("Resume Game");

        // test Ingame Back To Main Menu
        type(KeyCode.P);
        clickOn("Save Game & Leave");
        verify(app).show(mainMenuController);
    }

    @Test
    void sendMessageTest() {
        final TextField messageField = lookup("#messageField").query();
        when(messageService.newMessage(any(), any(), any()))
                .thenReturn(Observable.just(new Message(
                        "2023-05-30T12:03:57.510Z",
                        "2023-05-30T12:01:57.510Z",
                        "6475e595ac3946b6a812d868",
                        "6477bc8f27adf9b5b978401f",
                        "Hello World")));
        // Send with enter key
        clickOn("#messageField");
        write("Hello World");
        type(KeyCode.ENTER);
        assertEquals("", messageField.getText());
        verify(messageService).newMessage(any(), any(), any());
        assertFalse(messageField.isFocused());

        // Send with button
        clickOn("#messageField");
        write("Hello World");
        clickOn("#sendMessageButton");
        assertEquals("", messageField.getText());
        verify(messageService, times(2)).newMessage(any(), any(), any());
    }
}
