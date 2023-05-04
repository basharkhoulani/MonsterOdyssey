package de.uniks.stpmon.team_m.dto;

import java.util.List;

public record LoginResult(
        String _id,
        String name,
        String status,
        String avatar,
        List<String> friends,
        String accessToken,
        String refreshToken
) {
}
