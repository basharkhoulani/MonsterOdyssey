package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.utils.BestFriendUtils;
import de.uniks.stpmon.team_m.utils.ImageProcessor;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.Objects;
import java.util.prefs.Preferences;

import static de.uniks.stpmon.team_m.Constants.*;
import static javafx.geometry.Pos.CENTER;

public class UserCell extends ListCell<User> {
    Preferences preferences;

    /**
     * UserCell is used to handle user cells in every ListView. It is the parent class for every user cell.
     * It includes the status of the user, status as best friend and the username.
     * rootHBox is the root of the user cell which contains data about the user.
     *
     * @param preferences The {@link Preferences} of the user are used to get the best friend status of the user.
     *                    If the user is a best friend, a star will be shown next to the username, and if the user is
     *                    not a best friend, a circle will be shown next to the username.
     */

    public UserCell(Preferences preferences) {
        this.preferences = preferences;
    }

    private HBox rootHBox;

    /**
     * Updates the user cell if changes are made.
     *
     * @param user  The selected {@link User}
     * @param empty The empty status of the user cell
     */

    @Override
    protected void updateItem(User user, boolean empty) {
        super.updateItem(user, empty);
        if (user == null || empty) {
            setText(null);
            setGraphic(null);
        } else {
            final Label usernameLabel = new Label(user.name());
            final Image onlineImage = new Image(Objects.requireNonNull(App.class.getResource(ONLINE_IMG)).toString());
            final Image offlineImage = new Image(Objects.requireNonNull(App.class.getResource(OFFLINE_IMG)).toString());
            final Image onlineStar = new Image(Objects.requireNonNull(App.class.getResource(ONLINE_STAR)).toString());
            final Image offlineStar = new Image(Objects.requireNonNull(App.class.getResource(OFFLINE_STAR)).toString());
            ImageView statusImageView = new ImageView();
            final HBox statusHBox = new HBox(statusImageView);
            statusHBox.setAlignment(CENTER);
            final Image avatar = ImageProcessor.toFXImage(user.avatar());
            final ImageView avatarImageView = new ImageView(avatar);
            avatarImageView.setPreserveRatio(true);
            avatarImageView.setFitHeight(AVATAR_SIZE);
            final HBox avatarHBox = new HBox(avatarImageView);
            avatarHBox.setAlignment(CENTER);
            final HBox nameHBox = new HBox(usernameLabel);
            nameHBox.setAlignment(CENTER);
            rootHBox = new HBox(HBOX_FRIENDS_SPACING, statusHBox, avatarHBox, nameHBox);
            final BestFriendUtils bestFriendUtils = new BestFriendUtils(preferences);
            statusImageView.setImage(Objects.equals(user.status(), USER_STATUS_ONLINE) ? onlineImage : offlineImage);
            if (preferences != null) {
                if (bestFriendUtils.isBestFriend(user)) {
                    statusImageView.setImage(Objects.equals(user.status(), USER_STATUS_ONLINE) ? onlineStar : offlineStar);
                }
            }
            rootHBox.setId(user.name());
            rootHBox.setUserData(user);
            setGraphic(rootHBox);
            setText(null);
        }
    }


    /**
     * Returns the root of the user cell.
     *
     * @return The root of the user cell
     */

    public HBox getRootHBox() {
        return rootHBox;
    }

}
