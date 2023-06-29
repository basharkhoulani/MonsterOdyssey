package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.IngameController;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;

public class IngameMiniMapController extends Controller {
    @FXML
    public Label regionName;
    @FXML
    public AnchorPane mapAnchorPane;
    @FXML
    public Button closeButton;
    @FXML
    public ImageView miniMapImageView;
    @Inject
    Provider<IngameController> ingameControllerProvider;
    @Inject
    Provider<TrainerStorage> trainerStorageProvider;
    private IngameController ingameController;
    private VBox miniMapVBox;
    private Canvas miniMapCanvas;

    @Inject
    public IngameMiniMapController() {
    }

    public void init(IngameController ingameController, App app, Canvas miniMapCanvas, VBox miniMapVBox) {
        this.ingameController = ingameController;
        this.app = app;
        this.miniMapCanvas = miniMapCanvas;
        this.miniMapVBox = miniMapVBox;
    }

    public Parent render() {
        final Parent parent = super.render();
        regionName.setText(trainerStorageProvider.get().getRegion().name());
        miniMapImageView.setImage(miniMapCanvas.snapshot(null, null));
        return parent;
    }

    public void closeMiniMap() {
        ingameController.root.getChildren().remove(miniMapVBox);
        ingameController.buttonsDisable(false);
    }

}
