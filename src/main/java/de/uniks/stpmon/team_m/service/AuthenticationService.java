package de.uniks.stpmon.team_m.service;

import de.uniks.stpmon.team_m.dto.LoginDto;
import de.uniks.stpmon.team_m.dto.LoginResult;
import de.uniks.stpmon.team_m.dto.LogoutResult;
import de.uniks.stpmon.team_m.rest.AuthApiService;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;

public class AuthenticationService {
    private final TokenStorage tokenStorage;
    private final AuthApiService authApiService;

    @Inject
    public AuthenticationService(
        TokenStorage tokenStorage,
        AuthApiService authApiService
    ) {
        this.tokenStorage = tokenStorage;
        this.authApiService = authApiService;
    }

    public Observable<LoginResult> login(String username, String password){
        return authApiService.login(new LoginDto(username, password)).map(lr -> {
            tokenStorage.setToken(lr.accessToken());
            return lr;
        });
    }

    public Observable<LogoutResult> logout(){
        return authApiService.logout().map(lr -> lr);
    }

    //TODO: Remember me (refresh)
}
