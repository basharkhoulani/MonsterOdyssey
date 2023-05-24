package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.utils.FriendListUtils;
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

    /**
     * GroupUserCell is used to handle the group user cells in the GroupController.
     * It includes a button to remove and add to the group.
     * GroupUserCell extends UserCell which includes the status, best friend status, and name of the user.
     *
     * @param preferences     The preferences of the user.
     *                        The preferences are used to get the best friend status of the user.
     * @param chosenUsers     The chosen users of the group.
     *                        The chosen users are used to remove and add users to the group.
     * @param friendsListView The friends list view.
     * @param foreignListView The foreign list view.
     * @param friends         The friends of the user.
     */

    public GroupUserCell(Preferences preferences, ObservableList<User> chosenUsers, ListView<User> friendsListView,
                         ListView<User> foreignListView, List<User> friends) {
        super(preferences);
        this.chosenUsers = chosenUsers;
        this.friendsListView = friendsListView;
        this.foreignListView = foreignListView;
        this.friends = friends;
    }

    /**
     * Updates and renders the group user cell.
     * It subdivides the chosen users into friends and foreign users.
     */

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


    /**
     * Adds or removes the user to the group.
     * If the user is already in the group, the user will be removed from the group.
     * If the user is not in the group, the user will be added to the group.
     * The user will be added to the friends list view if the user is a friend.
     * The user will be added to the foreign list view if the user is not a friend.
     *
     * @param item              The user.
     * @param addOrRemoveButton The button to add or remove the user to the group.
     */

    private void addOrRemoveToGroup(User item, Button addOrRemoveButton) {
        if (chosenUsers.contains(item)) {
            removeUser(item, addOrRemoveButton);
        } else {
            addUser(item, addOrRemoveButton);
        }
    }

    private void addUser(User item, Button addOrRemoveButton) {
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

    private void removeUser(User item, Button addOrRemoveButton) {
        chosenUsers.remove(item);
        addOrRemoveButton.setText(ADD_MARK);
        if (friends.contains(item)) {
            friendsListView.getItems().remove(item);
            addUserAndSort(friendsListView, item);
        } else {
            foreignListView.getItems().remove(item);
        }
    }

    /**
     * Adds the user to the list view and sorts the list view.
     *
     * @param listView The list view.
     * @param item     The user.
     */

    private void addUserAndSort(ListView<User> listView, User item) {
        listView.getItems().add(item);
        listView.getItems().sort(FriendListUtils::sortByOnline);
    }

}
