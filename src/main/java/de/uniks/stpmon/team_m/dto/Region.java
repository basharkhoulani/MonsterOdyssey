package de.uniks.stpmon.team_m.dto;

import org.mapeditor.core.Map;

public record Region(
        String createdAt,
        String updatedAt,
        String _id,
        String name,
        Spawn spawn,
        Map map
) {
}
