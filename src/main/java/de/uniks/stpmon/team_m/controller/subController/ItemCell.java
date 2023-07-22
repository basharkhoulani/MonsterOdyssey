package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.dto.AbilityDto;
import de.uniks.stpmon.team_m.dto.Item;
import de.uniks.stpmon.team_m.dto.ItemTypeDto;
import de.uniks.stpmon.team_m.service.PresetsService;
import de.uniks.stpmon.team_m.utils.ImageProcessor;
import io.reactivex.rxjava3.core.Observable;
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
import javafx.scene.layout.VBox;


import javax.inject.Inject;
import javax.inject.Provider;
import java.net.URL;
import java.util.ResourceBundle;

import static de.uniks.stpmon.team_m.Constants.ABILITYPALETTE;
import static de.uniks.stpmon.team_m.Constants.TYPESCOLORPALETTE;


public class ItemCell extends ListCell<Item> {
    public final PresetsService presetsService;
    public final ItemMenuController itemMenuController;
    private final ResourceBundle resources;
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

    @Inject
    Provider<ItemDescriptionController> itemDescriptionControllerProvider;


    public ItemCell(PresetsService presetsService, ItemMenuController itemMenuController,
                    ResourceBundle resources, VBox itemDescriptionBox) {
        this.presetsService = presetsService;
        this.itemDescriptionBox = itemDescriptionBox;
        this.itemMenuController = itemMenuController;
        this.resources = resources;
    }


    protected void updateItem(Item item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setText(null);
            setGraphic(null);
            setStyle("-fx-background-color: #FFFFFF;");
        } else {
            loadFXML();

            disposables.add(presetsService.getItemImage(Integer.parseInt(item._id())).observeOn(FX_SCHEDULER)
                    .subscribe(itemTypeDto -> {
                                this.itemTypeDto = itemTypeDto;
                            }, error -> {
                                itemMenuController.showError(error.getMessage());
                            }));

            disposables.add(presetsService.getItemImage(itemTypeDto.id()).observeOn(FX_SCHEDULER)
                    .subscribe(itemImage -> {
                        this.itemImage = ImageProcessor.resonseBodyToJavaFXImage(itemImage);
                        itemImageView.setImage(this.itemImage);
                    }, error -> {
                         itemMenuController.showError(error.getMessage());
                    }));
            itemLabel.setText(itemTypeDto.name());
            itemNumber.setText(itemTypeDto.use());

            itemHBox.setOnMouseClicked(event -> openItemDescription(itemTypeDto,this.itemImage));

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

    public void openItemDescription(ItemTypeDto itemTypeDto, Image itemImage) {
        ItemDescriptionController itemDescriptionController = itemDescriptionControllerProvider.get();
        itemDescriptionController.init(itemTypeDto, itemImage);
        itemDescriptionBox.getChildren().add(itemDescriptionController.render());
    }
}
