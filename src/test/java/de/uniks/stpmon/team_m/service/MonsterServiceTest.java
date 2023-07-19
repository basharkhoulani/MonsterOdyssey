package de.uniks.stpmon.team_m.service;

import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.dto.MonsterAttributes;
import de.uniks.stpmon.team_m.rest.MonstersApiService;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MonsterServiceTest {
    @Mock
    MonstersApiService monstersApiService;
    @InjectMocks
    MonstersService monstersService;

    @Test
    void getMonstersTest() {
        LinkedHashMap<String, Integer> abilities = new LinkedHashMap<>();
        abilities.put("1", 1);
        abilities.put("23", 2);
        abilities.put("4", 3);
        when(monstersApiService.getMonsters("646bab5cecf584e1be02598a", "646bac8c1a74032c70fffe24"))
                .thenReturn(Observable.just(List.of(
                        new Monster(
                                "2023-05-22T17:51:46.772Z",
                                "2023-05-22T17:51:46.772Z",
                                "646bac223b4804b87c0b8054",
                                "646bac8c1a74032c70fffe24",
                                1,
                                3,
                                56,
                                abilities,
                                new MonsterAttributes(40, 23, 45, 67),
                                new MonsterAttributes(20, 23, 45, 67),
                                List.of()
                        ),
                        new Monster(
                                "2023-05-22T17:51:46.772Z",
                                "2023-05-22T17:51:46.772Z",
                                "646bac223b480c87c0b8054",
                                "646bac8c1a74032c70fffe24",
                                2,
                                5,
                                23,
                                abilities,
                                new MonsterAttributes(40, 23, 45, 67),
                                new MonsterAttributes(20, 23, 45, 67),
                                List.of()
                        )
                )));

        final List<Monster> monsters = monstersService.getMonsters("646bab5cecf584e1be02598a", "646bac8c1a74032c70fffe24").blockingFirst();

        assertNotNull(monsters);

        verify(monstersApiService).getMonsters("646bab5cecf584e1be02598a", "646bac8c1a74032c70fffe24");
    }

    @Test
    void getMonsterTest() {
        LinkedHashMap<String, Integer> abilities = new LinkedHashMap<>();
        abilities.put("1", 1);
        abilities.put("23", 2);
        abilities.put("4", 3);
        when(monstersApiService.getMonster("646bab5cecf584e1be02598a", "646bac8c1a74032c70fffe24", "646bac223b4804b87c0b8054"))
                .thenReturn(Observable.just(
                        new Monster(
                                "2023-05-22T17:51:46.772Z",
                                "2023-05-22T17:51:46.772Z",
                                "646bac223b4804b87c0b8054",
                                "646bac8c1a74032c70fffe24",
                                1,
                                3,
                                56,
                                abilities,
                                new MonsterAttributes(40, 23, 45, 67),
                                new MonsterAttributes(20, 23, 45, 67),
                                List.of()
                        )
                ));

        final Monster monster = monstersService.getMonster("646bab5cecf584e1be02598a", "646bac8c1a74032c70fffe24", "646bac223b4804b87c0b8054").blockingFirst();

        assertNotNull(monster);
        assertEquals("2023-05-22T17:51:46.772Z", monster.createdAt());
        assertEquals("2023-05-22T17:51:46.772Z", monster.updatedAt());
        assertEquals("646bac223b4804b87c0b8054", monster._id());
        assertEquals("646bac8c1a74032c70fffe24", monster.trainer());
        assertEquals(1, monster.type());
        assertEquals(3, monster.level());
        assertEquals(56, monster.experience());
        assertEquals(abilities, monster.abilities());
        assertEquals(40, monster.attributes().health());
        assertEquals(23, monster.attributes().attack());
        assertEquals(45, monster.attributes().defense());
        assertEquals(67, monster.attributes().speed());
        assertEquals(20, monster.currentAttributes().health());

        verify(monstersApiService).getMonster("646bab5cecf584e1be02598a", "646bac8c1a74032c70fffe24", "646bac223b4804b87c0b8054");
    }
}
