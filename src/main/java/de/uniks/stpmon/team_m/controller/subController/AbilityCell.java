package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.dto.AbilityDto;
import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.service.PresetsService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

import static de.uniks.stpmon.team_m.Constants.ABILITYPALETTE;
import static de.uniks.stpmon.team_m.Constants.TYPESCOLORPALETTE;


public class AbilityCell extends ListCell<AbilityDto> {

    private final ResourceBundle resources;
    private FXMLLoader loader;

    @FXML
    public VBox typeIcon;
    @FXML
    public ImageView typeImageView;
    @FXML
    public Label abilityName;
    @FXML
    public Text abilityDescription;
    @FXML
    public HBox rootAbilityBox;
    @FXML
    public Label damageLabel;
    @FXML
    public Label accuracyLabel;
    @FXML
    public Label usesLabel;
    @FXML
    public ImageView damageImageView;
    @FXML
    public ImageView accuracyImageView;
    final Monster monster;
    public AbilityCell(Monster monster, ResourceBundle resources) {
        this.monster = monster;
        this.resources = resources;

    }

    @Override
    protected void updateItem(AbilityDto abilityDto, boolean empty) {
        super.updateItem(abilityDto, empty);
        if (abilityDto == null || empty) {
            setText(null);
            setGraphic(null);
            setStyle("-fx-background-color: #D6E8FE;");
        } else {
            loadFXML();
            String typeColor = TYPESCOLORPALETTE.get(abilityDto.type());
            String style = "-fx-background-color: " + typeColor + ";";
            typeIcon.setStyle(style);

            if (!GraphicsEnvironment.isHeadless()) {
                String typeImagePath = ABILITYPALETTE.get(abilityDto.type());
                URL resourceType = Main.class.getResource("images/" + typeImagePath);
                assert resourceType != null;
                Image typeImage = new Image(resourceType.toString());
                typeImageView.setImage(typeImage);
                typeImageView.setFitHeight(45);
                typeImageView.setFitWidth(45);

                URL resourceDamage = Main.class.getResource("images/ability-electic.png");
                assert resourceDamage != null;
                Image damageImage = new Image(resourceDamage.toString());
                damageImageView.setImage(damageImage);

                URL resourceAccuracy = Main.class.getResource("images/accuracy.png");
                assert resourceAccuracy != null;
                Image accuracyImage = new Image(resourceAccuracy.toString());
                accuracyImageView.setImage(accuracyImage);
            }
            abilityName.setText(abilityDto.name());
            abilityDescription.setText(abilityDto.description());

            damageLabel.setText(abilityDto.power() + " DMG");
            accuracyLabel.setText((abilityDto.accuracy() * 100) + " %");
            Integer uses = monster.abilities().get(String.valueOf(abilityDto.id()));
            usesLabel.setText(resources.getString("USES") + ": " + uses + "/"+ abilityDto.maxUses());
            setGraphic(rootAbilityBox);
            setText(null);
        }
    }

    private void loadFXML() {
        if (loader == null) {
            loader = new FXMLLoader(Main.class.getResource("views/AbilityCell.fxml"));
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
