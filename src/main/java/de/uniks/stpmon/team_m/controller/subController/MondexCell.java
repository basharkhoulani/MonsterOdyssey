package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.dto.MonsterTypeDto;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;

public class MondexCell extends ListCell<MonsterTypeDto> {
    @FXML
    public Label monsterNumberLabel;
    @FXML
    public ImageView monsterImageView;
    @FXML
    public Label monsterNameLabel;

    public MondexCell() {

    }

    @Override
    protected void updateItem(MonsterTypeDto monsterTypeDto, boolean empty) {

    }
}
