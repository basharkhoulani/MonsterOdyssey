package de.uniks.stpmon.team_m.dto;

import org.mapeditor.core.Map;

public record Area(
        String createdAt,
        String updatedAt,
        String _id,
        String region,
        String name,
        Map map
) {
}
