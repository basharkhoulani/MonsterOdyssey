package de.uniks.stpmon.team_m.dto;

import java.util.List;

public record Map(
        int height,
        int width,
        List<Layer> layers,
        int tileheight,
        int tilewidth,
        List<TileSet> tilesets,
        List<TileProperty> properties
) {
}
