package de.uniks.stpmon.team_m.dto;

public record AbilityDto(
        int id,
        String name,
        String description,
        String type,
        int maxUses,
        double accuracy,
        int power
) {
}
