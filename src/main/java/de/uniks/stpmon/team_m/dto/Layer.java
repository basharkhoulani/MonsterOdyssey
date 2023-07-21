package de.uniks.stpmon.team_m.dto;

import java.util.List;

public record Layer(
        List<Chunk> chunks,
        List<Long> data,
        int height,
        int id,
        String name,
        int opacity,
        int startx,
        int starty,
        String type,
        boolean visible,
        int width,
        int x,
        int y,
        String draworder,
        List<TileObject> objects
) {
}
