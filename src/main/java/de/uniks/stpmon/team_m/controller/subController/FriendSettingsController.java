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

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Optional;
import java.util.prefs.Preferences;

import static de.uniks.stpmon.team_m.Constants.*;

public class FriendSettingsController extends Controller {

    @Inject
    Preferences preferences;
    @Inject
    Provider<UserStorage> userStorage;
    @Inject
    UsersService usersService;
    @FXML
    public Button bestFriendButton;
    @FXML
    public Button deleteFriendButton;
    private User user;
    private ListView<User> friendsListView;

    /**
     * FriendSettingsContoller is used to handle the friend settings. It includes options to delete a friend and to set
     * a friend as best friend. The best friend is always on top of the friends list.
     */

    @Inject
    public FriendSettingsController() {
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
        disposables.add(usersService.updateUser(null, null, null, userStorage.get().getFriends(), null)
                .observeOn(FX_SCHEDULER)
                .subscribe(updatedUser -> {
                    userStorage.get().deleteFriend(user._id());
                    friendsListView.getItems().remove(user);
                }, error -> showError(error.getMessage())));
    }

    /**
     * Sets the user.
     *
     * @param user The {@link User}
     */

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Sets the friends list view.
     *
     * @param friendsListView The {@link ListView}
     */

    public void setFriendsListView(ListView<User> friendsListView) {
        this.friendsListView = friendsListView;
    }
}
