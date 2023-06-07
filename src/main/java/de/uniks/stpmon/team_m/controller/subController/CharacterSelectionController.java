package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.WelcomeSceneController;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Objects;
import static de.uniks.stpmon.team_m.Constants.*;

public class CharacterSelectionController extends Controller {
    @FXML
    public Button previousButton;
    @FXML
    public Button nextButton;
    @FXML
    public VBox chooseYourCharacterField;
    @FXML
    public ImageView character1ImageView;
    @FXML
    public RadioButton character1RadioButton;
    @FXML
    public ImageView character2ImageView;
    @FXML
    public RadioButton character2RadioButton;
    public ToggleGroup selectCharacter;
    public String selectedCharacter = PREMADE_CHARACTERS[0];
    @Inject
    Provider<WelcomeSceneController> welcomeSceneControllerProvider;
    @Inject
    Provider<TrainerStorage> trainerStorageProvider;
    @Inject
    public CharacterSelectionController() {
    }

    @Override
    public Parent render() {
        final Parent parent = super.render();

        WelcomeSceneController welcomeSceneController = welcomeSceneControllerProvider.get();
        nextButton.setOnAction(event -> {
            app.show(welcomeSceneController);
            welcomeSceneController.sceneNumber = 7;
            welcomeSceneController.switchScene();
            trainerStorageProvider.get().setTrainerSprite(selectedCharacter);
        });
        previousButton.setOnAction(event -> {
            app.show(welcomeSceneController);
            welcomeSceneController.sceneNumber = 5;
            welcomeSceneController.switchScene();
        });

        character1RadioButton.setSelected(true);

        character1ImageView.setImage(new Image(Objects.requireNonNull(App.class.getResource(PREMADE_CHARACTERS[0])).toString()));
        character2ImageView.setImage(new Image(Objects.requireNonNull(App.class.getResource(PREMADE_CHARACTERS[1])).toString()));

        return parent;
    }

    /**
     * This method selects the first character.
     */
    public void selectCharacter1() {
        selectedCharacter = PREMADE_CHARACTERS[0];
    }

    /**
     * This method selects the second character.
     */
    public void selectCharacter2() {
        selectedCharacter = PREMADE_CHARACTERS[0];
    }
}
