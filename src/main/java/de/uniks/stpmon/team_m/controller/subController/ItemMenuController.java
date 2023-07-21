package de.uniks.stpmon.team_m.controller.subController;

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
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.net.URL;
import java.util.List;

public class ItemMenuController extends Controller {

    IngameController ingameController;
    @Inject
    Provider<TrainerItemsService> trainerItemsService;
    Provider<TrainerStorage> trainerStorageProvider;
    TrainersService trainersService;
    @FXML
    public ImageView closeImageView;
    @FXML
    public ListView<Item> itemListView;
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

    public void initItemList() {

    }
    @Override
    public Parent render() {
        final Parent parent = super.render();
        URL resourceClose = Main.class.getResource("images/close-x.png");
        assert resourceClose != null;
        Image closeImage = new Image(resourceClose.toString());
        closeImageView.setImage(closeImage);
        disposables.add(trainerItemsService.get().getItems(trainerStorageProvider.get().getRegion()._id(), trainerStorageProvider.get().getTrainer()._id(), String.valueOf(1)).observeOn(FX_SCHEDULER)
                .subscribe(itemList -> {
                    System.out.println(itemList.size());
                }, throwable -> {
                    System.out.println(throwable.getMessage());
                    //showError(throwable.getMessage())
                }));

        return parent;
    }

    public void initItems(List<Item> itemList) {
        itemListView.setCellFactory(param -> new ItemCell(presetsService, this, resources));
        itemListView.getItems().addAll(itemList);
        itemListView.setFocusModel(null);
        itemListView.setSelectionModel(null);
    }

    public void closeItemMenu() {
        ingameController.root.getChildren().remove(itemMenuBox);
        ingameController.buttonsDisable(false);
    }
}
