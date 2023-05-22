package de.uniks.stpmon.team_m.dto;

import com.fasterxml.jackson.databind.JsonNode;

public record Area(
        String createdAt,
        String updatedAt,
        String _id,
        String region,
        String name
) {
}
