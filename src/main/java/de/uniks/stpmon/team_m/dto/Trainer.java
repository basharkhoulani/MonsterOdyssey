package de.uniks.stpmon.team_m.dto;

import java.util.List;

public record Trainer(
        String createdAt,
        String updatedAt,
        String _id,
        String region,
        String user,
        String name,
        String image,
        int coins,
        List<String> team,
        List<Integer> encounteredMonsterTypes,
        String area,
        int x,
        int y,
        int direction,
        NPCInfo npc
) {
}
