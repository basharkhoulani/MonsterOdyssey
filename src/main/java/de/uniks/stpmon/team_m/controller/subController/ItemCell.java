package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.Constants;
import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.controller.IngameController;
import de.uniks.stpmon.team_m.dto.Item;
import de.uniks.stpmon.team_m.dto.ItemTypeDto;
import de.uniks.stpmon.team_m.service.PresetsService;
import de.uniks.stpmon.team_m.utils.ImageProcessor;
import de.uniks.stpmon.team_m.utils.ItemStorage;
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
    private final ItemStorage itemStorage;
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
    VBox itemDescriptionBox;

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
                    ItemStorage itemStorage
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
        this.itemStorage = itemStorage;
    }

    public void updateItem(Item item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setText(null);
            setGraphic(null);
            setStyle("-fx-background-color: #FFFFFF;");
        } else {
            this.itemTypeDto = itemMenuController.itemTypeHashMap.get(item.type());
            loadFXML();
            itemLabel.setText(itemTypeDto.name());
            itemNumber.setText("(" + item.amount() + ")");

            if (itemMenuController.itemImageHashMap.containsKey(item.type())) {
                this.itemImage = itemMenuController.itemImageHashMap.get(item.type());
                itemImageView.setImage(this.itemImage);
            } else {
                if (itemStorage.getItemData(item._id()) != null && itemStorage.getItemData(item._id()).getItemImage() != null) {
                    itemImageView.setImage(itemStorage.getItemData(item._id()).getItemImage());
                    this.itemImage = itemStorage.getItemData(item._id()).getItemImage();
                } else {
                    Image scaledImage = ImageProcessor.showScaledItemImage(itemTypeDto.image());
                    if (scaledImage == null) {
                        disposables.add(presetsService.getItemImage(itemTypeDto.id()).observeOn(FX_SCHEDULER)
                                .subscribe(itemImageResBody -> {
                                    Image itemImage = ImageProcessor.resonseBodyToJavaFXImage(itemImageResBody);
                                    itemMenuController.itemImageHashMap.put(item.type(), itemImage);
                                    this.itemImage = itemImage;
                                    itemImageView.setImage(itemImage);
                                    itemStorage.updateItemData(item, null, itemImage);
                                }, error -> {
                                    itemMenuController.showError(error.getMessage());
                                    error.printStackTrace();
                                }));
                    }
                    else {
                        itemStorage.updateItemData(item, null, scaledImage);
                        itemImageView.setImage(scaledImage);
                        this.itemImage = scaledImage;
                    }
                }
            }

            itemHBox.setOnMouseClicked(event -> {
                openItemDescription(itemTypeDto, this.itemImage, item);
                itemMenuController.setItemNameLabel(itemTypeDto.name());
            });
            setGraphic(itemHBox);

            // hide item count when buying items from clerk
            if (itemMenuController.getInventoryType() == Constants.InventoryType.buyItems) {
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

    public void openItemDescription(ItemTypeDto itemTypeDto, Image itemImage, Item item) {
        int ownAmountOfItem = 0;
        List<Item> trainerItems = itemMenuController.trainerStorageProvider.get().getItems();
        for (Item trainerItem : trainerItems) {
            if (trainerItem.type() == item.type()) {
                ownAmountOfItem = trainerItem.amount();
                break;
            }
        }

        ItemDescriptionController itemDescriptionController = new ItemDescriptionController();
        itemDescriptionController.setValues(resources, preferences, resourceBundleProvider, itemDescriptionController, app);
        itemDescriptionController.init(itemTypeDto, itemImage, item, itemMenuController.getInventoryType(), ownAmountOfItem, closeItemMenu, rootStackPane, ingameController);
        if (itemDescriptionBox.getChildren().size() != 0) {
            itemDescriptionBox.getChildren().clear();
        }
        itemDescriptionBox.getChildren().add(itemDescriptionController.render());
    }
}
