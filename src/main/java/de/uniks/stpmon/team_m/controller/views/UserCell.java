package de.uniks.stpmon.team_m.controller.views;

import de.uniks.stpmon.team_m.dto.User;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

import java.util.List;

import static de.uniks.stpmon.team_m.Constants.ADD_MARK;
import static de.uniks.stpmon.team_m.Constants.CHECK_MARK;


public class UserCell extends ListCell<User> {
    private final ObservableList<User> chosenUsers;
    private final ListView<User> listView;
    private final List<String> friends;

    public UserCell(ObservableList<User> chosenUsers, ListView<User> listView, List<String> friends) {
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
            final Label usernameLabel = new Label(item.name());
            final Button addOrRemoveButton = new Button();
            final HBox leftSideHBox = new HBox(usernameLabel);
            final HBox rightSideHBox = new HBox(addOrRemoveButton);
            final HBox rootHBox = new HBox(leftSideHBox, rightSideHBox);
            rootHBox.setId(item.name());
            usernameLabel.setUserData(item);
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
            if (friends.contains(item._id())) {
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
        listView.getItems().sort((o1, o2) -> o1.name().compareToIgnoreCase(o2.name()));
    }

}
