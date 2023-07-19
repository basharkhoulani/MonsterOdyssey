package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.controller.subController.*;
import de.uniks.stpmon.team_m.dto.*;
import de.uniks.stpmon.team_m.service.*;
import de.uniks.stpmon.team_m.udp.UDPEventListener;
import de.uniks.stpmon.team_m.utils.EncounterOpponentStorage;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import de.uniks.stpmon.team_m.ws.EventListener;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;
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
import java.util.prefs.Preferences;

import static io.reactivex.rxjava3.core.Observable.empty;
import static io.reactivex.rxjava3.core.Observable.just;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IngameControllerTest extends ApplicationTest {

    @Spy
    final
    App app = new App(null);
    @Mock
    Provider<MainMenuController> mainMenuControllerProvider;
    @Mock
    Provider<TrainerStorage> trainerStorageProvider;
    @Mock
    AreasService areasService;
    @Mock
    Provider<UDPEventListener> udpEventListenerProvider;
    @Mock
    Provider<MonstersListController> monstersListControllerProvider;
    @Mock
    Provider<NotificationListHandyController> notificationListHandyControllerProvider;
    @Mock
    Provider<IngameStarterMonsterController> ingameStarterMonsterControllerProvider;
    @Mock
    Provider<EncounterController> encounterControllerProvider;

    // Leave this mock!! it ensures that tests run fine
    // -- WHY?????? add explanation
    @Mock
    TrainerStorage trainerStorage;
    // Please also keep this mock, it is needed for the tests
    // -- which ones????
    @Spy
    EncounterOpponentStorage encounterOpponentStorage;
    @Mock
    Provider<IngamePauseMenuController> pauseMenuControllerProvider;
    @Mock
    TrainersService trainersService;
    @Mock
    MessageService messageService;
    @Mock
    EncounterOpponentsService encounterOpponentsService;
    @Mock
    PresetsService presetsService;
    @InjectMocks
    IngameController ingameController;
    @Mock
    Provider<EventListener> eventListener;
    @Mock
    Parent parent;
    @InjectMocks
    NotificationListHandyController notificationListHandyController;
    @InjectMocks
    IngameStarterMonsterController ingameStarterMonsterController;
    @InjectMocks
    IngamePauseMenuController pauseMenuController;
    @InjectMocks
    MainMenuController mainMenuController;
    @Mock
    Provider<IngameSettingsController> ingameSettingsControllerProvider;
    @InjectMocks
    IngameSettingsController ingameSettingsController;
    @Mock
    Provider<IngameKeybindingsController> ingameKeybindingsControllerProvider;
    @InjectMocks
    IngameKeybindingsController ingameKeybindingsController;
    @Mock
    Provider<ChangeAudioController> changeAudioControllerProvider;
    @InjectMocks
    ChangeAudioController changeAudioController;

    @Override
    public void start(Stage stage) {
        ResourceBundle bundle = ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
        Preferences preferences = Preferences.userNodeForPackage(IngameController.class);
        ingameController. setValues (bundle, preferences, null, ingameController, app);

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
                List.of("63va3w6d11sj2hq0nzpsa20w", "86m1imksu4jkrxuep2gtpi4a"),
                List.of(1,2),
                "646bacc568933551792bf3d5",
                33,
                19,
                0,
                new NPCInfo(false, false, false, false, null, null)
        ));
        when(trainerStorageProvider.get().getRegion()).thenReturn(
                new Region(
                        "2023-05-22T17:51:46.772Z",
                        "2023-05-22T17:51:46.772Z",
                        "646bab5cecf584e1be02598a",
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
        doNothing().when(trainerStorage).setMonsters(any());
        lenient().when(presetsService.getCharacter(any())).thenReturn(Observable.empty());
        when(trainersService.getTrainers(any(), any(), any())).thenReturn(Observable.just(List.of(
                new Trainer(
                    "2023-05-30T12:02:57.510Z",
                    "2023-05-30T12:01:57.510Z",
                    "6475e595ac3946b6a812d863",
                    "646bab5cecf584e1be02598a",
                    "6475e595ac3946b6a812d868",
                    "Hans",
                    "Premade_Character_01.png",
                    0,
                    List.of("63va3w6d11sj2hq0nzpsa20w", "86m1imksu4jkrxuep2gtpi4a"),
                    List.of(1,2),
                    "6475e595ac3946b6a812d863",
                    33,
                    18,
                    0,
                    new NPCInfo(false, false, false, false, null, null)),
                new Trainer(
                        "2023-05-30T12:02:57.510Z",
                        "2023-05-30T12:01:57.510Z",
                        "6475e595ac3946b6a812d863",
                        "646bab5cecf584e1be02598a",
                        "6475e595ac3946b6a812d868",
                        "Krankenschwester Erna",
                        "Nurse_2_16x16.png",
                        0,
                        List.of(),
                        List.of(),
                        "6475e595ac3946b6a812d863",
                        20,
                        18,
                        2,
                        new NPCInfo(false, false, false, true, null, null)),
                new Trainer(
                        "2023-05-30T12:02:57.510Z",
                        "2023-05-30T12:01:57.510Z",
                        "6475e595ac3946b6a812d869",
                        "646bab5cecf584e1be02598a",
                        "6475e595ac3946b6a812d868",
                        "Prof. Testikus Maximus",
                        "Premade_Character_02.png",
                        0,
                        List.of(),
                        List.of(),
                        "6475e595ac3946b6a812d863",
                        69,
                        69,
                        2,
                        new NPCInfo(false, false, false, false, null, List.of("1", "3", "5")))
                )
        ));
        EventListener eventListenerMock = mock(EventListener.class);
        when(eventListener.get()).thenReturn(eventListenerMock);
        Message message = new Message("2023-05-30T12:01:57.510Z", "2023-05-30T12:01:57.510Z", "6475e595ac3946b6a812d863",
                "6475e595ac3946b6a812d868", "Test1");
        when(eventListener.get().listen("regions.646bab5cecf584e1be02598a.messages.*.*", Message.class)).thenReturn(just(
                new Event<>("regions.646bab5cecf584e1be02598a.messages.6475e595ac3946b6a812d863.created", message)
        ));
        Trainer trainer = new Trainer(
                "2023-05-30T12:02:57.510Z",
                "2023-05-30T12:01:57.510Z",
                "6475e595ac3946b6a812d865",
                "646bab5cecf584e1be02598a",
                "6475e595ac3946b6a812d868",
                "Peter",
                "Premade_Character_02.png",
                0,
                List.of("1", "2"),
                List.of(),
                "6475e595ac3946b6a812d863",
                33,
                18,
                1,
                new NPCInfo(false, false,false, false, null,null));

        when(eventListener.get().listen("regions." + trainerStorageProvider.get().getRegion()._id() + ".trainers.*.*", Trainer.class)).thenReturn(just(
                new Event<>("regions.646bab5cecf584e1be02598a.trainers.6475e595ac3946b6a812d865.created", trainer)));

        MonstersListController monstersListController = mock(MonstersListController.class);
        when(trainerStorageProvider.get().getTrainer()).thenReturn(trainer);
        when(monstersListControllerProvider.get()).thenReturn(monstersListController);

        notificationListHandyController.setValues(bundle, null, null, notificationListHandyController, app);
        when(notificationListHandyControllerProvider.get()).thenReturn(notificationListHandyController);
        ingameStarterMonsterController.setValues(bundle, null, null, ingameStarterMonsterController, app);
        lenient().when(ingameStarterMonsterControllerProvider.get()).thenReturn(ingameStarterMonsterController);

        lenient().when(presetsService.getMonsters()).thenReturn(Observable.just(List.of(
                new MonsterTypeDto(
                        1,
                        "Monster1",
                        "Flamander_1.png",
                        List.of("fire"),
                        "Monster1lololol"
                ),
                new MonsterTypeDto(
                        2,
                        "Monster2",
                        "Flamander_1.png",
                        List.of("fire"),
                        "ich bin ein echter gangster"
                ),
                new MonsterTypeDto(
                        3,
                        "Monster3",
                        "Flamander_1.png",
                        List.of("fire"),
                        "waaas willst du tun"
                ),
                new MonsterTypeDto(
                        4,
                        "Monster4",
                        "Flamander_1.png",
                        List.of("fire"),
                        "dieser big tasty hat nichts mehr mit einem big tasty zu tun"
                ),
                new MonsterTypeDto(
                        5,
                        "Monster5",
                        "Flamander_1.png",
                        List.of("fire"),
                        "auch ein blindes schaf findet manchmal ein huhn"
                )
        )));

        lenient().when(presetsService.getMonsterImage(anyInt())).thenReturn(Observable.just(
                new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() { return null; }
                }
        ));

        Opponent opponent = new Opponent(
                "2023-05-30T12:02:57.510Z",
                "2023-05-30T12:01:57.510Z",
                "rqtjej4dcoqsm4e9yln1loy5",
                "a98db973kwl8xp1lz94kjf0b",
                "646bac223b4804b87c0b8054",
                false,
                false,
                "pn2iz308akz07eau5iwa6ykq",
                null,
                List.of(),
                0);

        //Mocking the opponent (Situation)
        when(eventListener.get().listen("encounters.*.trainers." + trainerStorageProvider.get().getTrainer()._id() +".opponents.*.*", Opponent.class)).thenReturn(just(
                new Event<>("encounters.*.trainers.6475e595ac3946b6a812d865,opponents.*.nothappening", null)))
                .thenReturn(just(new Event<>("encounters.a98db973kwl8xp1lz94kjf0b.trainers.646bac223b4804b87c0b8054.opponents.rqtjej4dcoqsm4e9yln1loy5.created", opponent)));

        when(encounterOpponentsService.getTrainerOpponents(anyString(), anyString())).thenReturn(Observable.just(List.of()));

        app.start(stage);
        app.show(ingameController);
        stage.requestFocus();
    }

    @Test
    void showHelp() throws InterruptedException {
        // TODO: apply asserts once we have the time
        clickOn("#smallHandyButton");

        Thread.sleep(1000);
        clickOn("close");
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
        //verify(messageService).newMessage(any(), any(), any());
        assertFalse(messageField.isFocused());

        // Send with button
        clickOn("#messageField");
        write("Hello World");
        clickOn("#sendMessageButton");
        assertEquals("", messageField.getText());
        //verify(messageService, times(2)).newMessage(any(), any(), any());
    }

    @Test
    void getMessages() {
        ListView<Message> chat = lookup("#chatListView").query();
        assertEquals(chat.getOpacity(), 0);
        clickOn("#showChatButton");
        assertEquals(chat.getOpacity(), 1);
        moveTo("Test1");
        clickOn("#showChatButton");
        assertEquals(chat.getOpacity(),  0);
    }

    @Test
    void testDialog() throws InterruptedException {
        Mockito.when(trainerStorageProvider.get().getX()).thenReturn(33);
        Mockito.when(trainerStorageProvider.get().getY()).thenReturn(19);
        Mockito.when(trainerStorageProvider.get().getDirection()).thenReturn(1);
        when(udpEventListenerProvider.get().talk(any(), any())).thenReturn(empty());

        press(KeyCode.E);
        release(KeyCode.E);

        final TextFlow dialogTextFlow = lookup("#dialogTextFlow").query();
        final Text dialogText = (Text) dialogTextFlow.getChildren().get(0);
        final String firstDefaultText = dialogText.getText();
        assertNotEquals("", firstDefaultText);

        Thread.sleep(30);

        press(KeyCode.E);
        release(KeyCode.E);

        assertNotEquals(firstDefaultText, dialogText.getText());

        Thread.sleep(30);

        press(KeyCode.E);
        release(KeyCode.E);

        Thread.sleep(30);

        press(KeyCode.E);
        release(KeyCode.E);

        Thread.sleep(30);
    }

    @Test
    void testNurseDialog() throws InterruptedException {
        Mockito.when(trainerStorageProvider.get().getX()).thenReturn(20);
        Mockito.when(trainerStorageProvider.get().getY()).thenReturn(20);    // two tiles apart from Nurse
        Mockito.when(trainerStorageProvider.get().getDirection()).thenReturn(1);
        when(udpEventListenerProvider.get().talk(any(), any())).thenReturn(empty());

        press(KeyCode.E);
        release(KeyCode.E);

        final TextFlow dialogTextFlow = lookup("#dialogTextFlow").query();

        final Text dialogText = (Text) dialogTextFlow.getChildren().get(0);
        final String firstNurseText = dialogText.getText();

        assertNotEquals("", firstNurseText);

        Thread.sleep(30);

        press(KeyCode.E);
        release(KeyCode.E);

        Thread.sleep(30);

        assertNotEquals(firstNurseText, dialogText.getText());

        press(KeyCode.E);
        release(KeyCode.E);

        Thread.sleep(30);

        press(KeyCode.E);
        release(KeyCode.E);

        Thread.sleep(30);

        clickOn("No");

        final StackPane rootStackPane = lookup("#root").query();
        final Node node = rootStackPane.getChildren().get(rootStackPane.getChildren().size() - 1);

        assertNotEquals("nurseVBox", node.getId());

        press(KeyCode.E);
        release(KeyCode.E);

        Thread.sleep(30);


        final StackPane stackPane = lookup("#stackPane").query();
        final Node node2 = stackPane.getChildren().get(stackPane.getChildren().size() - 1);

        assertNotEquals("dialogStackPane", node2.getId());

        for (int i = 0; i < 4; i++) {
            press(KeyCode.E);
            release(KeyCode.E);

            Thread.sleep(30);
        }

        clickOn("Yes");
        // healing of monsters cannot be tested, since this should happen on the server, when you encounter the nurse

        press(KeyCode.E);
        release(KeyCode.E);

        Thread.sleep(30);
    }


    @Test
    void testTalkToNPC2TilesAway() throws InterruptedException {
        Mockito.when(trainerStorageProvider.get().getX()).thenReturn(33);
        Mockito.when(trainerStorageProvider.get().getY()).thenReturn(20);
        when(udpEventListenerProvider.get().talk(any(), any())).thenReturn(empty());

        press(KeyCode.E);
        release(KeyCode.E);

        final StackPane stackPane = lookup("#stackPane").query();
        final Node node = stackPane.getChildren().get(stackPane.getChildren().size() - 1);

        assertNotEquals("dialogStackPane", node.getId());
    }

    @Test
    void testNurseDialogWithNoMons() throws InterruptedException {
        Mockito.when(trainerStorageProvider.get().getX()).thenReturn(20);
        Mockito.when(trainerStorageProvider.get().getY()).thenReturn(20);    // two tiles apart from Nurse
        when(udpEventListenerProvider.get().talk(any(), any())).thenReturn(empty());
        when(trainerStorageProvider.get().getTrainer()).thenReturn(new Trainer(
                "2023-05-30T12:02:57.510Z",
                "2023-05-30T12:01:57.510Z",
                "6475e595ac3946b6a812d865",
                "646bab5cecf584e1be02598a",
                "6475e595ac3946b6a812d868",
                "Peter",
                "Premade_Character_02.png",
                0,
                List.of(),
                null,
                "6475e595ac3946b6a812d863",
                33,
                18,
                0,
                new NPCInfo(false, false,false, false, null,null)));

        press(KeyCode.E);
        release(KeyCode.E);

        Thread.sleep(30);

        press(KeyCode.E);
        release(KeyCode.E);

        Thread.sleep(30);

        press(KeyCode.E);
        release(KeyCode.E);

        Thread.sleep(30);

        final StackPane stackPane = lookup("#stackPane").query();
        final Node node = stackPane.getChildren().get(stackPane.getChildren().size() - 1);

        assertNotEquals("dialogStackPane", node.getId());
    }

    @Test
    void testPauseMenuAndSettings(){
        ResourceBundle bundle = ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
        Preferences preferences = Preferences.userNodeForPackage(IngameController.class);
        pauseMenuController.setValues(bundle, null, null, pauseMenuController, app);
        when(pauseMenuControllerProvider.get()).thenReturn(pauseMenuController);

        ingameSettingsController.setValues(bundle, null, null, ingameSettingsController, app);
        when(ingameSettingsControllerProvider.get()).thenReturn(ingameSettingsController);

        ingameKeybindingsController.setValues(bundle, preferences, null, ingameKeybindingsController, app);
        when(ingameKeybindingsControllerProvider.get()).thenReturn(ingameKeybindingsController);

        changeAudioController.setValues(bundle, preferences, null, changeAudioController, app);
        when(changeAudioControllerProvider.get()).thenReturn(changeAudioController);

        mainMenuController.setValues(bundle, null, null, mainMenuController, app);
        when(mainMenuControllerProvider.get()).thenReturn(mainMenuController);
        doNothing().when(app).show(mainMenuController);

        clickOn("#pauseButton");
        clickOn("#resumeGameButton");
        clickOn("#pauseButton");

        clickOn("#settingsButton");
        clickOn("#keybindingsButton");
        clickOn(ingameKeybindingsController.goBackButton);

        clickOn("#audioSettingsButton");
        clickOn("#closeButton");

        clickOn("#goBackButton");
        clickOn("#leaveGameButton");
        verify(app).show(mainMenuController);
    }

    @Test
    void testStarterMonsterNpcDialog() throws InterruptedException {
        Mockito.when(trainerStorageProvider.get().getX()).thenReturn(69);
        Mockito.when(trainerStorageProvider.get().getY()).thenReturn(70);
        Mockito.when(trainerStorageProvider.get().getDirection()).thenReturn(1);

        when(udpEventListenerProvider.get().talk(any(), any())).thenReturn(empty());
        when(trainerStorageProvider.get().getTrainer()).thenReturn(new Trainer(
                "2023-05-30T12:02:57.510Z",
                "2023-05-30T12:01:57.510Z",
                "6475e595ac3946b6a812d865",
                "646bab5cecf584e1be02598a",
                "6475e595ac3946b6a812d868",
                "Peter",
                "Premade_Character_02.png",
                0,
                List.of(),
                null,
                "6475e595ac3946b6a812d863",
                33,
                18,
                0,
                new NPCInfo(false, false,false, false, null,null)));

        press(KeyCode.E);
        release(KeyCode.E);
        Thread.sleep(30);

        press(KeyCode.E);
        release(KeyCode.E);
        Thread.sleep(30);

        press(KeyCode.E);
        release(KeyCode.E);
        Thread.sleep(30);

        press(KeyCode.E);
        release(KeyCode.E);
        Thread.sleep(30);

        final VBox starterSelectionVBox = lookup("#starterSelectionVBox").query();
        assertNotNull(starterSelectionVBox);

        final Label starterSelectionLabel0 = lookup("#starterSelectionLabel").query();
        final String starterSelectionLabelText0 = starterSelectionLabel0.getText();

        clickOn("#arrowRight");

        final Label starterSelectionLabel1 = lookup("#starterSelectionLabel").query();
        final String starterSelectionLabelText1 = starterSelectionLabel1.getText();

        assertNotEquals(starterSelectionLabelText0, starterSelectionLabelText1);

        clickOn("#arrowLeft");

        final Label starterSelectionLabel2 = lookup("#starterSelectionLabel").query();
        final String starterSelectionLabelText2 = starterSelectionLabel2.getText();

        assertEquals(starterSelectionLabelText0, starterSelectionLabelText2);

        clickOn("#starterSelectionOkButton");
        clickOn("#starterSelectionOkButton");

        press(KeyCode.E);
        release(KeyCode.E);
        Thread.sleep(30);
    }
}
