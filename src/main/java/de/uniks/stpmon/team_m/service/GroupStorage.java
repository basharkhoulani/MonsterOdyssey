package de.uniks.stpmon.team_m.service;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class GroupStorage {
    private String _id;
    private String name;
    private List<String> members;

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

    public void addMember(String newMember) {
        if (!this.members.contains(newMember)) {
            this.members.add(newMember);
        }
    }

    public void removeMember(String member) {
        this.members.remove(member);
    }

    public void clearMembers() {
        this.members.clear();
    }

    public boolean hasMember(String member) {
        return this.members.contains(member);
    }

    public boolean hasMembers() {
        return !this.members.isEmpty();
    }

    public boolean hasNoMembers() {
        return this.members.isEmpty();
    }

}
