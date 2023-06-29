package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.IngameController;
import de.uniks.stpmon.team_m.controller.MainMenuController;
import de.uniks.stpmon.team_m.service.AudioService;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;

import java.awt.*;

import static de.uniks.stpmon.team_m.Constants.MENU_SOUND;

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
    private VBox ingameVbox;
    @Inject
    Provider<IngameController> ingameControllerProvider;
    @Inject
    Provider<MainMenuController> mainMenuControllerProvider;
    @Inject
    public IngamePauseMenuController() {
    }

    public Parent render() {
        return super.render();
    }

    public void init(IngameController ingameController, VBox ingameVbox, Provider<MainMenuController> mainMenuControllerProvider, App app) {
        this.ingameController = ingameController;
        this.ingameVbox = ingameVbox;
        this.mainMenuControllerProvider = mainMenuControllerProvider;
        this.app = app;
    }


    public void resumeGame() {
        ingameController.root.getChildren().remove(ingameVbox);
        ingameController.buttonsDisable(false);
    }

    public void settings() {
        ingameController.root.getChildren().remove(ingameVbox);
        ingameController.buttonsDisable(false);
        ingameController.showSettings();
    }

    public void leaveGame() {
        ingameController.root.getChildren().remove(ingameVbox);
        double volume = AudioService.getInstance().getVolume();
        AudioService.getInstance().setVolume(volume);
        app.show(mainMenuControllerProvider.get());
    }
}
