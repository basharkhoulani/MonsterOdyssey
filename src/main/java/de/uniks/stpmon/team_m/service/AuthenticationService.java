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

public class AuthenticationService {
    private final TokenStorage tokenStorage;
    private final AuthApiService authApiService;
    private final Preferences preferences;

    @Inject
    public AuthenticationService(
            TokenStorage tokenStorage,
            AuthApiService authApiService,
            Preferences preferences
    ) {
        this.tokenStorage = tokenStorage;
        this.authApiService = authApiService;
        this.preferences = preferences;
    }

    public Observable<LoginResult> login(String username, String password, boolean rememberMe) {
        return authApiService.login(new LoginDto(username, password)).map(lr -> {
            tokenStorage.setToken(lr.accessToken());
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

    // TODO: Remember me (refresh)
}
