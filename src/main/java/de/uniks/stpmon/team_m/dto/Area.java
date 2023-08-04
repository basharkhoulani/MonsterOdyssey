package de.uniks.stpmon.team_m.dto;

import java.util.List;

public record Area(
        String createdAt,
        String updatedAt,
        String _id,
        String region,
        String name,
        Spawn spawn,
        Map map
) {
}
