package de.uniks.stpmon.team_m.dto;

public record ItemTypeDto(
        int id,
        String image,
        String name,
        int price,
        String description,
        String use
) {
}
