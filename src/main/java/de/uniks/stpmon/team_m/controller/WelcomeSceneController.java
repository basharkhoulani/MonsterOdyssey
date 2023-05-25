package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.service.TrainersService;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;

import java.util.Optional;

import static de.uniks.stpmon.team_m.Constants.*;

public class WelcomeSceneController extends Controller{
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
    public int sceneNumber;
    private final SimpleStringProperty trainerName = new SimpleStringProperty();
    @Inject
    Provider<IngameController> ingameControllerProvider;
    @Inject
    Provider<MainMenuController> mainMenuControllerProvider;
    @Inject
    TrainersService trainersService;
    @Inject
    public WelcomeSceneController(){}
    @Override
    public String getTitle() {return INGAME_TITLE;}

    @Override
    public Parent render() {
        final Parent parent = super.render();

        sceneNumber = 1;
        nextButton.setOnAction(event -> {
            sceneNumber++;
            switchScene(sceneNumber, messagePane);
        });
        previousButton.setOnAction(event -> {
            sceneNumber--;
            switchScene(sceneNumber, messagePane);
        });
        return parent;
    }

    private void switchScene(int sceneNumber, AnchorPane messagePane) {
        switch (sceneNumber) {
            case 0 -> app.show(mainMenuControllerProvider.get());
            case 1 -> {
                if (messagePane.getChildren().size() > 2) {
                    messagePane.getChildren().remove(2);
                }
                firstMessage.setText(FIRST_MESSAGE);
                secondMessage.setText(SECOND_MESSAGE);
            }
            case 2 -> {
                firstMessage.setText(THIRD_MESSAGE);
                secondMessage.setText(FOURTH_MESSAGE);
                final VBox messageVBox3 = new VBox();
                messageVBox3.getStylesheets().add(STYLE_RESOURCE);
                messageVBox3.getStyleClass().add(WELCOME_MESSAGE_STYLE);
                final Label thirdMessage = new Label();
                thirdMessage.setText(FIFTH_MESSAGE);
                thirdMessage.setPrefHeight(45);
                thirdMessage.setPrefWidth(170);
                messageVBox3.getChildren().add(thirdMessage);
                messageVBox3.setLayoutY(270);
                messagePane.getChildren().add(messageVBox3);
            }
            case 3 -> {
                if (messagePane.getChildren().size() > 2) {
                    messagePane.getChildren().remove(2);
                }
                firstMessage.setText(SIXTH_MESSAGE);
                firstMessage.setWrapText(true);
                firstMessage.setPrefWidth(170);
                secondMessage.setText(SEVENTH_MESSAGE);
            }
            case 4 -> {
                final Alert alert = new Alert(Alert.AlertType.NONE);
                final DialogPane dialogPane = alert.getDialogPane();
                final ButtonType cancelButton = new ButtonType(CANCEL, ButtonBar.ButtonData.APPLY);
                final ButtonType okButton = new ButtonType(OK);
                final TextField textFieldName = new TextField();

                dialogPane.getButtonTypes().addAll(cancelButton, okButton);

                final Button cancelButton2 = (Button) alert.getDialogPane().lookupButton(cancelButton);
                final Button okButton2 = (Button) alert.getDialogPane().lookupButton(okButton);
                cancelButton2.getStyleClass().add(WELCOME_SCENE_BUTTON);
                okButton2.getStyleClass().add(WELCOME_SCENE_BUTTON);

                final VBox vbox = new VBox(textFieldName);

                alert.setTitle(NAME_ALERT_TITLE);
                dialogPane.getStylesheets().add(STYLE_RESOURCE);
                dialogPane.getStyleClass().add(ALERT_DIALOG_NAME);

                dialogPane.setContent(vbox);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == cancelButton) {
                    alert.close();
                } else if (result.isPresent() && result.get() == okButton) {
                    BooleanBinding isInvalidUsername = trainerName.isEmpty();
                    if (!isInvalidUsername.get()) {
                        alert.close();
                        textFieldName.textProperty().bindBidirectional(trainerName);
                        app.getStage().getScene().setOnKeyPressed(null);
                        // hier muss dann der trainer erstellt werde jedoch fehlt der avatar
                        app.show(ingameControllerProvider.get());
                    }
                }
            }
        }

    }

}
