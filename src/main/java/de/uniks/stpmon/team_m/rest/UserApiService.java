package de.uniks.stpmon.team_m.rest;

import de.uniks.stpmon.team_m.dto.CreateUserDto;
import de.uniks.stpmon.team_m.dto.User;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApiService {
    @POST("users")
    Observable<User> create(@Body CreateUserDto dto);
}
