package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.Constants;
import de.uniks.stpmon.team_m.Constants.InventoryType;
import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.EncounterController;
import de.uniks.stpmon.team_m.controller.IngameController;
import de.uniks.stpmon.team_m.dto.Item;
import de.uniks.stpmon.team_m.dto.ItemTypeDto;
import de.uniks.stpmon.team_m.service.TrainerItemsService;
import de.uniks.stpmon.team_m.service.TrainersService;
import de.uniks.stpmon.team_m.utils.ItemStorage;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ItemMenuController extends Controller {
    @FXML
    public VBox inventoryRoot;
    @FXML
    public Label itemNameLabel;
    @FXML
    public ImageView closeInventoryIcon;
    @FXML
    public ListView<Item> itemListView;
    @FXML
    public VBox itemDescriptionBox;

    @Inject
    Provider<TrainerItemsService> trainerItemsService;
    @Inject
    Provider<TrainerStorage> trainerStorageProvider;
    @Inject
    TrainersService trainersService;
    @Inject
    Provider<ItemStorage> itemStorageProvider;

    public VBox itemMenuBox;
    IngameController ingameController;
    private InventoryType inventoryType;
    private List<Integer> npcItemList;
    public HashMap<Integer, ItemTypeDto> itemTypeHashMap = new HashMap<>();
    private StackPane rootStackPane;
    private EncounterController encounterController;

    @Inject
    public ItemMenuController() {
    }

    public void init(
            IngameController ingameController,
            TrainersService trainersService,
            Provider<TrainerStorage> trainerStorageProvider,
            VBox itemMenuBox,
            InventoryType inventoryType,
            List<Integer> npcItemList,
            StackPane rootStackPane
    ) {
        super.init();
        this.ingameController = ingameController;
        this.trainersService = trainersService;
        this.trainerStorageProvider = trainerStorageProvider;
        this.itemMenuBox = itemMenuBox;
        this.inventoryType = inventoryType;
        this.npcItemList = npcItemList;
        this.rootStackPane = rootStackPane;
    }

    public void initFromEncounter(
            EncounterController encounterController,
            TrainersService trainersService,
            Provider<TrainerStorage> trainerStorageProvider,
            VBox itemMenuBox,
            InventoryType inventoryType,
            List<Integer> npcItemList,
            StackPane rootStackPane
    ) {
        super.init();
        this.encounterController = encounterController;
        this.trainersService = trainersService;
        this.trainerStorageProvider = trainerStorageProvider;
        this.itemMenuBox = itemMenuBox;
        this.inventoryType = inventoryType;
        this.npcItemList = npcItemList;
        this.rootStackPane = rootStackPane;
    }

    @Override
    public Parent render() {
        final Parent parent = super.render();
        loadItems();

        if (!GraphicsEnvironment.isHeadless()) {
            closeInventoryIcon.setImage(new Image(Objects.requireNonNull(Main.class.getResource("images/close-x.png")).toExternalForm()));
        }
        closeInventoryIcon.setOnMouseClicked(evt -> closeItemMenu());
        return parent;
    }

    public void setItemNameLabel(String itemName) {
        itemNameLabel.setText(itemName);
    }

    public void initItems(List<Item> itemList) {
        for (Item item : itemList) {
            // if inventoryType == sell  AND  the item cannot be used, then skip rendering item
            if (this.inventoryType == InventoryType.sellItems && itemTypeHashMap.get(item.type()).use() == null) {
                continue;
            } else if (this.encounterController != null) {
                String use = itemTypeHashMap.get(item.type()).use();
                if (use == null) {
                    continue;
                } else {
                    if (use.equals(Constants.ITEM_USAGE_MONSTER_BOX) || use.equals(Constants.ITEM_USAGE_ITEM_BOX)) {
                        continue;
                    } else if (use.equals(Constants.ITEM_USAGE_BALL) && !encounterController.isWildEncounter()) {
                        continue;
                    }
                }
            }
            if (item.amount() == 0) {
                continue;
            }
            initItem(item);
        }
    }

    public void initNpcItems() {
        for (Integer itemTypeID : npcItemList) {
            // dummy item, so we don't have to refactor everything in the ItemCell controller for now
            initItem(new Item("1231245", "npc", itemTypeID, 69));
        }
    }

    public void initItem(Item item) {
        itemListView.setCellFactory(param -> new ItemCell(presetsService, this, resources, itemDescriptionBox, preferences, resourceBundleProvider, app, this::closeItemMenu, rootStackPane, ingameController, itemStorageProvider));
        if (ingameController != null) {
            ingameController.listenToItems(itemListView.getItems(), trainerStorageProvider.get().getTrainer()._id());
            itemListView.setCellFactory(param -> new ItemCell(presetsService, this, resources, itemDescriptionBox, preferences, resourceBundleProvider, app, this::closeItemMenu, rootStackPane, ingameController, itemStorageProvider));
        } else if (encounterController != null) {
            encounterController.getIngameController().listenToItems(itemListView.getItems(), trainerStorageProvider.get().getTrainer()._id());
            itemListView.setCellFactory(param -> new ItemCell(presetsService, this, resources, itemDescriptionBox, preferences, resourceBundleProvider, app, this::closeItemMenu, rootStackPane, encounterController, itemStorageProvider));
        }
        itemListView.getItems().add(item);
        itemListView.setFocusModel(null);
        itemListView.setSelectionModel(null);
    }

    public void onItemUsed(Item item) {
        if (item.amount() == 0) {
            itemListView.getItems().remove(item);
            return;
        }
        Item itemCopy = new Item(
                item._id(),
                item.trainer(),
                item.type(),
                item.amount() - 1
        );
        itemListView.getItems().set(itemListView.getItems().indexOf(item), itemCopy);
        itemStorageProvider.get().updateItemData(itemCopy, null, null);
        itemListView.refresh();

    }

    public void closeItemMenu() {
        if (ingameController != null) {
            ingameController.root.getChildren().remove(itemMenuBox);
            ingameController.buttonsDisable(false);
        }
        if (encounterController != null) {
            encounterController.rootStackPane.getChildren().remove(itemMenuBox);
            encounterController.buttonsDisableEncounter(false);
        }
    }

    public InventoryType getInventoryType() {
        return this.inventoryType;
    }

    private void loadItems() {
        disposables.add(presetsService.getItems().observeOn(FX_SCHEDULER)
                .subscribe(itemTypes -> {
                    for (ItemTypeDto itemType : itemTypes) {
                        itemTypeHashMap.put(itemType.id(), itemType);
                        trainerStorageProvider.get().getItems().stream().filter(item -> item.type() == itemType.id()).findFirst().ifPresent(relatedItem -> itemStorageProvider.get().addItemData(relatedItem, itemType, null));
                    }

                    if (this.inventoryType == InventoryType.buyItems) {
                        initNpcItems();
                    } else {
                        // Items are now saved in trainerStorage
                        initItems(trainerStorageProvider.get().getItems());
                    }
                }, error -> {
                    showError(error.getMessage());
                    error.printStackTrace();
                }));
    }

    public void updateListView() {
        this.itemListView.getItems().clear();
        initItems(trainerStorageProvider.get().getItems());
    }
}
