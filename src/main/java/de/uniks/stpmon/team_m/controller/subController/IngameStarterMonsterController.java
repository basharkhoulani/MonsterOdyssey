package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.IngameController;
import de.uniks.stpmon.team_m.dto.MonsterTypeDto;
import de.uniks.stpmon.team_m.service.PresetsService;
import de.uniks.stpmon.team_m.utils.ImageProcessor;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;

public class IngameStarterMonsterController extends Controller {
    @FXML
    public AnchorPane starterSelectionAnchorPane;
    @FXML
    public Label starterSelectionLabel;
    @FXML
    public VBox starterSelectionVBox;
    @FXML
    public HBox starterSelectionHBox;
    @FXML
    public ImageView starterImageView;
    @FXML
    public ImageView typeImageView;
    @FXML
    public TextFlow starterDescription;
    @FXML
    public ImageView arrowLeft;
    @FXML
    public ImageView arrowRight;
    @FXML
    public VBox typeVBox;
    @Inject
    IngameController ingameController;
    @Inject
    Provider<TrainerStorage> trainerStorageProvider;
    @Inject
    PresetsService presetsService;
    private VBox popUpVBox;
    private List<String> starters;
    private MonsterTypeDto monster1;
    private MonsterTypeDto monster2;
    private MonsterTypeDto monster3;
    private Image monster1Image;
    private Image monster2Image;
    private Image monster3Image;


    @Inject
    public IngameStarterMonsterController() {

    }

    public void init(IngameController ingameController, VBox starterSelectionVBox, App app, List<String> starters) {
        this.ingameController = ingameController;
        this.popUpVBox = starterSelectionVBox;
        this.app = app;
        this.starters = starters;
    }

    public Parent render() {
        final Parent parent = super.render();
        // get monsters
        disposables.add(presetsService.getMonsters().observeOn(FX_SCHEDULER).subscribe(monsterType -> {
            monster1 = monsterType.get(Integer.parseInt(starters.get(0)) - 1);
            monster2 = monsterType.get(Integer.parseInt(starters.get(1)) - 1);
            monster3 = monsterType.get(Integer.parseInt(starters.get(2)) - 1);
            // add description
            starterDescription.getChildren().add(new Text(resources.getString("NAME")));
            starterDescription.getChildren().add(new Text(" " + monster1.name() + "\n"));
            starterDescription.getChildren().add(new Text(monster1.description()));
            // add type
            typeVBox.setStyle("-fx-background-color: red");
            // get Images
            disposables.add(presetsService.getMonsterImage(Integer.parseInt(starters.get(0))).observeOn(FX_SCHEDULER).subscribe(monsterImage -> {
                monster1Image = ImageProcessor.resonseBodyToJavaFXImage(monsterImage);
                starterImageView.setImage(monster1Image);
            }, error -> {
                showError(error.getMessage());
                error.printStackTrace();
            }));
            disposables.add(presetsService.getMonsterImage(Integer.parseInt(starters.get(1))).observeOn(FX_SCHEDULER).subscribe(monsterImage ->
                    monster2Image = ImageProcessor.resonseBodyToJavaFXImage(monsterImage), error -> {
                showError(error.getMessage());
                error.printStackTrace();
            }));
            disposables.add(presetsService.getMonsterImage(Integer.parseInt(starters.get(2))).observeOn(FX_SCHEDULER).subscribe(monsterImage ->
                    monster3Image = ImageProcessor.resonseBodyToJavaFXImage(monsterImage), error -> {
                showError(error.getMessage());
                error.printStackTrace();
            }));

        }, error -> {
            showError(error.getMessage());
            error.printStackTrace();
        }));
        return parent;
    }

    public void rotateLeft() {

    }

    public void rotateRight() {

    }
}
