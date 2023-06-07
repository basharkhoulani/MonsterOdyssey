package de.uniks.stpmon.team_m.controller;


import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.controller.subController.IngameTrainerSettingsController;
import de.uniks.stpmon.team_m.dto.Chunk;
import de.uniks.stpmon.team_m.dto.Layer;
import de.uniks.stpmon.team_m.dto.Map;
import de.uniks.stpmon.team_m.service.AreasService;
import de.uniks.stpmon.team_m.service.PresetsService;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import javax.inject.Inject;
import javax.inject.Provider;
import java.awt.*;
import java.util.Objects;
import java.util.Optional;

import static de.uniks.stpmon.team_m.Constants.FX_STYLE_BORDER_COLOR_BLACK;
import static de.uniks.stpmon.team_m.Constants.TILE_SIZE;


public class IngameController extends Controller {

    @FXML
    public Button helpSymbol;
    @FXML
    public Button monstersButton;
    @FXML
    public Button settingsButton;
    @FXML
    public Pane ingamePane;
    @Inject
    Provider<IngameTrainerSettingsController> ingameTrainerSettingsControllerProvider;
    @Inject
    Provider<MainMenuController> mainMenuControllerProvider;
    @Inject
    Provider<TrainerStorage> trainerStorageProvider;
    @Inject
    AreasService areasService;
    @Inject
    PresetsService presetsService;
    public static final KeyCode PAUSE_MENU_KEY = KeyCode.P;

    /**
     * IngameController is used to show the In-Game screen and to pause the game.
     */

    @Inject
    public IngameController() {
    }

    @Override
    public void init() {
        super.init();
    }

    private static String getFileName(String name) {
        name = name.substring(name.lastIndexOf("/") + 1);
        name = name.substring(0, name.lastIndexOf("."));
        return name;
    }

    /**
     * This method sets the title of the {@link IngameController}.
     *
     * @return Title of the {@link IngameController}.
     */

    @Override
    public String getTitle() {
        return resources.getString("INGAME.TITLE");
    }

    /**
     * This method is used to render the In-Game screen.
     *
     * @return Parent of the In-Game screen.
     */

    @Override
    public Parent render() {
        final Parent parent = super.render();
        app.getStage().getScene().setOnKeyPressed(event -> {
            if (!(event.getCode() == PAUSE_MENU_KEY)) {
                return;
            }
            pauseGame();
        });
        disposables.add(areasService.getArea(trainerStorageProvider.get().getTrainer().region(),
                trainerStorageProvider.get().getRegion().spawn().area()).subscribe(area -> {
            if (area != null) {
                loadMap(area.map());
            } else {
                System.out.println("Area is null");
            }
        }, error -> showError(error.getMessage())));
        return parent;
    }

    private void loadMap(Map map) {
        System.out.println("Loading map: " + map);
        final String mapName = getFileName(map.tilesets().get(0).source());
        disposables.add(presetsService.getTilesetImage(mapName).observeOn(FX_SCHEDULER).subscribe(image -> {
            if (image != null) {
                renderMap(map, image);
            } else {
                System.out.println("Image is null");
            }
        }, error -> showError(error.getMessage())));
    }

    private void renderMap(Map map, Image image) {
        app.getStage().setWidth(map.width() * TILE_SIZE);
        app.getStage().setHeight(map.height() * TILE_SIZE);
        int tilesPerRow = (int) (image.getWidth() / TILE_SIZE);
        for (Layer layer : map.layers()) {
            if (!layer.type().equals("tilelayer")) {
                continue;
            }
            for (Chunk chunk : layer.chunks()) {
                for (int y = 0; y < chunk.height(); y++) {
                    for (int x = 0; x < chunk.width(); x++) {
                        int tileId = chunk.data().get(y * chunk.width() + x);
                        if (tileId == 0) {
                            continue;
                        }
                        int tileX = ((tileId - 1) % tilesPerRow) * TILE_SIZE;
                        int tileY = ((tileId - 1) / tilesPerRow) * TILE_SIZE;
                        WritableImage writableImage = new WritableImage(image.getPixelReader(), tileX, tileY, TILE_SIZE, TILE_SIZE);
                        ImageView imageView = new ImageView(writableImage);
                        imageView.setTranslateX((chunk.x() + x) * TILE_SIZE);
                        imageView.setTranslateY((chunk.y() + y) * TILE_SIZE);
                        ingamePane.getChildren().add(imageView);
                    }
                }
            }
        }
    }

    /**
     * This method is used to show the help screen.
     */

    public void showHelp() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(null);
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.initOwner(app.getStage());
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setContentText(resources.getString("HELP.LABEL"));
        final DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStyleClass().add("comicSans");
        dialogPane.setStyle(FX_STYLE_BORDER_COLOR_BLACK);
        alert.showAndWait();
    }

    /**
     * This method is used to pause the game. It shows a dialog with two buttons.
     * The first button is used to resume the game and the second button is used to save the game and leave.
     * If the user presses the resume button, the dialog will be closed.
     * If the user presses the save and leave button, the game will be saved and
     * the user will be redirected to the main menu.
     */

    public void pauseGame() {
        final Alert alert = new Alert(Alert.AlertType.NONE);
        final DialogPane dialogPane = alert.getDialogPane();
        final ButtonType resume = new ButtonType(resources.getString("RESUME.BUTTON.LABEL"));
        final ButtonType saveAndExit = new ButtonType(resources.getString("SAVE.GAME.AND.LEAVE.BUTTON.LABEL"));
        dialogPane.getButtonTypes().addAll(resume, saveAndExit);
        if (!GraphicsEnvironment.isHeadless()) {
            dialogPane.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("styles.css")).toString());
            dialogPane.getStyleClass().add("comicSans");
        }
        final Button resumeButton = (Button) dialogPane.lookupButton(resume);
        resumeButton.setOnKeyPressed(event -> {
            if (!(event.getCode() == PAUSE_MENU_KEY)) {
                return;
            }
            alert.setResult(resume);
        });

        alert.setTitle(resources.getString("PAUSE.MENU.TITLE"));
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setContentText(resources.getString("PAUSE.MENU.LABEL"));
        alert.initStyle(StageStyle.UNDECORATED);
        dialogPane.setStyle(FX_STYLE_BORDER_COLOR_BLACK);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == resume) {
            alert.close();
        } else if (result.isPresent() && result.get() == saveAndExit) {
            alert.close();
            app.getStage().getScene().setOnKeyPressed(null);
            app.show(mainMenuControllerProvider.get());
        }
    }

    public void showTrainerSettings() {
        Dialog<?> trainerSettingsDialog = new Dialog<>();
        trainerSettingsDialog.setTitle(resources.getString("Trainer.Profil"));
        trainerSettingsDialog.getDialogPane().setContent(ingameTrainerSettingsControllerProvider.get().render());
        trainerSettingsDialog.getDialogPane().setExpandableContent(null);
        trainerSettingsDialog.getDialogPane().getStylesheets().add(Objects.requireNonNull(Main.class.getResource("styles.css")).toString());
        trainerSettingsDialog.getDialogPane().getStyleClass().add("trainerSettingsDialog");
        Window popUp = trainerSettingsDialog.getDialogPane().getScene().getWindow();
        popUp.setOnCloseRequest(evt ->
                ((Stage) trainerSettingsDialog.getDialogPane().getScene().getWindow()).close()
        );
        trainerSettingsDialog.showAndWait();
    }
}
