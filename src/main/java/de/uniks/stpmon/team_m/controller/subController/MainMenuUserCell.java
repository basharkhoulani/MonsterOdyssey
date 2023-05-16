package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.dto.User;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.controlsfx.control.PopOver;

import static de.uniks.stpmon.team_m.Constants.BUTTON_PREF_SIZE;
import static de.uniks.stpmon.team_m.Constants.THREE_DOTS;
import static javafx.geometry.Pos.CENTER_RIGHT;

public class MainMenuUserCell extends UserCell {
    @Override
    protected void updateItem(User item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setText(null);
            setGraphic(null);
        } else {
            final Button popOverButton = new Button();
            final HBox buttonHBox = new HBox(popOverButton);
            popOverButton.setPrefSize(BUTTON_PREF_SIZE, BUTTON_PREF_SIZE);
            buttonHBox.setAlignment(CENTER_RIGHT);
            HBox.setHgrow(buttonHBox, Priority.ALWAYS);
            popOverButton.setText(THREE_DOTS);
            super.getRootHBox().getChildren().add(buttonHBox);
            popOverButton.setOnAction(event -> showPopOver(popOverButton, item));
        }
    }

    private void showPopOver(Button button, User user) {
        PopOver popOver = new PopOver();
        popOver.setContentNode(new FriendSettingsController(user).render());
        popOver.setDetachable(false);
        popOver.show(button);
    }
}
