package de.uniks.stpmon.team_m.controller.subController;


import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.UsersService;
import de.uniks.stpmon.team_m.utils.FriendListUtils;
import de.uniks.stpmon.team_m.utils.UserStorage;
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

    /**
     * FriendSettingsContoller is used to handle the friend settings. It includes options to delete a friend and to set
     * a friend as best friend. The best friend is always on top of the friends list.
     *
     * @param preferences     The preferences of the user
     * @param userStorage     The user storage {@link UserStorage}
     * @param usersService    The users service {@link UsersService}
     * @param friendsListView The list view of the friends
     * @param user            The selected user {@link User}
     */

    public FriendSettingsController(Preferences preferences, UserStorage userStorage,
                                    UsersService usersService, ListView<User> friendsListView, User user) {
        this.preferences = preferences;
        this.userStorage = userStorage;
        this.usersService = usersService;
        this.friendsListView = friendsListView;
        this.user = user;
    }

    /**
     * Sets the best friend. If the user is already the best friend, the user will be removed as best friends and
     * returns as a normal friend.
     */

    public void bestFriendAction() {
        if (user._id().equals(preferences.get(BEST_FRIEND_PREF, null))) {
            preferences.put(BEST_FRIEND_PREF, EMPTY_STRING);
        } else {
            preferences.put(BEST_FRIEND_PREF, user._id());
        }
        FriendListUtils.sortListView(friendsListView);
    }

    /**
     * Shows a warning to the user if he wants to delete the friend.
     */

    public void deleteFriendAction() {
        Alert alert = new Alert(Alert.AlertType.WARNING, DELETE_FRIEND_WARNING, ButtonType.YES, ButtonType.NO);
        alert.setTitle(SURE);
        alert.setHeaderText(null);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            removeFriend();
        } else {
            alert.close();
        }
    }

    /**
     * Removes the friend from the friends list. The method handles the request to the server and the response.
     */

    private void removeFriend() {
        disposables.add(usersService.updateUser(null, null, null, userStorage.getFriends(), null)
                .observeOn(FX_SCHEDULER)
                .subscribe(updatedUser -> {
                    userStorage.deleteFriend(user._id());
                    friendsListView.getItems().remove(user);
                }, error -> showError(error.getMessage())));
    }
}
