package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.controller.subController.CharacterSelectionController;
import de.uniks.stpmon.team_m.dto.Region;
import de.uniks.stpmon.team_m.service.PresetsService;
import de.uniks.stpmon.team_m.service.TrainersService;
import de.uniks.stpmon.team_m.utils.ImageProcessor;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Optional;
import java.util.prefs.Preferences;

import static de.uniks.stpmon.team_m.Constants.MESSAGEBOX_HEIGHT;
import static de.uniks.stpmon.team_m.Constants.MESSAGEBOX_WIDTH;

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
    Provider<Preferences> preferencesProvider;
    @Inject
    Provider<TrainersService> trainersServiceProvider;
    @Inject
    TrainerStorage trainerStorage;

    @Inject
    Provider<PresetsService> presetsServiceProvider;

    @Inject
    public WelcomeSceneController() {
    }

    @Override
    public String getTitle() {
        return resources.getString("INGAME.TITLE");
    }


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
                messageVBox3.getStyleClass().add("welcomeMessage");
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
                final ButtonType okButton = new ButtonType(resources.getString("OK"));
                final TextField textFieldName = new TextField();
                textFieldName.setId("nameField");
                textFieldName.setPromptText("Name?");
                textFieldName.textProperty().bindBidirectional(trainerName);
                dialogPane.getButtonTypes().addAll(cancelButton, okButton);

                final Button cancelButton2 = (Button) alert.getDialogPane().lookupButton(cancelButton);
                final Button okButton2 = (Button) alert.getDialogPane().lookupButton(okButton);
                cancelButton2.getStyleClass().add("welcomeSceneButton");
                okButton2.getStyleClass().add("welcomeSceneButton");

                final VBox vbox = new VBox(textFieldName);

                alert.setTitle(resources.getString("NAME.ALERT.TITLE"));
                alert.initStyle(StageStyle.UNDECORATED);
                dialogPane.getStyleClass().add(resources.getString("ALERT.DIALOG.NAME"));
                dialogPane.setContent(vbox);

                textFieldName.addEventHandler(KeyEvent.KEY_PRESSED, event -> enterButtonTrainerInput(event, alert, textFieldName, okButton));

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == cancelButton) {
                    alert.close();
                    changeCount(false);
                } else if (result.isPresent() && result.get() == okButton) {
                    trainerStorage.setTrainerName(trainerName.get());
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
                firstMessage.setText(resources.getString("TENTH.MESSAGE"));
                secondMessage.setText(resources.getString("ELEVENTH.MESSAGE"));
                secondMessage.setWrapText(true);
                secondMessage.setPrefWidth(200);
            }
            case 8 -> {
                Region region = trainerStorage.getRegion();
                disposables.add(trainersServiceProvider.get().createTrainer(
                        region._id(),
                        trainerStorage.getTrainerName(),
                        trainerStorage.getTrainerSprite()
                ).observeOn(FX_SCHEDULER).subscribe(result -> {
                            trainerStorage.setTrainer(result);
                            disposables.add(presetsServiceProvider.get().getCharacter(result.image()).observeOn(FX_SCHEDULER).subscribe(
                                    response -> {
                                        trainerStorage.setTrainerSpriteChunk(ImageProcessor.resonseBodyToJavaFXImage(response));
                                        app.show(ingameControllerProvider.get());
                                    },
                                    error -> {
                                        showError(error.getMessage());
                                        error.printStackTrace();
                                    }
                            ));
                        }, error -> showError(error.getMessage())
                ));
            }
        }

    }

    public void enterButtonTrainerInput(KeyEvent event, Alert alert, TextField textFieldName, ButtonType okButton) {
        if (event.getCode() == KeyCode.ENTER) {
            event.consume();
            alert.setResult(okButton);
            textFieldName.removeEventHandler(KeyEvent.KEY_PRESSED, event1 -> enterButtonTrainerInput(event, alert, textFieldName, okButton));
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
