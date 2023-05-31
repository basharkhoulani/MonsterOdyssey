package de.uniks.stpmon.team_m.service;

import de.uniks.stpmon.team_m.dto.LoginDto;
import de.uniks.stpmon.team_m.dto.LoginResult;
import de.uniks.stpmon.team_m.dto.LogoutResult;
import de.uniks.stpmon.team_m.dto.RefreshDto;
import de.uniks.stpmon.team_m.rest.AuthApiService;
import de.uniks.stpmon.team_m.utils.TokenStorage;
import de.uniks.stpmon.team_m.utils.UserStorage;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.prefs.Preferences;

import static de.uniks.stpmon.team_m.Constants.REFRESH_TOKEN_PREF;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Spy
    TokenStorage tokenStorage;
    @Spy
    UserStorage userStorage;
    @Spy
    Preferences preferences;
    @Mock
    AuthApiService authApiService;

    @InjectMocks
    AuthenticationService authenticationService;

    @Test
    void login() {
        // Successful login of user

        //define mocks
        when(authApiService.login(any()))
                .thenReturn(Observable.just(new LoginResult("645ccafaaa5cd5e15e00f65f", "t", "online", null, null, "a1a2", "a3a4")));

        // Login of a User
        final LoginResult result = authenticationService.login("t", "12345678", false).blockingFirst();

        // Check for existing token
        assertEquals("a1a2", result.accessToken());
        assertEquals("a1a2", tokenStorage.getToken());

        // Check for successful login
        assertEquals("645ccafaaa5cd5e15e00f65f", result._id());
        assertEquals("645ccafaaa5cd5e15e00f65f", userStorage.get_id());

        //check mocks
        verify(authApiService).login(new LoginDto("t", "12345678"));

        // Service is not able to handle with Error, the error handling is at the
    }

    @Test
    void logout() {
        when(authApiService.logout()).thenReturn(Observable.just(new LogoutResult()));

        String string = authenticationService.logout().blockingFirst();

        assertEquals("LogoutSuccess", string);

        verify(authApiService).logout();
    }

    @Test
    void refresh() {
        preferences.put(REFRESH_TOKEN_PREF, "a1a2");
        RefreshDto refreshDto = new RefreshDto(preferences.get(REFRESH_TOKEN_PREF, null));
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        when(authApiService.refresh(refreshDto))
                .thenReturn(Observable.just(new LoginResult("645ccafaaa5cd5e15e00f65f", "t", "online", null, null, "a1a2", "a3a4")));
        final LoginResult result = authenticationService.refresh().blockingFirst();
        verify(preferences).put(eq(REFRESH_TOKEN_PREF), argumentCaptor.capture());
        assertEquals("a1a2", result.accessToken());
        assertEquals("a1a2", argumentCaptor.getValue());
        verify(authApiService).refresh(refreshDto);
    }

    @Test
    void loginRememberMe() {
        when(authApiService.login(any()))
                .thenReturn(Observable.just(new LoginResult("645ccafaaa5cd5e15e00f65f", "t", "online", null, null, "a1a2", "a3a4")));

        final LoginResult result = authenticationService.login("t", "12345678", true).blockingFirst();
        when(preferences.get(REFRESH_TOKEN_PREF, null)).thenReturn("a1a2");
        assertEquals("a1a2", result.accessToken());
        assertEquals("a1a2", tokenStorage.getToken());
        assertEquals("645ccafaaa5cd5e15e00f65f", result._id());
        assertEquals("645ccafaaa5cd5e15e00f65f", userStorage.get_id());
        assertEquals("a1a2", preferences.get(REFRESH_TOKEN_PREF, null));
    }
}