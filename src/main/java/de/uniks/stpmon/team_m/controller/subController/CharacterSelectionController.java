package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.WelcomeSceneController;
import de.uniks.stpmon.team_m.utils.ImageProcessor;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
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
    public ImageView characterImageView;
    @FXML
    public ImageView arrowRight;
    @FXML
    public ImageView arrowLeft;
    @Inject
    Provider<WelcomeSceneController> welcomeSceneControllerProvider;
    @Inject
    Provider<TrainerStorage> trainerStorageProvider;

    @Inject
    public CharacterSelectionController() {
    }

    public int index = 1;
    /**
     * characters holds the premade-character-models
     */
    final private String[] characters = PREMADE_CHARACTERS;

    /**
     * selectedCharacter holds the selected character model
     */
    public String selectedCharacter = characters[index - 1];

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

        showCharacter();
        return parent;
    }

    /**
     * this method sets the selected character and shows it in the imageView
     */
    public void showCharacter() {
        Image[] character = ImageProcessor.cropTrainerImages(new Image(Objects.requireNonNull(App.class.getResource("charactermodels/" + characters[index - 1])).toString()), "down", false);
        characterImageView.setImage(character[0]);
        selectedCharacter = characters[index - 1];
    }

    /**
     * this method navigates to the next character
     */
    public void onArrowLeftClicked() {
        index--;
        if (index < 1) {
            index = index + characters.length;
        }
        showCharacter();
    }

    /**
     * this method navigates to the previous character
     */
    public void onArrowRightClicked() {
        index++;
        if (index > characters.length) {
            index = index - characters.length;
        }
        showCharacter();
    }
}
