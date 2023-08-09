package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.IngameController;
import de.uniks.stpmon.team_m.dto.Layer;
import de.uniks.stpmon.team_m.dto.Map;
import de.uniks.stpmon.team_m.dto.MonsterTypeDto;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import javax.inject.Inject;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

import static de.uniks.stpmon.team_m.Constants.*;

public class MonsterLocationsController extends Controller {
    @FXML
    public BorderPane miniMapBorderPane;
    @FXML
    public Label header;
    @FXML
    public Button closeButton;
    @FXML
    public AnchorPane mapContainer;
    @FXML
    public ImageView miniMapImageView;
    @FXML
    public TextFlow mapTextFlow;
    private IngameController ingameController;
    private VBox container;
    private Canvas miniMapCanvas;
    private Map miniMap;
    private MonsterTypeDto monsterTypeDto;

    @Inject
    public MonsterLocationsController() {

    }

    public void init(IngameController ingameController, VBox container, Canvas miniMapCanvas, Map miniMap, MonsterTypeDto monsterTypeDto) {
        this.ingameController = ingameController;
        this.container = container;
        this.miniMapCanvas = miniMapCanvas;
        this.miniMap = miniMap;
        this.monsterTypeDto = monsterTypeDto;
    }

    public Parent render() {
        final Parent parent = super.render();

        header.setText(resources.getString("MONSTER.LOCATIONS"));

        Text monsterLocations = new Text(resources.getString("THERE.ARE.NO.KNOWN.LOCATIONS.FOR") + " " + monsterTypeDto.name());
        mapTextFlow.getChildren().add(monsterLocations);
        final boolean[] knownLocations = {false};

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
            // filter for monster
            if (tileObject.properties().size() > 1) {
                if (tileObject.properties().get(1).name().equals(MONSTERS)) {
                    String string = tileObject.properties().get(1).value();
                    if (string.startsWith("[")) {
                        string = string.substring(1, string.length() - 1);
                    }
                    String[] split = string.split(",");
                    List<String> monsters = Arrays.stream(split).toList();
                    if (monsters.contains((String.valueOf(monsterTypeDto.id())))) {
                        knownLocations[0] = true;
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
                        location.setStyle("-fx-border-color: red; -fx-border-width: 2px");
                        location.setOnMouseEntered(event -> description.setText(tileObject.name() + "\n\n" + tileObject.properties().get(0).value()));
                        location.setOnMouseExited(event -> description.setText(""));
                        mapContainer.getChildren().add(location);
                    }
                }
            }
        });
        if (knownLocations[0]) {
            monsterLocations.setText(monsterTypeDto.name() + " " + resources.getString("CAN.BE.FOUND.IN.THE.HIGHLIGHTED.LOCATIONS") + "\n\n\n");
        }
        miniMapImageView.setFitHeight(miniMapCanvas.getHeight());
        miniMapImageView.setFitWidth(miniMapCanvas.getWidth());
        mapContainer.setMaxHeight(miniMapCanvas.getHeight());
        mapContainer.setMinHeight(miniMapCanvas.getHeight());
        mapContainer.setMaxWidth(miniMapCanvas.getWidth());
        mapContainer.setMinWidth(miniMapCanvas.getWidth());
        if (!GraphicsEnvironment.isHeadless()) {
            miniMapImageView.setImage(miniMapCanvas.snapshot(null, null));
        }
        return parent;
    }

    public void closeMiniMap() {
        ingameController.root.getChildren().remove(container);
    }
}
