package de.uniks.stpmon.team_m.dto;

import java.util.List;

public record UpdateUserDto(
        String name,
        String status,
        List<String> friends,
        String password
) {
}
