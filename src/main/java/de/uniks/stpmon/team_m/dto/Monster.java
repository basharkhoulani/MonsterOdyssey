package de.uniks.stpmon.team_m.dto;

import java.util.LinkedHashMap;
import java.util.List;

public record Monster(
        String createdAt,
        String updatedAt,
        String _id,
        String trainer,
        int type,
        int level,
        int experience,
        LinkedHashMap<String, Integer> abilities,
        MonsterAttributes attributes,
        MonsterAttributes currentAttributes,
        List<String> status
) {
}
