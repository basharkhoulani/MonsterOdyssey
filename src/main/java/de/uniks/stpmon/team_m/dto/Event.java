package de.uniks.stpmon.team_m.dto;

public record Event<T>(
        String event,
        T data
) {
    public String suffix() {
        return event.substring(event.lastIndexOf('.') + 1);
    }
}
