package de.uniks.stpmon.team_m.controller.subController;


import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.utils.BestFriendUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.util.prefs.Preferences;

import static de.uniks.stpmon.team_m.Constants.BEST_FRIEND_PREF;

public class FriendSettingsController extends Controller {

    private final User user;
    private final Preferences preferences;
    private final ListView<User> friendsListView;
    @FXML
    public Button bestFriendButton;
    @FXML
    public Button deleteFriendButton;

    public FriendSettingsController(Preferences preferences, ListView<User> friendsListView, User user) {
        this.preferences = preferences;
        this.friendsListView = friendsListView;
        this.user = user;
    }

    public void bestFriendAction() {
        final BestFriendUtils bestFriendUtils = new BestFriendUtils(preferences);
        if (user._id().equals(preferences.get(BEST_FRIEND_PREF, null))) {
            preferences.put(BEST_FRIEND_PREF, "");
            bestFriendUtils.sortBestFriendTop(friendsListView);
        } else {
            preferences.put(BEST_FRIEND_PREF, user._id());
            bestFriendUtils.sortBestFriendTop(friendsListView);
        }
    }

    public void deleteFriendAction() {
    }
}
