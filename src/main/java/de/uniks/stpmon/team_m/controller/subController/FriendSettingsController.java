package de.uniks.stpmon.team_m.controller.subController;


import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.UserStorage;
import de.uniks.stpmon.team_m.service.UsersService;
import de.uniks.stpmon.team_m.utils.BestFriendUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;

import java.util.Optional;
import java.util.prefs.Preferences;

import static de.uniks.stpmon.team_m.Constants.*;

public class FriendSettingsController extends Controller {

    private final User user;
    private final Preferences preferences;
    private final ListView<User> friendsListView;
    private final UserStorage userStorage;
    private final UsersService usersService;
    @FXML
    public Button bestFriendButton;
    @FXML
    public Button deleteFriendButton;

    public FriendSettingsController(Preferences preferences, UserStorage userStorage, UsersService usersService, ListView<User> friendsListView, User user) {
        this.preferences = preferences;
        this.userStorage = userStorage;
        this.usersService = usersService;
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
        deleteAlert();
    }
    private void deleteAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING, DELETE_FRIEND_WARNING, ButtonType.YES, ButtonType.NO);
        alert.setTitle(SURE);
        alert.setHeaderText(null);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            userStorage.deleteFriend(user._id());
            disposables.add(usersService.updateUser(null, null, null, userStorage.getFriends(), null).observeOn(FX_SCHEDULER).subscribe(updatedUser -> friendsListView.getItems().remove(user), error -> {
                userStorage.addFriend(user._id());
                alert.setContentText(GENERIC_ERROR);
                alert.setTitle(error.getMessage());
                alert.getButtonTypes().remove(ButtonType.NO);
                alert.getButtonTypes().remove(ButtonType.YES);
                alert.getButtonTypes().add(ButtonType.OK);
                alert.showAndWait();
            }));

        } else {
            alert.close();
        }
    }
}
