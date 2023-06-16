package de.uniks.stpmon.team_m.controller;


import de.uniks.stpmon.team_m.Constants;
import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.controller.subController.IngameMessageCell;
import de.uniks.stpmon.team_m.controller.subController.IngameTrainerSettingsController;
import de.uniks.stpmon.team_m.controller.subController.MonstersListController;
import de.uniks.stpmon.team_m.controller.subController.TrainerController;
import de.uniks.stpmon.team_m.dto.*;
import de.uniks.stpmon.team_m.service.AreasService;
import de.uniks.stpmon.team_m.service.MessageService;
import de.uniks.stpmon.team_m.service.PresetsService;
import de.uniks.stpmon.team_m.service.TrainersService;
import de.uniks.stpmon.team_m.udp.UDPEventListener;
import de.uniks.stpmon.team_m.utils.*;
import de.uniks.stpmon.team_m.ws.EventListener;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
    private static final int DELAY = 100;
    private static final int DELAY_LONG = 500;
    private static final int SCALE_FACTOR = 2;

    @FXML
    public Button helpSymbol;
    @FXML
    public Button monstersButton;
    @FXML
    public Button settingsButton;
    @FXML
    public ImageView trainerSpriteImageView;

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
    @FXML
    public Canvas groundCanvas;
    @FXML
    public Canvas trainerCanvas;
    @FXML
    public Canvas overTrainerCanvas;
    @FXML
    public Canvas userTrainerCanvas;
    @FXML
    public Canvas trainersCanvas;

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
    Provider<IngameController> ingameControllerProvider;

    GraphicsContext graphicsContext;
    public static final KeyCode PAUSE_MENU_KEY = KeyCode.P;
    private boolean isChatting = false;

    @Inject
    Provider<UDPEventListener> udpEventListenerProvider;


    @Inject
    Provider<MonstersListController> monstersListControllerProvider;

    private final ObservableList<MoveTrainerDto> moveTrainerDtos = FXCollections.observableArrayList();
    HashMap<String, Image> tileSetImages = new HashMap<>();


    private SpriteAnimation trainerSpriteAnimation;
    private Timeline mapMovementTransition;

    HashMap<String, TileSet> tileSetJsons = new HashMap<>();
    private final ObservableList<Message> messages = FXCollections.observableArrayList();
    private ObservableList<Trainer> trainers;

    private Long lastKeyEventTimeStamp;
    private EventHandler<KeyEvent> keyReleasedHandler;
    private EventHandler<KeyEvent> keyPressedHandler;

    private HashMap<Trainer, TrainerController> trainerControllerHashMap;

    private HashMap<Trainer, Position> trainerPositionHashMap;

    /**
     * IngameController is used to show the In-Game screen and to pause the game.
     */

    @Inject
    public IngameController() {
    }

    @Override
    public void init() {
        super.init();
        trainerControllerHashMap = new HashMap<>();
        trainerPositionHashMap = new HashMap<>();
        // Initialize key event listeners
        keyPressedHandler = event -> {
            event.consume();
            if (isChatting || (lastKeyEventTimeStamp != null && System.currentTimeMillis() - lastKeyEventTimeStamp < DELAY + 50)) {
                return;
            }
            lastKeyEventTimeStamp = System.currentTimeMillis();

            if (event.getCode() == PAUSE_MENU_KEY) {
                pauseGame();
            }
            if ((event.getCode() == KeyCode.W)) {
                ///*
                disposables.add(udpEventListenerProvider.get().move(new MoveTrainerDto(trainerStorageProvider.get().getTrainer()._id(),
                        trainerStorageProvider.get().getTrainer().area(),
                        trainerStorageProvider.get().getX(), trainerStorageProvider.get().getY() - 1, 0)).subscribe());
                //*/
                /*
                trainerSpriteAnimation.walk(0);
                mapMovementTransition = getMapMovementTransition(groundCanvas, 0, SCALE_FACTOR * TILE_SIZE, DELAY);
                Timeline trainerMovementTransition = getMapMovementTransition(trainersCanvas, 0, SCALE_FACTOR * TILE_SIZE, DELAY);
                Timeline overTrainerMovementTransition = getMapMovementTransition(overTrainerCanvas, 0, SCALE_FACTOR * TILE_SIZE, DELAY);
                overTrainerMovementTransition.play();
                trainerMovementTransition.play();
                this.mapMovementTransition.play();
                */
            }
            if ((event.getCode() == KeyCode.S)) {
                ///*
                disposables.add(udpEventListenerProvider.get().move(new MoveTrainerDto(trainerStorageProvider.get().getTrainer()._id(),
                        trainerStorageProvider.get().getTrainer().area(),
                        trainerStorageProvider.get().getX(), trainerStorageProvider.get().getY() + 1, 2)).subscribe());
                //*/
                /*
                trainerSpriteAnimation.walk(2);
                mapMovementTransition = getMapMovementTransition(groundCanvas, 0, -SCALE_FACTOR * TILE_SIZE, DELAY);
                Timeline mapMovementTransition = getMapMovementTransition(trainersCanvas, 0, -SCALE_FACTOR * TILE_SIZE, DELAY);
                Timeline overTrainerTransition = getMapMovementTransition(overTrainerCanvas, 0, -SCALE_FACTOR * TILE_SIZE, DELAY);
                overTrainerTransition.play();
                mapMovementTransition.play();
                this.mapMovementTransition.play();

                 */
            }
            if ((event.getCode() == KeyCode.A)) {
                ///*
                disposables.add(udpEventListenerProvider.get().move(new MoveTrainerDto(trainerStorageProvider.get().getTrainer()._id(),
                        trainerStorageProvider.get().getTrainer().area(),
                        trainerStorageProvider.get().getX() - 1, trainerStorageProvider.get().getY(), 3)).subscribe());

                //*/
                /*
                trainerSpriteAnimation.walk(3);
                mapMovementTransition = getMapMovementTransition(groundCanvas, SCALE_FACTOR * TILE_SIZE, 0, DELAY);
                Timeline mapMovementTransition = getMapMovementTransition(trainersCanvas, SCALE_FACTOR * TILE_SIZE, 0, DELAY);
                Timeline overTrainerTransition = getMapMovementTransition(overTrainerCanvas, SCALE_FACTOR * TILE_SIZE, 0, DELAY);
                overTrainerTransition.play();
                this.mapMovementTransition.play();
                mapMovementTransition.play();
                */
            }
            if ((event.getCode() == KeyCode.D)) {
                ///*
                disposables.add(udpEventListenerProvider.get().move(new MoveTrainerDto(trainerStorageProvider.get().getTrainer()._id(),
                        trainerStorageProvider.get().getTrainer().area(),
                        trainerStorageProvider.get().getX() + 1, trainerStorageProvider.get().getY(), 1)).subscribe());
                //*/
                /*
                trainerSpriteAnimation.walk(1);
                mapMovementTransition = getMapMovementTransition(groundCanvas, -SCALE_FACTOR * TILE_SIZE, 0, DELAY);
                Timeline mapMovementTransition = getMapMovementTransition(trainersCanvas, -SCALE_FACTOR * TILE_SIZE, 0, DELAY);
                Timeline trainerTransition = getMapMovementTransition(trainerCanvas, -SCALE_FACTOR * TILE_SIZE, 0, DELAY);
                Timeline overTrainerTransition = getMapMovementTransition(overTrainerCanvas, -SCALE_FACTOR * TILE_SIZE, 0, DELAY);
                trainerTransition.play();
                overTrainerTransition.play();
                mapMovementTransition.play();
                this.mapMovementTransition.play();

                 */
            }

        };

        keyReleasedHandler = event -> {
            if (isChatting) {
                return;
            }
            if ((event.getCode() == KeyCode.W)) {
                trainerSpriteAnimation.stay(0);
            }
            if ((event.getCode() == KeyCode.S)) {
                trainerSpriteAnimation.stay(2);
            }
            if ((event.getCode() == KeyCode.A)) {
                trainerSpriteAnimation.stay(3);
            }
            if ((event.getCode() == KeyCode.D)) {
                trainerSpriteAnimation.stay(1);
            }
        };
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


        // Create and start standing animation
        trainerSpriteAnimation = new SpriteAnimation(trainerStorageProvider.get().getTrainerSpriteChunk(), trainerStorageProvider.get().getTrainer(), DELAY_LONG, userTrainerCanvas.getGraphicsContext2D());
        if (!GraphicsEnvironment.isHeadless()) {
            trainerSpriteAnimation.stay(trainerStorageProvider.get().getDirection());
            trainerSpriteAnimation.start();
        }

        // Add event handlers
        app.getStage().getScene().addEventHandler(KeyEvent.KEY_PRESSED, keyPressedHandler);
        app.getStage().getScene().addEventHandler(KeyEvent.KEY_RELEASED, keyReleasedHandler);

        Region region = trainerStorageProvider.get().getRegion();
        disposables.add(areasService.getArea(region._id(), trainerStorageProvider.get().getTrainer().area()).observeOn(FX_SCHEDULER).subscribe(area -> loadMap(area.map()), error -> showError(error.getMessage())));
        monstersListControllerProvider.get().init();
        return parent;
    }
    public void listenToMovement(ObservableList<MoveTrainerDto> moveTrainerDtos, String area) {
        disposables.add(udpEventListenerProvider.get().listen("areas." + area + ".trainers.*.*", MoveTrainerDto.class)
                .observeOn(FX_SCHEDULER).subscribe(event -> {
                    final MoveTrainerDto moveTrainerDto = event.data();
                    moveTrainerDtos.add(moveTrainerDto);
                    if (moveTrainerDto._id().equals(trainerStorageProvider.get().getTrainer()._id())) {
                        if (!Objects.equals(moveTrainerDto.area(), trainerStorageProvider.get().getTrainer().area())) {
                            disposables.add(trainersService.getTrainer(trainerStorageProvider.get().getRegion()._id(), trainerStorageProvider.get().getTrainer()._id()).observeOn(FX_SCHEDULER).subscribe(
                                    trainer -> {
                                        trainerStorageProvider.get().setTrainer(trainer);
                                        destroy();
                                        app.show(ingameControllerProvider.get());
                                    },
                                    error -> {
                                        showError(error.getMessage());
                                        error.printStackTrace();
                                    }
                                    ));


                        }
                        int oldXValue = trainerStorageProvider.get().getX();
                        int oldYValue = trainerStorageProvider.get().getY();
                        if (oldXValue != moveTrainerDto.x() || oldYValue != moveTrainerDto.y()) {
                            trainerSpriteAnimation.walk(moveTrainerDto.direction());
                            if (oldXValue < moveTrainerDto.x()) {
                                mapMovementTransition = getMapMovementTransition(groundCanvas, -SCALE_FACTOR * TILE_SIZE, 0, DELAY);
                                Timeline mapMovementTransition = getMapMovementTransition(trainersCanvas, -SCALE_FACTOR * TILE_SIZE, 0, DELAY);
                                Timeline trainerTransition = getMapMovementTransition(trainerCanvas, -SCALE_FACTOR * TILE_SIZE, 0, DELAY);
                                Timeline overTrainerTransition = getMapMovementTransition(overTrainerCanvas, -SCALE_FACTOR * TILE_SIZE, 0, DELAY);
                                trainerTransition.play();
                                overTrainerTransition.play();
                                mapMovementTransition.play();
                                this.mapMovementTransition.play();
                            } else if (oldXValue > moveTrainerDto.x()) {
                                mapMovementTransition = getMapMovementTransition(groundCanvas, SCALE_FACTOR * TILE_SIZE, 0, DELAY);
                                Timeline mapMovementTransition = getMapMovementTransition(trainersCanvas, SCALE_FACTOR * TILE_SIZE, 0, DELAY);
                                Timeline trainerTransition = getMapMovementTransition(trainerCanvas, SCALE_FACTOR * TILE_SIZE, 0, DELAY);
                                Timeline overTrainerTransition = getMapMovementTransition(overTrainerCanvas, SCALE_FACTOR * TILE_SIZE, 0, DELAY);
                                trainerTransition.play();
                                overTrainerTransition.play();
                                mapMovementTransition.play();
                                this.mapMovementTransition.play();
                            } else if (oldYValue < moveTrainerDto.y()) {
                                mapMovementTransition = getMapMovementTransition(groundCanvas, 0, -SCALE_FACTOR * TILE_SIZE, DELAY);
                                Timeline mapMovementTransition = getMapMovementTransition(trainersCanvas, 0, -SCALE_FACTOR * TILE_SIZE, DELAY);
                                Timeline trainerTransition = getMapMovementTransition(trainerCanvas, 0, -SCALE_FACTOR * TILE_SIZE, DELAY);
                                Timeline overTrainerTransition = getMapMovementTransition(overTrainerCanvas, 0, -SCALE_FACTOR * TILE_SIZE, DELAY);
                                trainerTransition.play();
                                overTrainerTransition.play();
                                mapMovementTransition.play();
                                this.mapMovementTransition.play();
                            } else {
                                mapMovementTransition = getMapMovementTransition(groundCanvas, 0, SCALE_FACTOR * TILE_SIZE, DELAY);
                                Timeline mapMovementTransition = getMapMovementTransition(trainersCanvas, 0, SCALE_FACTOR * TILE_SIZE, DELAY);
                                Timeline trainerTransition = getMapMovementTransition(trainerCanvas, 0, SCALE_FACTOR * TILE_SIZE, DELAY);
                                Timeline overTrainerTransition = getMapMovementTransition(overTrainerCanvas, 0, SCALE_FACTOR * TILE_SIZE, DELAY);
                                trainerTransition.play();
                                overTrainerTransition.play();
                                mapMovementTransition.play();
                                this.mapMovementTransition.play();
                            }
                        }
                        else {
                            trainerSpriteAnimation.stay(moveTrainerDto.direction());
                        }
                        System.out.println("New position X: " + moveTrainerDto.x() + ", Y: " + moveTrainerDto.y() + ", direction: " + moveTrainerDto.direction());
                        trainerStorageProvider.get().setX(moveTrainerDto.x());
                        trainerStorageProvider.get().setY(moveTrainerDto.y());
                        trainerStorageProvider.get().setDirection(moveTrainerDto.direction());
                    }
                    else {
                        Trainer trainer = trainers.stream().filter(tr -> tr._id().equals(moveTrainerDto._id())).toList().get(0);
                        Position oldPosition = trainerPositionHashMap.get(trainer);
                        TrainerController trainerController = trainerControllerHashMap.get(trainer);
                        if (oldPosition != null && trainerController != null) {
                            trainersCanvas.getGraphicsContext2D().clearRect(oldPosition.getX() * TILE_SIZE, oldPosition.getY() * TILE_SIZE, 16, 25);
                            if (oldPosition.getX() != moveTrainerDto.x() || oldPosition.getY() != moveTrainerDto.y()) {
                                trainerController.getSpriteAnimation().walk(moveTrainerDto.direction());
                            }
                            else {
                                trainerController.getSpriteAnimation().stay(moveTrainerDto.direction());
                            }
                            trainerController.getSpriteAnimation().start();
                            trainersCanvas.getGraphicsContext2D().drawImage(trainerController.getSpriteAnimation().currentImage, moveTrainerDto.x() * TILE_SIZE, moveTrainerDto.y() * TILE_SIZE, 16, 25);
                            trainerPositionHashMap.put(trainer, new Position(moveTrainerDto.x(), moveTrainerDto.y(), moveTrainerDto.direction()));

                        }

                    }
                }, error -> {
                    showError(error.getMessage());
                    error.printStackTrace();
                }));
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
                    .observeOn(FX_SCHEDULER).subscribe(image -> afterAllTileSetsLoaded(map), error -> {
                        showError(error.getMessage());
                        error.printStackTrace();
                    }));
        }

        // Shift map initially to match the trainers position
        int xOffset = (int) calculateInitialCameraXOffset(map.width());
        int yOffset = (int) calculateInitialCameraYOffset(map.height());
        getMapMovementTransition(groundCanvas, xOffset, yOffset - 5 * TILE_SIZE, DELAY).play();
        getMapMovementTransition(trainersCanvas, xOffset, yOffset - 8 * TILE_SIZE, DELAY).play();
        getMapMovementTransition(userTrainerCanvas, xOffset, yOffset - 7 * TILE_SIZE, DELAY).play();
        getMapMovementTransition(trainerCanvas, xOffset, yOffset -8 *  TILE_SIZE, DELAY).play();
        getMapMovementTransition(overTrainerCanvas, xOffset, yOffset -5 *  TILE_SIZE, DELAY).play();
    }

    /**
     * This method is used to calculate the initial x offset for the camera to center the player at the right position
     *
     * @param mapWidth - width of the rendered map (in tiles)
     * @return x offset that the map needs to be shifted
     */
    private double calculateInitialCameraXOffset(double mapWidth) {
        return (int) ((((mapWidth * TILE_SIZE) / (double) TILE_SIZE) / SCALE_FACTOR) - trainerStorageProvider.get().getX()) * TILE_SIZE * SCALE_FACTOR;
    }

    /**
     * This method is used to calculate the initial y offset for the camera to center the player at the right position
     *
     * @param mapHeight - height of the rendered map (in tiles)
     * @return y offset that the map needs to be shifted
     */
    private double calculateInitialCameraYOffset(double mapHeight) {
        return (int) (((((mapHeight * TILE_SIZE) / (double) TILE_SIZE) / SCALE_FACTOR) - trainerStorageProvider.get().getY()) * TILE_SIZE * SCALE_FACTOR);
    }

    /**
     * loadPlayers is used to initially load all players on the map. For every trainer it loads the image of the player and sets its position.
     */
    private void loadPlayers() {
        trainers.forEach(trainer -> {
            if (!Objects.equals(trainerStorageProvider.get().getTrainer()._id(), trainer._id())) {
                if (trainer.area().equals(trainerStorageProvider.get().getTrainer().area())) {
                    loadPlayer(trainer);
                }
            }
        });
        afterAllTrainersLoaded();
    }

    private void afterAllTrainersLoaded() {
        trainers.forEach(trainer -> {
            if (trainerControllerHashMap.get(trainer) != null) {
                trainerControllerHashMap.get(trainer).startAnimations();
            }
        });
    }


    /**
     * loadPlayer is used to load the player on the map. It loads the image of the player and sets its position.
     */
    private void loadPlayer(Trainer trainer) {
        if (trainerControllerHashMap.containsKey(trainer)) {
            return;
        }
        if (trainer.image().contains("Premade_Character") || trainer.image().equals("Nurse_2_16x16.png") || trainer.image().equals("Albert_16x16.png") || trainer.image().equals("Bob_16x16.png") || trainer.image().equals("Amelia_16x16.png") || trainer.image().equals("Adam_16x16.png")) {
            Image trainerChunk = new Image(Objects.requireNonNull(Main.class.getResource("charactermodels/" + trainer.image())).toString());
            TrainerController trainerController = new TrainerController(trainer, trainerChunk, trainersCanvas.getGraphicsContext2D());
            trainerControllerHashMap.put(trainer, trainerController);
            trainerController.init();
        } else {
            if (trainer.npc() != null) {
                System.out.println(trainer.image());
            }
            disposables.add(presetsService.getCharacter(trainer.image()).observeOn(FX_SCHEDULER).subscribe(
                    image -> {
                        Image trainerChunk = ImageProcessor.resonseBodyToJavaFXImage(image);
                        TrainerController trainerController = new TrainerController(trainer, trainerChunk, trainersCanvas.getGraphicsContext2D());
                        trainerControllerHashMap.put(trainer, trainerController);
                        trainerController.init();
                    },
                    error -> {
                        showError(error.getMessage());
                        error.printStackTrace();
                    }
            ));
        }
        trainerPositionHashMap.put(trainer, new Position(trainer.x(), trainer.y(), trainer.direction()));

    }

    /**
     * afterAllTileSetsLoaded is used to render the map. It renders every chunk of every layer of every tileset. After
     * rendering the map, it calls loadPlayer to load the player on the map.
     *
     * @param map Tiled Map of the current area.
     */

    private void afterAllTileSetsLoaded(Map map) {
        if (tileSetImages.size() == map.tilesets().size()) {
            if ((tileSetImages.size() + tileSetJsons.size()) == 2 * map.tilesets().size()) {
                app.getStage().setWidth(Math.max(getWidth(), map.width() * TILE_SIZE) + OFFSET_WIDTH);
                app.getStage().setHeight(Math.max(getHeight(), map.height() * TILE_SIZE) + OFFSET_HEIGHT);
                //app.getStage().setWidth(STANDARD_WIDTH + 100);
                //app.getStage().setHeight(STANDARD_HEIGHT + 100);
                userTrainerCanvas.setScaleX(SCALE_FACTOR);
                userTrainerCanvas.setScaleY(SCALE_FACTOR);
                userTrainerCanvas.setWidth(map.width() * TILE_SIZE);
                userTrainerCanvas.setHeight(map.height() * TILE_SIZE);
                trainersCanvas.setScaleX(SCALE_FACTOR);
                trainersCanvas.setScaleY(SCALE_FACTOR);
                trainersCanvas.setWidth(map.width() * TILE_SIZE);
                trainersCanvas.setHeight(map.height() * TILE_SIZE);
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
                loadPlayers();
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
        if (!GraphicsEnvironment.isHeadless()) {
            trainerSettingsDialog.getDialogPane().getStylesheets().add(Objects.requireNonNull(Main.class.getResource("styles.css")).toString());
            trainerSettingsDialog.getDialogPane().getStyleClass().add("trainerSettingsDialog");
        }
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
                }, error -> {
                    showError(error.getMessage());
                    error.printStackTrace();
                }));
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

    public void showMonsters() {
        Scene scene = new Scene(monstersListControllerProvider.get().render());
        Stage popupStage = new Stage();
        popupStage.initOwner(app.getStage());
        popupStage.setScene(scene);
        popupStage.setTitle(resources.getString("MONSTERS"));
        popupStage.show();
    }

    @Override
    public void destroy() {
        super.destroy();
        app.getStage().getScene().removeEventHandler(KeyEvent.KEY_PRESSED, keyPressedHandler);
        app.getStage().getScene().removeEventHandler(KeyEvent.KEY_RELEASED, keyReleasedHandler);
        messageField.removeEventHandler(KeyEvent.KEY_PRESSED, this::enterButtonPressedToSend);
    }
}
