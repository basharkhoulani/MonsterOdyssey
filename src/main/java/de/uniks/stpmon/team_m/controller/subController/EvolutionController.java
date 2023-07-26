package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.dto.MonsterTypeDto;
import de.uniks.stpmon.team_m.utils.ImageProcessor;
import javafx.fxml.FXML;
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
    private Monster monster;
    private MonsterTypeDto currentMonsterTypeDto;
    private MonsterTypeDto oldMonsterTypeDto;
    private Monster oldMonster;
    private Image oldMonsterImage;
    private Image currentMonsterImage;

    @Inject
    public EvolutionController() {
    }

    public void init(VBox container, StackPane root, Monster currentMonster, MonsterTypeDto currentMonsterTypeDto, Monster oldMonster, MonsterTypeDto oldMonsterTypeDto) {
        this.container = container;
        this.root = root;
        this.monster = currentMonster;
        this.currentMonsterTypeDto = currentMonsterTypeDto;
        this.oldMonster = oldMonster;
        this.oldMonsterTypeDto = oldMonsterTypeDto;
        disposables.add(presetsService.getMonsterImage(oldMonster.type()).observeOn(FX_SCHEDULER).subscribe(responseBody ->
                oldMonsterImage = ImageProcessor.resonseBodyToJavaFXImage(responseBody),Throwable::printStackTrace));
        disposables.add(presetsService.getMonsterImage(oldMonster.type()).observeOn(FX_SCHEDULER).subscribe(responseBody ->
                currentMonsterImage = ImageProcessor.resonseBodyToJavaFXImage(responseBody),Throwable::printStackTrace));
    }

    public Parent render() {
        final Parent parent = super.render();
        if (!GraphicsEnvironment.isHeadless()) {
            arrowImageView.setImage(new Image(Objects.requireNonNull(App.class.getResource("images/arrowRight.png")).toString()));
            oldMonsterImageView.setImage(oldMonsterImage);
            newMonsterImageView.setImage(currentMonsterImage);
        }
        // TODO: translation
        evolutionTextFlow.getChildren().add(new Text("Unglaublich! " + oldMonsterTypeDto.name() + " entwickelt sich zu " + currentMonsterTypeDto.name()));
        oldMonsterTypeDto.type().forEach(s -> oldMonsterLabel.setText(s));
        currentMonsterTypeDto.type().forEach(s -> newMonsterLabel.setText(s));
        return parent;
    }

    public void okButtonPressed() {
        root.getChildren().remove(container);
    }
}
