package de.uniks.stpmon.team_m.dto;

import java.util.List;

public record ValidationErrorResponse(
        int statusCode,
        String error,
        List<String> message
) {
}
