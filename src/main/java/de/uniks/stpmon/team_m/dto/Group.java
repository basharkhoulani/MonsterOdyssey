package de.uniks.stpmon.team_m.dto;

import java.util.List;

public record Group(
        String _id,
        String name,
        List<String> members
) {
    public String membersToString() {
        return String.join(",", members);
    }
}
