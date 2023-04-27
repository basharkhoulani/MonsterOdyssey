package de.uniks.stpmon.team_m.rest;

import de.uniks.stpmon.team_m.dto.LoginDto;
import de.uniks.stpmon.team_m.dto.LoginResult;
import de.uniks.stpmon.team_m.dto.LogoutResult;
import de.uniks.stpmon.team_m.dto.RefreshDto;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApiService {
    @POST("auth/login")
    Observable<LoginResult> login(@Body LoginDto dto);

    @POST("auth/refresh")
    Observable<LoginResult> refresh(@Body RefreshDto dto);

    @POST("auth/logout")
    Observable<LogoutResult> logout();
}
