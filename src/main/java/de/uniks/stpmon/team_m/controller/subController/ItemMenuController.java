package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.IngameController;
import de.uniks.stpmon.team_m.dto.AbilityDto;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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

    @Inject
    public ItemMenuController() {
    }

    public void init(IngameController ingameController, TrainersService trainersService, Provider<TrainerStorage> trainerStorageProvider,
                     VBox itemMenuBox) {
        super.init();
        this.ingameController = ingameController;
        this.trainersService = trainersService;
        this.trainerStorageProvider = trainerStorageProvider;
        this.itemMenuBox = itemMenuBox;
    }


    @Override
    public Parent render() {
        final Parent parent = super.render();
        // Items are now saved in trainerStorage
        initItems(trainerStorageProvider.get().getItems());

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
        System.out.println("Initing items: " + itemList.size());
        for (Item item : itemList) {
            initItem(item);
        }
        /*
        itemListView.setCellFactory(param -> new ItemCell(presetsService, this, resources, itemDescriptionBox));
        itemListView.getItems().addAll(itemList);
        itemListView.setFocusModel(null);
        itemListView.setSelectionModel(null);

         */
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
}
