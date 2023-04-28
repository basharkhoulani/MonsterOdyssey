package de.uniks.stpmon.team_m.dto;

public record UpdateUserDto(
        String name,
        String status,
        String[] friends,
        String password
) {
}
