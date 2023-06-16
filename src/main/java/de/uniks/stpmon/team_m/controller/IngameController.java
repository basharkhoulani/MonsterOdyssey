package de.uniks.stpmon.team_m.controller;


import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.controller.subController.IngameMessageCell;
import de.uniks.stpmon.team_m.controller.subController.IngameTrainerSettingsController;
import de.uniks.stpmon.team_m.controller.subController.MonstersListController;
import de.uniks.stpmon.team_m.dto.*;
import de.uniks.stpmon.team_m.service.AreasService;
import de.uniks.stpmon.team_m.service.MessageService;
import de.uniks.stpmon.team_m.service.PresetsService;
import de.uniks.stpmon.team_m.service.TrainersService;
import de.uniks.stpmon.team_m.udp.UDPEventListener;
import de.uniks.stpmon.team_m.utils.ImageProcessor;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import de.uniks.stpmon.team_m.ws.EventListener;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;

import javax.inject.Inject;
import javax.inject.Provider;
import java.awt.*;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

import static de.uniks.stpmon.team_m.Constants.*;


public class IngameController extends Controller {

    @FXML
    public ImageView playerSpriteImageView;
    @FXML
    public VBox ingameVBoxPlayer;
    @FXML
    public Button helpSymbol;
    @FXML
    public Button monstersButton;
    @FXML
    public Button settingsButton;
    @FXML
    public VBox ingameVBox;
    @FXML
    public StackPane ingameStackPane;
    @FXML
    public TextField messageField;
    @FXML
    public Button showChatButton;
    @FXML
    public Button sendMessageButton;
    @FXML
    public ListView<Message> chatListView;
    @FXML
    public BorderPane borderPane;
    @Inject
    Provider<IngameTrainerSettingsController> ingameTrainerSettingsControllerProvider;
    @Inject
    Provider<EventListener> eventListener;
    @Inject
    Provider<MainMenuController> mainMenuControllerProvider;
    @Inject
    Provider<TrainerStorage> trainerStorageProvider;
    @Inject
    AreasService areasService;
    @Inject
    PresetsService presetsService;
    @Inject
    MessageService messageService;
    @Inject
    TrainersService trainersService;
    @Inject
    TrainerStorage trainerStorage;
    @Inject
    Provider<UDPEventListener> udpEventListenerProvider;
    @FXML
    public Canvas groundCanvas;
    @FXML
    public Canvas trainerCanvas;
    @FXML
    public Canvas overTrainerCanvas;
    public static final KeyCode PAUSE_MENU_KEY = KeyCode.P;
    private boolean isChatting = false;
    @Inject
    Provider<MonstersListController> monstersListControllerProvider;
    private final ObservableList<MoveTrainerDto> moveTrainerDtos = FXCollections.observableArrayList();
    HashMap<String, Image> tileSetImages = new HashMap<>();
    HashMap<String, TileSet> tileSetJsons = new HashMap<>();
    private Timeline spriteWalkingAnimation;
    private Timeline spriteStandingAnimation;
    private Image[] trainerStandingDown;
    private Image[] trainerStandingUp;
    private Image[] trainerStandingLeft;
    private Image[] trainerStandingRight;
    private Image[] trainerWalkingUp;
    private Image[] trainerWalkingDown;
    private Image[] trainerWalkingLeft;
    private Image[] trainerWalkingRight;
    private final ObservableList<Message> messages = FXCollections.observableArrayList();
    private ObservableList<Trainer> trainers;

    private EventHandler<KeyEvent> keyPressedEventHandler;
    private EventHandler<KeyEvent> keyReleasedEventHandler;

    /**
     * IngameController is used to show the In-Game screen and to pause the game.
     */

    @Inject
    public IngameController() {
    }

    @Override
    public void init() {
        super.init();
        // Image arrays for sprite animations
        trainerStandingDown = ImageProcessor.cropTrainerImages(trainerStorage.getTrainerSpriteChunk(), "down", false);
        trainerStandingUp = ImageProcessor.cropTrainerImages(trainerStorage.getTrainerSpriteChunk(), "up", false);
        trainerStandingLeft = ImageProcessor.cropTrainerImages(trainerStorage.getTrainerSpriteChunk(), "left", false);
        trainerStandingRight = ImageProcessor.cropTrainerImages(trainerStorage.getTrainerSpriteChunk(), "right", false);
        trainerWalkingUp = ImageProcessor.cropTrainerImages(trainerStorage.getTrainerSpriteChunk(), "up", true);
        trainerWalkingDown = ImageProcessor.cropTrainerImages(trainerStorage.getTrainerSpriteChunk(), "down", true);
        trainerWalkingLeft = ImageProcessor.cropTrainerImages(trainerStorage.getTrainerSpriteChunk(), "left", true);
        trainerWalkingRight = ImageProcessor.cropTrainerImages(trainerStorage.getTrainerSpriteChunk(), "right", true);
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
     * This method is used to play the stay animation for the given direction for the trainer character
     *
     * @param direction - either "up", "down", "left" or "right" are valid directions
     */
    private void stay(String direction) {
        if (!GraphicsEnvironment.isHeadless()) {
            spriteWalkingAnimation.stop();
            switch (direction) {
                case "up" -> {
                    spriteStandingAnimation = getSpriteAnimationTimeLine(trainerStandingUp, false);
                    spriteStandingAnimation.play();
                }
                case "down" -> {
                    spriteStandingAnimation = getSpriteAnimationTimeLine(trainerStandingDown, false);
                    spriteStandingAnimation.play();
                }
                case "left" -> {
                    spriteStandingAnimation = getSpriteAnimationTimeLine(trainerStandingLeft, false);
                    spriteStandingAnimation.play();
                }
                case "right" -> {
                    spriteStandingAnimation = getSpriteAnimationTimeLine(trainerStandingRight, false);
                    spriteStandingAnimation.play();
                }
                default -> {
                }
            }
        }
    }

    /**
     * This method is used to play the walk animation for the given direction for the trainer character and move the map
     *
     * @param direction - either "up", "down", "left" or "right" are valid directions
     */
    private void walk(String direction) {
        if (!GraphicsEnvironment.isHeadless()) {
            if (spriteStandingAnimation != null) {
                spriteStandingAnimation.stop();
            }
            switch (direction) {
                case "up" -> {
                    spriteWalkingAnimation = getSpriteAnimationTimeLine(trainerWalkingUp, true);
                    spriteWalkingAnimation.play();
                    groundCanvas.setTranslateY(groundCanvas.getTranslateY() + TILE_SIZE / 2.0);
                    overTrainerCanvas.setTranslateY(overTrainerCanvas.getTranslateY() + TILE_SIZE / 2.0);
                }
                case "down" -> {
                    spriteWalkingAnimation = getSpriteAnimationTimeLine(trainerWalkingDown, true);
                    spriteWalkingAnimation.play();
                    groundCanvas.setTranslateY(groundCanvas.getTranslateY() - TILE_SIZE / 2.0);
                    overTrainerCanvas.setTranslateY(overTrainerCanvas.getTranslateY() - TILE_SIZE / 2.0);
                }
                case "left" -> {
                    spriteWalkingAnimation = getSpriteAnimationTimeLine(trainerWalkingLeft, true);
                    spriteWalkingAnimation.play();
                    groundCanvas.setTranslateX(groundCanvas.getTranslateX() + TILE_SIZE / 2.0);
                    overTrainerCanvas.setTranslateX(overTrainerCanvas.getTranslateX() + TILE_SIZE / 2.0);
                }
                case "right" -> {
                    spriteWalkingAnimation = getSpriteAnimationTimeLine(trainerWalkingRight, true);
                    spriteWalkingAnimation.play();
                    groundCanvas.setTranslateX(groundCanvas.getTranslateX() - TILE_SIZE / 2.0);
                    overTrainerCanvas.setTranslateX(overTrainerCanvas.getTranslateX() - TILE_SIZE / 2.0);
                }
                default -> {
                }
            }
        }
    }

    /**
     * This method is used to render the In-Game screen.
     *
     * @return Parent of the In-Game screen.
     */

    @Override
    public Parent render() {
        final Parent parent = super.render();
        trainerStorageProvider.get().setX(trainerStorageProvider.get().getTrainer().x());
        trainerStorageProvider.get().setY(trainerStorageProvider.get().getTrainer().y());
        trainerStorageProvider.get().setDirection(trainerStorageProvider.get().getTrainer().direction());
        listenToMovement(moveTrainerDtos, trainerStorageProvider.get().getTrainer().area());

        // Setup trainers
        disposables.add(trainersService.getTrainers(trainerStorageProvider.get().getRegion()._id(), null, null).observeOn(FX_SCHEDULER).subscribe(
                trainers -> {
                    this.trainers = FXCollections.observableArrayList(trainers);
                    listenToTrainers(this.trainers);
                }, error -> showError(error.getMessage())));

        // Setup chat
        messageField.addEventHandler(KeyEvent.KEY_PRESSED, this::enterButtonPressedToSend);
        listenToMessages(trainerStorageProvider.get().getTrainer().region());
        chatListView.setItems(messages);
        chatListView.setCellFactory(param -> new IngameMessageCell(this));
        chatListView.setPlaceholder(new Label(resources.getString("NO.MESSAGES.YET")));
        chatListView.setFocusModel(null);
        chatListView.setSelectionModel(null);

        // Start standing animation
        playerSpriteImageView.setScaleX(SCALE_FACTOR);
        playerSpriteImageView.setScaleY(SCALE_FACTOR);
        playerSpriteImageView.relocate(trainerStorage.getX(), trainerStorage.getY());
        spriteStandingAnimation = getSpriteAnimationTimeLine(trainerStandingDown, false);
        if (!GraphicsEnvironment.isHeadless()) {
            spriteStandingAnimation.play();
        }
        keyPressedEventHandler = evt -> {
            evt.consume();
            if (isChatting) {
                return;
            }

            if (spriteStandingAnimation != null) {
                spriteStandingAnimation.stop();
            }
            if (evt.getCode() == KeyCode.ENTER) {
                messageField.requestFocus();
                isChatting = true;
            }
            if (evt.getCode() == PAUSE_MENU_KEY) {
                pauseGame();
            }
            if ((evt.getCode() == KeyCode.W)) {
                walk("up");
            }
            if ((evt.getCode() == KeyCode.S)) {
                walk("down");
            }
            if ((evt.getCode() == KeyCode.A)) {
                walk("left");
            }
            if ((evt.getCode() == KeyCode.D)) {
                walk("right");
            }
        };

        keyReleasedEventHandler = evt -> {
            evt.consume();
            if (isChatting) {
                return;
            }
            if (!GraphicsEnvironment.isHeadless()) {
                if (spriteWalkingAnimation != null) {
                    spriteWalkingAnimation.stop();
                }
                if ((evt.getCode() == KeyCode.W)) {
                    stay("up");
                }
                if ((evt.getCode() == KeyCode.S)) {
                    stay("down");
                }
                if ((evt.getCode() == KeyCode.A)) {
                    stay("left");
                }
                if ((evt.getCode() == KeyCode.D)) {
                    stay("right");
                }
            }

        };

        app.getStage().getScene().addEventHandler(KeyEvent.KEY_PRESSED, keyPressedEventHandler);
        app.getStage().getScene().addEventHandler(KeyEvent.KEY_RELEASED, keyReleasedEventHandler);
        Region region = trainerStorageProvider.get().getRegion();
        disposables.add(areasService.getArea(region._id(), trainerStorageProvider.get().getTrainer().area()).observeOn(FX_SCHEDULER).subscribe(area -> loadMap(area.map()), error -> showError(error.getMessage())));
        monstersListControllerProvider.get().init();
        return parent;
    }

    /**
     * This method returns a timeline animation for the trainer character
     *
     * @param movementImages are the images to be rapidly switched through
     * @param isWalking      determines if the walking - or standing animation is requested.
     */
    private Timeline getSpriteAnimationTimeLine(Image[] movementImages, Boolean isWalking) {
        Timeline timeline;// Calculate the index of the next image
        if (isWalking) {
            timeline = new Timeline(
                    new KeyFrame(Duration.millis(66), e -> {
                        // Calculate the index of the next image
                        int currentIndex = (int) (System.currentTimeMillis() / 66 % 6);
                        playerSpriteImageView.setImage(movementImages[currentIndex]);
                    })
            );
            timeline.setCycleCount(2);
        } else {
            timeline = new Timeline(
                    new KeyFrame(Duration.millis(300), e -> {
                        // Calculate the index of the next image
                        int currentIndex = (int) (System.currentTimeMillis() / 300 % 6);
                        playerSpriteImageView.setImage(movementImages[currentIndex]);
                    })
            );
            timeline.setCycleCount(Animation.INDEFINITE);
        }
        return timeline;
    }

    public void listenToMovement(ObservableList<MoveTrainerDto> moveTrainerDtos, String area) {
        disposables.add(udpEventListenerProvider.get().listen("areas." + area + ".trainers.*.*", MoveTrainerDto.class)
                .observeOn(FX_SCHEDULER).subscribe(event -> {
                    final MoveTrainerDto moveTrainerDto = event.data();
                    moveTrainerDtos.add(moveTrainerDto);
                    if (moveTrainerDto._id().equals(trainerStorageProvider.get().getTrainer()._id())) {
                        trainerStorageProvider.get().setX(moveTrainerDto.x());
                        trainerStorageProvider.get().setY(moveTrainerDto.y());
                        trainerStorageProvider.get().setDirection(moveTrainerDto.direction());
                    }
                }, error -> showError(error.getMessage())));
    }

    /**
     * loadMap is used to load the map of the current area, given a Tiled Map. It loads every image of every tileset, then
     * calls afterAllTileSetsLoaded to render the map. It also sets the size of the stage to the size of the map.
     *
     * @param map Tiled Map of the current area.
     */

    private void loadMap(Map map) {
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }
        tileSetImages.clear();
        for (TileSet tileSet : map.tilesets()) {
            final String mapName = getFileName(tileSet.source());
            disposables.add(presetsService.getTilesetImage(mapName)
                    .doOnNext(image -> tileSetImages.put(mapName, image))
                    .flatMap(image -> presetsService.getTileset(mapName).observeOn(FX_SCHEDULER))
                    .doOnNext(tileset -> tileSetJsons.put(mapName, tileset))
                    .observeOn(FX_SCHEDULER).subscribe(image -> afterAllTileSetsLoaded(map), error -> showError(error.getMessage())));
        }
    }

    /**
     * afterAllTileSetsLoaded is used to render the map. It renders every chunk of every layer of every tileset. After
     * rendering the map, it calls loadPlayer to load the player on the map.
     *
     * @param map Tiled Map of the current area.
     */

    private void afterAllTileSetsLoaded(Map map) {
        if ((tileSetImages.size() + tileSetJsons.size()) == 2 * map.tilesets().size()) {
            app.getStage().setWidth(Math.max(getWidth(), map.width() * TILE_SIZE) + OFFSET_WIDTH);
            app.getStage().setHeight(Math.max(getHeight(), map.height() * TILE_SIZE) + OFFSET_HEIGHT);
            groundCanvas.setWidth(map.width() * TILE_SIZE);
            groundCanvas.setHeight(map.height() * TILE_SIZE);
            groundCanvas.setScaleX(SCALE_FACTOR);
            groundCanvas.setScaleY(SCALE_FACTOR);
            overTrainerCanvas.setWidth(map.width() * TILE_SIZE);
            overTrainerCanvas.setHeight(map.height() * TILE_SIZE);
            overTrainerCanvas.setScaleX(SCALE_FACTOR);
            overTrainerCanvas.setScaleY(SCALE_FACTOR);
            for (TileSet tileSet : map.tilesets()) {
                renderMap(map, tileSetImages.get(getFileName(tileSet.source())), tileSetJsons.get(getFileName(tileSet.source())),
                        tileSet, map.tilesets().size() > 1);
            }
        }
    }

    /**
     * renderMap is used to render the map. It renders every chunk of every layer of the map. It skips every layer that
     * is not a tilelayer. It calls renderChunk to render every chunk.
     *
     * @param map              Tiled Map of the current area.
     * @param image            Image of the current tileset.
     * @param tileSet          Current tileset.
     * @param multipleTileSets Boolean that is true if there are multiple tilesets.
     */

    private void renderMap(Map map, Image image, TileSet tileSetJson, TileSet tileSet, boolean multipleTileSets) {
        for (Layer layer : map.layers()) {
            if (layer.chunks() == null) {
                continue;
            }
            for (Chunk chunk : layer.chunks()) {
                renderChunk(map, image, tileSet, tileSetJson, multipleTileSets, chunk);
            }
        }
    }

    /**
     * renderChunk is used to render a chunk of the map. It renders every tile of the chunk. It skips every tile that
     * is not in the current tileset. It calls extractTile to extract the image of the tile.
     *
     * @param map              Tiled Map of the current area.
     * @param image            Image of the current tileset.
     * @param tileSet          Current tileset.
     * @param multipleTileSets Boolean that is true if there are multiple tilesets.
     * @param chunk            Current chunk.
     */

    private void renderChunk(Map map, Image image, TileSet tileSet, TileSet tileSetJson, boolean multipleTileSets, Chunk chunk) {
        WritableImage writableImageGround = new WritableImage(chunk.width() * TILE_SIZE, chunk.height() * TILE_SIZE);
        WritableImage writableImageTop = new WritableImage(chunk.width() * TILE_SIZE, chunk.height() * TILE_SIZE);
        for (int y = 0; y < chunk.height(); y++) {
            for (int x = 0; x < chunk.width(); x++) {
                int tileId = chunk.data().get(y * chunk.width() + x);
                if (tileId == 0) {
                    continue;
                }
                if (multipleTileSets) if (checkIfNotInTileSet(map, tileSet, tileId)) continue;

                int tilesPerRow = (int) (image.getWidth() / TILE_SIZE);
                int tileX = ((tileId - tileSet.firstgid()) % tilesPerRow) * TILE_SIZE;
                int tileY = ((tileId - tileSet.firstgid()) / tilesPerRow) * TILE_SIZE;
                if (isRoof(tileSet, tileSetJson, tileId)) {
                    writableImageTop.getPixelWriter().setPixels(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE,
                            image.getPixelReader(), tileX, tileY);
                } else {
                    writableImageGround.getPixelWriter().setPixels(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE,
                            image.getPixelReader(), tileX, tileY);
                }
            }
        }
        groundCanvas.getGraphicsContext2D().drawImage(writableImageGround, chunk.x() * TILE_SIZE, chunk.y() * TILE_SIZE);
        overTrainerCanvas.getGraphicsContext2D().drawImage(writableImageTop, chunk.x() * TILE_SIZE, chunk.y() * TILE_SIZE);
    }

    private boolean isRoof(TileSet tileSet, TileSet tileSetJson, int tileId) {
        if (tileSetJson.tiles() == null) {
            return false;
        }
        for (TileObject tile : tileSetJson.tiles()) {
            if (tile.id() == tileId - tileSet.firstgid()) {
                if (tile.properties() == null) {
                    return false;
                }
                return tile.properties().stream().anyMatch(property -> property.name().equals("Roof") && property.value().equals("true"));
            }
        }
        return false;
    }

    /**
     * checkIfNotInTileSet is used to check if a tile is not in the current tileset.
     *
     * @param map     Tiled Map of the current area.
     * @param tileSet Current tileset.
     * @param tileId  ID of the tile.
     * @return Boolean that is true if the tile is not in the current tileset.
     */

    private boolean checkIfNotInTileSet(Map map, TileSet tileSet, int tileId) {
        int tileSetIndex = map.tilesets().indexOf(tileSet);
        if (tileSetIndex < map.tilesets().size() - 1) {
            TileSet nextTileSet = map.tilesets().get(tileSetIndex + 1);
            return tileId >= nextTileSet.firstgid();
        } else return tileId < tileSet.firstgid();
    }

    /**
     * getFileName is used to get the name of the file.
     *
     * @param name Name of the file.
     * @return Name of the file.
     */

    private static String getFileName(String name) {
        name = name.substring(name.lastIndexOf("/") + 1);
        name = name.substring(0, name.lastIndexOf("."));
        return name;
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
            app.getStage().getScene().setOnKeyReleased(null);
            destroy();
            app.show(mainMenuControllerProvider.get());
        }
    }

    public void showTrainerSettings() {
        Dialog<?> trainerSettingsDialog = new Dialog<>();
        trainerSettingsDialog.setTitle(resources.getString("TRAINER.PROFIL"));
        ingameTrainerSettingsControllerProvider.get().setApp(this.app);
        ingameTrainerSettingsControllerProvider.get().setValues(resources, preferences, resourceBundleProvider, ingameTrainerSettingsControllerProvider.get(), app);
        ingameTrainerSettingsControllerProvider.get().setIngameController(this);
        trainerSettingsDialog.getDialogPane().setContent(ingameTrainerSettingsControllerProvider.get().render());
        trainerSettingsDialog.getDialogPane().setExpandableContent(null);
        trainerSettingsDialog.getDialogPane().getStylesheets().add(Objects.requireNonNull(Main.class.getResource("styles.css")).toString());
        trainerSettingsDialog.getDialogPane().getStyleClass().add("trainerSettingsDialog");
        trainerSettingsDialog.initOwner(app.getStage());
        Window popUp = trainerSettingsDialog.getDialogPane().getScene().getWindow();
        popUp.setOnCloseRequest(evt -> {
                    ((Stage) trainerSettingsDialog.getDialogPane().getScene().getWindow()).close();
                    groundCanvas.requestFocus();
                }
        );
        trainerSettingsDialog.showAndWait();
    }

    public void sendMessageButton() {
        sendMessage();
    }

    private void sendMessage() {
        if (messageField.getText().isEmpty()) {
            groundCanvas.requestFocus();
            isChatting = false;
            return;
        }
        String regionID = trainerStorageProvider.get().getRegion()._id();
        if (regionID != null) {
            String messageBody = messageField.getText();
            disposables.add(messageService.newMessage(regionID, messageBody, MESSAGE_NAMESPACE_REGIONS).observeOn(FX_SCHEDULER).subscribe(message -> {
                messageField.setText(EMPTY_STRING);
                isChatting = false;
                groundCanvas.requestFocus();
            }, error -> showError(error.getMessage())));
        }
    }

    private void enterButtonPressedToSend(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            event.consume();
            sendMessage();
        }
    }

    public void paneClicked() {
        groundCanvas.requestFocus();
        isChatting = false;
    }

    public void messageFieldClicked() {
        messageField.requestFocus();
        isChatting = true;
    }

    public void showChat() {
        if (chatListView.getOpacity() == ZERO) {
            chatListView.setOpacity(ONE);
        } else {
            chatListView.setOpacity(ZERO);
        }
    }

    public void listenToTrainers(ObservableList<Trainer> trainers) {
        disposables.add(eventListenerProvider.get().listen("regions." + trainerStorageProvider.get().getRegion()._id() + ".trainers.*.*", Trainer.class).observeOn(FX_SCHEDULER).subscribe(event -> {
                    final Trainer trainer = event.data();
                    switch (event.suffix()) {
                        case "created" -> trainers.add(trainer);
                        case "updated" -> updateTrainer(trainers, trainer);
                        case "deleted" -> trainers.removeIf(t -> t._id().equals(trainer._id()));
                    }
                }, error -> showError(error.getMessage()))
        );
    }

    public void listenToMessages(String id) {
        disposables.add(eventListener.get().listen("regions." + id + ".messages.*.*", Message.class)
                .observeOn(FX_SCHEDULER).subscribe(event -> {
                    final Message message = event.data();
                    switch (event.suffix()) {
                        case "created" -> {
                            for (Message m : messages) {
                                if (m._id().equals(message._id())) {
                                    return;
                                }
                            }
                            messages.add(message);
                            chatListView.scrollTo(chatListView.getItems().size() - 1);
                        }
                        case "updated" -> updateMessage(messages, message);
                        case "deleted" -> messages.removeIf(m -> m._id().equals(message._id()));
                    }
                }, error -> showError(error.getMessage())));
    }

    private void updateTrainer(ObservableList<Trainer> trainers, Trainer trainer) {
        String trainerId = trainer._id();
        trainers.stream().filter(t -> t._id().equals(trainerId)).findFirst().ifPresent(t -> trainers.set(trainers.indexOf(t), trainer));
    }

    private void updateMessage(ObservableList<Message> messages, Message message) {
        String messageID = message._id();
        messages.stream()
                .filter(m -> m._id().equals(messageID))
                .findFirst()
                .ifPresent(m -> messages.set(messages.indexOf(m), message));
    }

    public Trainer getTrainer(String userId) {
        for (Trainer trainer : trainers) {
            if (trainer.user().equals(userId)) {
                return trainer;
            }
        }
        return null;
    }

    public void setTrainerSpriteImageView(Trainer trainer, ImageView imageView) {
        if (!GraphicsEnvironment.isHeadless()) {
            disposables.add(presetsService.getCharacter(trainer.image()).observeOn(FX_SCHEDULER).subscribe(responseBody -> {
                        Image trainerSprite = ImageProcessor.resonseBodyToJavaFXImage(responseBody);
                        Image[] character = ImageProcessor.cropTrainerImages(trainerSprite, "down", false);
                        imageView.setImage(character[0]);
                    }, error -> showError(error.getMessage())
            ));
        }
    }

    public void showMonsters() {
        Scene scene = new Scene(monstersListControllerProvider.get().render());
        Stage popupStage = new Stage();
        popupStage.initOwner(app.getStage());
        popupStage.setScene(scene);
        popupStage.setTitle(resources.getString("MONSTERS"));
        popupStage.show();
    }

    public Map getMiniMap() {
        return null;
    }

    @Override
    public void destroy() {
        super.destroy();
        app.getStage().getScene().removeEventHandler(KeyEvent.KEY_PRESSED, keyPressedEventHandler);
        app.getStage().getScene().removeEventHandler(KeyEvent.KEY_RELEASED, keyReleasedEventHandler);
        messageField.removeEventHandler(KeyEvent.KEY_PRESSED, this::enterButtonPressedToSend);
    }
}
