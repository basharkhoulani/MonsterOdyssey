package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.dto.User;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.List;
import java.util.prefs.Preferences;

import static de.uniks.stpmon.team_m.Constants.*;
import static javafx.geometry.Pos.CENTER_RIGHT;

public class GroupUserCell extends UserCell {

    private final ListView<User> friendsListView;
    private final ListView<User> foreignListView;
    private final ObservableList<User> chosenUsers;
    private final List<User> friends;

    public GroupUserCell(Preferences preferences, ObservableList<User> chosenUsers, ListView<User> friendsListView,
                         ListView<User> foreignListView, List<User> friends) {
        super(preferences);
        this.chosenUsers = chosenUsers;
        this.friendsListView = friendsListView;
        this.foreignListView = foreignListView;
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
            if (friends.contains(item)) {
                friendsListView.getItems().remove(item);
                addUserAndSort(friendsListView, item);
            } else {
                foreignListView.getItems().remove(item);
            }
        } else {
            chosenUsers.add(item);
            addOrRemoveButton.setText(CHECK_MARK);
            if (friends.contains(item)) {
                friendsListView.getItems().remove(item);
                addUserAndSort(friendsListView, item);
            } else {
                foreignListView.getItems().remove(item);
                addUserAndSort(foreignListView, item);
            }
        }
    }

    private void addUserAndSort(ListView<User> listView, User item) {
        listView.getItems().add(item);
        listView.getItems().sort(Controller::sortByOnline);
    }

}
