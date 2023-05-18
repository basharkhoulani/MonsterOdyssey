package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.dto.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

public class GroupCell extends ListCell<Group> {

    @Override
    protected void updateItem(Group group, boolean empty) {
        super.updateItem(group, empty);
        if (group != null || empty) {
            final Label groupNameLabel = new Label(group != null ? group.name() : "");
            final HBox rootHBox = new HBox(groupNameLabel);
            rootHBox.setId(group != null ? group.name() : "");
            rootHBox.setUserData(group);
            setGraphic(rootHBox);
            setText(null);
        } else {
            setText(null);
            setGraphic(null);
        }
    }
}
