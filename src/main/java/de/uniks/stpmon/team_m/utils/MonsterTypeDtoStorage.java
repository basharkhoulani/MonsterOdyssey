package de.uniks.stpmon.team_m.utils;

import de.uniks.stpmon.team_m.dto.MonsterTypeDto;
import javafx.scene.image.Image;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@Singleton
public class MonsterTypeDtoStorage {
    private List<MonsterTypeDto> monsterTypeDtoList;
    private final HashMap<Integer, MonsterTypeDto> monsterTypeDtoHashMap;
    private HashMap<Integer, Image> monsterImageHashMap;

    @Inject
    public MonsterTypeDtoStorage() {
        monsterTypeDtoHashMap = new HashMap<>();
        monsterImageHashMap = new HashMap<>();
    }

    /**
     * Function to determine whether HashMaps have been already filled with data.
     * @return true if HashMaps are empty, else false
     */
    public boolean isInitial() {
        return monsterTypeDtoHashMap.isEmpty();
    }

    public void initMonsterTypeDtoHashMap(List<MonsterTypeDto> monsterTypeDtoList) {
        this.monsterTypeDtoList = monsterTypeDtoList;
        this.monsterTypeDtoList.sort(Comparator.comparingInt(MonsterTypeDto::id));

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

    public List<MonsterTypeDto> getMonsterTypeDtoList () {
        monsterTypeDtoList.sort(Comparator.comparingInt(MonsterTypeDto::id));
        return monsterTypeDtoList;
    }
}
