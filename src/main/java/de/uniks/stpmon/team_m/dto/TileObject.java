package de.uniks.stpmon.team_m.dto;

import java.util.List;

public record TileObject(
        int id,
        int x,
        int y,
        int width,
        int height,
        int rotation,
        String type,
        String name,
        boolean visible,
        List<TileProperty> properties
) {
}
