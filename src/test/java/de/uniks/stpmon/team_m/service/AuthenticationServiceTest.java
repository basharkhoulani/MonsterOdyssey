package de.uniks.stpmon.team_m.service;

import de.uniks.stpmon.team_m.dto.LoginDto;
import de.uniks.stpmon.team_m.dto.LoginResult;
import de.uniks.stpmon.team_m.dto.LogoutResult;
import de.uniks.stpmon.team_m.rest.AuthApiService;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Spy
    TokenStorage tokenStorage;
    @Spy
    UserStorage userStorage;
    @Mock
    AuthApiService authApiService;

    @InjectMocks
    AuthenticationService authenticationService;

    @Test
    void login() {
        // Successful login of user

        //define mocks
        when(authApiService.login(ArgumentMatchers.any()))
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
    void logout(){
        when(authApiService.logout()).thenReturn(Observable.just(new LogoutResult()));

        authenticationService.logout().blockingFirst();

        verify(authApiService).logout();
    }
}