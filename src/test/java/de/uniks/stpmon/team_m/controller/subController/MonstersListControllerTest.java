package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.dto.*;
import de.uniks.stpmon.team_m.service.MonstersService;
import de.uniks.stpmon.team_m.service.PresetsService;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MonstersListControllerTest extends ApplicationTest {
    @Spy
    App app = new App(null);
    @InjectMocks
    MonstersListController monsterListController;
    @Mock
    MonstersService monstersService;
    @Mock
    Provider<TrainerStorage> trainerStorageProvider;
    @Mock
    Provider<PresetsService> presetsServiceProvider;

    @Override
    public void start(Stage stage) {
        ResourceBundle bundle = ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
        monsterListController.setValues(bundle, null, null, monsterListController, app);
        stage.requestFocus();
        PresetsService presetsService = mock(PresetsService.class);
        when(presetsServiceProvider.get()).thenReturn(presetsService);
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
                        new MonsterAttributes(14, 8, 8, 5)));
        when(monstersService.getMonsters(any(), any())).thenReturn(Observable.just(monsters));
        TrainerStorage trainerStorage = mock(TrainerStorage.class);
        when(trainerStorageProvider.get()).thenReturn(trainerStorage);
        doNothing().when(trainerStorage).setMonsters(any());
        when(trainerStorage.getMonsters()).thenReturn(monsters);
        when(presetsService.getMonster(1)).thenReturn(Observable.just(
                new MonsterTypeDto(
                        1,
                        "Flamander",
                        "Flamander_1.png",
                        List.of("fire"),
                        "Flamander is a small, agile monster that lives in the hot deserts of the world.")
        ));
        when(presetsService.getMonsterImage(1)).thenReturn(Observable.just(ResponseBody.create(null, new byte[0])));
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
        when(trainerStorage.getTrainer()).thenReturn(new Trainer(
                "2023-06-05T17:02:40.332Z",
                "2023-06-12T22:14:00.005Z",
                "647e15308c1bb6a91fb57321",
                "645e32c6866ace359554a7ec",
                "647a466a494a75c31bb05921",
                "Hans",
                "Premade_Character_04.png",
                0,
                List.of("647e1530866ace3595866db2"),
                List.of(1,2),
                "645e32c6866ace359554a7fa",
                45,
                23,
                0,
                null
        ));
        app.start(stage);
        app.show(monsterListController);
    }

    @Test
    void controllerTest() {
        moveTo("Name: Flamander");
        moveTo("Level: 1");
        moveTo("Remove from Team");
        moveTo("View Details");
    }
}
