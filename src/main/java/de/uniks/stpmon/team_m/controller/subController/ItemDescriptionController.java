package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.EncounterController;
import de.uniks.stpmon.team_m.controller.IngameController;
import de.uniks.stpmon.team_m.dto.Item;
import de.uniks.stpmon.team_m.dto.ItemTypeDto;
import de.uniks.stpmon.team_m.dto.Trainer;
import de.uniks.stpmon.team_m.dto.UpdateItemDto;
import de.uniks.stpmon.team_m.service.AudioService;
import de.uniks.stpmon.team_m.service.TrainerItemsService;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.awt.*;
import java.net.URL;

import static de.uniks.stpmon.team_m.Constants.*;
import static de.uniks.stpmon.team_m.Constants.SoundEffect.BUY_SELL;

public class ItemDescriptionController extends Controller {

    private final Runnable onItemUsed;
    ItemTypeDto itemTypeDto;
    Image itemImage;
    Item item;
    @FXML
    public Label descriptionLabel;
    @FXML
    public Label itemAmountTitleLabel;
    @FXML
    public ImageView itemImageView;
    @FXML
    public Label itemAmountLabel;
    @FXML
    public Label itemPriceLabel;
    @FXML
    public TextFlow itemDescription;
    @FXML
    public ImageView coinImageView;
    @FXML
    public Button useButton;
    private InventoryType inventoryType;
    private int ownAmountOfItem;

    private Runnable closeItemMenu;
    private StackPane rootStackPane;
    private IngameController ingameController;
    private EncounterController encounterController;
    private ItemMenuController itemMenuController;
    private TrainerItemsService trainerItemsService;
    private TrainerStorage trainerStorage;


    public ItemDescriptionController(Runnable onItemUsed) {
        this.onItemUsed = onItemUsed;
    }

    public void init(ItemTypeDto itemTypeDto,
                     Image itemImage,
                     Item item,
                     InventoryType inventoryType,
                     int ownAmountOfITem,
                     Runnable closeItemMenu,
                     StackPane rootStackPane,
                     IngameController ingameController,
                     ItemMenuController itemMenuController) {
        super.init();
        this.itemImage = itemImage;
        this.itemTypeDto = itemTypeDto;
        this.item = item;
        this.inventoryType = inventoryType;
        this.ownAmountOfItem = ownAmountOfITem;
        this.closeItemMenu = closeItemMenu;
        this.rootStackPane = rootStackPane;
        this.ingameController = ingameController;
        this.itemMenuController = itemMenuController;

        this.trainerItemsService = ingameController.getTrainerItemsService();
        this.trainerStorage = ingameController.getTrainerStorage();
    }

    public void initFromEncounter(ItemTypeDto itemTypeDto,
                                  Image itemImage,
                                  Item item,
                                  InventoryType inventoryType,
                                  int ownAmountOfITem,
                                  Runnable closeItemMenu,
                                  StackPane rootStackPane,
                                  EncounterController encounterController,
                                  TrainerStorage trainerStorage) {
        super.init();
        this.itemImage = itemImage;
        this.itemTypeDto = itemTypeDto;
        this.item = item;
        this.inventoryType = inventoryType;
        this.ownAmountOfItem = ownAmountOfITem;
        this.closeItemMenu = closeItemMenu;
        this.rootStackPane = rootStackPane;
        this.encounterController = encounterController;
        this.trainerStorage = trainerStorage;
    }

    @Override
    public Parent render() {
        final Parent parent = super.render();
        itemImageView.setImage(itemImage);
        itemAmountLabel.setText(String.valueOf(item.amount()));
        Trainer trainer = trainerStorage.getTrainer();
        if (trainer.settings() != null && trainer.settings().itemPriceMultiplier() != null) {
            itemPriceLabel.setText(String.valueOf((int) (itemTypeDto.price() * trainer.settings().itemPriceMultiplier())));
        } else {
            itemPriceLabel.setText(String.valueOf(itemTypeDto.price()));
        }

        Text description = new Text(itemTypeDto.description());
        itemDescription.getChildren().add(description);

        if (itemTypeDto.use() == null) {
            useButton.setVisible(false);
            useButton.setDisable(true);
        } else if (inventoryType == InventoryType.showItems) {
            if (encounterController == null && itemTypeDto.use().equals(ITEM_USAGE_BALL)) {
                useButton.setVisible(false);
                useButton.setDisable(true);
            }
            if (item.amount() == 0) {
                useButton.setDisable(true);
            }
            useButton.setOnAction(evt -> {
                if (itemTypeDto.use().equals(ITEM_USAGE_EFFECT)) {
                    showMonsterList(item);
                    closeItemMenu.run();
                } else if (itemTypeDto.use().equals(ITEM_USAGE_BALL)) {
                    if (encounterController != null) {
                        encounterController.useItem(item, null);
                        onItemUsed.run();
                        closeItemMenu.run();
                    }
                } else {
                    ingameController.useItem(item, null);
                    closeItemMenu.run();
                    onItemUsed.run();
                }
            });
        }
        switch (this.inventoryType) {
            case buyItems -> {
                itemAmountTitleLabel.setText(resources.getString("CLERK.IN.BAG"));
                this.itemAmountLabel.setText(String.valueOf(ownAmountOfItem));

                useButton.setText(resources.getString("CLERK.BUY"));
                useButton.setOnAction(event -> buyItem());
            }
            case sellItems -> {
                useButton.setText(resources.getString("CLERK.SELL"));
                useButton.setOnAction(event -> sellItem());
                itemPriceLabel.setText(String.valueOf((itemTypeDto.price() / 2)));
            }
            default -> {
            }
        }

        if (!GraphicsEnvironment.isHeadless()) {
            URL resourceImage = Main.class.getResource("images/coin.png");
            assert resourceImage != null;
            Image coinImage = new Image(resourceImage.toString());
            coinImageView.setImage(coinImage);
        }

        return parent;
    }

    public void buyItem() {
        Trainer trainer = trainerStorage.getTrainer();
        int trainerCoins = Integer.parseInt(ingameController.coinsLabel.getText());
        if (trainer.settings() != null && trainer.settings().itemPriceMultiplier() != null) {
            if (trainerCoins < (int) (itemTypeDto.price() * trainer.settings().itemPriceMultiplier())) {
                descriptionLabel.setText(resources.getString("NOT.ENOUGH.COINS"));
                descriptionLabel.setStyle("-fx-text-fill: red");
                return;
            }
        } else if (trainerCoins < itemTypeDto.price()) {
            descriptionLabel.setText(resources.getString("NOT.ENOUGH.COINS"));
            descriptionLabel.setStyle("-fx-text-fill: red");
            return;
        }
        if (trainer.settings() != null && trainer.settings().itemPriceMultiplier() != null) {
            int i;
            double itemPriceMultiplier = trainer.settings().itemPriceMultiplier();
            for (i = 1; itemPriceMultiplier > 1.0; i++) {
                itemPriceMultiplier = itemPriceMultiplier - 0.5;
            }
            int finalI = i;
            if (finalI * itemTypeDto.price() > Integer.parseInt(ingameController.coinsLabel.getText())) {
                int canBuy = trainerCoins / itemTypeDto.price();
                int buy = finalI - canBuy;
                disposables.add(trainerItemsService.useOrTradeItem(
                        trainer.region(),
                        trainer._id(),
                        ITEM_ACTION_TRADE_ITEM,
                        new UpdateItemDto(canBuy, item.type(), null)
                ).observeOn(FX_SCHEDULER).subscribe(result -> {
                    disposables.add(trainerItemsService.useOrTradeItem(
                            trainer.region(),
                            trainer._id(),
                            ITEM_ACTION_TRADE_ITEM,
                            new UpdateItemDto(-canBuy, item.type(), null)
                    ).observeOn(FX_SCHEDULER).subscribe(result1 -> {
                        disposables.add(trainerItemsService.useOrTradeItem(
                                trainer.region(),
                                trainer._id(),
                                ITEM_ACTION_TRADE_ITEM,
                                new UpdateItemDto(buy, item.type(), null)
                        ).observeOn(FX_SCHEDULER).subscribe(result2 -> {
                            trainerStorage.addItem(result2);
                            trainerStorage.updateItem(result2);
                            ownAmountOfItem += buy;
                            this.itemAmountLabel.setText(String.valueOf(ownAmountOfItem));

                            ingameController.coinsLabel.setText(String.valueOf(Integer.parseInt(ingameController.coinsLabel.getText()) - (int) (itemTypeDto.price() * trainer.settings().itemPriceMultiplier())));
                        }));
                    }));
                }));
            } else {
                disposables.add(trainerItemsService.useOrTradeItem(
                        trainerStorage.getRegion()._id(),
                        trainerStorage.getTrainer()._id(),
                        ITEM_ACTION_TRADE_ITEM,
                        new UpdateItemDto(i, item.type(), null)
                ).observeOn(FX_SCHEDULER).subscribe(
                        result -> {
                            trainerStorage.addItem(result);
                            trainerStorage.updateItem(result);
                            ownAmountOfItem++;
                            this.itemAmountLabel.setText(String.valueOf(ownAmountOfItem));

                            ingameController.coinsLabel.setText(String.valueOf(Integer.parseInt(ingameController.coinsLabel.getText()) - (int) (itemTypeDto.price() * trainer.settings().itemPriceMultiplier())));
                            disposables.add(trainerItemsService.useOrTradeItem(
                                    trainerStorage.getRegion()._id(),
                                    trainerStorage.getTrainer()._id(),
                                    ITEM_ACTION_TRADE_ITEM,
                                    new UpdateItemDto(-(finalI - 1), item.type(), null)
                            ).observeOn(FX_SCHEDULER).subscribe());
                        },
                        error -> {
                            showError(error.getMessage());
                            error.printStackTrace();
                        }));
            }
        } else {
            disposables.add(trainerItemsService.useOrTradeItem(
                    trainerStorage.getRegion()._id(),
                    trainerStorage.getTrainer()._id(),
                    ITEM_ACTION_TRADE_ITEM,
                    new UpdateItemDto(1, item.type(), null)
            ).observeOn(FX_SCHEDULER).subscribe(
                    result -> {
                        trainerStorage.addItem(result);
                        trainerStorage.updateItem(result);
                        ownAmountOfItem++;
                        this.itemAmountLabel.setText(String.valueOf(ownAmountOfItem));

                        ingameController.coinsLabel.setText(String.valueOf(Integer.parseInt(ingameController.coinsLabel.getText()) - itemTypeDto.price()));
                    },
                    error -> {
                        showError(error.getMessage());
                        error.printStackTrace();
                    }));
        }
        if (!GraphicsEnvironment.isHeadless()) {
            AudioService.getInstance().playEffect(BUY_SELL, ingameController);
        }
    }

    public void sellItem() {
        disposables.add(trainerItemsService.useOrTradeItem(
                trainerStorage.getRegion()._id(),
                trainerStorage.getTrainer()._id(),
                ITEM_ACTION_TRADE_ITEM,
                new UpdateItemDto(-1, item.type(), null)
        ).observeOn(FX_SCHEDULER).subscribe(
                result -> {
                    trainerStorage.updateItem(result);
                    this.itemMenuController.updateListView();
                    ownAmountOfItem--;
                    this.itemAmountLabel.setText(String.valueOf(ownAmountOfItem));

                    ingameController.coinsLabel.setText(String.valueOf(Integer.parseInt(ingameController.coinsLabel.getText()) + itemTypeDto.price() / 2));

                    if (ownAmountOfItem == 0) {
                        itemMenuController.itemDescriptionBox.getChildren().clear();
                        itemMenuController.setItemNameLabel("");
                    }
                },
                error -> {
                    showError(error.getMessage());
                    error.printStackTrace();
                }));
        if (!GraphicsEnvironment.isHeadless()) {
            AudioService.getInstance().playEffect(BUY_SELL, ingameController);
        }
    }

    private void showMonsterList(Item item) {
        VBox monsterListVBox = new VBox();
        monsterListVBox.setMinWidth(useItemMonsterListVBoxWidth);
        monsterListVBox.setMinHeight(useItemMonsterListVBoxHeight);
        monsterListVBox.setAlignment(Pos.CENTER);
        if (ingameController != null) {
            MonstersListController monstersListController = ingameController.getMonstersListController();
            monstersListController.setValues(resources, preferences, resourceBundleProvider, this, app);
            monstersListController.init(ingameController, monsterListVBox, rootStackPane, item, onItemUsed);
            monsterListVBox.getChildren().add(monstersListController.render());
            rootStackPane.getChildren().add(monsterListVBox);
            monsterListVBox.requestFocus();
            monstersListController.monsterListViewOther.refresh();
            monstersListController.monsterListViewActive.refresh();
        } else if (encounterController != null) {
            ChangeMonsterListController changeMonsterListController = encounterController.getChangeMonsterListController();
            changeMonsterListController.setValues(resources, preferences, resourceBundleProvider, this, app);
            changeMonsterListController.init(encounterController, monsterListVBox, encounterController.getIngameController(), item, onItemUsed);
            monsterListVBox.getChildren().add(changeMonsterListController.render());
            rootStackPane.getChildren().add(monsterListVBox);
            monsterListVBox.requestFocus();
        }
    }
}
