package de.uniks.stpmon.team_m.service;

import de.uniks.stpmon.team_m.dto.LoginDto;
import de.uniks.stpmon.team_m.dto.LoginResult;
import de.uniks.stpmon.team_m.rest.AuthApiService;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mockito.*;

import java.util.*;
import java.util.prefs.Preferences;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Spy
    TokenStorage tokenStorage;
    @Spy
    UserStorage userStorage;
    @Mock
    AuthApiService authApiService;
    @Mock
    Preferences preferences;

    @InjectMocks
    AuthenticationService authenticationService;

    @Test
    void login() {
        // Successful login of user

        //define mocks
        Mockito.when(authApiService.login(ArgumentMatchers.any()))
                .thenReturn(Observable.just(new LoginResult("1", "t", "online", null, null, "a1a2", "a3a4")));

        // Login of a User
        final LoginResult result = authenticationService.login("t","12345678",false).blockingFirst();

        // Check for existing token
        assertEquals("a1a2", result.accessToken());
        assertEquals("a1a2", tokenStorage.getToken());

        // Check for successful login
        assertEquals("1", result._id());
        assertEquals("1", userStorage.get_id());

        //check mocks
        Mockito.verify(authApiService).login(new LoginDto("t", "12345678"));

    }
}