package de.uniks.stpmon.team_m.dto;

import java.util.List;

public record Chunk(
        List<Integer> data,
        int height,
        int width,
        int x,
        int y
) {
}
