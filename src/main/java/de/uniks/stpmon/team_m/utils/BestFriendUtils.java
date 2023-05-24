package de.uniks.stpmon.team_m.utils;

import de.uniks.stpmon.team_m.dto.User;

import java.util.Objects;
import java.util.prefs.Preferences;

import static de.uniks.stpmon.team_m.Constants.BEST_FRIEND_PREF;

public class BestFriendUtils {

    private final Preferences preferences;

    public BestFriendUtils(Preferences preferences) {
        this.preferences = preferences;
    }

    public boolean isBestFriend(User user) {
        return Objects.equals(preferences.get(BEST_FRIEND_PREF, null), user._id());
    }
}
