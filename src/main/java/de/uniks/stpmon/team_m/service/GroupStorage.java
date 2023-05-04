package de.uniks.stpmon.team_m.service;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GroupStorage {
    private String _id;

    @Inject
    public GroupStorage() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
