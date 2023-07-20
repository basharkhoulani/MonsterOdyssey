package de.uniks.stpmon.team_m.dto;

import java.util.List;

public record NPCInfo(
        boolean walkRandomly,
        boolean encounterOnSight,
        boolean encounterOnTalk,
        boolean canHeal,
        List<Integer> sells,
        List<String> encountered,
        List<String> starters
) {
}
