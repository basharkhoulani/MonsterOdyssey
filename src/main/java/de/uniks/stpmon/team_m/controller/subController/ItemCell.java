package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.Constants.InventoryType;
import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.controller.EncounterController;
import de.uniks.stpmon.team_m.controller.IngameController;
import de.uniks.stpmon.team_m.dto.Item;
import de.uniks.stpmon.team_m.dto.ItemTypeDto;
import de.uniks.stpmon.team_m.service.PresetsService;
import de.uniks.stpmon.team_m.utils.ImageProcessor;
import de.uniks.stpmon.team_m.utils.ItemStorage;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javax.inject.Provider;
import java.util.List;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;


public class ItemCell extends ListCell<Item> {
    public final PresetsService presetsService;
    public final ItemMenuController itemMenuController;
    private final ResourceBundle resources;
    private final Preferences preferences;
    private final Provider<ResourceBundle> resourceBundleProvider;
    private final App app;
    private final Runnable closeItemMenu;
    private final StackPane rootStackPane;
    private final IngameController ingameController;
    private final Provider<ItemStorage> itemStorageProvider;
    private final TrainerStorage trainerStorage;
    private final EncounterController encounterController;
    private FXMLLoader loader;
    protected final CompositeDisposable disposables = new CompositeDisposable();
    public static final Scheduler FX_SCHEDULER = Schedulers.from(Platform::runLater);

    @FXML
    public ImageView itemImageView;
    @FXML
    public Label itemLabel;
    @FXML
    public Label itemNumber;
    @FXML
    public HBox itemHBox;
    private Image itemImage;
    final VBox itemDescriptionBox;
    ItemTypeDto itemTypeDto;

    public ItemCell(PresetsService presetsService,
                    ItemMenuController itemMenuController,
                    ResourceBundle resources,
                    VBox itemDescriptionBox,
                    Preferences preferences,
                    Provider<ResourceBundle> resourceBundleProvider,
                    App app,
                    Runnable closeItemMenu,
                    StackPane rootStackPane,
                    IngameController ingameController,
                    Provider<ItemStorage> itemStorageProvider,
                    TrainerStorage trainerStorage
    ) {
        this.presetsService = presetsService;
        this.itemDescriptionBox = itemDescriptionBox;
        this.itemMenuController = itemMenuController;
        this.resources = resources;
        this.preferences = preferences;
        this.resourceBundleProvider = resourceBundleProvider;
        this.app = app;
        this.closeItemMenu = closeItemMenu;
        this.rootStackPane = rootStackPane;
        this.ingameController = ingameController;
        this.itemStorageProvider = itemStorageProvider;
        this.encounterController = null;
        this.trainerStorage = trainerStorage;
    }

    public ItemCell(PresetsService presetsService,
                    ItemMenuController itemMenuController,
                    ResourceBundle resources,
                    VBox itemDescriptionBox,
                    Preferences preferences,
                    Provider<ResourceBundle> resourceBundleProvider,
                    App app,
                    Runnable closeItemMenu,
                    StackPane rootStackPane,
                    EncounterController encounterController,
                    Provider<ItemStorage> itemStorageProvider,
                    TrainerStorage trainerStorage
    ) {
        this.presetsService = presetsService;
        this.itemDescriptionBox = itemDescriptionBox;
        this.itemMenuController = itemMenuController;
        this.resources = resources;
        this.preferences = preferences;
        this.resourceBundleProvider = resourceBundleProvider;
        this.app = app;
        this.closeItemMenu = closeItemMenu;
        this.rootStackPane = rootStackPane;
        this.encounterController = encounterController;
        this.itemStorageProvider = itemStorageProvider;
        this.ingameController = null;
        this.trainerStorage = trainerStorage;
    }

    public void updateItem(Item item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setText(null);
            setGraphic(null);
            setStyle("-fx-background-color: #FFFFFF;");
        } else {
            this.itemTypeDto = itemMenuController.itemTypeHashMap.get(item.type());
//            this.itemTypeDto = itemStorageProvider.get().getItemData(item._id()).getItemTypeDto();
            loadFXML();
            itemLabel.setText(itemTypeDto.name());
            itemNumber.setText("(" + item.amount() + ")");

            if (itemStorageProvider.get().getItemData(item._id()) != null && itemStorageProvider.get().getItemData(item._id()).itemImage() != null) {
                this.itemImage = itemStorageProvider.get().getItemData(item._id()).itemImage();
                itemImageView.setImage(this.itemImage);
            } else {
                disposables.add(presetsService.getItemImage(itemTypeDto.id()).observeOn(FX_SCHEDULER)
                        .subscribe(itemImageResBody -> {
                            Image itemImage = ImageProcessor.resonseBodyToJavaFXImage(itemImageResBody);
                            //itemMenuController.itemImageHashMap.put(item.type(), itemImage);
                            itemStorageProvider.get().updateItemData(item, null, itemImage);
                            this.itemImage = itemImage;
                            itemImageView.setImage(this.itemImage);
                        }, error -> {
                            itemMenuController.showError(error.getMessage());
                            error.printStackTrace();
                        }));
            }

            itemHBox.setOnMouseClicked(event -> {
                openItemDescription(itemTypeDto, this.itemImage, item, () -> itemMenuController.onItemUsed(item));
                itemMenuController.setItemNameLabel(itemTypeDto.name());
            });
            setGraphic(itemHBox);

            // hide item count when buying items from clerk
            if (itemMenuController.getInventoryType() == InventoryType.buyItems) {
                itemNumber.setVisible(false);
            }

            setStyle("-fx-border-width: 1px; -fx-border-color: #000000");
        }
    }

    private void loadFXML() {
        if (loader == null) {
            loader = new FXMLLoader(Main.class.getResource("views/ItemCell.fxml"));
            loader.setResources(resources);
            loader.setControllerFactory(c -> this);
            try {
                loader.load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void openItemDescription(ItemTypeDto itemTypeDto, Image itemImage, Item item, Runnable onItemUsed) {
        int ownAmountOfItem = 0;
        List<Item> trainerItems = itemMenuController.trainerStorageProvider.get().getItems();
        for (Item trainerItem : trainerItems) {
            if (trainerItem.type() == item.type()) {
                ownAmountOfItem = trainerItem.amount();
                break;
            }
        }

        ItemDescriptionController itemDescriptionController = new ItemDescriptionController(onItemUsed);
        itemDescriptionController.setValues(resources, preferences, resourceBundleProvider, itemDescriptionController, app);
        if (ingameController != null) {
            itemDescriptionController.init(itemTypeDto, itemImage, item, itemMenuController.getInventoryType(), ownAmountOfItem, closeItemMenu, rootStackPane, ingameController, itemMenuController);
        } else if (encounterController != null) {
            itemDescriptionController.initFromEncounter(itemTypeDto, itemImage, item, itemMenuController.getInventoryType(), ownAmountOfItem, closeItemMenu, rootStackPane, encounterController, trainerStorage);
        }
        if (itemDescriptionBox.getChildren().size() != 0) {
            itemDescriptionBox.getChildren().clear();
        }
        itemDescriptionBox.getChildren().add(itemDescriptionController.render());
    }
}
