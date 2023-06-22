package de.uniks.stpmon.team_m.dto;

public record Opponent(
        String createdAt,
        String updatedAt,
        String _id,
        String encounter,
        String trainer,
        boolean isAttacker,
        boolean isNPC,
        String monster,
        Move move,
        Result result,
        int coin
) {
}
