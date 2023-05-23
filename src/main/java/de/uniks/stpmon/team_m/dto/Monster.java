package de.uniks.stpmon.team_m.dto;

import java.util.SortedMap;

public record Monster(
        String createdAt,
        String updatedAt,
        String _id,
        String trainer,
        int type,
        int level,
        int experience,
        SortedMap<String, Integer> abilities,
        MonsterAttributes attributes,
        MonsterAttributes currentAttributes
) {
}
