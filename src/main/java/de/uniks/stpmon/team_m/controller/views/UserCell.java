package de.uniks.stpmon.team_m.controller.views;

import de.uniks.stpmon.team_m.dto.User;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Objects;

import static de.uniks.stpmon.team_m.Constants.USER_STATUS_ONLINE;
import static javafx.geometry.Pos.CENTER;
import static javafx.geometry.Pos.CENTER_LEFT;

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
            final Circle circle = new Circle(5);
            final HBox statusHBox = new HBox(circle);
            final HBox nameHBox = new HBox(usernameLabel);
            rootHBox = new HBox(statusHBox, nameHBox);
            statusHBox.setAlignment(CENTER_LEFT);
            nameHBox.setAlignment(CENTER);
            HBox.setHgrow(statusHBox, Priority.ALWAYS);
            HBox.setHgrow(nameHBox, Priority.ALWAYS);
            HBox.setHgrow(rootHBox, Priority.ALWAYS);
            circle.setFill(Objects.equals(user.status(), USER_STATUS_ONLINE) ? Color.LIGHTGREEN : Color.RED);
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
