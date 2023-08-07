package de.uniks.stpmon.team_m.dto;

public record CreateTrainerDto(
        String name,
        String image,
        Settings settings
) {
}
