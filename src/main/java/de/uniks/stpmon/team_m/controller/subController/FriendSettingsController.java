package de.uniks.stpmon.team_m.controller.subController;


import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.MainMenuController;
import de.uniks.stpmon.team_m.dto.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.prefs.Preferences;

import static de.uniks.stpmon.team_m.Constants.BEST_FRIEND_PREF;

public class FriendSettingsController extends Controller {

    private final User user;
    private final Preferences preferences;
    @FXML
    public Button bestFriendButton;
    @FXML
    public Button deleteFriendButton;

    public FriendSettingsController(Preferences preferences, User user) {
        this.preferences = preferences;
        this.user = user;
    }

    public void bestFriendAction() {
        if (user._id().equals(preferences.get(BEST_FRIEND_PREF, null))) {
            preferences.put(BEST_FRIEND_PREF, "");

        } else {
            preferences.put(BEST_FRIEND_PREF, user._id());
        }
    }

    public void deleteFriendAction() {
    }
}
