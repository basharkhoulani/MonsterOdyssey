package de.uniks.stpmon.team_m.dto;

public record Region(
        String createdAt,
        String updatedAt,
        String _id,
        String name,
        Spawn spawn,
        Object map
) {
}
