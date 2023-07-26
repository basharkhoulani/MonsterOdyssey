package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.IngameController;
import de.uniks.stpmon.team_m.dto.Map;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

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
    @FXML
    public AnchorPane mapContainer;
    @Inject
    Provider<IngameController> ingameControllerProvider;
    @Inject
    Provider<TrainerStorage> trainerStorageProvider;
    private IngameController ingameController;
    private VBox miniMapVBox;
    private Canvas miniMapCanvas;
    private Map miniMap;

    @Inject
    public IngameMiniMapController() {
    }

    public void init(IngameController ingameController, App app, Canvas miniMapCanvas, VBox miniMapVBox, Map miniMap) {
        this.ingameController = ingameController;
        this.app = app;
        this.miniMapCanvas = miniMapCanvas;
        this.miniMapVBox = miniMapVBox;
        this.miniMap = miniMap;
    }

    public Parent render() {
        final Parent parent = super.render();
        regionName.setText(trainerStorageProvider.get().getRegion().name());

        Image pin = new Image(String.valueOf(App.class.getResource("images/pin.png")));
        double pinWidth = pin.getWidth() / 20;
        double pinHeight = pin.getHeight() / 20;
        double xPinOffset = -pinWidth / 2;
        double yPinOffset = -pinHeight;
        final VBox[] descriptionVBox = new VBox[1];
        miniMap.layers().get(2).objects().forEach(tileObject -> {
            double width;
            double height;
            if (tileObject.width() != 0) {
                width = tileObject.width();
            } else {
                width = 16;
            }
            if (tileObject.height() != 0) {
                height = tileObject.height();
            } else {
                height = 16;
            }
            VBox location = new VBox();
            location.setMinSize(width, height);
            location.setPrefSize(width, height);
            location.setMaxSize(width, height);
            location.setLayoutX(tileObject.x());
            location.setLayoutY(tileObject.y());
            location.setOnMouseEntered(event -> {
                TextFlow description = new TextFlow();
                description.getChildren().add(new Text(tileObject.name() + "\n\n" + tileObject.properties().get(0).value()));

                descriptionVBox[0] = new VBox();
                descriptionVBox[0].setMaxSize(632, 40);
                descriptionVBox[0].setStyle("-fx-padding: 4 4 4 4px;-fx-background-color: #d3ebd3;-fx-border-color: black;-fx-border-style: solid;-fx-font-family: \"Comic Sans MS\";-fx-font-weight: bold;");
                descriptionVBox[0].setLayoutX(-60);
                descriptionVBox[0].setLayoutY(-32);

                descriptionVBox[0].getChildren().add(description);
                mapContainer.getChildren().add(descriptionVBox[0]);
            });
            location.setOnMouseExited(event -> mapContainer.getChildren().remove(descriptionVBox[0]));

            miniMapCanvas.getGraphicsContext2D().drawImage(pin, tileObject.x() + width / 2 + xPinOffset, tileObject.y() + height / 2 + yPinOffset, pinWidth, pinHeight);
            mapContainer.getChildren().add(location);
        });

        miniMapImageView.setImage(miniMapCanvas.snapshot(null, null));
        return parent;
    }

    public void closeMiniMap() {
        ingameController.root.getChildren().remove(miniMapVBox);
        System.out.println("Close MiniMap");
        ingameController.buttonsDisable(false);
    }

}
