package de.uniks.stpmon.team_m.dto;

public record Encounter(
        String createdAt,
        String updateAt,
        String _id,
        String region,
        Boolean isWild
) {
}
