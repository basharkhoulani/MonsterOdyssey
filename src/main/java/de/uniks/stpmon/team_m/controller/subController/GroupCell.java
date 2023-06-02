package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.dto.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

import static de.uniks.stpmon.team_m.Constants.EMPTY_STRING;

public class GroupCell extends ListCell<Group> {

    /**
     * GroupCell is used to handle the group cells. It includes the name of the group. The group cell contains data
     * about the group. The group cell is used in the group list view.
     * Every group cell has a rootHBox which contains the name of the group. The rootHBox has the id of the group.
     */

    @Override
    protected void updateItem(Group group, boolean empty) {
        super.updateItem(group, empty);
        if (group != null || empty) {
            final Label groupNameLabel = new Label(group != null ? group.name() : EMPTY_STRING);
            final HBox rootHBox = new HBox(groupNameLabel);
            rootHBox.setId(group != null ? group.name() : EMPTY_STRING);
            rootHBox.getStyleClass().add("groupCell");
            rootHBox.setUserData(group);
            setGraphic(rootHBox);
            setText(null);
        } else {
            setText(null);
            setGraphic(null);
        }
    }
}
