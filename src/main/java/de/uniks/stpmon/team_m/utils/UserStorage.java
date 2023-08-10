package de.uniks.stpmon.team_m.utils;

import de.uniks.stpmon.team_m.dto.LoginResult;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class UserStorage {
    private String _id;
    private String name;
    private String avatar;
    private List<String> friends;
    private String refreshToken;

    /**
     * UserStorage handles the storage of the user that needs to be used as the currently logged in user.
     */

    @Inject
    public UserStorage() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<String> getFriends() {
        return this.friends;
    }

    public void addFriend(String newFriend) {
        if (!this.friends.contains(newFriend)) {
            this.friends.add(newFriend);
        }
    }

    public void deleteFriend(String friend) {
        this.friends.remove(friend);
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public void setUser(LoginResult loginResult) {
        this._id = loginResult._id();
        this.name = loginResult.name();
        this.avatar = loginResult.avatar();
        this.friends = loginResult.friends();
        this.refreshToken = loginResult.refreshToken();
    }

    public void removeUser() {
        this._id = null;
        this.name = null;
        this.avatar = null;
        this.friends = null;
    }
}
