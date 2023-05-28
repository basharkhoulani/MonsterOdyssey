package de.uniks.stpmon.team_m.dto;

import netscape.javascript.JSObject;

import javax.json.JsonObject;

public record Region(
        String _id,
        String name,
        String createdAt,
        String updatedAt,
        JsonObject map,
        Spawn spawn
) {
}
