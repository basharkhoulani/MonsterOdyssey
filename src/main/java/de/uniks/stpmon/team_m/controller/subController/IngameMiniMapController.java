package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.IngameController;
import de.uniks.stpmon.team_m.dto.Area;
import de.uniks.stpmon.team_m.dto.Map;
import de.uniks.stpmon.team_m.service.AreasService;
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
import java.util.ArrayList;
import java.util.List;


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
    @Inject
    AreasService areasService;
    private IngameController ingameController;
    private VBox miniMapVBox;
    private Canvas miniMapCanvas;
    private Map miniMap;
    private List<Area> areasList = new ArrayList<>();
    private final List<String> discoveredLocations = new ArrayList<>();

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
        TrainerStorage trainerStorage = trainerStorageProvider.get();
        disposables.add(areasService.getAreas(trainerStorage.getRegion()._id()).observeOn(FX_SCHEDULER).subscribe(areas -> {
            areasList = areas;
            // compare visited areas
            areasList.forEach(area -> {
                if (trainerStorage.getTrainer().visitedAreas().contains(area._id())) {
                    discoveredLocations.add(area.name());
                }
            });
            // set region name
            regionName.setText(trainerStorage.getRegion().name());
            // set marker data
            Image pin = new Image(String.valueOf(App.class.getResource("images/pin.png")));
            Image questionmark = new Image(String.valueOf(App.class.getResource("images/question-mark.png")));
            double pinWidth = pin.getWidth() / 20;
            double pinHeight = pin.getHeight() / 20;
            double xPinOffset = -pinWidth / 2;
            double yPinOffset = -pinHeight;
            final VBox[] descriptionVBox = new VBox[1];
            miniMap.layers().get(2).objects().forEach(tileObject -> {
                boolean discovered;
                discovered = discoveredLocations.contains(tileObject.name());
                // create locations
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
                    // create description depending on discovered
                    TextFlow description = new TextFlow();
                    if (discovered) {
                        description.getChildren().add(new Text(tileObject.name() + "\n\n" + tileObject.properties().get(0).value()));
                    } else {
                        description.getChildren().add(new Text("\n " + resources.getString("NOT.DISCOVERED") + " \n"));
                    }
                    descriptionVBox[0] = new VBox();
                    descriptionVBox[0].setMaxSize(632, 40);
                    descriptionVBox[0].setStyle("-fx-padding: 4 4 4 4px;-fx-background-color: #d3ebd3;-fx-border-color: black;-fx-border-style: solid;-fx-font-family: \"Comic Sans MS\";-fx-font-weight: bold;");
                    descriptionVBox[0].setLayoutX(-60);
                    descriptionVBox[0].setLayoutY(-32);
                    descriptionVBox[0].getChildren().add(description);
                    mapContainer.getChildren().add(descriptionVBox[0]);
                });
                location.setOnMouseExited(event -> mapContainer.getChildren().remove(descriptionVBox[0]));
                // draw marker
                if (discovered) {
                    miniMapCanvas.getGraphicsContext2D().drawImage(pin, tileObject.x() + width / 2 + xPinOffset, tileObject.y() + height / 2 + yPinOffset, pinWidth, pinHeight);
                } else {
                    miniMapCanvas.getGraphicsContext2D().drawImage(questionmark, tileObject.x() + width / 2 + xPinOffset, tileObject.y() + height / 2 + yPinOffset, pinWidth, pinHeight);
                }
                mapContainer.getChildren().add(location);
            });
            miniMapImageView.setImage(miniMapCanvas.snapshot(null, null));
        }, Throwable::printStackTrace));
        return parent;
    }

    public void closeMiniMap() {
        ingameController.root.getChildren().remove(miniMapVBox);
        ingameController.buttonsDisable(false);
    }

}
