package de.uniks.stpmon.team_m.controller;


import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.controller.subController.*;
import de.uniks.stpmon.team_m.dto.*;
import de.uniks.stpmon.team_m.dto.Map;
import de.uniks.stpmon.team_m.dto.Region;
import de.uniks.stpmon.team_m.service.*;
import de.uniks.stpmon.team_m.udp.UDPEventListener;
import de.uniks.stpmon.team_m.utils.*;
import de.uniks.stpmon.team_m.ws.EventListener;
import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
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
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.inject.Inject;
import javax.inject.Provider;
import java.awt.*;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.Objects;

import static de.uniks.stpmon.team_m.Constants.*;


public class IngameController extends Controller {
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
    public Canvas overUserTrainerCanvas;
    @FXML
    public Canvas roofCanvas;
    @FXML
    public Canvas userTrainerCanvas;
    @FXML
    public Canvas behindUserTrainerCanvas;
    @FXML
    public ImageView mapSymbol;
    @FXML
    public StackPane stackPane;
    @FXML
    public StackPane root;
    @FXML
    public StackPane smallHandyButton;
    @FXML
    public ImageView notificationBell;
    @FXML
    public ImageView smallHandyImageView;
    @FXML
    public ImageView monsterForHandyImageView;

    @Inject
    Provider<IngameMiniMapController> ingameMiniMapControllerProvider;
    @Inject
    Provider<IngameTrainerSettingsController> ingameTrainerSettingsControllerProvider;
    @Inject
    Provider<NotificationListHandyController> notificationListHandyControllerProvider;
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
    Provider<EncounterController> encounterControllerProvider;
    @Inject
    AreasService areasService;
    @Inject
    PresetsService presetsService;
    @Inject
    MessageService messageService;
    @Inject
    TrainersService trainersService;
    @Inject
    MonstersService monstersService;
    @Inject
    EncounterOpponentStorage encounterOpponentStorage;
    @Inject
    EncounterOpponentsService encounterOpponentsService;
    @Inject
    Provider<IngameStarterMonsterController> ingameStarterMonsterControllerProvider;
    @Inject
    Provider<ChangeAudioController> changeAudioControllerProvider;
    @Inject
    RegionsService regionsService;
    @Inject
    Provider<IngameController> ingameControllerProvider;

    private IngamePauseMenuController ingamePauseMenuController;

    public static final KeyCode PAUSE_MENU_KEY = KeyCode.P;
    public static final KeyCode INTERACT_KEY = KeyCode.E;
    private boolean isChatting = false;
    private boolean inDialog = false;
    private boolean inNpcPopup = false;
    private boolean isPaused = false;
    private boolean inSettings = false;

    @Inject
    Provider<UDPEventListener> udpEventListenerProvider;


    @Inject
    Provider<MonstersListController> monstersListControllerProvider;

    private final ObservableList<MoveTrainerDto> moveTrainerDtos = FXCollections.observableArrayList();
    HashMap<String, Image> tileSetImages = new HashMap<>();

    HashMap<String, TileSet> tileSetJsons = new HashMap<>();
    private final ObservableList<Message> messages = FXCollections.observableArrayList();
    private ObservableList<Trainer> trainers;
    private ObservableList<Opponent> opponents = FXCollections.observableArrayList();

    private Long lastKeyEventTimeStamp;
    private EventHandler<KeyEvent> keyPressedHandler;

    private HashMap<Trainer, TrainerController> trainerControllerHashMap;
    private HashMap<Trainer, Position> trainerPositionHashMap;
    Stage popupStage;
    private VBox nursePopupVBox;
    private DialogController dialogController;
    private Trainer currentNpc;
    private NpcTextManager npcTextManager;
    private VBox miniMapVBox;
    private StackPane dialogStackPane;
    private VBox starterSelectionVBox;
    private NotificationListHandyController notificationListHandyController;
    private StackPane notificationHandyStackPane;
    private boolean movementDisabled;
    private final Canvas miniMapCanvas = new Canvas();
    private Map miniMap;
    private TrainerController trainerController;

    private ParallelTransition shiftMapRightTransition;
    private ParallelTransition shiftMapLeftTransition;
    private ParallelTransition shiftMapUpTransition;
    private ParallelTransition shiftMapDownTransition;
    private boolean loadingMap;

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

            if (event.getCode() == INTERACT_KEY) {
                if (!inNpcPopup) {
                    interactWithTrainer();
                }
            }
            if (event.getCode() == PAUSE_MENU_KEY) {
                if (inSettings) {
                    return;
                }
                if (!isPaused) {
                    pauseGame();
                } else {
                    ingamePauseMenuController.resumeGame();
                }
            }
            if (isChatting || loadingMap || (lastKeyEventTimeStamp != null && System.currentTimeMillis() - lastKeyEventTimeStamp < TRANSITION_DURATION + 50)) {
                return;
            }
            if (event.getCode() == KeyCode.ENTER) {
                messageField.requestFocus();
                isChatting = true;
            }
            lastKeyEventTimeStamp = System.currentTimeMillis();

            if (inDialog) {
                return;
            }

            if (movementDisabled) {
                return;
            }
            if ((event.getCode() == KeyCode.W)) {
                checkMovement(0, -1, 1);
            }
            if ((event.getCode() == KeyCode.S)) {
                checkMovement(0, 1, 3);
            }
            if ((event.getCode() == KeyCode.A)) {
                checkMovement(-1, 0, 2);
            }
            if ((event.getCode() == KeyCode.D)) {
                checkMovement(1, 0, 0);
            }
            event.consume();
        };
        this.npcTextManager = new NpcTextManager(resources);

    }

    private void checkMovement(int x, int y, int direction) {
        if (trainerStorageProvider.get().getDirection() == direction) {
            sendMoveRequest(x, y, direction);
        } else {
            sendTurnRequest(direction);
        }
    }

    public void sendMoveRequest(int x, int y, int direction) {
        disposables.add(udpEventListenerProvider.get().move(new MoveTrainerDto(trainerStorageProvider.get().getTrainer()._id(),
                trainerStorageProvider.get().getTrainer().area(),
                trainerStorageProvider.get().getX() + x, trainerStorageProvider.get().getY() + y, direction)).subscribe());
    }

    public void sendTurnRequest(int direction) {
        disposables.add(udpEventListenerProvider.get().move(new MoveTrainerDto(trainerStorageProvider.get().getTrainer()._id(),
                trainerStorageProvider.get().getTrainer().area(),
                trainerStorageProvider.get().getX(),
                trainerStorageProvider.get().getY(),
                direction)
        ).subscribe());
    }

    private void setCacheHint(CacheHint cacheHint) {
        groundCanvas.setCacheHint(cacheHint);
        overUserTrainerCanvas.setCacheHint(cacheHint);
        userTrainerCanvas.setCacheHint(cacheHint);
        behindUserTrainerCanvas.setCacheHint(cacheHint);
        roofCanvas.setCacheHint(cacheHint);
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
        disposables.add(trainersService.getTrainers(trainerStorageProvider.get().getTrainer().region(), null, null).observeOn(FX_SCHEDULER).subscribe(
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

        // Init cache hints
        groundCanvas.setCache(true);
        overUserTrainerCanvas.setCache(true);
        userTrainerCanvas.setCache(true);
        behindUserTrainerCanvas.setCache(true);
        roofCanvas.setCache(true);
        setCacheHint(CacheHint.QUALITY);

        initMapShiftTransitions();

        // Setup trainer controller for own trainer
        trainerController = new TrainerController(
                this,
                trainerStorageProvider.get().getTrainer(),
                trainerStorageProvider.get().getTrainerSpriteChunk(),
                userTrainerCanvas.getGraphicsContext2D(),
                null
        );
        trainerController.init();

        if (!GraphicsEnvironment.isHeadless()) {
            trainerController.startAnimations();
        }

        // Add event handlers
        app.getStage().getScene().addEventHandler(KeyEvent.KEY_PRESSED, keyPressedHandler);

        Region region = trainerStorageProvider.get().getRegion();
        disposables.add(areasService.getArea(region._id(), trainerStorageProvider.get().getTrainer().area()).observeOn(FX_SCHEDULER).subscribe(area -> {
            loadMap(area.map());
            ;
        }, error -> showError(error.getMessage())));
        monstersListControllerProvider.get().init();

        popupStage = new Stage();
        popupStage.initOwner(app.getStage());

        this.notificationListHandyController = notificationListHandyControllerProvider.get();
        notificationListHandyController.init(this, trainerStorageProvider.get().getTrainer());
        stackPane.getChildren().add(notificationListHandyController.render());
        this.notificationHandyStackPane = (StackPane) stackPane.getChildren().get(stackPane.getChildren().size() - 1);

        this.notificationHandyStackPane.translateXProperty().bind(
                anchorPane.
                        widthProperty().
                        add(notificationHandyStackPane.widthProperty()).
                        divide(2).
                        add(offsetToNotShowPhoneInScreen)
        );

        if (!GraphicsEnvironment.isHeadless()) {
            smallHandyImageView.setImage(new Image(Objects.requireNonNull(App.class.getResource(smallHandyImage)).toString()));
            monsterForHandyImageView.setImage(new Image(Objects.requireNonNull(App.class.getResource(AVATAR_1)).toString()));
            notificationBell.setImage(new Image(Objects.requireNonNull(App.class.getResource(notificationBellImage)).toString()));
        }

        //Setup Encounter
        checkIfEncounterAlreadyExist();
        listenToEncounter();
        listenToOpponents();
        encounterOpponentStorage.setRegionId(trainerStorageProvider.get().getRegion()._id());


        specificSounds();

        return parent;
    }

    private void initMapShiftTransitions() {
        shiftMapLeftTransition = new ParallelTransition(
                getMapMovementTransition(groundCanvas, -SCALE_FACTOR * TILE_SIZE, 0),
                getMapMovementTransition(behindUserTrainerCanvas, -SCALE_FACTOR * TILE_SIZE, 0),
                getMapMovementTransition(userTrainerCanvas, -SCALE_FACTOR * TILE_SIZE, 0),
                getMapMovementTransition(overUserTrainerCanvas, -SCALE_FACTOR * TILE_SIZE, 0),
                getMapMovementTransition(roofCanvas, -SCALE_FACTOR * TILE_SIZE, 0)
        );
        shiftMapRightTransition = new ParallelTransition(
                getMapMovementTransition(groundCanvas, SCALE_FACTOR * TILE_SIZE, 0),
                getMapMovementTransition(behindUserTrainerCanvas, SCALE_FACTOR * TILE_SIZE, 0),
                getMapMovementTransition(userTrainerCanvas, SCALE_FACTOR * TILE_SIZE, 0),
                getMapMovementTransition(overUserTrainerCanvas, SCALE_FACTOR * TILE_SIZE, 0),
                getMapMovementTransition(roofCanvas, SCALE_FACTOR * TILE_SIZE, 0)
        );
        shiftMapUpTransition = new ParallelTransition(
                getMapMovementTransition(groundCanvas, 0, -SCALE_FACTOR * TILE_SIZE),
                getMapMovementTransition(behindUserTrainerCanvas, 0, -SCALE_FACTOR * TILE_SIZE),
                getMapMovementTransition(userTrainerCanvas, 0, -SCALE_FACTOR * TILE_SIZE),
                getMapMovementTransition(overUserTrainerCanvas, 0, -SCALE_FACTOR * TILE_SIZE),
                getMapMovementTransition(roofCanvas, 0, -SCALE_FACTOR * TILE_SIZE)
        );
        shiftMapDownTransition = new ParallelTransition(
                getMapMovementTransition(groundCanvas, 0, SCALE_FACTOR * TILE_SIZE),
                getMapMovementTransition(behindUserTrainerCanvas, 0, SCALE_FACTOR * TILE_SIZE),
                getMapMovementTransition(userTrainerCanvas, 0, SCALE_FACTOR * TILE_SIZE),
                getMapMovementTransition(overUserTrainerCanvas, 0, SCALE_FACTOR * TILE_SIZE),
                getMapMovementTransition(roofCanvas, 0, SCALE_FACTOR * TILE_SIZE)
        );
    }

    private void changeRegion() {
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

    public void listenToMovement(ObservableList<MoveTrainerDto> moveTrainerDtos, String area) {
        disposables.add(udpEventListenerProvider.get().listen("areas." + area + ".trainers.*.*", MoveTrainerDto.class)
                .observeOn(FX_SCHEDULER).subscribe(event -> {
                    final MoveTrainerDto moveTrainerDto = event.data();
                    moveTrainerDtos.add(moveTrainerDto);
                    if (moveTrainerDto._id().equals(trainerStorageProvider.get().getTrainer()._id())) {
                        if (!Objects.equals(moveTrainerDto.area(), trainerStorageProvider.get().getTrainer().area())) {
                            changeRegion();
                        }
                        int oldXValue = trainerStorageProvider.get().getX();
                        int oldYValue = trainerStorageProvider.get().getY();
                        if (oldXValue != moveTrainerDto.x() || oldYValue != moveTrainerDto.y()) {
                            trainerController.setTrainerTargetPosition(moveTrainerDto.x(), moveTrainerDto.y());
                            trainerController.setTrainerDirection(moveTrainerDto.direction());
                            setCacheHint(CacheHint.SPEED);
                            if (oldXValue < moveTrainerDto.x()) {
                                shiftMapLeftTransition.play();
                            } else if (oldXValue > moveTrainerDto.x()) {
                                shiftMapRightTransition.play();
                            } else if (oldYValue < moveTrainerDto.y()) {
                                shiftMapUpTransition.play();
                            } else {
                                shiftMapDownTransition.play();
                            }
                            setCacheHint(CacheHint.QUALITY);
                        } else {
                            trainerController.turn(moveTrainerDto.direction());
                        }
                        trainerStorageProvider.get().setX(moveTrainerDto.x());
                        trainerStorageProvider.get().setY(moveTrainerDto.y());
                        trainerStorageProvider.get().setDirection(moveTrainerDto.direction());
                        Position position = new Position(moveTrainerDto.x(), moveTrainerDto.y(), moveTrainerDto.direction());
                        trainerPositionHashMap.put(trainerStorageProvider.get().getTrainer(), position);
                    } else {
                        if (trainers != null) {
                            List<Trainer> equalTrainers = this.trainers.stream().filter(tr -> tr._id().equals(moveTrainerDto._id())).toList();
                            if (equalTrainers == null || equalTrainers.isEmpty()) {
                                return;
                            }
                            Trainer trainer = equalTrainers.get(0);
                            Position oldPosition = trainerPositionHashMap.get(trainer);
                            TrainerController trainerController = trainerControllerHashMap.get(trainer);
                            if (oldPosition != null && trainerController != null) {
                                if (oldPosition.getX() != moveTrainerDto.x() || oldPosition.getY() != moveTrainerDto.y()) {
                                    trainerController.setTrainerTargetPosition(moveTrainerDto.x(), moveTrainerDto.y());
                                }
                                trainerPositionHashMap.put(trainer, new Position(moveTrainerDto.x(), moveTrainerDto.y(), moveTrainerDto.direction()));
                            }
                        }

                    }
                }, error -> {
                    showError(error.getMessage());
                    error.printStackTrace();
                }));
    }

    /**
     * This method is used to get the transition of the map movement given the map, the x and y values.
     *
     * @param map : Canvas of the map
     * @param x   : x value of the movement (in pixels)
     * @param y   : y value of the movement (in pixels)
     * @return : new Timeline of the transition with the specified values.
     */
    private Timeline getMapMovementTransition(Canvas map, int x, int y) {
        return new Timeline(
                new KeyFrame(Duration.millis(TRANSITION_DURATION), e -> {
                    TranslateTransition translateTransition = new TranslateTransition();
                    translateTransition.setDuration(Duration.millis(TRANSITION_DURATION));
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
        loadingMap = true;
        tileSetImages.clear();
        for (TileSet tileSet : map.tilesets()) {
            final String mapName = getFileName(tileSet.source());
            disposables.add(presetsService.getTileset(mapName)
                    .doOnNext(tileset -> tileSetJsons.put(mapName, tileset))
                    .flatMap(tileset -> presetsService.getTilesetImage(tileset.image()))
                    .doOnNext(image -> tileSetImages.put(mapName, image))
                    .observeOn(FX_SCHEDULER).subscribe(image -> afterAllTileSetsLoaded(map), error -> {
                        showError(error.getMessage());
                        error.printStackTrace();
                    }));
        }
        focusOnPlayerPosition(map.width(), map.height(), trainerStorageProvider.get().getX(), trainerStorageProvider.get().getY());
    }

    private void focusOnPlayerPosition(double mapWidth, double mapHeight, int tilePosX, int tilePosY) {
        int shiftX = (int) calculateInitialCameraXOffset(mapWidth, tilePosX);
        int shiftY = (int) calculateInitialCameraYOffset(mapHeight, tilePosY);
        int additionalShiftY = TILE_SIZE * SCALE_FACTOR;
        getMapMovementTransition(groundCanvas, shiftX, shiftY + additionalShiftY).play();
        getMapMovementTransition(behindUserTrainerCanvas, shiftX, shiftY).play();
        getMapMovementTransition(userTrainerCanvas, shiftX, shiftY).play();
        getMapMovementTransition(overUserTrainerCanvas, shiftX, shiftY).play();
        getMapMovementTransition(roofCanvas, shiftX, shiftY + additionalShiftY + 1).play();
    }

    /**
     * This method is used to calculate the initial x offset for the camera to center the player at the right position
     *
     * @param mapWidth - width of the rendered map (in tiles)
     * @return x offset that the map needs to be shifted
     */
    private double calculateInitialCameraXOffset(double mapWidth, int tilePosX) {
        return (int) ((((mapWidth * TILE_SIZE) / (double) TILE_SIZE) / SCALE_FACTOR) - tilePosX) * TILE_SIZE * SCALE_FACTOR;
    }

    /**
     * This method is used to calculate the initial y offset for the camera to center the player at the right position
     *
     * @param mapHeight - height of the rendered map (in tiles)
     * @return y offset that the map needs to be shifted
     */
    private double calculateInitialCameraYOffset(double mapHeight, int tilePosY) {
        return (int) (((((mapHeight * TILE_SIZE) / (double) TILE_SIZE) / SCALE_FACTOR) - tilePosY) * TILE_SIZE * SCALE_FACTOR);
    }

    /**
     * loadPlayers is used to initially load all players on the map. For every trainer it loads the image of the player and sets its position.
     */
    private void loadPlayers() {
        trainers.forEach(trainer -> {
            if (!Objects.equals(trainerStorageProvider.get().getTrainer()._id(), trainer._id())
                    && trainer.area().equals(trainerStorageProvider.get().getTrainer().area())) {
                loadPlayer(trainer);
            }
        });
        if (!GraphicsEnvironment.isHeadless()) {
            afterAllTrainersLoaded();
        }
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
            TrainerController trainerController = new TrainerController(
                    this,
                    trainer,
                    trainerChunk,
                    behindUserTrainerCanvas.getGraphicsContext2D(),
                    overUserTrainerCanvas.getGraphicsContext2D()
            );
            trainerControllerHashMap.put(trainer, trainerController);
            trainerController.init();
        } else {
            disposables.add(presetsService.getCharacter(trainer.image()).observeOn(FX_SCHEDULER).subscribe(
                    image -> {
                        Image trainerChunk = ImageProcessor.resonseBodyToJavaFXImage(image);
                        TrainerController trainerController = new TrainerController(
                                this,
                                trainer,
                                trainerChunk,
                                behindUserTrainerCanvas.getGraphicsContext2D(),
                                overUserTrainerCanvas.getGraphicsContext2D()
                        );
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
                setCanvasSettings(map, groundCanvas, false);
                setCanvasSettings(map, behindUserTrainerCanvas, false);
                setCanvasSettings(map, userTrainerCanvas, false);
                setCanvasSettings(map, overUserTrainerCanvas, false);
                setCanvasSettings(map, roofCanvas, false);
                for (TileSet tileSet : map.tilesets()) {
                    renderMap(map, tileSetImages.get(getFileName(tileSet.source())), tileSetJsons.get(getFileName(tileSet.source())),
                            tileSet, map.tilesets().size() > 1, false);
                }
                loadPlayers();
                loadMiniMap();
                loadingMap = false;
            }
        }
    }

    public void setCanvasSettings(Map map, Canvas canvas, boolean forMiniMap) {
        if (!forMiniMap) {
            canvas.setScaleX(SCALE_FACTOR);
            canvas.setScaleY(SCALE_FACTOR);
        }
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
     * @param forMiniMap       True if used for miniMap, else False
     */

    private void renderMap(Map map, Image image, TileSet tileSetJson, TileSet tileSet, boolean multipleTileSets, boolean forMiniMap) {
        for (Layer layer : map.layers()) {
            if (layer.chunks() == null) {
                if (layer.data() != null) {
                    renderData(map, image, tileSet, tileSetJson, multipleTileSets, layer, forMiniMap);
                }
                continue;
            }
            for (Chunk chunk : layer.chunks()) {
                renderChunk(map, image, tileSet, tileSetJson, multipleTileSets, chunk, forMiniMap);
            }
        }
    }

    private void renderData(Map map, Image image, TileSet tileSet, TileSet tileSetJson, boolean multipleTileSets, Layer layer, boolean forMiniMap) {
        renderTiles(map, image, tileSet, tileSetJson, multipleTileSets, layer.width(), layer.height(), layer.data(), layer.x(), layer.y(), forMiniMap);
    }

    private void renderTiles(Map map, Image image, TileSet tileSet, TileSet tileSetJson, boolean multipleTileSets, int width, int height, List<Integer> data, int x2, int y2, boolean forMiniMap) {
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
        if (forMiniMap) {
            miniMapCanvas.getGraphicsContext2D().drawImage(writableImageGround, x2 * TILE_SIZE, y2 * TILE_SIZE);
        } else {
            groundCanvas.getGraphicsContext2D().drawImage(writableImageGround, x2 * TILE_SIZE, y2 * TILE_SIZE);
            roofCanvas.getGraphicsContext2D().drawImage(writableImageTop, x2 * TILE_SIZE, y2 * TILE_SIZE);
        }
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
     * @param forMiniMap       True if used for miniMap, else False
     */

    private void renderChunk(Map map, Image image, TileSet tileSet, TileSet tileSetJson, boolean multipleTileSets, Chunk chunk, boolean forMiniMap) {
        renderTiles(map, image, tileSet, tileSetJson, multipleTileSets, chunk.width(), chunk.height(), chunk.data(), chunk.x(), chunk.y(), forMiniMap);
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
        smallHandyButton.setVisible(false);
        notificationBell.setVisible(false);

        for (int i = 0; i < notificationHandyStackPane.getWidth(); i++) {
            int iterator = i;

            PauseTransition pause = new PauseTransition(Duration.millis(1));
            pause.setOnFinished(event -> notificationHandyStackPane.translateXProperty().bind(
                    anchorPane.
                            widthProperty().
                            add(notificationHandyStackPane.widthProperty()).
                            divide(2).
                            subtract(iterator)
            ));
            pause.setDelay(Duration.millis(i));
            pause.play();
        }
    }

    /**
     * This method is used to pause the game. It shows a dialog with two buttons.
     * The first button is used to resume the game and the second button is used to save the game and leave.
     * If the user presses the resume button, the dialog will be closed.
     * If the user presses the save and leave button, the game will be saved and
     * the user will be redirected to the main menu.
     */

    public void pauseGame() {
        ingamePauseMenuController = ingamePauseMenuControllerProvider.get();
        VBox pauseMenuVBox = new VBox();
        pauseMenuVBox.setAlignment(Pos.CENTER);
        ingamePauseMenuController.init(this, pauseMenuVBox, mainMenuControllerProvider, app);
        pauseMenuVBox.getChildren().add(ingamePauseMenuController.render());
        root.getChildren().add(pauseMenuVBox);
        pauseMenuVBox.requestFocus();
        buttonsDisable(true);
        inSettings = false;
    }

    public void buttonsDisable(Boolean set) {
        if (set) {
            stackPane.setEffect(new BoxBlur(10, 10, 3));
        } else {
            stackPane.setEffect(null);
        }
        isPaused = set;
        movementDisabled = set;
        inNpcPopup = set;
        monstersButton.setDisable(set);
        pauseButton.setDisable(set);
        showChatButton.setDisable(set);
        mapSymbol.setDisable(set);
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
        inSettings = true;
    }

    public void showTrainerSettings() {
        IngameTrainerSettingsController ingameTrainerSettingsController = ingameTrainerSettingsControllerProvider.get();
        VBox trainersettingsVBox = new VBox();
        trainersettingsVBox.setAlignment(Pos.CENTER);
        ingameTrainerSettingsController.initIngame(this, trainersettingsVBox);
        trainersettingsVBox.getChildren().add(ingameTrainerSettingsController.render());
        root.getChildren().add(trainersettingsVBox);
        trainersettingsVBox.requestFocus();
        buttonsDisable(true);


        ingameTrainerSettingsControllerProvider.get().setApp(this.app);
        ingameTrainerSettingsControllerProvider.get().setValues(resources, preferences, resourceBundleProvider, ingameTrainerSettingsControllerProvider.get(), app);
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
                                TrainerController trainerController = new TrainerController(
                                        this,
                                        trainer,
                                        new Image(resource.toString()),
                                        behindUserTrainerCanvas.getGraphicsContext2D(),
                                        overUserTrainerCanvas.getGraphicsContext2D()
                                );
                                trainerController.init();
                                if (!GraphicsEnvironment.isHeadless()) {
                                    trainerController.startAnimations();
                                }
                                trainerControllerHashMap.put(trainer, trainerController);
                                trainerPositionHashMap.put(trainer, new Position(trainer.x(), trainer.y(), trainer.direction()));
                            }
                        }
                        case "updated" -> {
                            updateTrainer(trainers, trainer);
                            if (trainerStorageProvider.get().getTrainer()._id().equals(trainer._id())) {
                                monstersListControllerProvider.get().init();
                            }
                            // albert
                            if (trainer._id().equals("645e32c6866ace359554a802")) {
                                trainerPositionHashMap.entrySet().removeIf(trainerPositionEntry -> trainerPositionEntry.getKey()._id().equals(trainer._id()));
                                trainerPositionHashMap.put(trainer, new Position(trainer.x(), trainer.y(), trainer.direction()));
                            }
                        }
                        case "deleted" -> {
                            trainers.removeIf(t -> t._id().equals(trainer._id()));
                            trainerControllerHashMap.get(trainer).destroy();
                            trainerControllerHashMap.remove(trainer);
                            trainerPositionHashMap.remove(trainer);
                        }
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
                            if (messages.stream().noneMatch(m -> m._id().equals(message._id()))) {
                                messages.add(message);
                                chatListView.scrollTo(chatListView.getItems().size() - 1);
                            }
                        }
                        case "updated" -> updateMessage(messages, message);
                        case "deleted" -> messages.removeIf(m -> m._id().equals(message._id()));
                    }
                }, error -> {
                    showError(error.getMessage());
                    error.printStackTrace();
                }));
    }

    public void listenToEncounter() {
        String regionId = trainerStorageProvider.get().getRegion()._id();
        String trainerId = trainerStorageProvider.get().getTrainer()._id();
        encounterOpponentStorage.setRegionId(regionId);
        disposables.add(eventListener.get().listen("regions." + trainerStorageProvider.get().getRegion()._id() + ".encounters.*.*", Encounter.class)
                .observeOn(FX_SCHEDULER).subscribe(event -> {
                    final Encounter encounter = event.data();
                    if (event.suffix().equals("created")) {
                        // getEncounterOpponents(regionId, encounter._id()); isn't working
                    }
                }, error -> {
                    showError(error.getMessage());
                    error.printStackTrace();
                })
        );
    }

    public void listenToOpponents() {
        encounterOpponentStorage.setRegionId(trainerStorageProvider.get().getRegion()._id());
        disposables.add(eventListener.get().listen("encounters.*.opponents.*.*", Opponent.class)
                .observeOn(FX_SCHEDULER).subscribe(opponentEvent -> {
                    final Opponent opponent = opponentEvent.data();
                    switch (opponentEvent.suffix()) {
                        case "created" -> {
                            opponents.add(opponent);
                        }
                        case "deleted" -> opponents.removeIf(o -> o._id().equals(opponent._id()));
                    }
                    for (Opponent o : opponents) {
                        if (o.trainer().equals(trainerStorageProvider.get().getTrainer()._id())) {
                            encounterOpponentStorage.setSelfOpponent(o);
                            encounterOpponentStorage.setEncounterId(o.encounter());
                        }
                    }
                    for (Opponent o : opponents) {
                        if (o.encounter().equals(encounterOpponentStorage.getEncounterId()) && !o.trainer().equals(trainerStorageProvider.get().getTrainer()._id())) {
                            encounterOpponentStorage.setEnemyOpponent(o);
                        }
                    }
                    if (encounterOpponentStorage.getSelfOpponent() != null && encounterOpponentStorage.getEnemyOpponent() != null) {
                        showEncounterInfoWindow();
                    }
                }, error -> {
                    showError(error.getMessage());
                    error.printStackTrace();
                })
        );
    }

    private void showEncounterInfoWindow() {
        //TODO: EncounterInfoWindow and Interaction with E
        showEncounterScene();
    }

    private void showEncounterScene() {
        app.show(encounterControllerProvider.get());
    }

    private void checkIfEncounterAlreadyExist() {
        String regionId = trainerStorageProvider.get().getRegion()._id();
        String trainerId = trainerStorageProvider.get().getTrainer()._id();

        //TODO: Encounter wieder hesrstellen
    }

    //TODO: Bei dem Dialogfenster wenn man E drückt, wird die Scenen nach Encountercontroller rü

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

    public void setTrainerSpriteImageView(Trainer trainer, ImageView imageView, int direction) {
        if (!GraphicsEnvironment.isHeadless()) {
            disposables.add(presetsService.getCharacter(trainer.image()).observeOn(FX_SCHEDULER).subscribe(responseBody -> {
                        Image trainerSprite = ImageProcessor.resonseBodyToJavaFXImage(responseBody);
                        Image[] character = ImageProcessor.cropTrainerImages(trainerSprite, direction, false);
                        imageView.setImage(character[0]);
                    }, error -> showError(error.getMessage())
            ));
        }
    }

    public void showMonsters() {
        VBox monsterListVBox = new VBox();
        monsterListVBox.maxWidth(600);
        monsterListVBox.maxHeight(410);
        monsterListVBox.setAlignment(Pos.CENTER);
        MonstersListController monstersListController = monstersListControllerProvider.get();
        monstersListController.init(this, monsterListVBox);
        monsterListVBox.getChildren().add(monstersListController.render());
        root.getChildren().add(monsterListVBox);
        monsterListVBox.requestFocus();
        buttonsDisable(true);

        /*Scene popupScene = new Scene(monstersListControllerProvider.get().render());
        popupStage.setScene(popupScene);
        popupStage.setTitle(resources.getString("MONSTERS"));
        popupStage.show();
         */
    }

    /*
     ** NPC methods **
     */

    public void interactWithTrainer() {
        if (inDialog) {
            try {
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

                if (trainerControllerHashMap.containsKey(this.currentNpc) && trainerControllerHashMap.get(this.currentNpc).getDirection() != currentDirection) {
                    int turnDirection;
                    switch (currentDirection) {
                        case 0 -> turnDirection = 2;
                        case 1 -> turnDirection = 3;
                        case 2 -> turnDirection = 0;
                        default -> turnDirection = 1;
                    }
                    trainerControllerHashMap.get(this.currentNpc).turn(turnDirection);
                }

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
            case 0 -> {                         // facing right
                checkTileX++;
                checkTileXForNurse += 2;
            }
            case 1 -> {                         // facing up
                checkTileY--;
                checkTileYForNurse -= 2;
            }
            case 2 -> {                         // facing left
                checkTileX--;
                checkTileXForNurse -= 2;
            }
            case 3 -> {                         // facing down
                checkTileY++;
                checkTileYForNurse += 2;
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
            ).observeOn(FX_SCHEDULER).subscribe());
        }
    }

    public void continueTrainerDialog(DialogSpecialInteractions specialInteractions) {
        ContinueDialogReturnValues continueDialogReturn = dialogController.continueDialog(specialInteractions);

        switch (continueDialogReturn) {
            case dialogFinishedTalkToTrainer -> endDialog(-1, true);
            case albertDialogFinished0 -> {
                endDialog(0, true);
                this.notificationListHandyController.displayStarterMessages();
                notificationBell.setVisible(true);
            }
            case albertDialogFinished1 -> {
                endDialog(1, true);
                this.notificationListHandyController.displayStarterMessages();
                notificationBell.setVisible(true);

            }
            case albertDialogFinished2 -> {
                endDialog(2, true);
                this.notificationListHandyController.displayStarterMessages();
                notificationBell.setVisible(true);
            }
            case dialogFinishedNoTalkToTrainer -> endDialog(0, false);
            case spokenToNurse -> createNurseHealPopup();
            case encounterOnTalk -> {
                disposables.add(udpEventListenerProvider.get().talk(trainerStorageProvider.get().getTrainer().area(), new TalkTrainerDto(
                        trainerStorageProvider.get().getTrainer()._id(),
                        this.currentNpc._id(),
                        0
                )).observeOn(FX_SCHEDULER).subscribe());
                endDialog(0, true);
            }
            default -> {
            }
        }
    }

    public void endDialog(int selectionValue, boolean encounterNpc) {
        this.dialogController.destroy();
        inDialog = false;

        // Turn NPC back to original direction after finishing dialog
        if (trainerControllerHashMap.containsKey(this.currentNpc)
                && trainerControllerHashMap.get(this.currentNpc).getDirection() != this.currentNpc.direction()
        ) {
            trainerControllerHashMap.get(this.currentNpc).turn(this.currentNpc.direction());
        }
        stackPane.getChildren().remove(dialogStackPane);

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
        StackPane dialogStackPane = new StackPane();
        dialogStackPane.setId("dialogStackPane");
        dialogStackPane.setMaxHeight(160);
        dialogStackPane.setMaxWidth(700);

        Label nameLabel = new Label(this.currentNpc.name());
        nameLabel.setPadding(new Insets(5, 10, 5, 10));

        VBox dialogVBox = new VBox();
        dialogVBox.setMinWidth(dialogVBoxWidth);
        dialogVBox.maxWidthProperty().bind(stackPane.widthProperty().divide(2));
        dialogVBox.setMaxHeight(getDialogVBoxHeight);
        dialogVBox.setId("dialogVBox");

        int constantSpacer = spacerToBottomOfScreen;
        dialogVBox.translateYProperty().bind((anchorPane.heightProperty().subtract(dialogVBox.maxHeightProperty()).subtract(constantSpacer)).divide(2).add(10));

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


        dialogStackPane.getChildren().add(dialogVBox);

        nameLabel.translateYProperty().bind(dialogVBox.translateYProperty().subtract(dialogVBox.heightProperty().divide(2)));
        nameLabel.translateXProperty().bind(dialogVBox.translateXProperty().subtract(dialogVBox.widthProperty()).divide(3));
        nameLabel.getStyleClass().add("npcNameLabel");
        dialogStackPane.getChildren().add(nameLabel);

        stackPane.getChildren().add(dialogStackPane);
        this.dialogStackPane = dialogStackPane;

        return dialogTextFlow;
    }

    public void loadMiniMap() {
        disposables.add(regionsService.getRegion(
                trainerStorageProvider.get().getRegion()._id()
        ).observeOn(FX_SCHEDULER).subscribe(region -> {
                    miniMap = region.map();
                    for (TileSet tileSet : miniMap.tilesets()) {
                        final String mapName = getFileName(tileSet.source());
                        disposables.add(presetsService.getTileset(mapName)
                                .doOnNext(tileset -> tileSetJsons.put(mapName, tileset))
                                .flatMap(tileset -> presetsService.getTilesetImage(tileset.image()))
                                .doOnNext(image -> tileSetImages.put(mapName, image))
                                .observeOn(FX_SCHEDULER).subscribe(image -> {
                                    setCanvasSettings(miniMap, miniMapCanvas, true);
                                    for (TileSet tileSet1 : miniMap.tilesets()) {
                                        renderMap(miniMap, tileSetImages.get(getFileName(tileSet1.source())), tileSetJsons.get(getFileName(tileSet1.source())),
                                                tileSet1, miniMap.tilesets().size() > 1, true);
                                    }
                                }, error -> {
                                    showError(error.getMessage());
                                    error.printStackTrace();
                                }));
                    }
                },
                error -> {
                    showError(error.getMessage());
                    error.printStackTrace();
                }
        ));
    }

    public void showMap() {
        IngameMiniMapController ingameMiniMapController = ingameMiniMapControllerProvider.get();
        if (miniMapVBox == null) {
            miniMapVBox = new VBox();
            miniMapVBox.getStyleClass().add("miniMapContainer");
            ingameMiniMapController.init(this, app, miniMapCanvas, miniMapVBox, miniMap);
            miniMapVBox.getChildren().add(ingameMiniMapController.render());
        }
        root.getChildren().add(miniMapVBox);
        miniMapVBox.requestFocus();
        buttonsDisable(true);
    }

    public void showStarterSelection(List<String> starters) {
        final boolean[] isSelection = {true};
        IngameStarterMonsterController ingameStarterMonsterController = ingameStarterMonsterControllerProvider.get();
        starterSelectionVBox = new VBox();
        starterSelectionVBox.getStyleClass().add("miniMapContainer");
        starterSelectionVBox.setStyle("-fx-max-height: 350px; -fx-max-width: 550px");
        starterSelectionVBox.setPadding(new Insets(0, 0, 8, 0));
        ingameStarterMonsterController.init(this, app, starters);
        starterSelectionVBox.getChildren().add(ingameStarterMonsterController.render());

        Button okButton = new Button();
        okButton.setId("okButton");
        okButton.setText(resources.getString("OK"));
        okButton.getStyleClass().add("welcomeSceneButton");
        okButton.setStyle("-fx-background-color: #e0ecfc");
        okButton.setOnAction(event -> {
            if (isSelection[0]) {
                isSelection[0] = false;
                AnchorPane starterAnchorPane = (AnchorPane) starterSelectionVBox.getChildren().get(0);
                Label starterLabel = (Label) starterAnchorPane.getChildren().get(0);
                starterLabel.setText(resources.getString("NEW.MONSTER.ADDED"));
                starterAnchorPane.getChildren().remove(3);
                starterAnchorPane.getChildren().remove(2);
            } else {
                root.getChildren().remove(starterSelectionVBox);
                buttonsDisable(false);
                switch (ingameStarterMonsterController.index - 1) {
                    case 0 -> continueTrainerDialog(DialogSpecialInteractions.starterSelection0);
                    case 1 -> continueTrainerDialog(DialogSpecialInteractions.starterSelection1);
                    case 2 -> continueTrainerDialog(DialogSpecialInteractions.starterSelection2);
                }
            }
        });
        starterSelectionVBox.getChildren().add(okButton);
        root.getChildren().add(starterSelectionVBox);
        starterSelectionVBox.requestFocus();
        buttonsDisable(true);
    }


    @Override
    public void destroy() {
        super.destroy();
        app.getStage().getScene().removeEventHandler(KeyEvent.KEY_PRESSED, keyPressedHandler);
        for (var trainerController : trainerControllerHashMap.values()) {
            trainerController.destroy();
        }
        trainerControllerHashMap.clear();
        trainerPositionHashMap.clear();
        messageField.removeEventHandler(KeyEvent.KEY_PRESSED, this::enterButtonPressedToSend);
    }

    public void showChangeAudioSettings() {
        VBox changeAudioVBox = new VBox();
        changeAudioVBox.setAlignment(Pos.CENTER);
        ChangeAudioController changeAudioController = changeAudioControllerProvider.get();
        changeAudioController.init(this, changeAudioVBox);
        changeAudioVBox.getChildren().add(changeAudioController.render());
        root.getChildren().add(changeAudioVBox);
        changeAudioVBox.requestFocus();
        buttonsDisable(true);
    }

    public void specificSounds() {
        if (!GraphicsEnvironment.isHeadless()) {
            disposables.add(areasService.getArea(trainerStorageProvider.get().getRegion()._id(), trainerStorageProvider.get().getTrainer().area()).
                    observeOn(FX_SCHEDULER).subscribe(area -> {
                        if (area.name().contains("Route")) {
                            AudioService.getInstance().stopSound();
                            AudioService.getInstance().playSound(ROUTE_SOUND);
                            AudioService.getInstance().setCurrentSound(ROUTE_SOUND);
                            AudioService.getInstance().setVolume(preferences.getDouble("volume", AudioService.getInstance().getVolume()));
                        } else if (area.map().infinite()) {
                            AudioService.getInstance().stopSound();
                            AudioService.getInstance().playSound(CITY_SOUND);
                            AudioService.getInstance().setCurrentSound(CITY_SOUND);
                            AudioService.getInstance().setVolume(preferences.getDouble("volume", AudioService.getInstance().getVolume()));
                        } else {
                            AudioService.getInstance().stopSound();
                            AudioService.getInstance().playSound(ROOMS_SOUND);
                            AudioService.getInstance().setCurrentSound(ROOMS_SOUND);
                            AudioService.getInstance().setVolume(preferences.getDouble("volume", AudioService.getInstance().getVolume()));
                        }
                        if (preferences.getBoolean("mute", false)) {
                            AudioService.getInstance().setVolume(0);
                        }
                    }, error -> this.showError(error.getMessage())));
        }
    }

    public int getUserTrainerY() {
        return trainerStorageProvider.get().getY();
    }
}
