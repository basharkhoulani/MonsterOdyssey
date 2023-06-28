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

import static de.uniks.stpmon.team_m.Constants.TYPESCOLORPALETTE;

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
    public TextFlow starterDescription;
    @FXML
    public ImageView arrowLeft;
    @FXML
    public ImageView arrowRight;
    @FXML
    public VBox typesVBox;
    @Inject
    IngameController ingameController;
    @Inject
    Provider<TrainerStorage> trainerStorageProvider;
    @Inject
    PresetsService presetsService;
    private List<String> starters;
    private MonsterTypeDto monster1;
    private MonsterTypeDto monster2;
    private MonsterTypeDto monster3;
    private Image monster1Image;
    private Image monster2Image;
    private Image monster3Image;
    public Integer index = 1;


    @Inject
    public IngameStarterMonsterController() {

    }

    public void init(IngameController ingameController, App app, List<String> starters) {
        this.ingameController = ingameController;
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
            // get Images
            disposables.add(presetsService.getMonsterImage(Integer.parseInt(starters.get(0))).observeOn(FX_SCHEDULER).subscribe(monsterImage -> {
                monster1Image = ImageProcessor.resonseBodyToJavaFXImage(monsterImage);
                showMonster(1);
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

    public VBox createTypeVBox(String type, Image image) {
        VBox typeVBox = new VBox();
        typeVBox.setMaxSize(32, 32);
        if (TYPESCOLORPALETTE.containsKey(type)) {
            String color = TYPESCOLORPALETTE.get(type);
            typeVBox.setStyle("-fx-background-color: " + color + ";-fx-border-color: black");
        }
        if (image == null) {
            image = new Image(String.valueOf(App.class.getResource("images/ingameHelpSymbol.png")));
        }
        ImageView typeImageView = new ImageView(image);
        typeImageView.setFitHeight(32);
        typeImageView.setFitWidth(32);
        typeVBox.getChildren().add(typeImageView);
        return typeVBox;
    }

    public void showMonster(int index) {
        typesVBox.getChildren().clear();
        starterDescription.getChildren().clear();
        MonsterTypeDto monster;
        Image monsterImage;
        switch (index) {
            default -> {
                monster = monster1;
                monsterImage = monster1Image;
                starterSelectionLabel.setText(resources.getString("FIRST.SELECTION"));
            }
            case 2 -> {
                monster = monster2;
                monsterImage = monster2Image;
                starterSelectionLabel.setText(resources.getString("SECOND.SELECTION"));
            }
            case 3 -> {
                monster = monster3;
                monsterImage = monster3Image;
                starterSelectionLabel.setText(resources.getString("THIRD.SELECTION"));
            }
        }

        // add description
        starterDescription.getChildren().add(new Text(resources.getString("NAME")));
        starterDescription.getChildren().add(new Text(" " + monster.name() + "\n\n"));
        starterDescription.getChildren().add(new Text(monster.description()));

        // add type
        monster.type().forEach(type -> {
            VBox typeVBox = createTypeVBox(type, null);
            typesVBox.getChildren().add(typeVBox);
        });

        // set image
        if (monsterImage == null) {
            return;
        }
        starterImageView.setImage(monsterImage);
    }

    public void rotateLeft() {
        index--;
        if (index < 1) {
            index = 3;
        }
        showMonster(index);
    }

    public void rotateRight() {
        index++;
        if (index > 3) {
            index = 1;
        }
        showMonster(index);
    }
}
