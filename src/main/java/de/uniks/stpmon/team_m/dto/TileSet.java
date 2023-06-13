package de.uniks.stpmon.team_m.dto;

import java.util.List;

public record TileSet(
        int columns,
        String image,
        int imageheight,
        int imagewidth,
        int margin,
        String name,
        int spacing,
        int tilecount,
        int tileheight,
        int tilewidth,
        List<TileObject> tiles,
        int firstgid,
        String source
) {
}
