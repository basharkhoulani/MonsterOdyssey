package de.uniks.stpmon.team_m.dto;

import java.util.List;

public record Layer(
        List<Chunk> chunks,
        int height,
        int id,
        String name,
        int startx,
        int starty,
        String type,
        int width
) {
}
