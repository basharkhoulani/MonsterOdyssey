package de.uniks.stpmon.team_m.dto;

import java.util.List;

public record Map(
        int compressionlevel,
        boolean infinite,
        int nextlayerid,
        int nextobjectid,
        String orientation,
        String renderorder,
        String tiledversion,
        String type,
        String version,
        int height,
        int width,
        List<Layer> layers,
        int tileheight,
        int tilewidth,
        List<TileSet> tilesets,
        List<TileProperty> properties
) {
}
