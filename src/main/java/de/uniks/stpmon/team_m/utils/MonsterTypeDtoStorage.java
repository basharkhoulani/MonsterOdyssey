package de.uniks.stpmon.team_m.utils;

import de.uniks.stpmon.team_m.dto.MonsterTypeDto;

import javax.inject.Singleton;
import java.awt.*;
import java.util.HashMap;
import java.util.List;

@Singleton
public class MonsterTypeDtoStorage {
    private HashMap<Integer, MonsterTypeDto> monsterTypeDtoHashMap;
    private HashMap<Integer, Image> monsterImageHashMap;

    /**
     * Function to determine whether HashMaps have been already filled with data.
     * @return true if HashMaps are empty, else false
     */
    public boolean isInitial() {
        return monsterTypeDtoHashMap.isEmpty();
    }

    public void initMonsterTypeDtoHashMap(List<MonsterTypeDto> monsterTypeDtoList) {
        for (MonsterTypeDto monsterTypeDto : monsterTypeDtoList) {
            monsterTypeDtoHashMap.put(monsterTypeDto.id(), monsterTypeDto);
        }
    }

    public void initMonsterImageHashMap(HashMap<Integer, Image> monsterImageHashMap) {
        this.monsterImageHashMap = monsterImageHashMap;
    }

    public MonsterTypeDto getMonsterTypeDto (int id) {
        return monsterTypeDtoHashMap.get(id);
    }

    public Image getMonsterImage (int id) {
        return monsterImageHashMap.get(id);
    }
}
