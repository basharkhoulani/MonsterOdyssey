package de.uniks.stpmon.team_m.dto;

import netscape.javascript.JSObject;

import javax.json.JsonObject;

public record Region(
        String createdAt,
        String updatedAt,
        String _id,
        String name,
        Spawn spawn,
        Object map
) {
}
