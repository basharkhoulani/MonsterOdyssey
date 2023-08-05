package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.Constants;
import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.EncounterController;
import de.uniks.stpmon.team_m.controller.IngameController;
import de.uniks.stpmon.team_m.dto.Item;
import de.uniks.stpmon.team_m.dto.ItemTypeDto;
import de.uniks.stpmon.team_m.service.AudioService;
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

import javax.inject.Inject;
import java.awt.*;
import java.net.URL;

import static de.uniks.stpmon.team_m.Constants.SoundEffect.BUY_SELL;
import static de.uniks.stpmon.team_m.Constants.useItemMonsterListVBoxHeight;
import static de.uniks.stpmon.team_m.Constants.useItemMonsterListVBoxWidth;

public class ItemDescriptionController extends Controller {

    ItemTypeDto itemTypeDto;
    Image itemImage;
    Item item;
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
    private Constants.inventoryType inventoryType;
    private int ownAmountOfItem;

    private Runnable closeItemMenu;
    private StackPane rootStackPane;
    private IngameController ingameController;
    private EncounterController encounterController;

    @Inject
    public ItemDescriptionController() {
    }

    public void init(ItemTypeDto itemTypeDto,
                     Image itemImage,
                     Item item,
                     Constants.inventoryType inventoryType,
                     int ownAmountOfITem,
                     Runnable closeItemMenu,
                     StackPane rootStackPane,
                     IngameController ingameController) {
        super.init();
        this.itemImage = itemImage;
        this.itemTypeDto = itemTypeDto;
        this.item = item;
        this.inventoryType = inventoryType;
        this.ownAmountOfItem = ownAmountOfITem;
        this.closeItemMenu = closeItemMenu;
        this.rootStackPane = rootStackPane;
        this.ingameController = ingameController;
    }

    public void initFromEncounter(ItemTypeDto itemTypeDto,
                                  Image itemImage,
                                  Item item,
                                  Constants.inventoryType inventoryType,
                                  int ownAmountOfITem,
                                  Runnable closeItemMenu,
                                  StackPane rootStackPane,
                                  EncounterController encounterController) {
        super.init();
        this.itemImage = itemImage;
        this.itemTypeDto = itemTypeDto;
        this.item = item;
        this.inventoryType = inventoryType;
        this.ownAmountOfItem = ownAmountOfITem;
        this.closeItemMenu = closeItemMenu;
        this.rootStackPane = rootStackPane;
        this.encounterController = encounterController;
    }

    @Override
    public Parent render() {
        final Parent parent = super.render();
        itemImageView.setImage(itemImage);
        itemAmountLabel.setText(String.valueOf(item.amount()));
        itemPriceLabel.setText(String.valueOf(itemTypeDto.price()));

        Text description = new Text(itemTypeDto.description());
        itemDescription.getChildren().add(description);

        if (itemTypeDto.use() == null) {
            useButton.setVisible(false);
            useButton.setDisable(true);
        } else if (inventoryType == Constants.inventoryType.showItems) {
            String use = itemTypeDto.use();
            Constants.itemType itemType = Constants.itemType.valueOf(use);

            if (encounterController == null && itemType == Constants.itemType.ball) {
                useButton.setVisible(false);
                useButton.setDisable(true);
            }

            useButton.setOnAction(evt -> {
                if (itemType == Constants.itemType.effect) {
                    showMonsterList(item);
                    closeItemMenu.run();
                } else if (itemType == Constants.itemType.ball) {
                    if(encounterController != null) {
                        encounterController.useItem(item, null);
                        closeItemMenu.run();
                    }
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
        if (!GraphicsEnvironment.isHeadless()) {
            AudioService.getInstance().playEffect(BUY_SELL);
        }
    }

    public void sellItem() {
        if (!GraphicsEnvironment.isHeadless()) {
            AudioService.getInstance().playEffect(BUY_SELL);
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
            monstersListController.init(ingameController, monsterListVBox, rootStackPane, item);
            monsterListVBox.getChildren().add(monstersListController.render());
            rootStackPane.getChildren().add(monsterListVBox);
            monsterListVBox.requestFocus();
            monstersListController.monsterListViewOther.refresh();
            monstersListController.monsterListViewActive.refresh();
        } else if (encounterController != null) {
            ChangeMonsterListController changeMonsterListController = encounterController.getChangeMonsterListController();
            changeMonsterListController.setValues(resources, preferences, resourceBundleProvider, this, app);
            changeMonsterListController.init(encounterController, monsterListVBox, encounterController.getIngameController(), item);
            monsterListVBox.getChildren().add(changeMonsterListController.render());
            rootStackPane.getChildren().add(monsterListVBox);
            monsterListVBox.requestFocus();
        }
    }
}
