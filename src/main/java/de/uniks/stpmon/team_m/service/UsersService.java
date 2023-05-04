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

    @Inject
    public UsersService(UsersApiService usersApiService) {
        this.usersApiService = usersApiService;
    }

    public Observable<User> createUser(String username, String password) {
        return usersApiService.createUser(new CreateUserDto(username, password));
    }

    public Observable<User> getUser(String id) {
        return usersApiService.getUser(id);
    }

    // For all users pass null for both parameters. Also only one parameter can be null.
    public Observable<List<User>> getUsers(List<String> ids, String status) {
        return usersApiService.getUsers(ids, status);
    }

    public Observable<User> updateUser(User user, String name, String status, List<String> friends, String password) {
        return usersApiService.updateUser(user._id(), new UpdateUserDto(name, status, friends, password));
    }

    public Observable<User> deleteUSer(User user) {
        return usersApiService.deleteUser(user._id());
    }
}
