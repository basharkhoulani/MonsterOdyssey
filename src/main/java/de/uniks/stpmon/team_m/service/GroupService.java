package de.uniks.stpmon.team_m.service;

import de.uniks.stpmon.team_m.dto.CreateGroupDto;
import de.uniks.stpmon.team_m.dto.Group;
import de.uniks.stpmon.team_m.dto.UpdateGroupDto;
import de.uniks.stpmon.team_m.rest.GroupsApiService;
import de.uniks.stpmon.team_m.utils.GroupStorage;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import java.util.List;

public class GroupService {

    private final GroupsApiService groupsApiService;
    private final GroupStorage groupStorage;

    /**
     * GroupService handles the communication with the backend for the groups.
     */

    @Inject
    public GroupService(GroupsApiService groupsApiService, GroupStorage groupStorage) {
        this.groupsApiService = groupsApiService;
        this.groupStorage = groupStorage;
    }

    /**
     * create creates a new group.
     *
     * @param name    The name of the group.
     * @param members The members of the group.
     * @return The created group.
     */

    public Observable<Group> create(String name, List<String> members) {
        return groupsApiService.create(new CreateGroupDto(name, members)).map(group -> {
            groupStorage.set_id(group._id());
            groupStorage.setName(group.name());
            groupStorage.setMembers(group.members());
            return group;
        });
    }

    /**
     * getGroups returns groups of the current user. If _ids is null, all groups are returned.
     * If _ids is not null, only the groups with the ids exclusively are returned.
     *
     * @param _ids The ids of the groups.
     * @return A list of groups.
     */

    public Observable<List<Group>> getGroups(String _ids) {
        return groupsApiService.getGroups(_ids);
    }

    /**
     * getGroup returns a specific group.
     *
     * @param _id The id of the group.
     * @return The group.
     */

    public Observable<Group> getGroup(String _id) {
        return groupsApiService.getGroup(_id);
    }

    /**
     * update updates a group.
     *
     * @param _id     The id of the group.
     * @param name    The name of the group.
     * @param members The members of the group.
     * @return The updated group.
     */

    public Observable<Group> update(String _id, String name, List<String> members) {
        return groupsApiService.update(_id, new UpdateGroupDto(name, members)).map(group -> {
            groupStorage.set_id(group._id());
            groupStorage.setName(group.name());
            groupStorage.setMembers(group.members());
            return group;
        });
    }

    /**
     * delete deletes a group.
     *
     * @param _id The id of the group.
     * @return The deleted group.
     */

    public Observable<Group> delete(String _id) {
        return groupsApiService.delete(_id).map(group -> {
            groupStorage.set_id(null);
            groupStorage.setName(null);
            groupStorage.setMembers(null);
            return group;
        });
    }

}
