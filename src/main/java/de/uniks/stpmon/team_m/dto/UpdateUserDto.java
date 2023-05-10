package de.uniks.stpmon.team_m.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UpdateUserDto(
        String name,
        String status,
        String avatar,
        List<String> friends,
        String password
) {
}
