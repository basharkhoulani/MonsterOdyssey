package de.uniks.stpmon.team_m.controller.views;

import de.uniks.stpmon.team_m.dto.User;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.List;
import java.util.Objects;

import static de.uniks.stpmon.team_m.Constants.*;
import static javafx.geometry.Pos.*;


public class UserCell extends ListCell<User> {
    private final ObservableList<User> chosenUsers;
    private final ListView<User> listView;
    private final List<User> friends;
    private final List<String> friendsIds;

    public UserCell(ObservableList<User> chosenUsers, ListView<User> listView, List<User> friends, List<String> friendsIds) {
        this.chosenUsers = chosenUsers;
        this.listView = listView;
        this.friends = friends;
        this.friendsIds = friendsIds;
    }

    @Override
    protected void updateItem(User item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setText(null);
            setGraphic(null);
        } else {
            final Label usernameLabel = new Label(item.name());
            final Button addOrRemoveButton = new Button();
            final Circle circle = new Circle(5);
            final HBox statusHBox = new HBox(circle);
            final HBox nameHBox = new HBox(usernameLabel);
            final HBox buttonHBox = new HBox(addOrRemoveButton);
            final HBox rootHBox = new HBox(statusHBox, nameHBox, buttonHBox);
            statusHBox.setAlignment(CENTER_LEFT);
            nameHBox.setAlignment(CENTER);
            buttonHBox.setAlignment(CENTER_RIGHT);
            HBox.setHgrow(statusHBox, Priority.ALWAYS);
            HBox.setHgrow(nameHBox, Priority.ALWAYS);
            HBox.setHgrow(buttonHBox, Priority.ALWAYS);
            HBox.setHgrow(rootHBox, Priority.ALWAYS);
            circle.setFill(Objects.equals(item.status(), USER_STATUS_ONLINE) ? Color.LIGHTGREEN : Color.RED);
            rootHBox.setId(item.name());
            rootHBox.setUserData(item);
            setGraphic(rootHBox);
            setText(null);
            addOrRemoveButton.setOnAction(event -> addOrRemoveToGroup(item, addOrRemoveButton));
            if (chosenUsers.contains(item)) {
                addOrRemoveButton.setText(CHECK_MARK);
            } else {
                addOrRemoveButton.setText(ADD_MARK);
            }
        }
    }

    private void addOrRemoveToGroup(User item, Button addOrRemoveButton) {
        if (chosenUsers.contains(item)) {
            chosenUsers.remove(item);
            addOrRemoveButton.setText(ADD_MARK);
            listView.getItems().remove(item);
            if (friendsIds.contains(item._id())) {
                addUserAndSort(item);
            }
        } else {
            chosenUsers.add(item);
            addOrRemoveButton.setText(CHECK_MARK);
            if (!listView.getItems().contains(item)) {
                listView.getItems().add(item);
            } else {
                listView.getItems().remove(item);
                addUserAndSort(item);
            }
        }
    }

    private void addUserAndSort(User item) {
        listView.getItems().add(item);
        listView.getItems().sort((o1, o2) -> {
            if (friends.contains(o1) && friends.contains(o2)) {
                return o1.name().compareTo(o2.name());
            } else if (friends.contains(o1)) {
                return -1;
            } else if (friends.contains(o2)) {
                return 1;
            } else {
                return o1.name().compareTo(o2.name());
            }
        });
    }

}
