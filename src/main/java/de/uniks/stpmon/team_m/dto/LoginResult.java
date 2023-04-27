package de.uniks.stpmon.team_m.dto;

public record LoginResult(
        String name,
        String status,
        String[] friends,
        String accessToken,
        String refreshToken
) {
}
