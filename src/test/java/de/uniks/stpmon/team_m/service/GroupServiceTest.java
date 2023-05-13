package de.uniks.stpmon.team_m.service;

import de.uniks.stpmon.team_m.dto.CreateGroupDto;
import de.uniks.stpmon.team_m.dto.Group;
import de.uniks.stpmon.team_m.dto.UpdateGroupDto;
import de.uniks.stpmon.team_m.rest.GroupsApiService;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GroupServiceTest {

    @Spy
    GroupStorage groupStorage;
    @Mock
    GroupsApiService groupsApiService;
    @InjectMocks
    GroupService groupService;

    @Test
    void createGroup() {
        when(groupsApiService.create(any())).thenReturn(Observable.just(
                new Group("645e7a140cd1c6fd58b6bc43", "CoolGroup",
                        List.of("645e7a2e0fbb32624ab2449b", "645e7a36f0b8cee356d87ecc"))));

        final Group group = groupService.create("CoolGroup", List.of("645e7a2e0fbb32624ab2449b",
                "645e7a36f0b8cee356d87ecc")).blockingFirst();

        assertNotNull(group);
        assertEquals(group.name(), groupStorage.getName());
        assertEquals(group.members(), groupStorage.getMembers());
        assertEquals(group._id(), groupStorage.get_id());

        verify(groupsApiService).create(new CreateGroupDto("CoolGroup",
                List.of("645e7a2e0fbb32624ab2449b", "645e7a36f0b8cee356d87ecc")));
    }

    @Test
    void getGroup() {
        when(groupsApiService.getGroup(any())).thenReturn(Observable.just(
                new Group("645e7a140cd1c6fd58b6bc43", "CoolGroup",
                        List.of("645e7a2e0fbb32624ab2449b", "645e7a36f0b8cee356d87ecc"))));

        final Group group = groupService.getGroup("645e7a140cd1c6fd58b6bc43").blockingFirst();

        assertNotNull(group);
        assertEquals(group.name(), "CoolGroup");
        assertEquals(group.members(), List.of("645e7a2e0fbb32624ab2449b", "645e7a36f0b8cee356d87ecc"));
        assertEquals(group._id(), "645e7a140cd1c6fd58b6bc43");

        verify(groupsApiService).getGroup("645e7a140cd1c6fd58b6bc43");
    }

    @Test
    void getGroups() {
        when(groupsApiService.getGroups(any())).thenReturn(Observable.just(List.of(
                new Group("645e7a140cd1c6fd58b6bc43", "CoolGroup",
                        List.of("645e7a2e0fbb32624ab2449b", "645e7a36f0b8cee356d87ecc")),
                new Group("645e7ba9cc22fc2c695933cb", "CoolerGroup",
                        List.of("645e7bb58eeea04ccc5f0b5c", "645e7bb7b8be26e7be3162b0")))));

        final List<Group> groups = groupService.
                getGroups(List.of("645e7a140cd1c6fd58b6bc43", "645e7ba9cc22fc2c695933cb")).blockingFirst();
        assertNotNull(groups);
        verify(groupsApiService).getGroups(List.of("645e7a140cd1c6fd58b6bc43", "645e7ba9cc22fc2c695933cb"));
    }

    @Test
    void updateGroup() {
        when(groupsApiService.update(any(), any())).thenReturn(Observable.just(
                new Group("645e7a140cd1c6fd58b6bc43", "MostCoolGroup",
                        List.of("645e7a2e0fbb32624ab2449b", "645e7a36f0b8cee356d87ecc"))));

        final Group group = groupService.update("645e7a140cd1c6fd58b6bc43", "CoolGroup",
                List.of("645e7a2e0fbb32624ab2449b", "645e7a36f0b8cee356d87ecc")).blockingFirst();

        assertNotNull(group);
        assertEquals(group.name(), groupStorage.getName());
        assertEquals(group.members(), groupStorage.getMembers());
        assertEquals(group._id(), groupStorage.get_id());

        verify(groupsApiService).update("645e7a140cd1c6fd58b6bc43", new UpdateGroupDto("CoolGroup",
                List.of("645e7a2e0fbb32624ab2449b", "645e7a36f0b8cee356d87ecc")));
    }

    @Test
    void deleteGroup() {
        when(groupsApiService.delete(any())).thenReturn(Observable.just(
                new Group("645e7a140cd1c6fd58b6bc43", "CoolGroup",
                        List.of("645e7a2e0fbb32624ab2449b", "645e7a36f0b8cee356d87ecc"))));

        final Group group = groupService.delete("645e7a140cd1c6fd58b6bc43").blockingFirst();

        assertNotNull(group);
        assertNotNull(group.name());
        assertNotNull(group.members());
        assertNotNull(group._id());
        assertNull(groupStorage.getName());
        assertNull(groupStorage.getMembers());
        assertNull(groupStorage.get_id());

        verify(groupsApiService).delete("645e7a140cd1c6fd58b6bc43");
    }

}
