package de.uniks.stpmon.team_m.dto;

public record Trainer(
        String createdAt,
        String updatedAt,
        String _id,
        String user,
        String name,
        String image,
        int coins,
        String area,
        int x,
        int y,
        int direction,
        NPCInfo npc
) {
}
