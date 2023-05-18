package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.UserStorage;
import de.uniks.stpmon.team_m.service.UsersService;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.controlsfx.control.PopOver;

import java.util.prefs.Preferences;

import static de.uniks.stpmon.team_m.Constants.BUTTON_PREF_SIZE;
import static de.uniks.stpmon.team_m.Constants.THREE_DOTS;
import static javafx.geometry.Pos.CENTER_RIGHT;

public class MainMenuUserCell extends UserCell {

    private final UserStorage userStorage;
    private final UsersService usersService;

    public MainMenuUserCell(Preferences preferences, UserStorage userStorage, UsersService usersService) {
        super(preferences);
        this.userStorage = userStorage;
        this.usersService = usersService;
    }

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
            popOverButton.setText(THREE_DOTS);
            popOverButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
            buttonHBox.setAlignment(CENTER_RIGHT);
            HBox.setHgrow(buttonHBox, Priority.ALWAYS);
            super.getRootHBox().getChildren().add(buttonHBox);
            popOverButton.setOnAction(event -> showPopOver(popOverButton, item));
        }
    }

    private void showPopOver(Button button, User user) {
        PopOver popOver = new PopOver();
        popOver.setContentNode(new FriendSettingsController(preferences, userStorage, usersService, getListView(), user).render());
        popOver.setDetachable(false);
        popOver.show(button);
    }
}
