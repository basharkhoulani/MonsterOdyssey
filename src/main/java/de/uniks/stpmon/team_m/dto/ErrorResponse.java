package de.uniks.stpmon.team_m.dto;

public record ErrorResponse(
        int statusCode,
        String error,
        String message
) {
}
