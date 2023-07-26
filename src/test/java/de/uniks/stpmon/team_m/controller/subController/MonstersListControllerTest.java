package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.controller.EncounterController;
import de.uniks.stpmon.team_m.controller.IngameController;
import de.uniks.stpmon.team_m.dto.Map;
import de.uniks.stpmon.team_m.dto.*;
import de.uniks.stpmon.team_m.service.MonstersService;
import de.uniks.stpmon.team_m.service.PresetsService;
import de.uniks.stpmon.team_m.service.TrainersService;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import io.reactivex.rxjava3.core.Observable;
import javafx.collections.FXCollections;
import javafx.scene.layout.StackPane;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MonstersListControllerTest extends ApplicationTest {
    @Spy
    final
    App app = new App(null);
    @InjectMocks
    MonstersListController monsterListController;
    @Mock
    MonstersService monstersService;
    @Mock
    TrainersService trainersService;
    @Mock
    Provider<TrainerStorage> trainerStorageProvider;
    @Mock
    Provider<PresetsService> presetsServiceProvider;
    @Mock
    IngameController ingameController;

    private List<Monster> monsters;
    private ResourceBundle resources;
    private StackPane rootStackPane;

    @Override
    public void start(Stage stage) {
        resources = ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
        monsterListController.setValues(resources, null, null, monsterListController, app);
        stage.requestFocus();
        PresetsService presetsService = mock(PresetsService.class);
        when(presetsServiceProvider.get()).thenReturn(presetsService);
        LinkedHashMap<String, Integer> abilities = new LinkedHashMap<>();
        abilities.put("1", 35);
        abilities.put("3", 20);
        abilities.put("6", 25);
        abilities.put("7", 15);
        monsters = List.of(
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
        when(presetsService.getMonster(1)).thenReturn(Observable.just(
                new MonsterTypeDto(
                        1,
                        "Flamander",
                        "Flamander_1.png",
                        List.of("fire"),
                        "Flamander is a small, agile monster that lives in the hot deserts of the world.")
        ));
        when(presetsService.getMonster(2)).thenReturn(Observable.just(
                new MonsterTypeDto(
                        2,
                        "Flamander_2",
                        "Flamander_1.png",
                        List.of("fire"),
                        "Flamander is a small, agile monster that lives in the hot deserts of the world.")
        ));
        when(presetsService.getMonster(3)).thenReturn(Observable.just(
                new MonsterTypeDto(
                        3,
                        "Flamander_3",
                        "Flamander_1.png",
                        List.of("fire"),
                        "Flamander is a small, agile monster that lives in the hot deserts of the world.")
        ));
        when(presetsService.getMonsterImage(1)).thenReturn(Observable.just(ResponseBody.create(null, new byte[0])));
        when(presetsService.getMonsterImage(2)).thenReturn(Observable.just(ResponseBody.create(null, new byte[0])));
        when(presetsService.getMonsterImage(3)).thenReturn(Observable.just(ResponseBody.create(null, new byte[0])));

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
                null
        );
        when(trainerStorage.getTrainer()).thenReturn(trainer);
        when(trainerStorageProvider.get().getTrainer()).thenReturn(trainer);

        app.start(stage);
        app.show(monsterListController);
        monsterListController.init(ingameController, monsterListController.monsterListVBox, rootStackPane, null);
    }

    @Test
    void onCloseMonsterList() {
        clickOn("#closeButton");
    }

    @Test
    void changeOrderDown() {
        when(trainersService.updateTrainer(any(), any(), any(), any(), any()))
                .thenReturn(Observable.just(
                        new Trainer(
                                "2023-06-05T17:02:40.332Z",
                                "2023-06-12T22:14:00.005Z",
                                "647e15308c1bb6a91fb57321",
                                "645e32c6866ace359554a7ec",
                                "647a466a494a75c31bb05921",
                                "Hans",
                                "Premade_Character_04.png",
                                0,
                                List.of("647e1530866ace3595866500", "647e1530866ace3595866db2"),
                                List.of(1, 2),
                                List.of("647e1530866ace3595866db2"),
                                "645e32c6866ace359554a7fa",
                                45,
                                23,
                                0,
                                null
                        )
                ));

        assertEquals(monsters.get(0), monsterListController.activeMonstersList.get(0));
        assertEquals(monsters.get(1), monsterListController.activeMonstersList.get(1));

        clickOn("#arrowDown647e1530866ace3595866500");

        assertEquals(monsters.get(0), monsterListController.activeMonstersList.get(0));
        assertEquals(monsters.get(1), monsterListController.activeMonstersList.get(1));


        clickOn("#arrowDown647e1530866ace3595866db2");

        assertEquals(monsters.get(0), monsterListController.activeMonstersList.get(1));
        assertEquals(monsters.get(1), monsterListController.activeMonstersList.get(0));
    }


    @Test
    void changeOrderUp() {
        when(trainersService.updateTrainer(any(), any(), any(), any(), any()))
                .thenReturn(Observable.just(
                        new Trainer(
                                "2023-06-05T17:02:40.332Z",
                                "2023-06-12T22:14:00.005Z",
                                "647e15308c1bb6a91fb57321",
                                "645e32c6866ace359554a7ec",
                                "647a466a494a75c31bb05921",
                                "Hans",
                                "Premade_Character_04.png",
                                0,
                                List.of("647e1530866ace3595866db2", "647e1530866ace3595866500"),
                                List.of(1, 2),
                                List.of("647e1530866ace3595866db2"),
                                "645e32c6866ace359554a7fa",
                                45,
                                23,
                                0,
                                null
                        )
                ));

        assertEquals(monsters.get(0), monsterListController.activeMonstersList.get(0));
        assertEquals(monsters.get(1), monsterListController.activeMonstersList.get(1));

        clickOn("#arrowUp647e1530866ace3595866db2");

        assertEquals(monsters.get(0), monsterListController.activeMonstersList.get(0));
        assertEquals(monsters.get(1), monsterListController.activeMonstersList.get(1));

        clickOn("#arrowUp647e1530866ace3595866500");

        assertEquals(monsters.get(0), monsterListController.activeMonstersList.get(1));
        assertEquals(monsters.get(1), monsterListController.activeMonstersList.get(0));
    }

    @Test
    void removeFromTeam() {
        @SuppressWarnings("unchecked") final List<String> team = spy(List.class);
        final Trainer trainer = new Trainer(
                "2023-06-05T17:02:40.332Z",
                "2023-06-12T22:14:00.005Z",
                "647e15308c1bb6a91fb57321",
                "645e32c6866ace359554a7ec",
                "647a466a494a75c31bb05921",
                "Hans",
                "Premade_Character_04.png",
                0,
                team,
                List.of(1, 2),
                List.of("647e1530866ace3595866db2", "647e1530866ace3595866900"),
                "645e32c6866ace359554a7fa",
                45,
                23,
                0,
                null
        );

        when(trainerStorageProvider.get().getTrainer()).thenReturn(trainer);
        when(team.remove(anyString())).thenReturn(true);
        when(trainersService.updateTrainer(any(), any(), any(), any(), any()))
                .thenReturn(Observable.just(
                        new Trainer(
                                "2023-06-05T17:02:40.332Z",
                                "2023-06-12T22:14:00.005Z",
                                "647e15308c1bb6a91fb57321",
                                "645e32c6866ace359554a7ec",
                                "647a466a494a75c31bb05921",
                                "Hans",
                                "Premade_Character_04.png",
                                0,
                                List.of("647e1530866ace3595866db2"),
                                List.of(1, 2),
                                List.of("647e1530866ace3595866db2", "647e1530866ace3595866500"),
                                "645e32c6866ace359554a7fa",
                                45,
                                23,
                                0,
                                null
                        )
                ));

        clickOn("#removeFromTeamButton647e1530866ace3595866500");
        verify(team).remove(monsters.get(1)._id());
        assertFalse(team.contains(monsters.get(1)._id()));
    }

    @Test
    void addToTeam() {
        clickOn("#othersTab");

        @SuppressWarnings("unchecked") final List<String> team = spy(List.class);
        final Trainer trainer = new Trainer(
                "2023-06-05T17:02:40.332Z",
                "2023-06-12T22:14:00.005Z",
                "647e15308c1bb6a91fb57321",
                "645e32c6866ace359554a7ec",
                "647a466a494a75c31bb05921",
                "Hans",
                "Premade_Character_04.png",
                0,
                team,
                List.of(1, 2),
                List.of("647e1530866ace3595866db2", "647e1530866ace3595866900"),
                "645e32c6866ace359554a7fa",
                45,
                23,
                0,
                null
        );
        when(trainerStorageProvider.get().getTrainer()).thenReturn(trainer);
        when(team.add(anyString())).thenReturn(true);
        when(trainersService.updateTrainer(any(), any(), any(), any(), any()))
                .thenReturn(Observable.just(
                        new Trainer(
                                "2023-06-05T17:02:40.332Z",
                                "2023-06-12T22:14:00.005Z",
                                "647e15308c1bb6a91fb57321",
                                "645e32c6866ace359554a7ec",
                                "647a466a494a75c31bb05921",
                                "Hans",
                                "Premade_Character_04.png",
                                0,
                                List.of("647e1530866ace3595866db2"),
                                List.of(1, 2),
                                List.of("647e1530866ace3595866db2", "647e1530866ace3595866900", "647e1530866ace3595866500"),
                                "645e32c6866ace359554a7fa",
                                45,
                                23,
                                0,
                                null
                        )
                ));

        assertEquals(monsterListController.otherMonstersList.size(), 1);
        assertEquals(monsterListController.activeMonstersList.size(), 2);

        clickOn("#removeFromTeamButton647e1530866ace3595866db2");

        clickOn("#activeTeamTab");

        assertEquals(monsterListController.otherMonstersList.size(), 0);
        assertEquals(monsterListController.activeMonstersList.size(), 3);
    }

    @Test
    void createLimitPopUp() {
        final StackPane root = mock(StackPane.class);
        when(ingameController.getRoot()).thenReturn(root);
        when(root.getChildren()).thenReturn(FXCollections.observableArrayList());
        when(trainerStorageProvider.get().getTrainer()).thenReturn(
                new Trainer(
                        "2023-06-05T17:02:40.332Z",
                        "2023-06-12T22:14:00.005Z",
                        "647e15308c1bb6a91fb57321",
                        "645e32c6866ace359554a7ec",
                        "647a466a494a75c31bb05921",
                        "Hans",
                        "Premade_Character_04.png",
                        0,
                        List.of("647e1530866ace3595866db2", "647e1530866ace3595866db3", "647e1530866ace3595866db4", "647e1530866ace3595866db5", "647e1530866ace3595866db6", "647e1530866ace3595866500"),
                        List.of(1, 2),
                        List.of("647e1530866ace3595866db2", "647e1530866ace3595866900"),
                        "645e32c6866ace359554a7fa",
                        45,
                        23,
                        0,
                        null
                )
        );
        clickOn("#othersTab");
        clickOn("#removeFromTeamButton647e1530866ace3595866900");
    }

    @Test
    void showMonsterDetails() {
        doNothing().when(ingameController).showMonsterDetails(any(), any(), any(), any(), any(), any());
        clickOn("#viewDetailsButton647e1530866ace3595866500");
    }

    @Test
    void testEncounterMonsterCell() {
        final EncounterController encounterController = mock(EncounterController.class);
        final ChangeMonsterListController changeMonsterListController = mock(ChangeMonsterListController.class);
        monsterListController.monsterListViewActive.setCellFactory(c ->
                new MonsterCell(
                        resources,
                        presetsServiceProvider.get(),
                        null,
                        changeMonsterListController,
                        encounterController,
                        ingameController,
                        true,
                        null
                ));
        monsterListController.monsterListViewActive.refresh();

        // Get a list cell and check values
    }

    @Test
    void testItemMonsterCell() {
        final Item item = mock(Item.class);
        monsterListController.monsterListViewActive.setCellFactory(c ->
                new MonsterCell(
                        resources,
                        presetsServiceProvider.get(),
                        monsterListController,
                        null,
                        null,
                        ingameController,
                        false,
                        item
                ));
        monsterListController.monsterListViewActive.refresh();

        // Get a list cell and check values
    }


    @Test
    void testEncounterItemMonsterCell() {
        final EncounterController encounterController = mock(EncounterController.class);
        final ChangeMonsterListController changeMonsterListController = mock(ChangeMonsterListController.class);
        final Item item = mock(Item.class);
        monsterListController.monsterListViewActive.setCellFactory(c ->
                new MonsterCell(
                        resources,
                        presetsServiceProvider.get(),
                        null,
                        changeMonsterListController,
                        encounterController,
                        ingameController,
                        true,
                        item
                ));
        monsterListController.monsterListViewActive.refresh();

        // Get a list cell and check values
    }
}