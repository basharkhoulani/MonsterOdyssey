package de.uniks.stpmon.team_m.dto;

import java.util.List;

public record UpdateTrainerDto(
        String name,
        String image,
        List<String> team,
        String area,
        List<String> settings
) {
}
