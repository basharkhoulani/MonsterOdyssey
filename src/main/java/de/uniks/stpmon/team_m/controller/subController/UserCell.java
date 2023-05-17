package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.utils.BestFriendUtils;
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

    public UserCell(Preferences preferences) {
        this.preferences = preferences;
    }

    private HBox rootHBox;

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
            final HBox nameHBox = new HBox(usernameLabel);
            nameHBox.setAlignment(CENTER);
            rootHBox = new HBox(HBOX_FRIENDS_SPACING, statusHBox, nameHBox);
            final BestFriendUtils bestFriendUtils = new BestFriendUtils(preferences);
            if (bestFriendUtils.isBestFriend(user)) {
                statusImageView.setImage(Objects.equals(user.status(), USER_STATUS_ONLINE) ? onlineStar : offlineStar);
            } else {
                statusImageView.setImage(Objects.equals(user.status(), USER_STATUS_ONLINE) ? onlineImage : offlineImage);
            }
            rootHBox.setId(user.name());
            rootHBox.setUserData(user);
            setGraphic(rootHBox);
            setText(null);
        }
    }

    public HBox getRootHBox() {
        return rootHBox;
    }
}
