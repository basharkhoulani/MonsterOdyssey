package de.uniks.stpmon.team_m.dto;

import java.util.List;

public record User(
        String _id,
        String name,
        String status,
        String avatar,
        List<String> friends
) {
}
