package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.EncounterController;
import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.dto.MonsterTypeDto;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;

import javax.inject.Inject;
import java.awt.*;
import java.util.Objects;

public class EvolutionController extends Controller {
    @FXML
    public VBox evolutionVBox;
    @FXML
    public TextFlow evolutionTextFlow;
    @FXML
    public ImageView oldMonsterImageView;
    @FXML
    public Label oldMonsterLabel;
    @FXML
    public ImageView newMonsterImageView;
    @FXML
    public Label newMonsterLabel;
    @FXML
    public Button okButton;
    @FXML
    public ImageView arrowImageView;
    private VBox container;
    private StackPane root;
    private EncounterController encounterController;
    private Monster monster;
    private MonsterTypeDto monsterTypeDto;
    private Monster oldMonster;
    private Image oldMonsterImage;
    private Image newMonsterImage;

    @Inject
    public EvolutionController() {
    }

    public void init(VBox container, StackPane root, EncounterController encounterController, Monster currentMonster, MonsterTypeDto currentMonsterTypeDto, Monster oldMonster, Image oldMonsterImage, Image newMonsterImage) {
        this.container = container;
        this.root = root;
        this.encounterController = encounterController;
        this.monster = currentMonster;
        this.monsterTypeDto = currentMonsterTypeDto;
        this.oldMonster = oldMonster;
        this.oldMonsterImage = oldMonsterImage;
        this.newMonsterImage = newMonsterImage;
    }

    public Parent render() {
        final Parent parent = super.render();
        if (!GraphicsEnvironment.isHeadless()) {
            arrowImageView.setImage(new Image(Objects.requireNonNull(App.class.getResource("images/arrowRight.png")).toString()));
        }
        return parent;
    }

    public void okButtonPressed() {
        root.getChildren().remove(container);
        encounterController.continueBattle();
    }
}
