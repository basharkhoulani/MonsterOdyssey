package de.uniks.stpmon.team_m.service;

import de.uniks.stpmon.team_m.dto.CreateUserDto;
import de.uniks.stpmon.team_m.dto.UpdateUserDto;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.rest.UsersApiService;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import java.util.List;

public class UsersService {

    private final UsersApiService usersApiService;
    private final UserStorage userStorage;

    @Inject
    public UsersService(UsersApiService usersApiService, UserStorage userStorage) {
        this.usersApiService = usersApiService;
        this.userStorage = userStorage;
    }

    public Observable<User> createUser(String username, String avatar, String password) {
        return usersApiService.createUser(new CreateUserDto(username, avatar, password));
    }

    public Observable<User> getUser(String id) {
        return usersApiService.getUser(id);
    }

    // For all users pass null for both parameters. Also only one parameter can be null.
    public Observable<List<User>> getUsers(List<String> ids, String status) {
        return usersApiService.getUsers(ids, status);
    }

    public Observable<User> updateUser(String name, String status, String avatar, List<String> friends, String password) {
        return usersApiService.updateUser(userStorage.get_id(), new UpdateUserDto(name, status, avatar, friends, password));
    }

    public Observable<User> deleteUser() {
        return usersApiService.deleteUser(userStorage.get_id()).map(user -> {
            userStorage.removeUser();
            return user;
        });
    }
}
