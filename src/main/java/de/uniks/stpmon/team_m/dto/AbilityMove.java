package de.uniks.stpmon.team_m.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("ability")
public record AbilityMove(
      String type,
      int ability,
      String target
) implements Move{
}
