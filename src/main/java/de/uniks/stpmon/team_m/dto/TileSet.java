package de.uniks.stpmon.team_m.dto;

import java.util.List;

public record TileSet(
        int columns,
        int firstgid,
        String image,
        int imageheight,
        int imagewidth,
        int margin,
        List<TileProperty> properties,
        String source,
        String name,
        String type,
        int spacing,
        int tilecount,
        int tileheight
) {
}
