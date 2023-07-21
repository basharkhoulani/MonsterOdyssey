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
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemMenuController extends Controller {

    IngameController ingameController;
    @Inject
    Provider<TrainerItemsService> trainerItemsService;
    Provider<TrainerStorage> trainerStorageProvider;
    TrainersService trainersService;
    @FXML
    public ListView<ItemTypeDto> itemListView;
    @FXML
    public VBox itemDescriptionBox;
    public VBox itemMenuBox;

    @Inject
    public ItemMenuController(){}

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
        disposables.add(presetsService.getItems().observeOn(FX_SCHEDULER)
                        .subscribe(itemTypeDtos -> {
                            System.out.println(itemTypeDtos.size());
                            initItems(itemTypeDtos);
                        }, throwable -> showError(throwable.getMessage())));
        /*disposables.add(trainerItemsService.get().getItems(trainerStorageProvider.get().getRegion()._id(), trainerStorageProvider.get().getTrainer()._id(), String.valueOf(1)).observeOn(FX_SCHEDULER)
                .subscribe(this::initItems, throwable -> showError(throwable.getMessage())));
         */

        return parent;
    }

    public void initItems(List<ItemTypeDto> itemList) {
        itemListView.setCellFactory(param -> new ItemCell(presetsService, this, resources, itemDescriptionBox));
        itemListView.getItems().addAll(itemList);
        itemListView.setFocusModel(null);
        itemListView.setSelectionModel(null);
    }
    public void closeItemMenu() {
        ingameController.root.getChildren().remove(itemMenuBox);
        ingameController.buttonsDisable(false);
    }
}
