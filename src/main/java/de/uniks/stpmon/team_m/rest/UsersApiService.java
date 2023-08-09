package de.uniks.stpmon.team_m.rest;

import de.uniks.stpmon.team_m.dto.CreateUserDto;
import de.uniks.stpmon.team_m.dto.UpdateUserDto;
import de.uniks.stpmon.team_m.dto.User;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.*;

import java.util.List;

public interface UsersApiService {
    @POST("users")
    Observable<User> createUser(@Body CreateUserDto ignoredDto);

    // For all users pass null for both parameters. Also only one parameter can be null.
    @GET("users")
    Observable<List<User>> getUsers(@Query("ids") List<String> ignoredIds, @Query("status") String ignoredStatus);

    @GET("users/{id}")
    Observable<User> getUser(@Path("id") String id);

    @PATCH("users/{id}")
    Observable<User> updateUser(@Path("id") String ignoredId, @Body UpdateUserDto dto);

    @DELETE("users/{id}")
    Observable<User> deleteUser(@Path("id") String ignoredId);
}
