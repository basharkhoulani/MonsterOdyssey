package de.uniks.stpmon.team_m.service;

import de.uniks.stpmon.team_m.Constants;
import de.uniks.stpmon.team_m.dto.LoginDto;
import de.uniks.stpmon.team_m.dto.LoginResult;
import de.uniks.stpmon.team_m.dto.RefreshDto;
import de.uniks.stpmon.team_m.rest.AuthApiService;
import de.uniks.stpmon.team_m.utils.TokenStorage;
import de.uniks.stpmon.team_m.utils.UserStorage;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import java.util.prefs.Preferences;

public class AuthenticationService {
    private final TokenStorage tokenStorage;
    private final AuthApiService authApiService;
    private final Preferences preferences;
    private final UserStorage userStorage;

    /**
     * AuthenticationService handles the communication with the backend for the authentication.
     */

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

    /**
     * login logs in a user. If the user wants to be remembered, the refresh token is stored in the preferences.
     *
     * @param username   The username of the user.
     * @param password   The password of the user.
     * @param rememberMe Whether the user wants to be remembered.
     * @return The login result.
     */

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

    /**
     * isRememberMe checks whether the user wants to be remembered.
     *
     * @return Whether the user wants to be remembered.
     */

    public boolean isRememberMe() {
        return preferences.get(Constants.REFRESH_TOKEN_PREF, null) != null;
    }

    /**
     * Logs in a user with the refresh token.
     *
     * @return The login result.
     */

    public Observable<LoginResult> refresh() {
        return authApiService.refresh(new RefreshDto(preferences.get(Constants.REFRESH_TOKEN_PREF, null))).map(lr -> {
            userStorage.setUser(lr);
            tokenStorage.setToken(lr.accessToken());
            return lr;
        });
    }

    public Observable<LoginResult> stayOnline() {
        return authApiService.refresh(new RefreshDto(userStorage.getRefreshToken())).map(lr -> {
            userStorage.setUser(lr);
            tokenStorage.setToken(lr.accessToken());
            return lr;
        });
    }

    /**
     * logout logs out a user.
     *
     * @return A string that says that the logout was successful.
     */

    public Observable<String> logout() {
        return authApiService.logout().map(lr -> "LogoutSuccess");
    }
}
