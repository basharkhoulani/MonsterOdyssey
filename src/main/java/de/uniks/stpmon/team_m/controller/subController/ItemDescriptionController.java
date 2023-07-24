package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.dto.Item;
import de.uniks.stpmon.team_m.dto.ItemTypeDto;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import javax.inject.Inject;
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

    @Inject
    public ItemDescriptionController(){}

    public void init(ItemTypeDto itemTypeDto, Image itemImage, Item item) {
        super.init();
        this.itemImage = itemImage;
        this.itemTypeDto = itemTypeDto;
        this.item = item;
    }

    @Override
    public Parent render() {
        final Parent parent = super.render();
        itemImageView.setImage(itemImage);
        itemAmountLabel.setText(String.valueOf(item.amount()));
        itemPriceLabel.setText(String.valueOf(itemTypeDto.price()));

        Text description = new Text(itemTypeDto.description());
        itemDescription.getChildren().add(description);

        URL resourceImage = Main.class.getResource("images/coin.png");
        assert resourceImage != null;
        Image coinImage = new Image(resourceImage.toString());
        coinImageView.setImage(coinImage);

        return parent;
    }
}
