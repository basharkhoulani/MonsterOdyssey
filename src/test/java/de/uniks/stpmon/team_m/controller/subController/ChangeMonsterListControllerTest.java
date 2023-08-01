package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.dto.Map;
import de.uniks.stpmon.team_m.dto.*;
import de.uniks.stpmon.team_m.service.MonstersService;
import de.uniks.stpmon.team_m.service.PresetsService;
import de.uniks.stpmon.team_m.utils.EncounterOpponentStorage;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import io.reactivex.rxjava3.core.Observable;
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
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChangeMonsterListControllerTest extends ApplicationTest {
    @Spy
    final App app = new App(null);
    @InjectMocks
    ChangeMonsterListController changeMonsterListController;
    @Mock
    Provider<TrainerStorage> trainerStorageProvider;
    @Mock
    MonstersService monstersService;
    @Mock
    Provider<EncounterOpponentStorage> encounterOpponentStorageProvider;
    @Mock
    Provider<PresetsService> presetsServiceProvider;

    @Override
    public void start(Stage stage) throws Exception {
        ResourceBundle resources = ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
        changeMonsterListController.setValues(resources, null, null, changeMonsterListController, app);
        stage.requestFocus();
        PresetsService presetsService = mock(PresetsService.class);
        when(presetsServiceProvider.get()).thenReturn(presetsService);
        when(presetsService.getMonster(anyInt())).thenReturn(Observable.just(new MonsterTypeDto(
                696969,
                "BattleCat",
                "images/monster1_without",
                List.of("fire"),
                "Jooooo das vieh ballert"
        )));
        when(presetsService.getMonsterImage(anyInt())).thenReturn(Observable.just(ResponseBody.create(null, new byte[0])));
        LinkedHashMap<String, Integer> abilities = new LinkedHashMap<>();
        abilities.put("1", 35);
        abilities.put("3", 20);
        abilities.put("6", 25);
        abilities.put("7", 15);
        List<Monster> monsters = List.of(
                new Monster("2023-06-05T17:02:40.357Z",
                        "023-06-05T17:02:40.357Z",
                        "647e1530866ace3595866db2",
                        "647e15308c1bb6a91fb57321",
                        1,
                        1,
                        0,
                        abilities,
                        new MonsterAttributes(14, 8, 8, 5),
                        new MonsterAttributes(14, 8, 8, 5),
                        List.of("poisoned")),
                new Monster("2023-06-05T17:02:40.357Z",
                        "023-06-05T17:02:40.357Z",
                        "647e1530866ace3595866500",
                        "647e15308c1bb6a91fb57321",
                        2,
                        1,
                        0,
                        abilities,
                        new MonsterAttributes(14, 8, 8, 5),
                        new MonsterAttributes(14, 8, 8, 5),
                        List.of()),
                new Monster("2023-06-05T17:02:40.357Z",
                        "023-06-05T17:02:40.357Z",
                        "647e1530866ace3595866900",
                        "647e15308c1bb6a91fb57321",
                        3,
                        1,
                        0,
                        abilities,
                        new MonsterAttributes(14, 8, 8, 5),
                        new MonsterAttributes(14, 8, 8, 5),
                        List.of()));
        when(monstersService.getMonsters(any(), any())).thenReturn(Observable.just(monsters));
        TrainerStorage trainerStorage = mock(TrainerStorage.class);
        when(trainerStorageProvider.get()).thenReturn(trainerStorage);
        doNothing().when(trainerStorage).setMonsters(any());
        when(trainerStorage.getMonsters()).thenReturn(monsters);
        when(trainerStorageProvider.get().getRegion()).thenReturn(new Region(
                "2023-05-22T17:51:46.772Z",
                "2023-05-22T17:51:46.772Z",
                "646bc436cfee07c0e408466f",
                "Albertina",
                new Spawn("646bc3c0a9ac1b375fb41d93", 1, 1),
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
                        List.of())
        ));
        final Trainer trainer = new Trainer(
                "2023-06-05T17:02:40.332Z",
                "2023-06-12T22:14:00.005Z",
                "647e15308c1bb6a91fb57321",
                "645e32c6866ace359554a7ec",
                "647a466a494a75c31bb05921",
                "Hans",
                "Premade_Character_04.png",
                0,
                Arrays.asList("647e1530866ace3595866db2", "647e1530866ace3595866500"),
                List.of(1, 2),
                List.of("647e1530866ace3595866db2", "647e1530866ace3595866900"),
                "645e32c6866ace359554a7fa",
                45,
                23,
                0,
                null,
                null
        );
        when(trainerStorage.getTrainer()).thenReturn(trainer);
        when(trainerStorageProvider.get().getTrainer()).thenReturn(trainer);

        EncounterOpponentStorage encounterOpponentStorage = mock(EncounterOpponentStorage.class);
        when(encounterOpponentStorageProvider.get()).thenReturn(encounterOpponentStorage);

        when(encounterOpponentStorage.isTwoMonster()).thenReturn(false);
        when(encounterOpponentStorage.getSelfOpponent()).thenReturn(new Opponent(
                "2023-07-09T11:52:17.658Z",
                "2023-07-09T11:52:35.578Z",
                "64aa9f7132eb8b56aa9eb20f",
                "64aa9f7132eb8b56aa9eb208",
                null,
                true,
                true,
                "64aa9f7132eb8b56aa9eb20c",
                null,
                List.of(),
                0
        ));

        changeMonsterListController.init(null, null, null);
        app.start(stage);
        app.show(changeMonsterListController);
    }

    @Test
    void test() {
        moveTo("Close");
    }
}
