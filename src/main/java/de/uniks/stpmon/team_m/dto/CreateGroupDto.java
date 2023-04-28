package de.uniks.stpmon.team_m.dto;

public record CreateGroupDto(
        String name,
        String[] members
) {
}
