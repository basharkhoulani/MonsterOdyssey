package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.dto.User;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.List;

import static de.uniks.stpmon.team_m.Constants.*;
import static de.uniks.stpmon.team_m.controller.Controller.sortByOnline;
import static javafx.geometry.Pos.CENTER_RIGHT;

public class GroupUserCell extends UserCell {

    private final ObservableList<User> chosenUsers;
    private final ListView<User> listView;
    private final List<User> friends;

    public GroupUserCell(ObservableList<User> chosenUsers, ListView<User> listView, List<User> friends) {
        this.chosenUsers = chosenUsers;
        this.listView = listView;
        this.friends = friends;
    }

    @Override
    protected void updateItem(User item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setText(null);
            setGraphic(null);
        } else {
            final Button addOrRemoveButton = new Button();
            final HBox buttonHBox = new HBox(addOrRemoveButton);
            addOrRemoveButton.setPrefSize(BUTTON_PREF_SIZE, BUTTON_PREF_SIZE);
            addOrRemoveButton.setStyle(BUTTON_TRANSPARENT_STYLE + BUTTON_BORDER_STYLE);
            buttonHBox.setAlignment(CENTER_RIGHT);
            HBox.setHgrow(buttonHBox, Priority.ALWAYS);
            super.getRootHBox().getChildren().add(buttonHBox);
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
            List<String> friendsIds = friends.stream().map(User::_id).toList();
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
                return sortByOnline(o1, o2);
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
