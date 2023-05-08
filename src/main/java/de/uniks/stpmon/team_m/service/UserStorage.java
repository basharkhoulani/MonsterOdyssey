package de.uniks.stpmon.team_m.service;

import de.uniks.stpmon.team_m.dto.User;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class UserStorage {
    private String _id;
    private String name;
    private String status;
    private String avatar;
    private List<User> friends;

    @Inject
    public UserStorage(){

    }

    public String get_id(){ return _id; }

    public void set_id(String _id) { this._id = _id; }

    public String getName(){ return name; }

    public void setName(String name) { this.name = name; }

    public String getStatus() { return status; }

    public void setStatus(String status){ this.status = status; }

    public String getAvatar() {return avatar; }

    public void setAvatar(String avatar) { this.avatar = avatar; }

    public List<User> getFriends() { return this.friends; }

    public void addFriend(User newFriend) {
        if(!this.friends.contains(newFriend)){
            this.friends.add(newFriend);
        }
    }

    public void deleteFriend(User friend) {
        this.friends.remove(friend);
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }
}
