package de.uniks.stpmon.team_m.dto;

public record ChangeMonsterMove(
        String type,
        String monster
) implements Move{
}
