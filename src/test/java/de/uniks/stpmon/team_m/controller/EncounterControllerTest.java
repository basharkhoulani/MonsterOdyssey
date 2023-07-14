package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.controller.subController.AbilitiesMenuController;
import de.uniks.stpmon.team_m.controller.subController.BattleMenuController;
import de.uniks.stpmon.team_m.controller.subController.EncounterOpponentController;
import de.uniks.stpmon.team_m.dto.*;
import de.uniks.stpmon.team_m.service.*;
import de.uniks.stpmon.team_m.utils.EncounterOpponentStorage;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import de.uniks.stpmon.team_m.ws.EventListener;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.Parent;
import javafx.stage.Stage;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import static io.reactivex.rxjava3.core.Observable.just;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EncounterControllerTest extends ApplicationTest {

    @InjectMocks
    EncounterController encounterController;
    @Spy
    final
    App app = new App(null);
    @Mock
    Preferences preferences;
    //Service
    @Mock
    RegionEncountersService regionEncountersService;
    @Mock
    Provider<EventListener> eventListener;
    @Mock
    PresetsService presetsService;
    @Mock
    TrainersService trainersService;
    @Mock
    MonstersService monstersService;
    // Storage
    @Spy
    EncounterOpponentStorage encounterOpponentStorage;
    @Mock
    Provider<TrainerStorage> trainerStorageProvider;
    // Controller
    @Spy
    BattleMenuController battleMenuController;
    @Mock
    Parent parent;
    @Mock
    AbilitiesMenuController abilitiesMenuController;
    @Mock
    EncounterOpponentController encounterOpponentController;


    public void start(Stage stage) {
        ResourceBundle bundle = ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
        encounterController.setValues(bundle, null, null, encounterController, app);

        // Mock the situation for 1 vs 1
        when(encounterOpponentStorage.getEncounterSize()).thenReturn(2);

        TrainerStorage trainerStorage = mock(TrainerStorage.class);
        when(trainerStorageProvider.get()).thenReturn(trainerStorage);
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

        when(encounterOpponentStorage.getEncounterId()).thenReturn("64aa9f7132eb8b56aa9eb208");

        // Mock the function in render methode
        when(regionEncountersService.getEncounter(anyString(), anyString())).thenReturn(Observable.just(
                new Encounter(
                        "2023-05-22T17:51:46.772Z",
                        "2023-05-22T17:51:46.772Z",
                        "64aa9f7132eb8b56aa9eb208",
                        "646bab5cecf584e1be02598a",
                        false
                )
        ));

        // Mock the function for ownTrainer
        when(trainerStorageProvider.get().getTrainer()).thenReturn(
                new Trainer(
                        "2023-05-30T12:02:57.510Z",
                        "2023-05-30T12:01:57.510Z",
                        "6475e595ac3946b6a812d865",
                        "646bab5cecf584e1be02598a",
                        "6475e595ac3946b6a812d868",
                        "Peter",
                        "Premade_Character_02.png",
                        0,
                        List.of("64aa9f636cec1b8f0fac57dc"),
                        List.of(1),
                        "6475e595ac3946b6a812d863",
                        33,
                        18,
                        1,
                        null
                )
        );

        when(encounterOpponentStorage.getEnemyOpponents()).thenReturn(
                List.of(
                        new Opponent(
                                "2023-07-09T11:52:17.658Z",
                                "2023-07-09T11:52:35.578Z",
                                "64aa9f7132eb8b56aa9eb20f",
                                "64aa9f7132eb8b56aa9eb208",
                                "64abfde932eb8b56aac8efac",
                                true,
                                true,
                                "64aa9f7132eb8b56aa9eb20c",
                                null,
                                List.of(),
                                0
                        )));
        when(encounterOpponentStorage.getSelfOpponent()).thenReturn(new Opponent(
                "2023-07-09T11:52:17.658Z",
                "2023-07-09T11:52:35.578Z",
                "64aa9f7132eb8b56aa9eb20f",
                "64aa9f7132eb8b56aa9eb208",
                "64abfde932eb8b56aac8efac",
                true,
                true,
                "64aa9f7132eb8b56aa9eb20c",
                null,
                List.of(),
                0
        ));

        // Mock the team and enemy Monster
        doNothing().when(encounterOpponentStorage).addCurrentMonster(any());
        LinkedHashMap<String, Integer> abilities = new LinkedHashMap<>();
        abilities.put("1", 35);
        abilities.put("3", 20);
        abilities.put("6", 25);
        abilities.put("7", 15);
        Monster monster = new Monster(
                "2023-05-22T17:51:46.772Z",
                "2023-05-22T17:51:46.772Z",
                "64aa9f636cec1b8f0fac57dc",
                "64aa9f4d32eb8b56aa9eace9",
                1,
                1,
                0,
                abilities,
                new MonsterAttributes(14, 8, 8, 5),
                new MonsterAttributes(14, 8, 8, 5)
        );
        when(monstersService.getMonster(anyString(), anyString(), anyString())).thenReturn(Observable.just(monster));

        MonsterTypeDto monsterType = new MonsterTypeDto(
                1,
                "Flamander",
                "Flamander_1.png",
                List.of("fire"),
                "Flamander is a small, agile monster that lives in the hot deserts of the world."
        );
        when(presetsService.getMonsterImage(1)).thenReturn(Observable.just(ResponseBody.create(null, new byte[0])));

        // Mock the enemy trainer
        when(trainersService.getTrainer(anyString(), anyString())).thenReturn(Observable.just(
                new Trainer(
                        "2023-05-30T12:02:57.510Z",
                        "2023-05-30T12:01:57.510Z",
                        "64abfde932eb8b56aac8efac",
                        "646bab5cecf584e1be02598a",
                        "6475e595ac3946b6a812d868",
                        "Peter",
                        "Premade_Character_02.png",
                        0,
                        List.of("64aa9f7132eb8b56aa9eb20c"),
                        List.of(1),
                        "6475e595ac3946b6a812d863",
                        33,
                        18,
                        1,
                        null
                )
        ));

        // Mock eventListener
        EventListener eventListenerMock = mock(EventListener.class);
        when(eventListener.get()).thenReturn(eventListenerMock);
        when(eventListener.get().listen("encounters." + encounterOpponentStorage.getEncounterId() + ".trainers.*.opponents.*.*", Opponent.class)).thenReturn(just(
                new Event<>("encounters.*.trainers.*.opponents.*.nothappening", null)));

        when(encounterOpponentStorage.isWild()).thenReturn(false);
        //when(presetsService.getCharacter(anyString())).thenReturn(Observable.just(ResponseBody.create(null, new byte[0])));
        //when(presetsService.getAbilities()).thenReturn(Observable.just(List.of(new AbilityDto(1, "Attack", "unknown", "fire", 5, 0.99, 100))));
        //when(presetsService.getMonster(anyInt())).thenReturn(Observable.just(monsterType));
        //when(preferences.getDouble("volume", AudioService.getInstance().getVolume())).thenReturn(0.5);
        doNothing().when(battleMenuController).init();
        app.start(stage);
        app.show(encounterController);
        stage.requestFocus();

    }

    @Test
    void controllerTest() {

    }

    @Test
    void renderFor1vs2Test() {
        when(encounterOpponentStorage.getEncounterSize()).thenReturn(3);
        when(encounterOpponentStorage.getEnemyOpponents()).thenReturn(
                List.of(
                        new Opponent(
                                "2023-07-09T11:52:17.658Z",
                                "2023-07-09T11:52:35.578Z",
                                "64aa9f7132eb8b56aa9eb20f",
                                "64aa9f7132eb8b56aa9eb208",
                                "64abfde932eb8b56aac8efac",
                                true,
                                true,
                                "64aa9f7132eb8b56aa9eb20c",
                                null,
                                List.of(),
                                0
                        ),
                        new Opponent(
                                "2023-07-09T11:52:17.658Z",
                                "2023-07-09T11:52:35.578Z",
                                "64aa9f7132eb8b56aa9eb20p",
                                "64aa9f7132eb8b56aa9eb208",
                                "64abfde932eb8b56aac8efap",
                                true,
                                true,
                                "64aa9f7132eb8b56aa9eb20c",
                                null,
                                List.of(),
                                0
                        )
                ));
        ResourceBundle bundle = ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
        encounterController.setValues(bundle, null, null, encounterController, app);
        app.show(encounterController);
    }

}