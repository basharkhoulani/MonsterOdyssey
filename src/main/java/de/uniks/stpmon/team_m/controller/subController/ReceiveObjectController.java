package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.dto.Item;
import de.uniks.stpmon.team_m.dto.ItemTypeDto;
import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.dto.MonsterTypeDto;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import javax.inject.Provider;

public class ReceiveObjectController extends Controller {
    private final Monster monster;
    private final MonsterTypeDto monsterTypeDto;
    private final Image objectImage;
    private final Item item;
    private final ItemTypeDto itemTypeDto;
    private final Runnable onOkButtonClick;
    private final Provider<TrainerStorage> trainerStorageProvider;
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

    public ReceiveObjectController(Monster monster, MonsterTypeDto monsterTypeDto, Image monsterImage, Runnable onOkButtonClick, Provider<TrainerStorage> trainerStorageProvider) {
        this.monster = monster;
        this.monsterTypeDto = monsterTypeDto;
        this.objectImage = monsterImage;
        this.item = null;
        this.itemTypeDto = null;
        this.onOkButtonClick = onOkButtonClick;
        this.trainerStorageProvider = trainerStorageProvider;
    }

    public ReceiveObjectController(Item item, ItemTypeDto itemTypeDto, Image itemImage, Runnable onOkButtonClick, Provider<TrainerStorage> trainerStorageProvider) {
        this.item = item;
        this.itemTypeDto = itemTypeDto;
        this.objectImage = itemImage;
        this.monster = null;
        this.monsterTypeDto = null;
        this.onOkButtonClick = onOkButtonClick;
        this.trainerStorageProvider = trainerStorageProvider;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        this.receivedObjectCongratulationLabel.setText(this.resources.getString("RECEIVE_OBJECT_CONGRATULATION"));
        if (this.monster != null && this.monsterTypeDto != null) {
            if (trainerStorageProvider.get().getTrainer().encounteredMonsterTypes().contains(monster.type())) {
                receivedObjectLabelVBox.getChildren().remove(receivedObjectNewLabel);
            }
            receiveObjectTextLabel.setText(monsterTypeDto.name() + " " + this.resources.getString("RECEIVE_OBJECT_TEXT"));
            receivedObjectLevelLabel.setText(
                    this.resources.getString("LEVEL") + "\t" + this.monster.level() + "\n"
                            + this.resources.getString("HEALTH") + "\t" + monster.currentAttributes().health() + " / " + monster.attributes().health() + "\n"
                            + this.resources.getString("ATTACK") + "\t" + monster.attributes().attack() + "\n"
                            + this.resources.getString("DEFENSE") + "\t" + monster.attributes().defense() + "\n"
                            + this.resources.getString("SPEED") + "\t" + monster.attributes().speed() + "\n"
            );
        } else {
            if (itemTypeDto != null && item != null) {
                if (trainerStorageProvider.get().getItems().contains(item)) {
                    receivedObjectLabelVBox.getChildren().remove(receivedObjectNewLabel);
                }
                receiveObjectTextLabel.setText(this.item.amount() + " x " + itemTypeDto.name() + " " + this.resources.getString("RECEIVE_OBJECT_TEXT"));
                receivedObjectLevelLabel.setWrapText(true);
                receivedObjectLevelLabel.setText(this.itemTypeDto.description());
            }
        }
        receivedObjectImageView.setImage(this.objectImage);
        this.receiveObjectOkButton.setOnAction(event -> onOkButtonClick.run());
        return parent;
    }
}
