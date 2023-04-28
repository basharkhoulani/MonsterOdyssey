package de.uniks.stpmon.team_m.dto;

import java.util.List;

public record CreateGroupDto(
        String name,
        List<String> members
) {
}
