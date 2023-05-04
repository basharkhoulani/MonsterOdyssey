package de.uniks.stpmon.team_m.dto;

public record CreateUserDto(
        String name,
        String avatar,
        String password
) {
}
