package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.IngameController;
import de.uniks.stpmon.team_m.dto.Item;
import de.uniks.stpmon.team_m.dto.ItemTypeDto;
import de.uniks.stpmon.team_m.service.TrainersService;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;

import javax.inject.Inject;
import javax.inject.Provider;
import java.net.URL;

public class ItemDescriptionController extends Controller {

    ItemTypeDto itemTypeDto;
    Image itemImage;
    @FXML
    public ImageView itemImageView;
    @FXML
    public Label itemAmountLabel;
    @FXML
    public Label itemPriceLabel;
    @FXML
    public Label itemDescription;

    @FXML
    public ImageView closeImageView;

    @Inject
    public ItemDescriptionController(){}

    public void init(ItemTypeDto itemTypeDto, Image itemImage) {
        super.init();
        this.itemImage = itemImage;
        this.itemTypeDto = itemTypeDto;
    }

    @Override
    public Parent render() {
        final Parent parent = super.render();

        URL resourceClose = Main.class.getResource("images/close-x.png");
        System.out.println(resourceClose != null);
        assert resourceClose != null;
        Image closeImage = new Image(resourceClose.toString());
        closeImageView.setImage(closeImage);

        //itemImageView.setImage(itemImage);
        itemPriceLabel.setText(String.valueOf(itemTypeDto.price()));
        itemDescription.setText(itemTypeDto.description());

        return parent;
    }
}
