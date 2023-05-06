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

    @Inject
    public GroupService(GroupsApiService groupsApiService) {
        this.groupsApiService = groupsApiService;
    }

    public Observable<Group> create(String name, List<String> members) {
        return groupsApiService.create(new CreateGroupDto(name, members));
    }

    // For all groups the current user is member of, pass null as the parameter.
    public Observable<List<Group>> getGroups(List<String> _ids) {
        return groupsApiService.getGroups(_ids);
    }

    public Observable<Group> getGroup(String _id) {
        return groupsApiService.getGroup(_id);
    }

    public Observable<Group> update(String _id, String name, List<String> members) {
        return groupsApiService.update(_id, new UpdateGroupDto(name, members));
    }

    public Observable<Group> delete(String _id) {
        return groupsApiService.delete(_id);
    }

}
