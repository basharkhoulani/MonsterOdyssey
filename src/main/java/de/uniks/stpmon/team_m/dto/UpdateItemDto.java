package de.uniks.stpmon.team_m.dto;

public record UpdateItemDto(
        int amount,
        int type,
        String monster
) {
}
