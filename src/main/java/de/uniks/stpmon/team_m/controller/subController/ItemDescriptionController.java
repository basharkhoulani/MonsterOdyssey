package de.uniks.stpmon.team_m.controller.subController;

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
import javax.inject.Provider;
import java.awt.*;
import java.net.URL;

public class ItemDescriptionController extends Controller {

    ItemTypeDto itemTypeDto;
    Image itemImage;
    Item item;
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

    @Inject
    Provider<IngameController> ingameControllerProvider;
    @Inject
    Provider<MonstersListController> monstersListControllerProvider;

    private Runnable closeItemMenu;
    private StackPane rootStackPane;

    @Inject
    public ItemDescriptionController(){}

    public void init(ItemTypeDto itemTypeDto, Image itemImage, Item item, Runnable closeItemMenu, StackPane rootStackPane) {
        super.init();
        this.itemImage = itemImage;
        this.itemTypeDto = itemTypeDto;
        this.item = item;
        this.closeItemMenu = closeItemMenu;
        this.rootStackPane = rootStackPane;
    }

    @Override
    public Parent render() {
        final Parent parent = super.render();
        itemImageView.setImage(itemImage);
        itemAmountLabel.setText(String.valueOf(item.amount()));
        itemPriceLabel.setText(String.valueOf(itemTypeDto.price()));

        Text description = new Text(itemTypeDto.description());
        itemDescription.getChildren().add(description);

        if(itemTypeDto.use() == null) {
            useButton.setVisible(false);
            useButton.setDisable(true);
        }
        useButton.setOnAction(evt -> {
            closeItemMenu.run();
            showMonsterList(item, itemTypeDto);
        });
        if (!GraphicsEnvironment.isHeadless()) {
            URL resourceImage = Main.class.getResource("images/coin.png");
            assert resourceImage != null;
            Image coinImage = new Image(resourceImage.toString());
            coinImageView.setImage(coinImage);
        }
        return parent;
    }

    private void showMonsterList(Item item, ItemTypeDto itemTypeDto) {
        VBox monsterListVBox = new VBox();
        monsterListVBox.setMinWidth(600);
        monsterListVBox.setMinHeight(410);
        monsterListVBox.setAlignment(Pos.CENTER);
        MonstersListController monstersListController = monstersListControllerProvider.get();
        IngameController ingameController = ingameControllerProvider.get();
        monstersListController.setValues(resources, preferences, resourceBundleProvider, this, app);
        monstersListController.init(ingameController, monsterListVBox, rootStackPane, item);
        monsterListVBox.getChildren().add(monstersListController.render());
        rootStackPane.getChildren().add(monsterListVBox);
        monsterListVBox.requestFocus();
        monstersListController.monsterListViewOther.refresh();
        monstersListController.monsterListViewActive.refresh();
    }
}
