package de.uniks.stpmon.team_m.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("change-Monster")
public record ChangeMonsterMove(
        String type,
        String monster
) implements Move{
}
