package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.WelcomeSceneController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;

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


    @Inject
    Provider<WelcomeSceneController> welcomeSceneControllerProvider;

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
            welcomeSceneController.render();
        });
        previousButton.setOnAction(event -> {
            app.show(welcomeSceneController);
            welcomeSceneController.sceneNumber = 5;
            welcomeSceneController.switchScene();
            welcomeSceneController.render();
        });

        character1RadioButton.setSelected(true);

        return parent;
    }

    public void selectCharacter1() {

    }

    public void selectCharacter2() {

    }
}
