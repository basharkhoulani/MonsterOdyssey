package de.uniks.stpmon.team_m.dto;

public record Message(
        String createdAt,
        String updatedAt,
        String _id,
        String sender,
        String body
) {
}
