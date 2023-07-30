package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.Constants;
import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.IngameController;
import de.uniks.stpmon.team_m.dto.Item;
import de.uniks.stpmon.team_m.dto.ItemTypeDto;
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
        }
        useButton.setOnAction(evt -> {
            if (itemTypeDto.use().equals(Constants.ITEM_USAGE_EFFECT)) {
                showMonsterList(item);
                closeItemMenu.run();
            }
        });

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

    }

    public void sellItem() {

    }

    private void showMonsterList(Item item) {
        VBox monsterListVBox = new VBox();
        monsterListVBox.setMinWidth(useItemMonsterListVBoxWidth);
        monsterListVBox.setMinHeight(useItemMonsterListVBoxHeight);
        monsterListVBox.setAlignment(Pos.CENTER);
        MonstersListController monstersListController = ingameController.getMonstersListController();
        monstersListController.setValues(resources, preferences, resourceBundleProvider, this, app);
        monstersListController.init(ingameController, monsterListVBox, rootStackPane, item);
        monsterListVBox.getChildren().add(monstersListController.render());
        rootStackPane.getChildren().add(monsterListVBox);
        monsterListVBox.requestFocus();
        monstersListController.monsterListViewOther.refresh();
        monstersListController.monsterListViewActive.refresh();
    }
}