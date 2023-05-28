package de.uniks.stpmon.team_m.dto;

import java.util.List;

public record MonsterTypeDto(
        int id,
        String name,
        String image,
        List<String> type,
        String description
) {
}
