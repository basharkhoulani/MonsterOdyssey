package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.IngameController;
import de.uniks.stpmon.team_m.controller.MainMenuController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;

public class IngamePauseMenuController extends Controller {

    @FXML
    public VBox pauseMenuVbox;
    @FXML
    public Button resumeGameButton;
    @FXML
    public Button settingsButton;
    @FXML
    public Button leaveGameButton;
    @FXML
    public ImageView resumeGameImageView;
    @FXML
    public ImageView settingsImageView;
    @FXML
    public ImageView leaveGameImageView;
    @Inject
    IngameController ingameController;
    @Inject
    Provider<IngameController> ingameControllerProvider;
    @Inject
    Provider<MainMenuController> mainMenuControllerProvider;
    @Inject
    public IngamePauseMenuController() {
    }

    public Parent render() {
        final Parent parent = super.render();
        return parent;
    }

    public void resumeGame() {
        pauseMenuVbox.setVisible(false);
        ingameController.stackPane.setEffect(null);
        ingameController.buttonsDisableFalse();
    }

    public void settings() {

    }

    public void leaveGame() {
        pauseMenuVbox.setVisible(false);
        app.show(mainMenuControllerProvider.get());
    }
}
