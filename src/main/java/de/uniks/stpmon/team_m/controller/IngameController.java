package de.uniks.stpmon.team_m.controller;


import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.controller.subController.IngameMessageCell;
import de.uniks.stpmon.team_m.controller.subController.IngameTrainerSettingsController;
import de.uniks.stpmon.team_m.dto.*;
import de.uniks.stpmon.team_m.service.AreasService;
import de.uniks.stpmon.team_m.service.MessageService;
import de.uniks.stpmon.team_m.service.PresetsService;
import de.uniks.stpmon.team_m.service.TrainersService;
import de.uniks.stpmon.team_m.udp.UDPEventListener;
import de.uniks.stpmon.team_m.utils.SpriteAnimation;
import de.uniks.stpmon.team_m.utils.ImageProcessor;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import de.uniks.stpmon.team_m.ws.EventListener;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import de.uniks.stpmon.team_m.utils.ImageProcessor;
import javafx.animation.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
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
    private static final int DELAY = 100;
    private static final int DELAY_LONG = 500;
    private static final int SCALE_FACTOR = 2;

    @FXML
    public ImageView playerSpriteImageView;
    @FXML
    public Button helpSymbol;
    @FXML
    public Button monstersButton;
    @FXML
    public Button settingsButton;
    @FXML
    public Canvas canvas;
    @FXML
    public VBox ingameVBox;
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
    GraphicsContext graphicsContext;
    public static final KeyCode PAUSE_MENU_KEY = KeyCode.P;
    private boolean isChatting = false;

    @Inject
    Provider<UDPEventListener> udpEventListenerProvider;
    @Inject
    TrainersService trainersService;

    private final ObservableList<MoveTrainerDto> moveTrainerDtos = FXCollections.observableArrayList();
    HashMap<String, Image> tileSetImages = new HashMap<>();


    private SpriteAnimation trainerSpriteAnimation;
    private Timeline mapMovementTransition;

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

    private Long lastKeyEventTimeStamp;
    private EventHandler<KeyEvent> keyReleasedHandler;
    private EventHandler<KeyEvent> keyPressedHandler;

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
        trainerStandingDown = ImageProcessor.cropTrainerImages(trainerStorageProvider.get().getTrainerSpriteChunk(), 2, false);
        trainerStandingUp = ImageProcessor.cropTrainerImages(trainerStorageProvider.get().getTrainerSpriteChunk(), 0, false);
        trainerStandingLeft = ImageProcessor.cropTrainerImages(trainerStorageProvider.get().getTrainerSpriteChunk(), 3, false);
        trainerStandingRight = ImageProcessor.cropTrainerImages(trainerStorageProvider.get().getTrainerSpriteChunk(), 1, false);
        trainerWalkingUp = ImageProcessor.cropTrainerImages(trainerStorageProvider.get().getTrainerSpriteChunk(), 0, true);
        trainerWalkingDown = ImageProcessor.cropTrainerImages(trainerStorageProvider.get().getTrainerSpriteChunk(), 2, true);
        trainerWalkingLeft = ImageProcessor.cropTrainerImages(trainerStorageProvider.get().getTrainerSpriteChunk(), 3, true);
        trainerWalkingRight = ImageProcessor.cropTrainerImages(trainerStorageProvider.get().getTrainerSpriteChunk(), 1, true);
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

        // Get images for the stand animation of the trainers direction
        Image[] initialImages;
        switch (trainerStorageProvider.get().getDirection()) {
            case 0 -> initialImages = trainerStandingUp;
            case 1 -> initialImages = trainerStandingLeft;
            case 3 -> initialImages = trainerStandingRight;
            default -> initialImages = trainerStandingDown;
        }

        // Create and start standing animation
        trainerSpriteAnimation = new SpriteAnimation(DELAY_LONG, playerSpriteImageView, initialImages);
        if (!GraphicsEnvironment.isHeadless()) {
            trainerSpriteAnimation.start();
        }

        playerSpriteImageView.setScaleX(SCALE_FACTOR);
        playerSpriteImageView.setScaleY(SCALE_FACTOR);

        // Initialize key event listeners
        keyPressedHandler = event -> {
            if (isChatting) {
                return;
            }
            if (lastKeyEventTimeStamp != null) {
                if (System.currentTimeMillis() - lastKeyEventTimeStamp < DELAY) {
                    return;
                }
                System.out.println("Time since last key event : " + (System.currentTimeMillis() - lastKeyEventTimeStamp) + " ms");
            }
            lastKeyEventTimeStamp = System.currentTimeMillis();

            if (event.getCode() == PAUSE_MENU_KEY) {
                pauseGame();
            }
            if ((event.getCode() == KeyCode.W)) {
                /*
                disposables.add(udpEventListenerProvider.get().move(new MoveTrainerDto(trainerStorageProvider.get().getTrainer()._id(),
                        trainerStorageProvider.get().getTrainer().area(),
                        trainerStorageProvider.get().getX(), trainerStorageProvider.get().getY() - 1, 0)).subscribe());
                */
                walk(0);

            }
            if ((event.getCode() == KeyCode.S)) {
                /*
                disposables.add(udpEventListenerProvider.get().move(new MoveTrainerDto(trainerStorageProvider.get().getTrainer()._id(),
                        trainerStorageProvider.get().getTrainer().area(),
                        trainerStorageProvider.get().getX(), trainerStorageProvider.get().getY() + 1, 2)).subscribe());
                 */
                walk(2);
            }
            if ((event.getCode() == KeyCode.A)) {
                /*
                disposables.add(udpEventListenerProvider.get().move(new MoveTrainerDto(trainerStorageProvider.get().getTrainer()._id(),
                        trainerStorageProvider.get().getTrainer().area(),
                        trainerStorageProvider.get().getX() - 1, trainerStorageProvider.get().getY(), 1)).subscribe());
                 */
                walk(3);
            }
            if ((event.getCode() == KeyCode.D)) {
                /*
                disposables.add(udpEventListenerProvider.get().move(new MoveTrainerDto(trainerStorageProvider.get().getTrainer()._id(),
                        trainerStorageProvider.get().getTrainer().area(),
                        trainerStorageProvider.get().getX() + 1, trainerStorageProvider.get().getY(), 3)).subscribe());
                 */
                walk(1);
            }
        };


        keyReleasedHandler = event -> {
            if (isChatting) {
                return;
            }
            if ((event.getCode() == KeyCode.W)) {
                stay(0);
            }
            if ((event.getCode() == KeyCode.S)) {
                stay(2);
            }
            if ((event.getCode() == KeyCode.A)) {
                stay(3);
            }
            if ((event.getCode() == KeyCode.D)) {
                stay(1);
            }
        };

        // Add event handlers
        app.getStage().getScene().addEventHandler(KeyEvent.KEY_PRESSED, keyPressedHandler);
        app.getStage().getScene().addEventHandler(KeyEvent.KEY_RELEASED, keyReleasedHandler);

        Region region = trainerStorageProvider.get().getRegion();
        disposables.add(areasService.getArea(region._id(), trainerStorageProvider.get().getTrainer().area()).observeOn(FX_SCHEDULER)
                .subscribe(area -> loadMap(area.map()), error -> showError(error.getMessage())));
        canvas.requestFocus();
        return parent;
    }


    /**
     * This method is used to play the stay animation for the given direction for the trainer character
     *
     * @param direction - either 0 [up], 1 [right], 2 [down], or 3 [left] are valid directions
     */
    private void stay(int direction) {
        if (!GraphicsEnvironment.isHeadless()) {
            if (mapMovementTransition != null) {
                mapMovementTransition.stop();
            }
            trainerSpriteAnimation.setDuration(DELAY_LONG);
            switch (direction) {
                case 0 -> trainerSpriteAnimation.setImages(trainerStandingUp);
                case 1 -> trainerSpriteAnimation.setImages(trainerStandingRight);
                case 2 -> trainerSpriteAnimation.setImages(trainerStandingDown);
                case 3 -> trainerSpriteAnimation.setImages(trainerStandingLeft);
                default -> {
                }
            }
        }
    }

    /**
     * This method is used to play the walk animation for the given direction for the trainer character and move the map
     *
     * @param direction - either 0 [up], 1 [right], 2 [down], or 3 [left] are valid directions
     */
    private void walk(int direction) {
        if (!GraphicsEnvironment.isHeadless()) {
            trainerSpriteAnimation.setDuration(DELAY);
            switch (direction) {
                case 0 -> {
                    trainerSpriteAnimation.setImages(trainerWalkingUp);
                    mapMovementTransition = getMapMovementTransition(canvas, 0, SCALE_FACTOR * TILE_SIZE, DELAY);
                    mapMovementTransition.play();
                }
                case 1 -> {
                    trainerSpriteAnimation.setImages(trainerWalkingRight);
                    mapMovementTransition = getMapMovementTransition(canvas, -SCALE_FACTOR * TILE_SIZE, 0, DELAY);
                    mapMovementTransition.play();
                }
                case 2 -> {
                    trainerSpriteAnimation.setImages(trainerWalkingDown);
                    mapMovementTransition = getMapMovementTransition(canvas, 0, -SCALE_FACTOR * TILE_SIZE, DELAY);
                    mapMovementTransition.play();
                }
                case 3 -> {
                    trainerSpriteAnimation.setImages(trainerWalkingLeft);
                    mapMovementTransition = getMapMovementTransition(canvas, SCALE_FACTOR * TILE_SIZE, 0, DELAY);
                    mapMovementTransition.play();
                }
                default -> {
                }
            }

        }
    }

    public void listenToMovement(ObservableList<MoveTrainerDto> moveTrainerDtos, String area) {
        disposables.add(udpEventListenerProvider.get().listen("areas." + area + ".trainers.*.*", MoveTrainerDto.class)
                .observeOn(FX_SCHEDULER).subscribe(event -> {
                    final MoveTrainerDto moveTrainerDto = event.data();
                    moveTrainerDtos.add(moveTrainerDto);
                    if (moveTrainerDto._id().equals(trainerStorageProvider.get().getTrainer()._id())) {
                        int oldXValue = trainerStorageProvider.get().getX();
                        int oldYValue = trainerStorageProvider.get().getY();
                        if (oldXValue != moveTrainerDto.x() || oldYValue != moveTrainerDto.y()) {
                            walk(moveTrainerDto.direction());
                        }
                        /*
                        if (oldXValue < moveTrainerDto.x()) {
                            walk(1);
                        } else if (oldXValue > moveTrainerDto.x()) {
                            walk(3);
                        } else if (oldYValue < moveTrainerDto.y()) {
                            walk(2);
                        } else if (oldYValue > moveTrainerDto.y()) {
                            walk(0);
                        }
                         */
                        else {
                            stay(moveTrainerDto.direction());
                        }
                        System.out.println("New position X: " + moveTrainerDto.x() + ", Y: " + moveTrainerDto.y() + ", direction: " + moveTrainerDto.direction());
                        trainerStorageProvider.get().setX(moveTrainerDto.x());
                        trainerStorageProvider.get().setY(moveTrainerDto.y());
                        trainerStorageProvider.get().setDirection(moveTrainerDto.direction());
                    }
                }, error -> showError(error.getMessage())));
    }

    /**
     * This function is used to remove the event handlers
     */
    private void unregisterListeners() {
        app.getStage().getScene().removeEventHandler(KeyEvent.KEY_PRESSED, keyPressedHandler);
        app.getStage().getScene().removeEventHandler(KeyEvent.KEY_RELEASED, keyReleasedHandler);
    }


    /**
     * This method returns a timeline animation for the trainer character
     *
     * @param movementImages are the images to be rapidly switched through
     * @param isWalking      determines if the walking - or standing animation is requested.
     */
    private Timeline getSpriteAnimationTimeLine(Image[] movementImages, Boolean isWalking) {
        if (isWalking) {
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.millis(66), e -> {
                        // Calculate the index of the next image
                        int currentIndex = (int) (System.currentTimeMillis() / 66 % 6);
                        playerSpriteImageView.setImage(movementImages[currentIndex]);
                    })
            );
            timeline.setCycleCount(2);
            return timeline;
        } else {
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.millis(300), e -> {
                        // Calculate the index of the next image
                        int currentIndex = (int) (System.currentTimeMillis() / 300 % 6);
                        playerSpriteImageView.setImage(movementImages[currentIndex]);
                    })
            );
            timeline.setCycleCount(Animation.INDEFINITE);
            return timeline;
        }
    }

    private Timeline getMapMovementTransition(Canvas map, int x, int y, int durationMillis) {
        return new Timeline(
                new KeyFrame(Duration.millis(durationMillis), e -> {
                    TranslateTransition translateTransition = new TranslateTransition();
                    translateTransition.setDuration(Duration.millis(durationMillis));
                    translateTransition.setNode(map);
                    translateTransition.setByX(x);
                    translateTransition.setByY(y);
                    translateTransition.play();
                })
        );
    }


    /**
     * loadMap is used to load the map of the current area, given a Tiled Map. It loads every image of every tileset, then
     * calls afterAllTileSetsLoaded to render the map. It also sets the size of the stage to the size of the map.
     *
     * @param map Tiled Map of the current area.
     */

    private void loadMap(Map map) {
        app.getStage().setWidth(Math.max(getWidth(), map.width() * TILE_SIZE) + OFFSET_WIDTH);
        app.getStage().setHeight(Math.max(getHeight(), map.height() * TILE_SIZE) + OFFSET_HEIGHT);
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }
        tileSetImages.clear();
        for (TileSet tileSet : map.tilesets()) {
            final String mapName = getFileName(tileSet.source());
            disposables.add(presetsService.getTilesetImage(mapName).observeOn(FX_SCHEDULER).subscribe(image -> {
                tileSetImages.put(mapName, image);
                afterAllTileSetsLoaded(map);
            }, error -> showError(error.getMessage())));
        }
        app.getStage().setX(0);
        app.getStage().setY(0);
        app.getStage().setWidth(Math.max(getWidth(), map.width() * TILE_SIZE) + OFFSET_WIDTH);
        app.getStage().setHeight(Math.max(getHeight(), map.height() * TILE_SIZE) + OFFSET_HEIGHT);
        System.out.println("Map data: \nWidth:  " + map.width() + " Tiles \nHeight: " + map.height() + " Tiles");
        System.out.println("Current player position: (" + trainerStorageProvider.get().getX() + ", " + trainerStorageProvider.get().getY() + "), direction: " + trainerStorageProvider.get().getDirection());

        // Shift map initially to match the trainers position
        getMapMovementTransition(canvas, (int) calculateInitialCameraXOffset(map.width()), (int) calculateInitialCameraYOffset(map.height()), DELAY).play();

    }

    /**
     * This method is used to calculate the initial x offset for the camera to center the player at the right position
     *
     * @param mapWidth - width of the rendered map (in tiles)
     * @return x offset that the map needs to be shifted
     */
    private double calculateInitialCameraXOffset(double mapWidth) {
        return -TILE_SIZE + (int) ((((mapWidth * TILE_SIZE) / (double) TILE_SIZE) / SCALE_FACTOR) - trainerStorageProvider.get().getX()) * TILE_SIZE * SCALE_FACTOR;
    }

    /**
     * This method is used to calculate the initial y offset for the camera to center the player at the right position
     *
     * @param mapHeight - height of the rendered map (in tiles)
     * @return y offset that the map needs to be shifted
     */
    private double calculateInitialCameraYOffset(double mapHeight) {
        return -TILE_SIZE + (int) (((((mapHeight * TILE_SIZE) / (double) TILE_SIZE) / SCALE_FACTOR) - trainerStorageProvider.get().getY()) * TILE_SIZE * SCALE_FACTOR) - (TILE_SIZE / (double) SCALE_FACTOR);
    }


    private void loadPlayers() {
        disposables.add(trainersService.getTrainers(trainerStorageProvider.get().getRegion()._id(), trainerStorageProvider.get().getTrainer().area(), null).observeOn(FX_SCHEDULER).subscribe(
                trainers -> {
                    for (Trainer trainer : trainers) {
                        if (!Objects.equals(trainerStorageProvider.get().getTrainer()._id(), trainer._id())) {
                            loadPlayer(trainer);
                        }
                    }
                },
                error -> {
                    showError(error.getMessage());
                    error.printStackTrace();
                }
        ));
    }


    /**
     * loadPlayer is used to load the player on the map. It loads the image of the player and sets its position.
     */
    private void loadPlayer(Trainer trainer) {
        // Other player characters
        String path;
        if (trainer.image().contains("Premade_Character")) {
            path = Objects.requireNonNull(Main.class.getResource("charactermodels/" + trainer.image())).toString();
        }
        // NPC characters
        else {
            // TODO: Add ressources needed for NPCs
            path = Objects.requireNonNull(Main.class.getResource("charactermodels/" + trainerStorageProvider.get().getTrainerSprite())).toString();
        }
        Image chunk = new Image(path);
        Image[] images = ImageProcessor.cropTrainerImages(chunk, trainer.direction(), false);
        graphicsContext.drawImage(images[0], trainer.x() * TILE_SIZE, trainer.y() * TILE_SIZE);
    }

    /**
     * afterAllTileSetsLoaded is used to render the map. It renders every chunk of every layer of every tileset. After
     * rendering the map, it calls loadPlayer to load the player on the map.
     *
     * @param map Tiled Map of the current area.
     */

    private void afterAllTileSetsLoaded(Map map) {
        canvas.setScaleX(2.0);
        canvas.setScaleY(2.0);
        canvas.setWidth(map.width() * TILE_SIZE);
        canvas.setHeight(map.height() * TILE_SIZE);
        if (tileSetImages.size() == map.tilesets().size()) {
            for (TileSet tileSet : map.tilesets()) {
                renderMap(map, tileSetImages.get(getFileName(tileSet.source())),
                        tileSet, map.tilesets().size() > 1);
            }
            loadPlayers();
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

    private void renderMap(Map map, Image image, TileSet tileSet, boolean multipleTileSets) {
        for (Layer layer : map.layers()) {
            if (layer.chunks() == null) {
                continue;
            }
            for (Chunk chunk : layer.chunks()) {
                renderChunk(map, image, tileSet, multipleTileSets, chunk);
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

    private void renderChunk(Map map, Image image, TileSet tileSet, boolean multipleTileSets, Chunk chunk) {
        graphicsContext = canvas.getGraphicsContext2D();
        for (int y = 0; y < chunk.height(); y++) {
            for (int x = 0; x < chunk.width(); x++) {
                int tileId = chunk.data().get(y * chunk.width() + x);
                if (tileId == 0) {
                    continue;
                }
                if (multipleTileSets) {
                    if (checkIfNotInTileSet(map, tileSet, tileId)) continue;
                }
                int tilesPerRow = (int) (image.getWidth() / TILE_SIZE);
                int tileX = ((tileId - tileSet.firstgid()) % tilesPerRow) * TILE_SIZE;
                int tileY = ((tileId - tileSet.firstgid()) / tilesPerRow) * TILE_SIZE;
                graphicsContext.drawImage(image, tileX, tileY, TILE_SIZE, TILE_SIZE,
                        (chunk.x() + x) * TILE_SIZE, (chunk.y() + y) * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
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
            unregisterListeners();
            app.getStage().getScene().setOnKeyPressed(null);
            app.show(mainMenuControllerProvider.get());
        }
    }

    public void showTrainerSettings() {
        Dialog<?> trainerSettingsDialog = new Dialog<>();
        trainerSettingsDialog.setTitle(resources.getString("TRAINER.PROFIL"));
        trainerSettingsDialog.getDialogPane().setContent(ingameTrainerSettingsControllerProvider.get().render());
        trainerSettingsDialog.getDialogPane().setExpandableContent(null);
        if (!GraphicsEnvironment.isHeadless()) {
            trainerSettingsDialog.getDialogPane().getStylesheets().add(Objects.requireNonNull(Main.class.getResource("styles.css")).toString());
            trainerSettingsDialog.getDialogPane().getStyleClass().add("trainerSettingsDialog");
        }
        Window popUp = trainerSettingsDialog.getDialogPane().getScene().getWindow();
        popUp.setOnCloseRequest(evt -> {
                    ((Stage) trainerSettingsDialog.getDialogPane().getScene().getWindow()).close();
                    canvas.requestFocus();
                }
        );
        trainerSettingsDialog.showAndWait();
    }

    public void sendMessageButton() {
        sendMessage();
    }

    private void sendMessage() {
        if (messageField.getText().isEmpty()) {
            canvas.requestFocus();
            isChatting = false;
            return;
        }
        String regionID = trainerStorageProvider.get().getRegion()._id();
        if (regionID != null) {
            String messageBody = messageField.getText();
            disposables.add(messageService.newMessage(regionID, messageBody, MESSAGE_NAMESPACE_REGIONS).observeOn(FX_SCHEDULER).subscribe(message -> {
                messageField.setText(EMPTY_STRING);
                isChatting = false;
                canvas.requestFocus();
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
        canvas.requestFocus();
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
                        Image[] character = ImageProcessor.cropTrainerImages(trainerSprite, 2, false);
                        imageView.setImage(character[0]);
                    }, error -> showError(error.getMessage())
            ));
        }
    }
}
