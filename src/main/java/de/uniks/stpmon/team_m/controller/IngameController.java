package de.uniks.stpmon.team_m.controller;


import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.controller.subController.*;
import de.uniks.stpmon.team_m.dto.*;
import de.uniks.stpmon.team_m.dto.Region;
import de.uniks.stpmon.team_m.service.AreasService;
import de.uniks.stpmon.team_m.service.MessageService;
import de.uniks.stpmon.team_m.service.PresetsService;
import de.uniks.stpmon.team_m.service.TrainersService;
import de.uniks.stpmon.team_m.udp.UDPEventListener;
import de.uniks.stpmon.team_m.utils.ImageProcessor;
import de.uniks.stpmon.team_m.utils.Position;
import de.uniks.stpmon.team_m.utils.SpriteAnimation;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import de.uniks.stpmon.team_m.ws.EventListener;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.scene.layout.AnchorPane;
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
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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
    public Button pauseButton;
    @FXML
    public TextField messageField;
    @FXML
    public Button showChatButton;
    @FXML
    public Button sendMessageButton;
    @FXML
    public ListView<Message> chatListView;
    @FXML
    public AnchorPane anchorPane;
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
    @FXML
    public ImageView mapSymbol;
    @FXML
    public StackPane stackPane;
    @FXML
    public StackPane root;

    @Inject
    Provider<IngameMiniMapController> ingameMiniMapControllerProvider;
    @Inject
    Provider<IngameTrainerSettingsController> ingameTrainerSettingsControllerProvider;
    @Inject
    Provider<IngamePauseMenuController> ingamePauseMenuControllerProvider;
    @Inject
    Provider<IngameSettingsController> ingameSettingsControllerProvider;
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
    Provider<IngameStarterMonsterController> ingameStarterMonsterControllerProvider;
    @Inject
    Provider<IngameController> ingameControllerProvider;

    public static final KeyCode PAUSE_MENU_KEY = KeyCode.P;
    public static final KeyCode INTERACT_KEY = KeyCode.E;
    private boolean isChatting = false;
    private boolean inDialog = false;
    private boolean inNpcPopup = false;

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
    Stage popupStage;
    private VBox dialogVBox;
    private VBox nursePopupVBox;
    private DialogController dialogController;
    private Trainer currentNpc;
    private NpcTextManager npcTextManager;
    private VBox miniMapVBox;
    private VBox starterSelectionVBox;
    private boolean movmentDisabled;

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
            if (isChatting || (lastKeyEventTimeStamp != null && System.currentTimeMillis() - lastKeyEventTimeStamp < DELAY + 25)) {
                return;
            }
            if (event.getCode() == KeyCode.ENTER) {
                messageField.requestFocus();
                isChatting = true;
            }
            if (event.getCode() == PAUSE_MENU_KEY) {
                pauseGame();
            }
            if (event.getCode() == INTERACT_KEY) {
                if (!inNpcPopup) {
                    interactWithTrainer();
                }
            }
            lastKeyEventTimeStamp = System.currentTimeMillis();

            if (inDialog) {
                return;
            }

            if (movmentDisabled) {
                return;
            }

            if ((event.getCode() == KeyCode.W)) {
                disposables.add(udpEventListenerProvider.get().move(new MoveTrainerDto(trainerStorageProvider.get().getTrainer()._id(),
                        trainerStorageProvider.get().getTrainer().area(),
                        trainerStorageProvider.get().getX(), trainerStorageProvider.get().getY() - 1, 0)).subscribe());
            }
            if ((event.getCode() == KeyCode.S)) {
                disposables.add(udpEventListenerProvider.get().move(new MoveTrainerDto(trainerStorageProvider.get().getTrainer()._id(),
                        trainerStorageProvider.get().getTrainer().area(),
                        trainerStorageProvider.get().getX(), trainerStorageProvider.get().getY() + 1, 2)).subscribe());
            }
            if ((event.getCode() == KeyCode.A)) {
                disposables.add(udpEventListenerProvider.get().move(new MoveTrainerDto(trainerStorageProvider.get().getTrainer()._id(),
                        trainerStorageProvider.get().getTrainer().area(),
                        trainerStorageProvider.get().getX() - 1, trainerStorageProvider.get().getY(), 3)).subscribe());
            }
            if ((event.getCode() == KeyCode.D)) {
                disposables.add(udpEventListenerProvider.get().move(new MoveTrainerDto(trainerStorageProvider.get().getTrainer()._id(),
                        trainerStorageProvider.get().getTrainer().area(),
                        trainerStorageProvider.get().getX() + 1, trainerStorageProvider.get().getY(), 1)).subscribe());
            }

        };

        keyReleasedHandler = event -> {
            if (isChatting || inDialog) {
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

        this.npcTextManager = new NpcTextManager(resources);
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
        app.getStage().widthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() < MINIMUM_WIDTH) {
                anchorPane.setMinWidth(MINIMUM_WIDTH - OFFSET_WIDTH_HEIGHT);
                anchorPane.setPrefWidth(MINIMUM_WIDTH - OFFSET_WIDTH_HEIGHT);
                anchorPane.setMaxWidth(MINIMUM_WIDTH - OFFSET_WIDTH_HEIGHT);
            } else {
                anchorPane.setMinWidth(newValue.doubleValue() - OFFSET_WIDTH_HEIGHT);
                anchorPane.setPrefWidth(newValue.doubleValue() - OFFSET_WIDTH_HEIGHT);
                anchorPane.setMaxWidth(newValue.doubleValue() - OFFSET_WIDTH_HEIGHT);
            }
        });
        app.getStage().heightProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() < MINIMUM_HEIGHT) {
                anchorPane.setMinHeight(MINIMUM_HEIGHT - OFFSET_WIDTH_HEIGHT);
                anchorPane.setPrefHeight(MINIMUM_HEIGHT - OFFSET_WIDTH_HEIGHT);
                anchorPane.setMaxHeight(MINIMUM_HEIGHT - OFFSET_WIDTH_HEIGHT);
            } else {
                anchorPane.setMinHeight(newValue.doubleValue() - OFFSET_WIDTH_HEIGHT);
                anchorPane.setPrefHeight(newValue.doubleValue() - OFFSET_WIDTH_HEIGHT);
                anchorPane.setMaxHeight(newValue.doubleValue() - OFFSET_WIDTH_HEIGHT);
            }
        });
        anchorPane.setMinHeight(getHeight() - OFFSET_WIDTH_HEIGHT);
        anchorPane.setPrefHeight(getHeight() - OFFSET_WIDTH_HEIGHT);
        anchorPane.setMaxHeight(getHeight() - OFFSET_WIDTH_HEIGHT);

        anchorPane.setMinWidth(getWidth() - OFFSET_WIDTH_HEIGHT);
        anchorPane.setPrefWidth(getWidth() - OFFSET_WIDTH_HEIGHT);
        anchorPane.setMaxWidth(getWidth() - OFFSET_WIDTH_HEIGHT);
        // Setup trainers
        disposables.add(trainersService.getTrainers(trainerStorageProvider.get().getRegion()._id(), null, null).observeOn(FX_SCHEDULER).subscribe(
                trainers -> {
                    this.trainers = FXCollections.observableArrayList(trainers);
                    listenToTrainers(this.trainers);

                    for (Trainer trainer : trainers) {
                        trainerPositionHashMap.put(trainer, new Position(trainer.x(), trainer.y(), trainer.direction()));
                    }
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

        popupStage = new Stage();
        popupStage.initOwner(app.getStage());

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
                                mapMovementTransition = getMapMovementTransition(groundCanvas, -SCALE_FACTOR * TILE_SIZE, 0);
                                Timeline mapMovementTransition = getMapMovementTransition(trainersCanvas, -SCALE_FACTOR * TILE_SIZE, 0);
                                Timeline trainerTransition = getMapMovementTransition(trainerCanvas, -SCALE_FACTOR * TILE_SIZE, 0);
                                Timeline overTrainerTransition = getMapMovementTransition(overTrainerCanvas, -SCALE_FACTOR * TILE_SIZE, 0);
                                trainerTransition.play();
                                overTrainerTransition.play();
                                mapMovementTransition.play();
                                this.mapMovementTransition.play();
                            } else if (oldXValue > moveTrainerDto.x()) {
                                mapMovementTransition = getMapMovementTransition(groundCanvas, SCALE_FACTOR * TILE_SIZE, 0);
                                Timeline mapMovementTransition = getMapMovementTransition(trainersCanvas, SCALE_FACTOR * TILE_SIZE, 0);
                                Timeline trainerTransition = getMapMovementTransition(trainerCanvas, SCALE_FACTOR * TILE_SIZE, 0);
                                Timeline overTrainerTransition = getMapMovementTransition(overTrainerCanvas, SCALE_FACTOR * TILE_SIZE, 0);
                                trainerTransition.play();
                                overTrainerTransition.play();
                                mapMovementTransition.play();
                                this.mapMovementTransition.play();
                            } else if (oldYValue < moveTrainerDto.y()) {
                                mapMovementTransition = getMapMovementTransition(groundCanvas, 0, -SCALE_FACTOR * TILE_SIZE);
                                Timeline mapMovementTransition = getMapMovementTransition(trainersCanvas, 0, -SCALE_FACTOR * TILE_SIZE);
                                Timeline trainerTransition = getMapMovementTransition(trainerCanvas, 0, -SCALE_FACTOR * TILE_SIZE);
                                Timeline overTrainerTransition = getMapMovementTransition(overTrainerCanvas, 0, -SCALE_FACTOR * TILE_SIZE);
                                trainerTransition.play();
                                overTrainerTransition.play();
                                mapMovementTransition.play();
                                this.mapMovementTransition.play();
                            } else {
                                mapMovementTransition = getMapMovementTransition(groundCanvas, 0, SCALE_FACTOR * TILE_SIZE);
                                Timeline mapMovementTransition = getMapMovementTransition(trainersCanvas, 0, SCALE_FACTOR * TILE_SIZE);
                                Timeline trainerTransition = getMapMovementTransition(trainerCanvas, 0, SCALE_FACTOR * TILE_SIZE);
                                Timeline overTrainerTransition = getMapMovementTransition(overTrainerCanvas, 0, SCALE_FACTOR * TILE_SIZE);
                                trainerTransition.play();
                                overTrainerTransition.play();
                                mapMovementTransition.play();
                                this.mapMovementTransition.play();
                            }
                        } else {
                            trainerSpriteAnimation.stay(moveTrainerDto.direction());
                        }
                        Position p = trainerSpriteAnimation.getCurrentPosition();
                        p.setDirection(trainerStorageProvider.get().getDirection());
                        trainerSpriteAnimation.setCurrentPosition(p);
                        trainerStorageProvider.get().setX(moveTrainerDto.x());
                        trainerStorageProvider.get().setY(moveTrainerDto.y());
                        trainerStorageProvider.get().setDirection(moveTrainerDto.direction());
                        Position position = new Position(moveTrainerDto.x(), moveTrainerDto.y(), moveTrainerDto.direction());
                        trainerPositionHashMap.put(trainerStorageProvider.get().getTrainer(), position);
                    } else {
                        if (trainers != null) {
                            Trainer trainer = trainers.stream().filter(tr -> tr._id().equals(moveTrainerDto._id())).toList().get(0);
                            Position oldPosition = trainerPositionHashMap.get(trainer);
                            TrainerController trainerController = trainerControllerHashMap.get(trainer);
                            if (oldPosition != null && trainerController != null) {
                                trainersCanvas.getGraphicsContext2D().clearRect(oldPosition.getX() * TILE_SIZE, oldPosition.getY() * TILE_SIZE, 16, 25);
                                if (oldPosition.getX() != moveTrainerDto.x() || oldPosition.getY() != moveTrainerDto.y()) {
                                    trainerController.getSpriteAnimation().setCurrentPosition(new Position(moveTrainerDto.x(), moveTrainerDto.y(), moveTrainerDto.direction()));
                                    trainerController.getSpriteAnimation().walk(oldPosition.getDirection());
                                }
                                trainersCanvas.getGraphicsContext2D().drawImage(trainerController.getSpriteAnimation().currentImage, moveTrainerDto.x() * TILE_SIZE, moveTrainerDto.y() * TILE_SIZE, 16, 25);
                                trainerPositionHashMap.put(trainer, new Position(moveTrainerDto.x(), moveTrainerDto.y(), moveTrainerDto.direction()));
                            }
                        }

                    }
                }, error -> {
                    showError(error.getMessage());
                    error.printStackTrace();
                }));
    }


    private Timeline getMapMovementTransition(Canvas map, int x, int y) {
        return new Timeline(
                new KeyFrame(Duration.millis(IngameController.DELAY), e -> {
                    TranslateTransition translateTransition = new TranslateTransition();
                    translateTransition.setDuration(Duration.millis(IngameController.DELAY));
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
        //
        getMapMovementTransition(groundCanvas, xOffset, yOffset).play();
        getMapMovementTransition(trainersCanvas, xOffset, yOffset - TILE_SIZE).play();
        getMapMovementTransition(userTrainerCanvas, xOffset, yOffset - TILE_SIZE).play();
        getMapMovementTransition(trainerCanvas, xOffset, yOffset - TILE_SIZE).play();
        getMapMovementTransition(overTrainerCanvas, xOffset, (yOffset) + 3).play();
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
        URL resource = Main.class.getResource("charactermodels/" + trainer.image());
        if (resource != null) {
            Image trainerChunk = new Image(resource.toString());
            TrainerController trainerController = new TrainerController(trainer, trainerChunk, trainersCanvas.getGraphicsContext2D());
            trainerControllerHashMap.put(trainer, trainerController);
            trainerController.init();
        } else {
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
                setCanvasSettings(map, userTrainerCanvas);
                setCanvasSettings(map, trainersCanvas);
                setCanvasSettings(map, groundCanvas);
                setCanvasSettings(map, overTrainerCanvas);
                for (TileSet tileSet : map.tilesets()) {
                    renderMap(map, tileSetImages.get(getFileName(tileSet.source())), tileSetJsons.get(getFileName(tileSet.source())),
                            tileSet, map.tilesets().size() > 1);
                }
                loadPlayers();
            }
        }
    }

    public void setCanvasSettings(Map map, Canvas canvas) {
        canvas.setScaleX(SCALE_FACTOR);
        canvas.setScaleY(SCALE_FACTOR);
        canvas.setWidth(map.width() * TILE_SIZE);
        canvas.setHeight(map.height() * TILE_SIZE);
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
                if (layer.data() != null) {
                    renderData(map, image, tileSet, tileSetJson, multipleTileSets, layer);
                }
                continue;
            }
            for (Chunk chunk : layer.chunks()) {
                renderChunk(map, image, tileSet, tileSetJson, multipleTileSets, chunk);
            }
        }
    }

    private void renderData(Map map, Image image, TileSet tileSet, TileSet tileSetJson, boolean multipleTileSets, Layer layer) {
        renderTiles(map, image, tileSet, tileSetJson, multipleTileSets, layer.width(), layer.height(), layer.data(), layer.x(), layer.y());
    }

    private void renderTiles(Map map, Image image, TileSet tileSet, TileSet tileSetJson, boolean multipleTileSets, int width, int height, List<Integer> data, int x2, int y2) {
        WritableImage writableImageGround = new WritableImage(width * TILE_SIZE, height * TILE_SIZE);
        WritableImage writableImageTop = new WritableImage(width * TILE_SIZE, height * TILE_SIZE);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int tileId = data.get(y * width + x);
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
        groundCanvas.getGraphicsContext2D().drawImage(writableImageGround, x2 * TILE_SIZE, y2 * TILE_SIZE);
        overTrainerCanvas.getGraphicsContext2D().drawImage(writableImageTop, x2 * TILE_SIZE, y2 * TILE_SIZE);
    }

    /**
     * renderChunk is used to render a chunk of the map. It renders every tile of the chunk. It skips every tile that
     * is not in the current tileset.
     *
     * @param map              Tiled Map of the current area.
     * @param image            Image of the current tileset.
     * @param tileSet          Current tileset.
     * @param multipleTileSets Boolean that is true if there are multiple tilesets.
     * @param chunk            Current chunk.
     */

    private void renderChunk(Map map, Image image, TileSet tileSet, TileSet tileSetJson, boolean multipleTileSets, Chunk chunk) {
        renderTiles(map, image, tileSet, tileSetJson, multipleTileSets, chunk.width(), chunk.height(), chunk.data(), chunk.x(), chunk.y());
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
        IngamePauseMenuController ingamePauseMenuController = ingamePauseMenuControllerProvider.get();
        VBox pauseMenuVBox = new VBox();
        pauseMenuVBox.setAlignment(Pos.CENTER);
        ingamePauseMenuController.init(this, pauseMenuVBox, mainMenuControllerProvider, app);
        pauseMenuVBox.getChildren().add(ingamePauseMenuController.render());
        root.getChildren().add(pauseMenuVBox);
        pauseMenuVBox.requestFocus();
        buttonsDisable(true);
    }

    public void buttonsDisable(Boolean set) {
        if (set) {
            stackPane.setEffect(new BoxBlur(10, 10, 3));
        } else {
            stackPane.setEffect(null);
        }
        movmentDisabled = set;
        monstersButton.setDisable(set);
        pauseButton.setDisable(set);
        showChatButton.setDisable(set);
        mapSymbol.setDisable(set);
        helpSymbol.setDisable(set);
        messageField.setDisable(set);
        sendMessageButton.setDisable(set);
    }

    public void showSettings() {
        IngameSettingsController ingameSettingsController = ingameSettingsControllerProvider.get();
        VBox settingsVBox = new VBox();
        settingsVBox.setAlignment(Pos.CENTER);
        ingameSettingsController.init(this, settingsVBox);
        settingsVBox.getChildren().add(ingameSettingsController.render());
        root.getChildren().add(settingsVBox);
        settingsVBox.requestFocus();
        buttonsDisable(true);
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
                        case "created" -> {
                            trainers.add(trainer);
                            URL resource = Main.class.getResource("charactermodels/" + trainer.image());
                            if (resource != null) {
                                TrainerController trainerController = new TrainerController(trainer, new Image(resource.toString()), trainersCanvas.getGraphicsContext2D());
                                trainerController.init();
                                trainerController.startAnimations();
                                trainerControllerHashMap.put(trainer, trainerController);
                                trainerPositionHashMap.put(trainer, new Position(trainer.x(), trainer.y(), trainer.direction()));
                            }
                        }
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
        Scene popupScene = new Scene(monstersListControllerProvider.get().render());
        popupStage.setScene(popupScene);
        popupStage.setTitle(resources.getString("MONSTERS"));
        popupStage.show();
    }

    /*
     ** NPC methods **
     */

    public void interactWithTrainer() {
        if (inDialog) {
            try {
                System.out.println(this.currentNpc.npc().starters());
                if (this.currentNpc.npc().canHeal() && trainerStorageProvider.get().getTrainer().team().size() == 0) {
                    continueTrainerDialog(DialogSpecialInteractions.nurseNoMons);
                } else {
                    continueTrainerDialog(null);
                }
            } catch (Error e) {
                continueTrainerDialog(null);
            }
        } else {
            int currentXPosition = trainerStorageProvider.get().getX();
            int currentYPosition = trainerStorageProvider.get().getY();
            int currentDirection = trainerStorageProvider.get().getDirection();

            this.currentNpc = checkTileInFront(currentXPosition, currentYPosition, currentDirection);

            if (this.currentNpc != null) {
                inDialog = true;

                this.dialogController = new DialogController(
                        this.currentNpc,
                        createDialogVBox(),
                        checkIfNpcEncounteredPlayer(this.currentNpc),
                        npcTextManager,
                        trainerStorageProvider.get().getTrainer(),
                        this
                );
            }
        }
    }

    /**
     * This method checks the tile in front of the player, if a npc is standing on that tile.
     *
     * @param currentX  current x coordinate of the player
     * @param currentY  current y coordinate of the player
     * @param direction current direction of the player
     * @return The npc TrainerDTO if true, else null
     */
    public Trainer checkTileInFront(int currentX, int currentY, int direction) {
        int checkTileX = currentX;
        int checkTileY = currentY;
        int checkTileXForNurse = currentX;
        int checkTileYForNurse = currentY;

        switch (direction) {
            case 0 -> {                         // facing up
                checkTileY--;
                checkTileYForNurse -= 2;
            }
            case 1 -> {                         // facing right
                checkTileX++;
                checkTileXForNurse += 2;
            }
            case 2 -> {                         // facing down
                checkTileY++;
                checkTileY += 2;
            }
            case 3 -> {                         // facing left
                checkTileX--;
                checkTileXForNurse -= 2;
            }
            default -> System.err.println("Unknown direction for Trainer: " + direction);
        }

        Trainer tileInFront = searchHashedMapForTrainer(checkTileX, checkTileY);

        if (tileInFront != null) {
            return tileInFront;
        } else {
            Trainer nurseBehindCounter = searchHashedMapForTrainer(checkTileXForNurse, checkTileYForNurse);

            if (nurseBehindCounter == null) {
                return null;
            }

            // maybe this if will throw an error in the future. I've looked into the server for all NPC's,
            // apparently almost all NPC's have the canHeal() boolean, but some only have walkRandomly().
            // If they don't have the canHeal(), it should be covered by this try/catch
            try {
                if (nurseBehindCounter.npc().canHeal()) {
                    return nurseBehindCounter;
                } else {
                    return null;
                }
            } catch (Error e) {
                System.err.println("NPC does not have the canHeal() attribute");
                e.printStackTrace();
                return null;
            }
        }
    }

    public Trainer searchHashedMapForTrainer(int checkX, int checkY) {
        for (java.util.Map.Entry<Trainer, Position> set : trainerPositionHashMap.entrySet()) {
            if (set.getValue().getX() == checkX && set.getValue().getY() == checkY) {
                if (set.getKey().npc() != null) {
                    return set.getKey();
                }
            }
        }
        return null;
    }

    public boolean checkIfNpcEncounteredPlayer(Trainer npc) {
        if (npc.npc().encountered() == null) {
            return false;
        }

        return npc.npc().encountered().contains(trainerStorageProvider.get().getTrainer()._id());
    }

    /**
     * Sends a talk event to a specific npc to the UDP Client
     *
     * @param npc       The npc that has been talked to
     * @param selection OPTIONAL: if npc has selection. Pass null if not
     */
    public void encounterNPC(Trainer npc, int selection) {
        if (npc.npc() == null) {
            return;
        }

        if (!checkIfNpcEncounteredPlayer(npc)) {
            String trainerID = trainerStorageProvider.get().getTrainer()._id();

            disposables.add(udpEventListenerProvider.get().talk(
                    npc.area(),
                    new TalkTrainerDto(
                            trainerID,
                            npc._id(),
                            selection
                    )
            ).subscribe());
        }
    }

    public void continueTrainerDialog(DialogSpecialInteractions specialInteractions) {
        ContinueDialogReturnValues continueDialogReturn = dialogController.continueDialog(specialInteractions);

        switch (continueDialogReturn) {
            case dialogFinishedTalkToTrainer -> endDialog(-1, true);
            case albertDialogFinished0 -> endDialog(0, true);
            case albertDialogFinished1 -> endDialog(1, true);
            case albertDialogFinished2 -> endDialog(2, true);
            case dialogFinishedNoTalkToTrainer -> endDialog(0, false);
            case spokenToNurse -> createNurseHealPopup();
            default -> {
            }
        }
    }

    public void endDialog(int selectionValue, boolean encounterNpc) {
        this.dialogController.destroy();
        inDialog = false;
        stackPane.getChildren().remove(dialogVBox);

        if (encounterNpc) {
            encounterNPC(this.currentNpc, selectionValue);
        }
    }

    public void createNurseHealPopup() {
        // base VBox
        VBox nurseVBox = new VBox();
        nurseVBox.setId("nurseVBox");
        nurseVBox.setMaxHeight(popupHeight);
        nurseVBox.setMaxWidth(popupWidth);
        nurseVBox.getStyleClass().add("dialogTextFlow");
        this.nursePopupVBox = nurseVBox;

        // text field
        TextFlow nurseQuestion = new TextFlow(new Text(resources.getString("NURSE.HEAL.QUESTION")));
        nurseQuestion.setPrefWidth(popupWidth);
        nurseQuestion.setPrefHeight(textFieldHeight);
        nurseQuestion.setPadding(dialogTextFlowInsets);
        nurseQuestion.setTextAlignment(TextAlignment.CENTER);

        // buttonsHBox
        HBox buttonsHBox = new HBox();
        buttonsHBox.setMaxHeight(buttonsHBoxHeight);
        buttonsHBox.setMaxWidth(popupWidth);
        buttonsHBox.setAlignment(Pos.TOP_CENTER);
        buttonsHBox.setSpacing(buttonsHBoxSpacing);

        // yes button
        Button yesButton = new Button(resources.getString("NURSE.YES"));
        yesButton.setMaxWidth(nurseButtonWidth);
        yesButton.setMinWidth(nurseButtonWidth);
        yesButton.setMaxHeight(nurseButtonHeight);
        yesButton.setMinHeight(nurseButtonHeight);
        yesButton.getStyleClass().add("buttonsYellow");
        yesButton.setOnAction(event -> {
            continueTrainerDialog(DialogSpecialInteractions.nurseYes);
            inNpcPopup = false;
            this.root.getChildren().remove(nursePopupVBox);
            buttonsDisable(false);
        });

        // no button
        Button noButton = new Button(resources.getString("NURSE.NO"));
        noButton.setMaxWidth(nurseButtonWidth);
        noButton.setMinWidth(nurseButtonWidth);
        noButton.setMaxHeight(nurseButtonHeight);
        noButton.setMinHeight(nurseButtonHeight);
        noButton.getStyleClass().add("buttonsWhite");
        noButton.setOnAction(event -> {
            continueTrainerDialog(DialogSpecialInteractions.nurseNo);
            inNpcPopup = false;
            this.root.getChildren().remove(nursePopupVBox);
            buttonsDisable(false);
        });

        // add buttons to buttonHBox
        buttonsHBox.getChildren().addAll(yesButton, noButton);

        // add text and buttonsHBox to nurseVBox
        nurseVBox.getChildren().addAll(nurseQuestion, buttonsHBox);

        // add nurseVBox to stackPane
        root.getChildren().add(nurseVBox);
        buttonsDisable(true);
        inNpcPopup = true;
    }

    public TextFlow createDialogVBox() {
        VBox dialogVBox = new VBox();
        dialogVBox.setMinWidth(dialogVBoxWidth);
        dialogVBox.maxWidthProperty().bind(stackPane.widthProperty().divide(2));
        dialogVBox.setMaxHeight(getDialogVBoxHeight);
        dialogVBox.setId("dialogVBox");

        int constantSpacer = spacerToBottomOfScreen;
        dialogVBox.translateYProperty().bind((anchorPane.heightProperty().subtract(dialogVBox.maxHeightProperty()).subtract(constantSpacer)).divide(2));

        Pane textPane = new Pane();

        textPane.prefWidthProperty().bind(dialogVBox.widthProperty());
        textPane.prefHeightProperty().bind(dialogVBox.heightProperty());

        TextFlow dialogTextFlow = new TextFlow();
        dialogTextFlow.setId("dialogTextFlow");

        dialogTextFlow.prefWidthProperty().bind(textPane.widthProperty());
        dialogTextFlow.prefHeightProperty().bind(textPane.heightProperty());
        dialogTextFlow.setPadding(dialogTextFlowInsets);

        VBox textVBox = new VBox();
        textVBox.maxWidthProperty().bind(textPane.widthProperty());
        textVBox.maxHeightProperty().bind(textPane.heightProperty());
        textVBox.getStyleClass().add("dialogTextFlow");

        Label dialogHelpLabel = new Label(resources.getString("NPC.DIALOG.HELP"));
        dialogHelpLabel.prefWidthProperty().bind(textVBox.widthProperty());
        dialogHelpLabel.setFont(new Font(helpLabelFontSize));
        dialogHelpLabel.setPadding(helpLabelInsets);
        dialogHelpLabel.setAlignment(Pos.CENTER_RIGHT);

        textVBox.getChildren().addAll(dialogTextFlow, dialogHelpLabel);

        textPane.getChildren().add(textVBox);

        dialogVBox.getChildren().add(textPane);

        stackPane.getChildren().add(dialogVBox);
        this.dialogVBox = dialogVBox;

        return dialogTextFlow;
    }

    public void showMap() {
        IngameMiniMapController ingameMiniMapController = ingameMiniMapControllerProvider.get();
        if (miniMapVBox == null) {
            miniMapVBox = new VBox();
            miniMapVBox.getStyleClass().add("miniMapContainer");
            miniMapVBox.setPadding(new Insets(0, 0, 8, 0));
            miniMapVBox.getChildren().add(ingameMiniMapController.render());

            Button closeButton = new Button();
            closeButton.setId("closeButton");
            closeButton.setText(resources.getString("CLOSE"));
            closeButton.getStyleClass().add("welcomeSceneButton");
            closeButton.setOnAction(event -> {
                        root.getChildren().remove(miniMapVBox);
                        buttonsDisable(false);
                    }
            );
            miniMapVBox.getChildren().add(closeButton);
        }
        root.getChildren().add(miniMapVBox);
        miniMapVBox.requestFocus();
        buttonsDisable(true);
    }

    public void showStarterSelection() {
        IngameStarterMonsterController ingameStarterMonsterController = ingameStarterMonsterControllerProvider.get();
        if (starterSelectionVBox == null) {
            starterSelectionVBox = new VBox();
            starterSelectionVBox.getStyleClass().add("miniMapContainer");
            starterSelectionVBox.setPadding(new Insets(0, 0, 8, 0));
            ingameStarterMonsterController.init(this, starterSelectionVBox, app);
            starterSelectionVBox.getChildren().add(ingameStarterMonsterController.render());

            Button okButton = new Button();
            okButton.setId("okButton");
            okButton.setText(resources.getString("OK"));
            okButton.getStyleClass().add("welcomeSceneButton");
            okButton.setOnAction(event -> {
                        root.getChildren().remove(starterSelectionVBox);
                        buttonsDisable(false);
                    }
            );
            starterSelectionVBox.getChildren().add(okButton);
        }
        root.getChildren().add(starterSelectionVBox);
        starterSelectionVBox.requestFocus();
        buttonsDisable(true);
    }

    @Override
    public void destroy() {
        super.destroy();
        app.getStage().getScene().removeEventHandler(KeyEvent.KEY_PRESSED, keyPressedHandler);
        app.getStage().getScene().removeEventHandler(KeyEvent.KEY_RELEASED, keyReleasedHandler);
        messageField.removeEventHandler(KeyEvent.KEY_PRESSED, this::enterButtonPressedToSend);
    }
}
