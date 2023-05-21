package de.uniks.stpmon.team_m.utils;

import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.dto.User;
import javafx.scene.control.ListView;

import java.util.prefs.Preferences;

import static de.uniks.stpmon.team_m.Constants.USER_STATUS_OFFLINE;
import static de.uniks.stpmon.team_m.Constants.USER_STATUS_ONLINE;

public class FriendListUtils {
    public static void sortListView(ListView<User> friendsListView) {
        friendsListView.getItems().sort(FriendListUtils::sortByOnline);
    }

    public static int sortByOnline(User o1, User o2) {
        BestFriendUtils bestFriendUtils = new BestFriendUtils(Preferences.userNodeForPackage(Main.class));
        if (bestFriendUtils.isBestFriend(o1) && !bestFriendUtils.isBestFriend(o2)) {
            return -1;
        } else if (!bestFriendUtils.isBestFriend(o1) && bestFriendUtils.isBestFriend(o2)) {
            return 1;
        } else if (o1.status().equals(USER_STATUS_ONLINE) && o2.status().equals(USER_STATUS_OFFLINE)) {
            return -1;
        } else if (o1.status().equals(USER_STATUS_OFFLINE) && o2.status().equals(USER_STATUS_ONLINE)) {
            return 1;
        } else {
            return o1.name().compareTo(o2.name());
        }
    }

}
