package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class EncounterTrainerMonsterImageBoxController extends Controller {
    @FXML
    public ImageView upperImageView;
    @FXML
    public ImageView lowerImageView;

    @Override
    public Parent render() {
        Parent parent = super.render();
        upperImageView.setImage(new Image(String.valueOf(Main.class.getResource("images/cat.jpeg"))));
        lowerImageView.setImage(new Image(String.valueOf(Main.class.getResource("images/character.png"))));
        return parent;
    }

    public void setUpperImage(Image image) {
        this.upperImageView.setImage(image);
    }

    public void setLowerImageView(Image image) {
        this.lowerImageView.setImage(image);
    }
}
