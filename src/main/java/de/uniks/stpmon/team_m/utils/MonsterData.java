package de.uniks.stpmon.team_m.utils;

import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.dto.MonsterTypeDto;
import javafx.scene.image.Image;

public class MonsterData {
    private final Monster monster;
    private final MonsterTypeDto monsterTypeDto;
    private final Image monsterImage;

    public MonsterData(Monster monster, MonsterTypeDto monsterTypeDto, Image monsterImage) {
        this.monster = monster;
        this.monsterTypeDto = monsterTypeDto;
        this.monsterImage = monsterImage;
    }

    public Monster getMonster() {
        return monster;
    }

    public MonsterTypeDto getMonsterTypeDto() {
        return monsterTypeDto;
    }

    public Image getMonsterImage() {
        return monsterImage;
    }
}

