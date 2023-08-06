package de.uniks.stpmon.team_m.utils;

import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.dto.MonsterTypeDto;
import io.reactivex.rxjava3.annotations.NonNull;
import javafx.scene.image.Image;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@Singleton
public class MonsterStorage {
    List<MonsterData> monsterDataList;
    private List<MonsterTypeDto> monsterTypeDtoList;
    private HashMap<Integer, MonsterTypeDto> monsterTypeDtoHashMap;
    private HashMap<Integer, Image> monsterImageHashMap;

    @Inject
    public MonsterStorage() {
        monsterDataList = new ArrayList<>();
        monsterTypeDtoList = new ArrayList<>();
        monsterTypeDtoHashMap = new HashMap<>();
        monsterImageHashMap = new HashMap<>();
    }

    public List<MonsterData> getMonsterDataList() {
        return monsterDataList;
    }

    public void addMonsterData(Monster monster, MonsterTypeDto monsterTypeDto, Image monsterImage) {
        if (monsterDataList.stream().anyMatch(m -> m.getMonster()._id().equals(monster._id()))) {
            return;
        }
        monsterDataList.add(new MonsterData(monster, monsterTypeDto, monsterImage));
    }

    public void addMonsterTypeDtoLists(List<MonsterTypeDto> monsterTypeDtoList) {
        monsterTypeDtoList.sort(Comparator.comparingInt(MonsterTypeDto::id));
        this.monsterTypeDtoList = monsterTypeDtoList;

        for (MonsterTypeDto monsterTypeDto : monsterTypeDtoList) {
            monsterTypeDtoHashMap.put(monsterTypeDto.id(), monsterTypeDto);
        }
    }

    public void addMonsterImageToHashMap(int id, Image monsterImage) {
        monsterImageHashMap.put(id, monsterImage);

    }

    // null as param means that the value should not be updated
    public void updateMonsterData(@NonNull Monster monster, MonsterTypeDto monsterTypeDto, Image monsterImage) {
        MonsterData oldMonsterData = monsterDataList.stream().filter(m -> m.getMonster()._id().equals(monster._id())).findFirst().orElse(null);
        if (oldMonsterData == null) {
            return;
        }
        monsterDataList.set(monsterDataList.indexOf(oldMonsterData), new MonsterData(
                monster,
                monsterTypeDto == null ? oldMonsterData.getMonsterTypeDto() : monsterTypeDto,
                monsterImage == null ? oldMonsterData.getMonsterImage() : monsterImage
        ));
    }

    public MonsterData getMonsterData(String monsterId) {
        return monsterDataList.stream().filter(m -> m.getMonster()._id().equals(monsterId)).findFirst().orElse(null);
    }

    public MonsterTypeDto getMonsterTypeDto (int id) {
        return monsterTypeDtoHashMap.get(id);
    }

    public Image getMonsterImage (int id) {
        return monsterImageHashMap.get(id);
    }

    public List<MonsterTypeDto> getMonsterTypeDtoList () {
        return monsterTypeDtoList;
    }
}
