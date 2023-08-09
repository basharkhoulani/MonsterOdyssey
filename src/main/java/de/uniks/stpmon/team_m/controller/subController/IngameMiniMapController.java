package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.IngameController;
import de.uniks.stpmon.team_m.dto.Area;
import de.uniks.stpmon.team_m.dto.Layer;
import de.uniks.stpmon.team_m.dto.Map;import de.uniks.stpmon.team_m.service.TrainersService;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

import javax.inject.Inject;
import javax.inject.Provider;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static de.uniks.stpmon.team_m.Constants.*;


public class IngameMiniMapController extends Controller {
    @FXML
    public Label regionName;
    @FXML
    public Button closeButton;
    @FXML
    public ImageView miniMapImageView;
    @FXML
    public AnchorPane mapContainer;
    @FXML
    public BorderPane miniMapBorderPane;
    @FXML
    public TextFlow mapTextFlow;
    @Inject
    Provider<IngameController> ingameControllerProvider;
    @Inject
    Provider<TrainerStorage> trainerStorageProvider;
    @Inject
    TrainersService trainersService;
    private IngameController ingameController;
    private VBox miniMapVBox;
    private Canvas miniMapCanvas;
    private Map miniMap;
    private List<Area> areasList = new ArrayList<>();
    private final List<String> discoveredLocations = new ArrayList<>();

    @Inject
    public IngameMiniMapController() {
    }

    public void init(IngameController ingameController, App app, Canvas miniMapCanvas, VBox miniMapVBox, Map miniMap, List<Area> areas) {
        this.ingameController = ingameController;
        this.app = app;
        this.miniMapCanvas = miniMapCanvas;
        this.miniMapVBox = miniMapVBox;
        this.miniMap = miniMap;
        this.areasList = areas;
    }

    public Parent render() {
        final Parent parent = super.render();
        TrainerStorage trainerStorage = trainerStorageProvider.get();

        // set region name
        regionName.setText(trainerStorage.getRegion().name());
        // set marker data
        Image pin = new Image(String.valueOf(App.class.getResource("images/pin.png")));
        Image stop = new Image(String.valueOf(App.class.getResource("images/bus-stop.png")));
        Image questionMark = new Image(String.valueOf(App.class.getResource("images/question-mark.png")));
        double pinWidth = pin.getWidth() / 20;
        double pinHeight = pin.getHeight() / 20;
        double xPinOffset = -pinWidth / 2;
        double yPinOffset = -pinHeight;

        // show current location
        final String[] currentLocation = new String[1];
        areasList.forEach(area -> {
            if (Objects.equals(area._id(), trainerStorage.getTrainer().area())) {
                currentLocation[0] = area.name();
            }
        });
        Text currentLocationText = new Text(resources.getString("YOU.ARE.IN") + " " + currentLocation[0] + "\n\n");
        mapTextFlow.getChildren().add(currentLocationText);

        // show map legend
        Text legend = new Text(resources.getString("LEGEND") + ":\n");
        ImageView pinImageView = new ImageView(pin);
        pinImageView.setFitWidth(pinWidth);
        pinImageView.setFitHeight(pinHeight);
        ImageView stopImageView = new ImageView(stop);
        stopImageView.setFitWidth(pinWidth);
        stopImageView.setFitHeight(pinHeight);
        ImageView questionMarkImageView = new ImageView(questionMark);
        questionMarkImageView.setFitWidth(pinWidth);
        questionMarkImageView.setFitHeight(pinHeight);
        mapTextFlow.getChildren().add(legend);
        mapTextFlow.getChildren().addAll(pinImageView, new Text(" " + resources.getString("DISCOVERED.LOCATION") + "\n"));
        mapTextFlow.getChildren().addAll(stopImageView, new Text(" " + resources.getString("FAST-TRAVEL.LOCATION") + "\n"));
        mapTextFlow.getChildren().addAll(questionMarkImageView, new Text(" " + resources.getString("UNDISCOVERED.LOCATION") + "\n\n\n"));

        Text description = new Text();
        mapTextFlow.getChildren().add(description);

        Layer layerMinimap = miniMap.layers().get(2);
        for (Layer layer : miniMap.layers()) {
            if (layer.type().equals(OBJECTGROUP)) {
                layerMinimap = layer;
                break;
            }
        }
        layerMinimap.objects().forEach(tileObject -> {
            // get area
            final Area[] newArea = new Area[1];
            areasList.forEach(area -> {
                if (trainerStorage.getTrainer().visitedAreas().contains(area._id())) {
                    discoveredLocations.add(area.name());
                }
                if (area.name().equals(tileObject.name())) {
                    newArea[0] = area;
                }
            });
            boolean discovered= discoveredLocations.contains(tileObject.name());
            // create locations
            double width;
            double height;
            if (tileObject.width() != 0) {
                width = tileObject.width();
            } else {
                width = TILE_SIZE;
            }
            if (tileObject.height() != 0) {
                height = tileObject.height();
            } else {
                height = TILE_SIZE;
            }
            VBox location = new VBox();
            location.setMinSize(width, height);
            location.setPrefSize(width, height);
            location.setMaxSize(width, height);
            location.setLayoutX(tileObject.x());
            location.setLayoutY(tileObject.y());

            location.setOnMouseEntered(event -> {
                // highlight hovered location
                location.setStyle("-fx-border-color: red; -fx-border-width: 2px");
                // create description depending on discovered
                if (discovered) {
                    description.setText(tileObject.name() + "\n\n" + tileObject.properties().get(0).value());
                } else {
                    description.setText("\n " + resources.getString("NOT.DISCOVERED") + " \n");
                }

            });
            location.setOnMouseExited(event -> {
                description.setText("");
                location.setStyle("");
            });
            // create fast travel popup
            location.setOnMouseClicked(mouseEvent -> {
                if (discovered && newArea[0].spawn() != null && !Objects.equals(trainerStorage.getTrainer().area(), newArea[0]._id())) {
                    VBox fastTravelPopUp = new VBox();
                    fastTravelPopUp.getStyleClass().add("miniMapContainer");
                    fastTravelPopUp.setLayoutX(miniMapCanvas.getWidth() / 4);
                    fastTravelPopUp.setLayoutY(miniMapCanvas.getHeight() / 3);
                    fastTravelPopUp.setPadding(new Insets(paddingAndSpacingValue));
                    fastTravelPopUp.setSpacing(paddingAndSpacingValue);
                    fastTravelPopUp.setAlignment(Pos.CENTER);
                    Label label = new Label(resources.getString("DO.YOU.WANT.TO.FAST-TRAVEL.TO") + " \n" + tileObject.name() + " ?");
                    label.setTextAlignment(TextAlignment.CENTER);
                    HBox hBox = new HBox();
                    hBox.setSpacing(paddingAndSpacingValue);
                    Button yesButton = new Button(resources.getString("ENCOUNTER_FLEE_CONFIRM_BUTTON"));
                    yesButton.getStyleClass().add("welcomeSceneButton");
                    // fast-travel to location
                    yesButton.setOnAction(actionEvent ->
                            disposables.add(trainersService.updateTrainer(trainerStorage.getRegion()._id(), trainerStorage.getTrainer()._id(), null, null, null, newArea[0]._id(), null)
                                    .observeOn(FX_SCHEDULER).subscribe(trainer -> {
                                        trainerStorage.setTrainer(trainer);
                                        ingameController.destroy();
                                        app.show(ingameControllerProvider.get());
                                    }, Throwable::printStackTrace)));
                    Button noButton = new Button(resources.getString("ENCOUNTER_FLEE_CANCEL_BUTTON"));
                    noButton.getStyleClass().add("welcomeSceneButton");
                    // no fast-travel
                    noButton.setOnAction(actionEvent -> mapContainer.getChildren().remove(fastTravelPopUp));
                    hBox.getChildren().addAll(yesButton, noButton);
                    fastTravelPopUp.getChildren().addAll(label, hBox);
                    mapContainer.getChildren().add(fastTravelPopUp);
                }
            });
            mapContainer.getChildren().add(location);
            // draw marker
            if (discovered) {
                if (newArea[0].spawn() != null) {
                    miniMapCanvas.getGraphicsContext2D().drawImage(stop, tileObject.x() + width / 2 + xPinOffset, tileObject.y() + height / 2 + yPinOffset, pinWidth, pinHeight);
                } else {
                    miniMapCanvas.getGraphicsContext2D().drawImage(pin, tileObject.x() + width / 2 + xPinOffset, tileObject.y() + height / 2 + yPinOffset, pinWidth, pinHeight);
                }
            } else {
                miniMapCanvas.getGraphicsContext2D().drawImage(questionMark, tileObject.x() + width / 2 + xPinOffset, tileObject.y() + height / 2 + yPinOffset, pinWidth, pinHeight);
            }
        });
        miniMapImageView.setFitHeight(miniMapCanvas.getHeight());
        miniMapImageView.setFitWidth(miniMapCanvas.getWidth());
        mapContainer.setMaxHeight(miniMapCanvas.getHeight());
        mapContainer.setMinHeight(miniMapCanvas.getHeight());
        mapContainer.setMaxWidth(miniMapCanvas.getWidth());
        mapContainer.setMinWidth(miniMapCanvas.getWidth());
        miniMapImageView.setImage(miniMapCanvas.snapshot(null, null));
        return parent;
    }

    public void closeMiniMap() {
        ingameController.root.getChildren().remove(miniMapVBox);
        ingameController.buttonsDisable(false);
    }

}
