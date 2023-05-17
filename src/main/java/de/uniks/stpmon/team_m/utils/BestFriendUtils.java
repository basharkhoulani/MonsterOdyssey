package de.uniks.stpmon.team_m.utils;

import de.uniks.stpmon.team_m.dto.User;
import javafx.scene.control.ListView;

import java.util.prefs.Preferences;

import static de.uniks.stpmon.team_m.Constants.BEST_FRIEND_PREF;

public class BestFriendUtils {

    private final Preferences preferences;

    public BestFriendUtils(Preferences preferences) {
        this.preferences = preferences;
    }

    public boolean isBestFriend(User user) {
        return preferences.get(BEST_FRIEND_PREF, null).equals(user._id());
    }

    public void sortBestFriendTop(ListView<User> friendsListView) {
        friendsListView.getItems().sort(this::compare);
    }

    private int compare(User o1, User o2) {
        if (isBestFriend(o1)) {
            return -1;
        } else if (isBestFriend(o2)) {
            return 1;
        } else {
            return o1.name().compareTo(o2.name());
        }
    }
}
