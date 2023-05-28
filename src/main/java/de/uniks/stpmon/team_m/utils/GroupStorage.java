package de.uniks.stpmon.team_m.utils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class GroupStorage {
    private String _id;
    private String name;
    private List<String> members;

    /**
     * GroupStorage handles the storage of groups that need to be used.
     */

    @Inject
    public GroupStorage() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMembers() {
        return this.members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

}
