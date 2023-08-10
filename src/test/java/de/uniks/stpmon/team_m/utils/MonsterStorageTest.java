package de.uniks.stpmon.team_m.utils;

import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.dto.MonsterAttributes;
import de.uniks.stpmon.team_m.dto.MonsterTypeDto;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MonsterStorageTest {
    final MonsterStorage monsterStorage = new MonsterStorage();

    @Test
    public void testMonsterStorage() {
        LinkedHashMap<String, Integer> abilities = new LinkedHashMap<>();
        abilities.put("1", 35);
        abilities.put("3", 20);
        abilities.put("6", 25);
        abilities.put("7", 15);
        Monster monster = new Monster("2023-06-05T17:02:40.357Z",
                "023-06-05T17:02:40.357Z",
                "647e1530866ace3595866db2",
                "647e15308c1bb6a91fb57321",
                1,
                1,
                0,
                abilities,
                new MonsterAttributes(14, 8, 8, 5),
                new MonsterAttributes(14, 8, 8, 5),
                List.of("poisoned")
        );
        MonsterTypeDto monsterTypeDto = new MonsterTypeDto(
                1,
                "Flamander",
                "Flamander_1.png",
                List.of("fire"),
                "Flamander is a small, agile monster that lives in the hot deserts of the world."
        );
        MonsterTypeDto monsterTypeDto2 = new MonsterTypeDto(
                2,
                "Flamander",
                "Flamander_1.png",
                List.of("fire"),
                "Flamander is a small, agile monster that lives in the hot deserts of the world."
        );
        assertEquals(monsterStorage.getMonsterDataList().size(), 0);
        monsterStorage.updateMonsterData(monster, monsterTypeDto, null);
        assertEquals(monsterStorage.getMonsterDataList().size(), 0);

        monsterStorage.addMonsterData(monster, monsterTypeDto, null);
        assertEquals(monsterTypeDto, monsterStorage.getMonsterData(monster._id()).monsterTypeDto());
        assertNull(monsterStorage.getMonsterData(monster._id()).monsterImage());

        monsterStorage.addMonsterData(monster, monsterTypeDto, null);
        assertEquals(monsterStorage.getMonsterDataList().size(), 1);

        monsterStorage.updateMonsterData(monster, monsterTypeDto2, null);
        assertEquals(monsterStorage.getMonsterDataList().stream().filter(monsterData -> monsterData.monster().equals(monster)).toList().get(0).monsterTypeDto(), monsterTypeDto2);
        monsterStorage.updateMonsterData(monster, null, null);
        assertEquals(monsterStorage.getMonsterDataList().stream().filter(monsterData -> monsterData.monster().equals(monster)).toList().get(0).monsterTypeDto(), monsterTypeDto2);
    }
}
