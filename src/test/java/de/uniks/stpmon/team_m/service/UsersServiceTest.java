package de.uniks.stpmon.team_m.service;

import de.uniks.stpmon.team_m.dto.CreateUserDto;
import de.uniks.stpmon.team_m.dto.UpdateUserDto;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.rest.UsersApiService;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static de.uniks.stpmon.team_m.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsersServiceTest {

    @Mock
    UsersApiService usersApiService;
    @InjectMocks
    UsersService usersService;

    @Test
    void createUser() {
        //Successful sign Up of user

        //define mocks
        when(usersApiService.createUser(ArgumentMatchers.any()))
                .thenReturn(Observable.just(new User(
                        "1",
                        "1",
                        "online",
                        null,
                        null
                )));

        //Sign Up of User
        final User user = usersService.createUser("1",null,"12345678").blockingFirst();

        //Check for successful Sign Up
        assertEquals("1", user._id());

        verify(usersApiService).createUser(new CreateUserDto("1", null, "12345678"));
    }

    @Test
    void updateUsername(){
        //Successful change the Username of user

        //define mocks
        when(usersApiService.updateUser(anyString(), any()))
                .thenReturn(Observable.just(new User(
                        "1",
                        "UserPatch",
                        STATUS_ONLINE,
                        null,
                        null)));
        final User user = usersService.updateUser("1", "UserPatch", null, null, null,null).blockingFirst();

        assertEquals(user.name(), "UserPatch");

        verify(usersApiService).updateUser("1", new UpdateUserDto("UserPatch", null, null, null, null));
    }

    @Test
    void updatePassword(){
        //Successful change the Username of user

        //define mocks
        when(usersApiService.updateUser(anyString(), any()))
                .thenReturn(Observable.just(new User(
                        "1",
                        "UserPatch",
                        STATUS_ONLINE,
                        null,
                        null)));
        final User user = usersService.updateUser("1", null, null, null, null,"12345678").blockingFirst();

        assertEquals(user.name(), "UserPatch");

        verify(usersApiService).updateUser("1", new UpdateUserDto(null, null, null, null, "12345678"));
    }
}