package de.uniks.stpmon.team_m.rest;

import de.uniks.stpmon.team_m.dto.*;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.*;

import java.util.List;

public interface GroupsApiService {
    @POST("groups")
    Observable<Group> create(@Body CreateGroupDto dto);

    // For all groups the current user is member of, pass null as the parameter.
    @GET("groups")
    Observable<Group[]> getGroups(@Query("ids") List<String> ids);

    @GET("groups/{id}")
    Observable<Group> getUser(@Path("id") String id);

    @PATCH("groups/{id}")
    Observable<Group> update(@Body UpdateGroupDto dto);

    @DELETE("groups/{id}")
    Observable<Group> delete(@Path("id") String id);
}
