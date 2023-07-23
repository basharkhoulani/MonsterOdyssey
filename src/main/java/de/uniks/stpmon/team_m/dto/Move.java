package de.uniks.stpmon.team_m.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = AbilityMove.class, name = "ability"),
        @JsonSubTypes.Type(value = ChangeMonsterMove.class, name = "change-monster"),
        @JsonSubTypes.Type(value = UseItemMove.class, name = "item")
})
public interface Move {
}
