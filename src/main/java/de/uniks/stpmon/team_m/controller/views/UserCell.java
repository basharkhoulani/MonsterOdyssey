package de.uniks.stpmon.team_m.controller.views;

import de.uniks.stpmon.team_m.dto.User;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import static de.uniks.stpmon.team_m.Constants.*;


public class UserCell extends ListCell<User> {
    private final ObservableList<User> chosenUsers;
    private final VBox groupMembersVBox;

    public UserCell(ObservableList<User> chosenUsers, VBox groupMembersVBox) {
        this.chosenUsers = chosenUsers;
        this.groupMembersVBox = groupMembersVBox;
    }

    @Override
    protected void updateItem(User item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setText(null);
            setGraphic(null);
        } else {
            final Text text = new Text(item.name());
            final Button button = new Button();
            button.setOnAction(event -> addOrRemoveUserToNewGroup(button));
            HBox hBox = new HBox(text, button);
            hBox.setSpacing(SPACING_BETWEEN_BUTTON_NAME_GROUP);
            hBox.setId(item.name());
            setGraphic(hBox);
            if (chosenUsers.contains(item)) {
                button.setText(CHECK_MARK);
            } else {
                button.setText(ADD_MARK);
            }
        }
    }

    private void addOrRemoveUserToNewGroup(Button button) {
        if (button.getText().equals(ADD_MARK)) {
            button.setText(CHECK_MARK);
            chosenUsers.add(getItem());
            final Text text = new Text(getItem().name());
            final Button removeButton = new Button();
            removeButton.setText(CHECK_MARK);
            removeButton.setOnAction(event -> removeFromSelectGroupMembers());
            final HBox hBox = new HBox(text, removeButton);
            hBox.setSpacing(SPACING_BETWEEN_BUTTON_NAME_GROUP);
            hBox.setId(getItem().name());
            groupMembersVBox.getChildren().add(hBox);
        } else {
            button.setText(ADD_MARK);
            chosenUsers.remove(getItem());
            groupMembersVBox.getChildren().removeIf(node -> node.getId().equals(getItem().name()));
        }
    }

    private void removeFromSelectGroupMembers() {
        chosenUsers.remove(getItem());
        groupMembersVBox.getChildren().removeIf(node -> node.getId().equals(getItem().name()));
    }

}
