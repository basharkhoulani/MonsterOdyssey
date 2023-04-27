package de.uniks.stpmon.team_m.dto;

public record User(
        String _id,
        String name,
        String status,
        String[] friends
) {
}
