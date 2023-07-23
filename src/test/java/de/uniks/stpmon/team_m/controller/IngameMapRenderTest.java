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
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
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

import static io.reactivex.rxjava3.core.Observable.just;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IngameMapRenderTest extends ApplicationTest {
    @Spy
    final
    App app = new App(null);
    @Mock
    Provider<TrainerStorage> trainerStorageProvider;
    @Mock
    AreasService areasService;
    @Mock
    RegionsService regionsService;
    @Mock
    Provider<UDPEventListener> udpEventListenerProvider;
    @Mock
    Provider<MonstersListController> monstersListControllerProvider;
    @Mock
    Provider<NotificationListHandyController> notificationListHandyControllerProvider;
    @Mock
    Provider<IngameStarterMonsterController> ingameStarterMonsterControllerProvider;

    // Please also keep this mock, it is needed for the tests
    // -- which ones????
    @Spy
    EncounterOpponentStorage encounterOpponentStorage;
    @Mock
    TrainersService trainersService;
    @Mock
    EncounterOpponentsService encounterOpponentsService;
    @Mock
    PresetsService presetsService;
    @InjectMocks
    IngameController ingameController;
    @Mock
    Provider<EventListener> eventListener;
    @InjectMocks
    NotificationListHandyController notificationListHandyController;
    @InjectMocks
    IngameStarterMonsterController ingameStarterMonsterController;
    @Override
    public void start(Stage stage) {
        ResourceBundle bundle = ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
        Preferences preferences = Preferences.userNodeForPackage(IngameController.class);
        ingameController.setValues(bundle, preferences, null, ingameController, app);

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
                List.of(1, 2),
                List.of("646bacc568933551792bf3d5"),
                "646bacc568933551792bf3d5",
                33,
                19,
                0,
                new NPCInfo(false, false, false, false, null, null, null)
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
                                List.of("646bacc568933551792bf3d5"),
                                "6475e595ac3946b6a812d863",
                                33,
                                18,
                                0,
                                new NPCInfo(false, false, false, false, null, null, null)),
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
                                List.of("646bacc568933551792bf3d5"),
                                "6475e595ac3946b6a812d863",
                                20,
                                18,
                                2,
                                new NPCInfo(false, false, false, true, null, null, null)),
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
                                List.of("646bacc568933551792bf3d5"),
                                "6475e595ac3946b6a812d863",
                                69,
                                69,
                                2,
                                new NPCInfo(false, false, false, false, null,null, List.of("1", "3", "5"))),
                        new Trainer(
                                "2023-05-30T12:02:57.510Z",
                                "2023-05-30T12:01:57.510Z",
                                "6475e595ac3946b6a812d811",
                                "646bab5cecf584e1be02598a",
                                "6475e595ac3946b6a812d868",
                                "Test Clerk",
                                "Premade_Character_02.png",
                                0,
                                List.of(),
                                List.of(),
                                List.of("646bacc568933551792bf3d5"),
                                "6475e595ac3946b6a812d863",
                                100,
                                100,
                                2,
                                new NPCInfo(false, false, false, false, List.of(1, 2, 3),null, null))
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
                List.of("646bacc568933551792bf3d5"),
                "6475e595ac3946b6a812d863",
                33,
                18,
                1,
                new NPCInfo(false, false, false, false, null, null, null));


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
                    public BufferedSource source() {
                        return null;
                    }
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
        when(eventListener.get().listen("encounters.*.trainers." + trainerStorageProvider.get().getTrainer()._id() + ".opponents.*.*", Opponent.class)).thenReturn(just(
                        new Event<>("encounters.*.trainers.6475e595ac3946b6a812d865,opponents.*.nothappening", null)))
                .thenReturn(just(new Event<>("encounters.a98db973kwl8xp1lz94kjf0b.trainers.646bac223b4804b87c0b8054.opponents.rqtjej4dcoqsm4e9yln1loy5.created", opponent)));

        when(encounterOpponentsService.getTrainerOpponents(anyString(), anyString())).thenReturn(Observable.just(List.of()));

        // mocking the map
        Layer minimaplayer = new Layer(
                List.of(new Chunk(
                        List.of(1L,2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L,2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L,2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L),
                        1,
                        1,
                        1,
                        1
                )),
                List.of(1L,2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L,2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L,2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L),
                1,
                1,
                "Tile Layer 1",
                1,
                0,
                0,
                "tilelayer",
                true,
                1,
                1,
                1,
                "right-down",
                List.of());
        Map minimap = new Map(
                -1,
                true,
                1,
                1,
                "orthogonal",
                "right-down",
                "1.6.1",
                "minimap",
                "1.6",
                1,
                1,
                List.of(minimaplayer),
                1,
                1,
                List.of(new TileSet(
                        1,
                        "Flamander_1.png",
                        1,
                        1,
                        0,
                        "Flamander_1",
                        0,
                        1,
                        1,
                        1,
                        List.of(),
                        1,
                        "Flamander_1.tsx"
                )),
                List.of());
        Layer maplayer = new Layer(
                null,
                List.of(1L,2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L,2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L,2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L),
                1,
                1,
                "Tile Layer 1",
                1,
                0,
                0,
                "tilelayer",
                true,
                1,
                1,
                1,
                "right-down",
                List.of());
        Map map = new Map(
                -1,
                true,
                1,
                1,
                "orthogonal",
                "right-down",
                "1.6.1",
                "map",
                "1.6",
                1,
                1,
                List.of(maplayer),
                1,
                1,
                List.of(new TileSet(
                        1,
                        "Flamander_1.png",
                        1,
                        1,
                        0,
                        "Flamander_1",
                        0,
                        1,
                        1,
                        1,
                        List.of(),
                        1,
                        "Flamander_1.tsx"
                )),
                List.of());
        when(trainerStorageProvider.get().getRegion()).thenReturn(
                new Region(
                        "2023-05-22T17:51:46.772Z",
                        "2023-05-22T17:51:46.772Z",
                        "646bab5cecf584e1be02598a",
                        "Albertina",
                        new Spawn("646bc3c0a9ac1b375fb41d93", 1, 1),
                        minimap));
        when(areasService.getArea(any(), any())).thenReturn(Observable.just(
                new Area(
                        "2023-05-22T17:51:46.772Z",
                        "2023-05-22T17:51:46.772Z",
                        "646bc3c0a9ac1b375fb41d93",
                        "646bc436cfee07c0e408466f",
                        "Albertina",
                        map

                )));
        when(presetsService.getTileset(any())).thenReturn(Observable.just(new TileSet(
                1,
                "Flamander_1.png",
                16,
                16,
                0,
                "Flamander_1",
                0,
                1,
                16,
                16,
                List.of(),
                1,
                "Flamander_1.tsx"
        )));
        Image mockedImage = mock(Image.class);
        when(mockedImage.getHeight()).thenReturn(16.0);
        when(mockedImage.getWidth()).thenReturn(16.0);
        when(presetsService.getTilesetImage(any())).thenReturn(Observable.just(mockedImage));
        when(regionsService.getRegion(any())).thenReturn(Observable.just(new Region(
                "2023-05-22T17:51:46.772Z",
                "2023-05-22T17:51:46.772Z",
                "646bab5cecf584e1be02598a",
                "Albertina",
                new Spawn("646bc3c0a9ac1b375fb41d93", 1, 1),
                minimap
        )));
        when(eventListener.get().listen("regions." + trainerStorageProvider.get().getRegion()._id() + ".trainers.*.*", Trainer.class)).thenReturn(just(
                new Event<>("regions.646bab5cecf584e1be02598a.trainers.6475e595ac3946b6a812d865.created", trainer)));

        app.start(stage);
        app.show(ingameController);
        stage.requestFocus();
    }

    @Test
    void testRender() {
        clickOn("#sendMessageButton");
    }
}
