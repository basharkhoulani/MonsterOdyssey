package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.dto.User;
import de.uniks.stpmon.team_m.service.UsersService;
import de.uniks.stpmon.team_m.utils.UserStorage;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.controlsfx.control.PopOver;

import java.util.prefs.Preferences;

import static de.uniks.stpmon.team_m.Constants.*;
import static javafx.geometry.Pos.CENTER_RIGHT;


public class MainMenuUserCell extends UserCell {

    private final UserStorage userStorage;
    private final UsersService usersService;


    /**
     * MainMenuUserCell is used to handle the main menu user cells in the MainMenuController.
     * The users are the friends of the currently logged-in user.
     * It includes a button to remove the friend and set/remove the friend as best friend.
     * It also includes the possibility to open the chat with the friend by clicking the cell.
     * MainMenuUserCell extends UserCell which includes the status, best friend status, and name of the user.
     *
     * @param preferences  The {@link Preferences} are used to get the best friend status of the user.
     * @param userStorage  The {@link UserStorage} is used to get the currently logged-in user.
     * @param usersService The {@link UsersService} is used to remove the friend.
     */

    public MainMenuUserCell(Preferences preferences, UserStorage userStorage, UsersService usersService) {
        super(preferences);
        this.userStorage = userStorage;
        this.usersService = usersService;
    }


    /**
     * Updates and renders the main menu user cell.
     * It creates a button to open the friend settings pop-over.
     */

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
            popOverButton.setStyle(BUTTON_TRANSPARENT_STYLE);
            buttonHBox.setAlignment(CENTER_RIGHT);
            HBox.setHgrow(buttonHBox, Priority.ALWAYS);
            super.getRootHBox().getChildren().add(buttonHBox);
            popOverButton.setOnAction(event -> showPopOver(popOverButton, item));
        }
    }

    /**
     * Shows the friend settings pop over.
     *
     * @param button The button which is used to show the pop-over.
     * @param user   The user which is used to show the pop-over.
     */

    private void showPopOver(Button button, User user) {
        PopOver popOver = new PopOver();
        popOver.setContentNode(new FriendSettingsController(preferences, userStorage,
                usersService, getListView(), user).render());
        popOver.setDetachable(false);
        popOver.show(button);
    }
}
