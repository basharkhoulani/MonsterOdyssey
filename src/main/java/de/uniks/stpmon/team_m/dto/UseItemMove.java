package de.uniks.stpmon.team_m.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("use-item")
public record UseItemMove(
        String type,
        int item,
        String target
) implements Move{
}
