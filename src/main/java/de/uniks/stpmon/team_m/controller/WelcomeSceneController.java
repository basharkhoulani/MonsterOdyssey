package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.controller.subController.CharacterSelectionController;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Optional;

import static de.uniks.stpmon.team_m.Constants.*;

public class WelcomeSceneController extends Controller {
    @FXML
    public Button nextButton;
    @FXML
    public Button previousButton;
    @FXML
    public AnchorPane messagePane;
    @FXML
    public Label firstMessage;
    @FXML
    public Label secondMessage;
    @FXML
    public VBox firstMessageBox;
    public int sceneNumber = 1;
    private final SimpleStringProperty trainerName = new SimpleStringProperty();
    @Inject
    Provider<IngameController> ingameControllerProvider;
    @Inject
    Provider<MainMenuController> mainMenuControllerProvider;
    @Inject
    Provider<CharacterSelectionController> characterSelectionControllerProvider;

    @Inject
    public WelcomeSceneController() {
    }

    @Override
    public String getTitle() {return resources.getString("INGAME.TITLE");}


    @Override
    public Parent render() {
        final Parent parent = super.render();

        nextButton.setOnAction(event -> changeCount(true));
        previousButton.setOnAction(event -> changeCount(false));
        return parent;
    }

    private void changeCount(boolean change) {
        sceneNumber = sceneCounter(sceneNumber, change);
        switchScene();
    }

    public void switchScene() {

        switch (sceneNumber) {
            case 0 -> app.show(mainMenuControllerProvider.get());
            case 1 -> {
                if (messagePane.getChildren().size() > 2) {
                    messagePane.getChildren().remove(2);
                }
                firstMessage.setText(resources.getString("FIRST.MESSAGE"));
                secondMessage.setText(resources.getString("SECOND.MESSAGE"));
            }
            case 2 -> {
                firstMessage.setText(resources.getString("THIRD.MESSAGE"));
                secondMessage.setText(resources.getString("FOURTH.MESSAGE"));
                final VBox messageVBox3 = new VBox();
                messageVBox3.getStyleClass().add(resources.getString("WELCOME.MESSAGE.STYLE"));
                final Label thirdMessage = new Label();
                thirdMessage.setId("thirdMessage");
                thirdMessage.setText(resources.getString("FIFTH.MESSAGE"));
                thirdMessage.setPrefHeight(MESSAGEBOX_HEIGHT);
                thirdMessage.setPrefWidth(MESSAGEBOX_WIDTH);
                messageVBox3.getChildren().add(thirdMessage);
                messageVBox3.setLayoutY(firstMessageBox.getLayoutY() + 100);
                messagePane.getChildren().add(messageVBox3);
            }
            case 3 -> {
                if (messagePane.getChildren().size() > 2) {
                    messagePane.getChildren().remove(2);
                }
                firstMessage.setText(resources.getString("SIXTH.MESSAGE"));
                firstMessage.setWrapText(true);
                firstMessage.setPrefWidth(200);
                secondMessage.setText(resources.getString("SEVENTH.MESSAGE"));
            }
            case 4 -> {
                final Alert alert = new Alert(Alert.AlertType.NONE);
                final DialogPane dialogPane = alert.getDialogPane();
                final ButtonType cancelButton = new ButtonType(resources.getString("CANCEL"), ButtonBar.ButtonData.APPLY);
                final ButtonType okButton = new ButtonType(OK);
                final TextField textFieldName = new TextField();
                textFieldName.setId("nameField");

                dialogPane.getButtonTypes().addAll(cancelButton, okButton);

                final Button cancelButton2 = (Button) alert.getDialogPane().lookupButton(cancelButton);
                final Button okButton2 = (Button) alert.getDialogPane().lookupButton(okButton);
                cancelButton2.getStyleClass().add(resources.getString("WELCOME.SCENE.BUTTON"));
                okButton2.getStyleClass().add(resources.getString("WELCOME.SCENE.BUTTON"));

                final VBox vbox = new VBox(textFieldName);

                alert.setTitle(resources.getString("NAME.ALERT.TITLE"));
                dialogPane.getStyleClass().add(resources.getString("ALERT.DIALOG.NAME"));


                dialogPane.setContent(vbox);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == cancelButton) {
                    alert.close();
                    changeCount(false);
                } else if (result.isPresent() && result.get() == okButton) {
                    textFieldName.textProperty().bindBidirectional(trainerName);
                    changeCount(true);
                }
            }
            case 5 -> {
                firstMessage.setText(resources.getString("EIGHTH.MESSAGE"));
                secondMessage.setText(resources.getString("NINTH.MESSAGE"));
                secondMessage.setWrapText(true);
                secondMessage.setPrefWidth(200);
            }
            case 6 -> app.show(characterSelectionControllerProvider.get());
            case 7 -> {
                firstMessage.setText(TENTH_MESSAGE);
                secondMessage.setText(ELEVENTH_MESSAGE);
                secondMessage.setWrapText(true);
                secondMessage.setPrefWidth(200);
            }
            case 8 -> app.show(ingameControllerProvider.get());
        }

    }

    public int sceneCounter(int sceneNumber, boolean next) {
        if (next) {
            return sceneNumber + 1;
        } else {
            return sceneNumber - 1;
        }
    }

}
