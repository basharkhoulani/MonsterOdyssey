package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.dto.Item;
import de.uniks.stpmon.team_m.dto.ItemTypeDto;
import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.dto.MonsterTypeDto;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class ReceiveObjectController extends Controller {
    private final Monster monster;
    private final MonsterTypeDto monsterTypeDto;
    private final Image objectImage;
    private final Item item;
    private final ItemTypeDto itemTypeDto;
    private final Runnable onOkButtonClick;
    @FXML
    public VBox receiveObjectRootVBox;
    @FXML
    public Label receivedObjectCongratulationLabel;
    @FXML
    public Label receiveObjectTextLabel;
    @FXML
    public ImageView receivedObjectImageView;
    @FXML
    public VBox receivedObjectLabelVBox;
    @FXML
    public Label receivedObjectNewLabel;
    @FXML
    public Label receivedObjectLevelLabel;
    @FXML
    public Button receiveObjectOkButton;

    public ReceiveObjectController(Monster monster, MonsterTypeDto monsterTypeDto, Image monsterImage, Runnable onOkButtonClick) {
        this.monster = monster;
        this.monsterTypeDto = monsterTypeDto;
        this.objectImage = monsterImage;
        this.item = null;
        this.itemTypeDto = null;
        this.onOkButtonClick = onOkButtonClick;
    }

    public ReceiveObjectController(Item item, ItemTypeDto itemTypeDto, Image itemImage, Runnable onOkButtonClick) {
        this.item = item;
        this.itemTypeDto = itemTypeDto;
        this.objectImage = itemImage;
        this.monster = null;
        this.monsterTypeDto = null;
        this.onOkButtonClick = onOkButtonClick;
    }

    @Override
    public Parent render() {
        Parent parent =  super.render();
        this.receivedObjectCongratulationLabel.setText(this.resources.getString("RECEIVE_OBJECT_CONGRATULATION"));
        if (this.monster != null && this.monsterTypeDto != null) {
            // TODO: check if monster is NOT new
            if (true) {
                receivedObjectLabelVBox.getChildren().remove(receivedObjectNewLabel);
            }
            receiveObjectTextLabel.setText(monsterTypeDto.name() + " " + this.resources.getString("RECEIVE_OBJECT_TEXT"));
            receivedObjectLevelLabel.setText(this.resources.getString("LEVEL") + " " + this.monster.level());
        }
        else {
            // TODO: check if item is NOT new
            if (itemTypeDto != null && item != null) {
                if (true) {
                    receivedObjectLabelVBox.getChildren().remove(receivedObjectNewLabel);
                }
                receiveObjectTextLabel.setText(itemTypeDto.name() + " " + this.resources.getString("RECEIVE_OBJECT_TEXT"));
                receivedObjectLevelLabel.setText(" " + this.item.amount());
            }
        }
        receivedObjectImageView.setImage(this.objectImage);
        this.receiveObjectOkButton.setOnAction(event -> onOkButtonClick.run());
        return parent;
    }
}
