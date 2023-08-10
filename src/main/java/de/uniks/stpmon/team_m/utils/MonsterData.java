package de.uniks.stpmon.team_m.utils;

import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.dto.MonsterTypeDto;
import javafx.scene.image.Image;

public record MonsterData(Monster monster, MonsterTypeDto monsterTypeDto, Image monsterImage) {
}

