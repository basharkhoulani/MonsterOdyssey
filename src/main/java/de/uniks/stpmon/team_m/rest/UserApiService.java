package de.uniks.stpmon.team_m.rest;

import de.uniks.stpmon.team_m.dto.CreateUserDto;
import de.uniks.stpmon.team_m.dto.User;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.*;

public interface UserApiService {
    @POST("users")
    Observable<User> create(@Body CreateUserDto dto);

    // For all users pass null for both parameters. Also only one parameter can be null.
    @GET("users")
    Observable<User[]> getUsers(@Query("ids") String[] ids, @Query("status") String status);
    @GET("users/{id}")
    Observable<User> getUser(@Path("id") String id);



}
