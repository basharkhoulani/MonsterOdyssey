package de.uniks.stpmon.team_m.service;

import de.uniks.stpmon.team_m.dto.CreateGroupDto;
import de.uniks.stpmon.team_m.dto.Group;
import de.uniks.stpmon.team_m.dto.UpdateGroupDto;
import de.uniks.stpmon.team_m.rest.GroupsApiService;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import java.util.List;

public class GroupService {

    private final GroupsApiService groupsApiService;
    private final GroupStorage groupStorage;

    @Inject
    public GroupService(GroupsApiService groupsApiService, GroupStorage groupStorage) {
        this.groupsApiService = groupsApiService;
        this.groupStorage = groupStorage;
    }

    public Observable<Group> create(String name, List<String> members) {
        return groupsApiService.create(new CreateGroupDto(name, members)).map(group -> {
            groupStorage.set_id(group._id());
            groupStorage.setName(group.name());
            groupStorage.setMembers(group.members());
            return group;
        });
    }

    // For all groups the current user is member of, pass null as the parameter.
    public Observable<List<Group>> getGroups(List<String> _ids) {
        return groupsApiService.getGroups(_ids);
    }

    public Observable<Group> getGroup(String _id) {
        return groupsApiService.getGroup(_id);
    }

    public Observable<Group> update(String _id, String name, List<String> members) {
        return groupsApiService.update(_id, new UpdateGroupDto(name, members)).map(group -> {
            groupStorage.set_id(group._id());
            groupStorage.setName(group.name());
            groupStorage.setMembers(group.members());
            return group;
        });
    }

    public Observable<Group> delete(String _id) {
        return groupsApiService.delete(_id).map(group -> {
            groupStorage.set_id(null);
            groupStorage.setName(null);
            groupStorage.setMembers(null);
            return group;
        });
    }

}
