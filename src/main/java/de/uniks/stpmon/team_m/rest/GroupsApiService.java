package de.uniks.stpmon.team_m.rest;

import de.uniks.stpmon.team_m.dto.CreateGroupDto;
import de.uniks.stpmon.team_m.dto.Group;
import de.uniks.stpmon.team_m.dto.UpdateGroupDto;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.*;

import java.util.List;

public interface GroupsApiService {
    @POST("groups")
    Observable<Group> create(@Body CreateGroupDto ignoredDto);

    // For all groups the current user is member of, pass null as the parameter.
    @GET("groups")
    Observable<List<Group>> getGroups(@Query("members") String ignoredMembers);

    @GET("groups/{id}")
    Observable<Group> getGroup(@Path("id") String ignoredId);

    @PATCH("groups/{id}")
    Observable<Group> update(@Path("id") String ignored_id, @Body UpdateGroupDto ignoredDto);

    @DELETE("groups/{id}")
    Observable<Group> delete(@Path("id") String ignored_id);
}
