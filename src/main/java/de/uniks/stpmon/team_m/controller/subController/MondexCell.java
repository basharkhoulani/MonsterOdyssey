package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.dto.MonsterTypeDto;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.awt.*;
import java.util.ResourceBundle;

public class MondexCell extends ListCell<MonsterTypeDto> {
    @FXML
    public Label monsterNumberLabel;
    @FXML
    public ImageView monsterImageView;
    @FXML
    public Label monsterNameLabel;
    @FXML
    public HBox rootHBox;
    private MonstersListController monstersListController;
    private ResourceBundle resources;
    private FXMLLoader loader;


    public MondexCell(MonstersListController monstersListController, ResourceBundle resources) {
        this.monstersListController = monstersListController;
        this.resources = resources;
    }

    @Override
    protected void updateItem(MonsterTypeDto monsterTypeDto, boolean empty) {
        super.updateItem(monsterTypeDto, empty);
        if (monsterTypeDto == null || empty) {
            setText(null);
            setGraphic(null);
            setStyle("-fx-background-color: #CFE9DB;");
        } else {
            loadFXML();
            monsterNumberLabel.setText(String.valueOf(monsterTypeDto.id()));
            if (monstersListController.checkIfPlayerEncounteredMonster(monsterTypeDto)) {
                if (!GraphicsEnvironment.isHeadless()) {
                    monsterImageView.setOpacity(1);
                    monsterImageView.setImage(monstersListController.monsterStorageProvider.get().getMonsterImage(monsterTypeDto.id()));
                }
                monsterNameLabel.setText(monsterTypeDto.name());
            } else {
                if (!GraphicsEnvironment.isHeadless()) {
                    monsterImageView.setOpacity(0.2);
                    monsterImageView.setImage(monstersListController.monsterStorageProvider.get().getMonsterImage(monsterTypeDto.id()));
                }
                monsterNameLabel.setText("???");
            }

            setGraphic(rootHBox);
        }
    }

    private void loadFXML() {
        if (loader == null) {
            loader = new FXMLLoader(Main.class.getResource("views/MondexCell.fxml"));
            loader.setResources(resources);
            loader.setControllerFactory(c -> this);
            try {
                loader.load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
