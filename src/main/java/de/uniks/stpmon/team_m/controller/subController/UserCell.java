package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.dto.User;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Objects;

import static de.uniks.stpmon.team_m.Constants.*;

public class UserCell extends ListCell<User> {

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
            ImageView statusImageView = new ImageView();
            final HBox statusHBox = new HBox(statusImageView);
            final HBox nameHBox = new HBox(usernameLabel);
            rootHBox = new HBox(HBOX_FRIENDS_SPACING, statusHBox, nameHBox);
            statusImageView.setImage(Objects.equals(user.status(), USER_STATUS_ONLINE) ? onlineImage : offlineImage);
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
