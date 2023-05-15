package de.uniks.stpmon.team_m.controller.subController;


import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.dto.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class FriendSettingsController extends Controller {

    private final User user;
    @FXML
    public Button bestFriendButton;
    @FXML
    public Button deleteFriendButton;

    public FriendSettingsController(User user) {
        this.user = user;
    }

    public void bestFriendAction() {
    }

    public void deleteFriendAction() {
    }
}
