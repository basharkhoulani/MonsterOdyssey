package de.uniks.stpmon.team_m.dto;

public record Result(
        String type,
        String monster,
        Integer ability,
        String effectiveness,
        Integer item,
        String status
) {
}
