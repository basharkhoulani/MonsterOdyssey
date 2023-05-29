package de.uniks.stpmon.team_m.service;

import de.uniks.stpmon.team_m.dto.CreateUserDto;
import de.uniks.stpmon.team_m.dto.UpdateUserDto;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.rest.UsersApiService;
import de.uniks.stpmon.team_m.utils.UserStorage;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import java.util.List;

public class UsersService {

    private final UsersApiService usersApiService;
    private final UserStorage userStorage;

    /**
     * UsersService handles the communication with the backend for the users.
     */

    @Inject
    public UsersService(UsersApiService usersApiService, UserStorage userStorage) {
        this.usersApiService = usersApiService;
        this.userStorage = userStorage;
    }

    /**
     * createUser creates a new user.
     *
     * @param username The username of the user.
     * @param avatar   The avatar of the user.
     * @param password The password of the user.
     * @return The created user.
     */

    public Observable<User> createUser(String username, String avatar, String password) {
        return usersApiService.createUser(new CreateUserDto(username, avatar, password));
    }

    /**
     * getUser returns a specific user.
     *
     * @param id The id of the user.
     * @return The user.
     */

    public Observable<User> getUser(String id) {
        return usersApiService.getUser(id);
    }

    /**
     * getUsers returns all users based on parameters.
     * For all users pass null for both parameters. Also, only one parameter can be null.
     *
     * @param ids    The ids of the users.
     * @param status The status of the users.
     * @return A list of users.
     */

    public Observable<List<User>> getUsers(List<String> ids, String status) {
        return usersApiService.getUsers(ids, status);
    }

    /**
     * updateUser updates the current user.
     *
     * @param name     The name of the user.
     * @param status   The status of the user.
     * @param avatar   The avatar of the user.
     * @param friends  The friends of the user.
     * @param password The password of the user.
     * @return The updated user.
     */

    public Observable<User> updateUser(String name, String status, String avatar, List<String> friends, String password) {
        return usersApiService.updateUser(userStorage.get_id(), new UpdateUserDto(name, status, avatar, friends, password));
    }

    /**
     * deleteUser deletes the current user.
     *
     * @return The deleted user.
     */

    public Observable<User> deleteUser() {
        return usersApiService.deleteUser(userStorage.get_id()).map(user -> {
            userStorage.removeUser();
            return user;
        });
    }
}
