package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.controller.subController.BattleMenuController;
import de.uniks.stpmon.team_m.dto.*;
import de.uniks.stpmon.team_m.service.*;
import de.uniks.stpmon.team_m.utils.EncounterOpponentStorage;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import de.uniks.stpmon.team_m.ws.EventListener;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Provider;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import static io.reactivex.rxjava3.core.Observable.just;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

@ExtendWith(MockitoExtension.class)
class EncounterBattleTest extends ApplicationTest {

    @Spy
    final App app = new App();
    @Mock
    Preferences preferences;
    @Mock
    RegionEncountersService regionEncountersService;
    @Mock
    MonstersService monstersService;
    @Mock
    Provider<EventListener> eventListener;
    @Mock
    PresetsService presetsService;
    @Mock
    TrainersService trainersService;
    // Storage
    @Spy
    EncounterOpponentStorage encounterOpponentStorage;
    @Mock
    Provider<TrainerStorage> trainerStorageProvider;
    // Controller
    @Spy
    BattleMenuController battleMenuController;
    @InjectMocks
    EncounterController encounterController;

    public void start(Stage stage) {
        ResourceBundle bundle = ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
        encounterController.setValues(bundle, preferences, null, encounterController, app);

        lenient().when(preferences.getDouble(anyString(), anyDouble())).thenReturn(0.0);

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
                        "64abfde932eb8b56aac8efac",
                        "646bab5cecf584e1be02598a",
                        "6475e595ac3946b6a812d868",
                        "Peter",
                        "Premade_Character_02.png",
                        0,
                        List.of("64aa9f636cec1b8f0fac57dc"),
                        List.of(1),
                        List.of("6475e595ac3946b6a812d863"),
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
                                "648c7a2a866ace3595acbf36",
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
                false,
                false,
                "64aa9f636cec1b8f0fac57dc",
                null,
                List.of(),
                0
        ));

        // Mock the team and enemy Monster
        doNothing().when(encounterOpponentStorage).addCurrentMonsters(anyString(), any());
        LinkedHashMap<String, Integer> abilities = new LinkedHashMap<>();
        abilities.put("1", 35);
        abilities.put("3", 20);
        abilities.put("6", 25);
        abilities.put("7", 15);
        Monster selfMonster = new Monster(
                "2023-05-22T17:51:46.772Z",
                "2023-05-22T17:51:46.772Z",
                "64aa9f636cec1b8f0fac57dc",
                "64abfde932eb8b56aac8efac",
                1,
                1,
                0,
                abilities,
                new MonsterAttributes(14, 8, 8, 5),
                new MonsterAttributes(14, 8, 8, 5),
                List.of()
        );

        Monster enemyMonster = new Monster(
                "2023-05-22T17:51:46.772Z",
                "2023-05-22T17:51:46.772Z",
                "64aa9f7132eb8b56aa9eb20c",
                "648c7a2a866ace3595acbf36",
                1,
                1,
                0,
                abilities,
                new MonsterAttributes(14, 8, 8, 5),
                new MonsterAttributes(14, 8, 8, 5),
                List.of()
        );

        when(monstersService.getMonster(anyString(), any(), anyString()))
                .thenReturn(Observable.just(selfMonster))
                .thenReturn(Observable.just(enemyMonster));

        when(presetsService.getMonsterImage(1)).thenReturn(Observable.just(ResponseBody.create(null, new byte[0])));

        lenient().doNothing().when(battleMenuController).setTrainerSpriteImageView(any(), any(), anyInt());

        lenient().when(presetsService.getCharacter(any())).thenReturn(new Observable<>() {
            @Override
            protected void subscribeActual(@NonNull Observer<? super ResponseBody> observer) {

            }
        });

        when(presetsService.getAbilities()).thenReturn(Observable.just(List.of(
                new AbilityDto(
                        124512,
                        "tolle Fähigkeit",
                        "macht ganz dolle aua",
                        "fire",
                        5,
                        100,
                        69
                )
        )));

        when(presetsService.getMonster(anyInt())).thenReturn(Observable.just(
                new MonsterTypeDto(
                        696969,
                        "Kätzchen Zerstörer",
                        "images/monster1_without",
                        List.of("fire"),
                        "Jooooo das vieh ballert"
                )
        ));


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
                        List.of("6475e595ac3946b6a812d863"),
                        "6475e595ac3946b6a812d863",
                        33,
                        18,
                        1,
                        null
                )
        ));

        // Mock eventListener Opponenten
        String encounterId = "64aa9f7132eb8b56aa9eb208";

        Opponent useAbilityOpponentSelf = new Opponent(
                "2023-07-09T11:52:17.658Z",
                "2023-07-09T11:52:35.578Z",
                "64aa9f7132eb8b56aa9eb20f",
                "64aa9f7132eb8b56aa9eb208",
                "64abfde932eb8b56aac8efac",
                false,
                false,
                "64bd8cadfcc75bfbe9a502ee",
                new AbilityMove(null, 6, "648c7a2a866ace3595acbf36"),
                List.of(),
                0
        );

        Opponent useAbilityOpponentEnemy = new Opponent(
                "2023-07-09T11:52:17.658Z",
                "2023-07-09T11:52:35.578Z",
                "64aa9f7132eb8b56aa9eb20f",
                "64aa9f7132eb8b56aa9eb208",
                "648c7a2a866ace3595acbf36",
                true,
                true,
                "64aa9f7132eb8b56aa9eb20c",
                new AbilityMove(null, 6, "64abfde932eb8b56aac8efac"),
                List.of(),
                0
        );

        Opponent resultOpponentSelf = new Opponent(
                "2023-07-09T11:52:17.658Z",
                "2023-07-09T11:52:35.578Z",
                "64aa9f7132eb8b56aa9eb20f",
                "64aa9f7132eb8b56aa9eb208",
                "64abfde932eb8b56aac8efac",
                false,
                false,
                "64bd8cadfcc75bfbe9a502ee",
                null,
                List.of(new Result("ability-success", null,6, "super-effective", null, null),
                        new Result("target-defeated", null,null, null, null, null),
                        new Result("monster-leanred", null,6, null, null, null),
                        new Result("monster-levelup", null,null, null, null, null),
                        new Result("monster-evolution", null,null, null, null, null),
                        new Result("status-added", null,null, null, null, "burned")),
                0
        );

        Opponent resultOpponentEnemy = new Opponent(
                "2023-07-09T11:52:17.658Z",
                "2023-07-09T11:52:35.578Z",
                "64aa9f7132eb8b56aa9eb20f",
                "64aa9f7132eb8b56aa9eb208",
                "648c7a2a866ace3595acbf36",
                true,
                true,
                null,
                null,
                List.of(new Result("ability-success", null,6, "super-effective", null, null)),
                0
        );

        EventListener eventListenerMock = mock(EventListener.class);
        when(eventListener.get()).thenReturn(eventListenerMock);
        when(eventListener.get().listen("encounters." + encounterId + ".trainers.*.opponents.*.*" , Opponent.class
        )).thenReturn(just(new Event<>("encounters.*.trainers.*.opponents.*.nothappening", null)
        )).thenReturn(just(new Event<>("encounters.*.trainers.*.opponents.*.updated", useAbilityOpponentSelf)
        )).thenReturn(just(new Event<>("encounters.*.trainers.*.opponents.*.updated", useAbilityOpponentEnemy)
        )).thenReturn(just(new Event<>("encounters.*.trainers.*.opponents.*.updated", resultOpponentSelf)
        )).thenReturn(just(new Event<>("encounters.*.trainers.*.opponents.*.updated", resultOpponentEnemy)
        )).thenReturn(just(new Event<>("encounters.*.trainers.*.opponents.*.deleted", resultOpponentSelf)
        )).thenReturn(just(new Event<>("encounters.*.trainers.*.opponents.*.deleted", resultOpponentEnemy)
        ));

        when(encounterOpponentStorage.isWild()).thenReturn(false);

        //Mock Eventlistener Monster
        Monster selfMonsterUpdated = new Monster(
                "2023-05-22T17:51:46.772Z",
                "2023-05-22T17:51:46.772Z",
                "64aa9f636cec1b8f0fac57dc",
                "64abfde932eb8b56aac8efac",
                1,
                1,
                0,
                abilities,
                new MonsterAttributes(8, 8, 8, 5),
                new MonsterAttributes(14, 8, 8, 5),
                List.of()
        );

        Monster enemyMonsterUpdated = new Monster(
                "2023-05-22T17:51:46.772Z",
                "2023-05-22T17:51:46.772Z",
                "64aa9f7132eb8b56aa9eb20c",
                "648c7a2a866ace3595acbf36",
                1,
                1,
                0,
                abilities,
                new MonsterAttributes(0, 8, 8, 5),
                new MonsterAttributes(14, 8, 8, 5),
                List.of()
        );
        String selfMonsterId = "64aa9f636cec1b8f0fac57dc";
        String selfTrainerId = "64abfde932eb8b56aac8efac";
        String enemyMonsterId = "64aa9f7132eb8b56aa9eb20c";
        String enemyTrainerId = "648c7a2a866ace3595acbf36";

        when(eventListener.get().listen("trainers." + selfTrainerId + ".monsters." + selfMonsterId + ".*", Monster.class)).thenReturn(just(
                new Event<>("trainers.*.monsters.*.updated", selfMonsterUpdated)
        ));

        when(eventListener.get().listen("trainers." + enemyTrainerId + ".monsters." + enemyMonsterId + ".*", Monster.class))
                .thenReturn(just(new Event<>("trainers.*.monsters.*.updated", enemyMonsterUpdated)));

        app.start(stage);
        app.show(encounterController);
        stage.requestFocus();
    }

    @Test
    void updateDescriptionTest () {
        String encounterId = "64aa9f7132eb8b56aa9eb208";
        for(int i = 0; i < 4; i++) {
            encounterController.listenToOpponents(encounterId);
        }

        waitForFxEvents();

        Text description = lookup("#battleDialogText").query();
        assertNotEquals("The Battle was begun! you are fight against Peter", description.getText());

        for(int i = 0; i < 2; i++) {
            encounterController.listenToOpponents(encounterId);
        }

    }


}