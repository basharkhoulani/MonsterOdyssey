package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.Constants;
import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.IngameController;
import de.uniks.stpmon.team_m.dto.Item;
import de.uniks.stpmon.team_m.dto.ItemTypeDto;
import de.uniks.stpmon.team_m.service.TrainerItemsService;
import de.uniks.stpmon.team_m.service.TrainersService;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    public VBox itemMenuBox;
    IngameController ingameController;
    private Constants.inventoryType inventoryType;
    private List<Integer> npcItemList;
    public HashMap<Integer, ItemTypeDto> itemTypeHashMap = new HashMap<>();
    public HashMap<Integer, Image> itemImageHashMap = new HashMap<>();

    @Inject
    public ItemMenuController() {
    }

    public void init(IngameController ingameController,
                     TrainersService trainersService,
                     Provider<TrainerStorage> trainerStorageProvider,
                     VBox itemMenuBox,
                     Constants.inventoryType inventoryType,
                     List<Integer> npcItemList) {
        super.init();
        this.ingameController = ingameController;
        this.trainersService = trainersService;
        this.trainerStorageProvider = trainerStorageProvider;
        this.itemMenuBox = itemMenuBox;
        this.inventoryType = inventoryType;
        this.npcItemList = npcItemList;
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
        itemListView.setCellFactory(param -> new ItemCell(presetsService, this, resources, itemDescriptionBox, preferences, resourceBundleProvider, app));
        itemListView.getItems().add(item);
        itemListView.setFocusModel(null);
        itemListView.setSelectionModel(null);
    }

    public void closeItemMenu() {
        ingameController.root.getChildren().remove(itemMenuBox);
        ingameController.buttonsDisable(false);
    }

    public Constants.inventoryType getInventoryType() {
        return this.inventoryType;
    }

    private void loadItems() {
        disposables.add(presetsService.getItems().observeOn(FX_SCHEDULER)
                .subscribe(itemTypes -> {
                    for (ItemTypeDto itemType : itemTypes) {
                        itemTypeHashMap.put(itemType.id(), itemType);
                    }

                    if (this.inventoryType == Constants.inventoryType.buyItems) {
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
}
