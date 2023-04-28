package de.uniks.stpmon.team_m.dto;

import java.util.List;

public record LoginResult(
        String _id,
        String name,
        String status,
        List<String> friends,
        String accessToken,
        String refreshToken
) {
}
