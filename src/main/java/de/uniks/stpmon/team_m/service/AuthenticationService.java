package de.uniks.stpmon.team_m.service;

import de.uniks.stpmon.team_m.Constants;
import de.uniks.stpmon.team_m.dto.LoginDto;
import de.uniks.stpmon.team_m.dto.LoginResult;
import de.uniks.stpmon.team_m.dto.LogoutResult;
import de.uniks.stpmon.team_m.dto.RefreshDto;
import de.uniks.stpmon.team_m.rest.AuthApiService;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import java.util.prefs.Preferences;

import static de.uniks.stpmon.team_m.Constants.*;

public class AuthenticationService {
    private final TokenStorage tokenStorage;
    private final AuthApiService authApiService;
    private final Preferences preferences;
    private final UserStorage userStorage;

    @Inject
    public AuthenticationService(
            TokenStorage tokenStorage,
            AuthApiService authApiService,
            Preferences preferences,
            UserStorage userStorage
    ) {
        this.tokenStorage = tokenStorage;
        this.authApiService = authApiService;
        this.preferences = preferences;
        this.userStorage = userStorage;
    }

    public Observable<LoginResult> login(String username, String password, boolean rememberMe) {
        return authApiService.login(new LoginDto(username, password)).map(lr -> {
            tokenStorage.setToken(lr.accessToken());
            userStorage.setUser(lr);
            if (rememberMe) {
                preferences.put(Constants.REFRESH_TOKEN_PREF, lr.refreshToken());
            }
            return lr;
        });
    }

    public boolean isRememberMe() {
        return preferences.get(Constants.REFRESH_TOKEN_PREF, null) != null;
    }

    public Observable<LoginResult> refresh() {
        return  authApiService.refresh(new RefreshDto(preferences.get(Constants.REFRESH_TOKEN_PREF, null))).map(lr -> {
            tokenStorage.setToken(lr.accessToken());
            return lr;
        });
    }

    public Observable<LogoutResult> logout() {
        return authApiService.logout().map(lr -> lr);
    }

    public String errorHandler(String error, String function){
        if(error.contains(HTTP_400)){
            return VALIDATION_FAIL;
        } else if(error.contains(HTTP_401)){
            if(function.equals(LOGIN_FUNC)){
                return "Invalid username or password";
            } else if (function.equals(REFRESH_FUNC)) {
                return "Invalid or expired refresh token";
            } else if (function.equals(LOGOUT_FUNC)){
                return "Missing or invalid Bearer token";
            }
            return CUSTOM_ERROR;
        } else if (error.contains(HTTP_429)) {
            return RATE_LIMIT;
        }
        return Constants.CUSTOM_ERROR;
    }

}
