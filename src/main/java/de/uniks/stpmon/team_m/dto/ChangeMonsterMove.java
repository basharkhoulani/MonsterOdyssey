package de.uniks.stpmon.team_m.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("change-monster")
public record ChangeMonsterMove(
        String type,
        String monster
) implements Move{
}
