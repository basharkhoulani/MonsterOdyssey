package de.uniks.stpmon.team_m.dto;

public record Chunk(
        int[] data,
        int height,
        int width,
        int x,
        int y
) {
}
