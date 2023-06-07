package de.uniks.stpmon.team_m.controller;


import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.controller.subController.IngameTrainerSettingsController;
import de.uniks.stpmon.team_m.utils.ImageProcessor;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Dialog;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;

import javax.inject.Inject;
import javax.inject.Provider;
import java.awt.*;
import java.util.Objects;
import java.util.Optional;

import static de.uniks.stpmon.team_m.Constants.*;


public class IngameController extends Controller {

    @FXML
    public ImageView playerSpriteImageView;
    @FXML
    public Button helpSymbol;
    @FXML
    public Button monstersButton;
    @FXML
    public Button settingsButton;

    private IngameTrainerSettingsController trainerSettingsController;

    @Inject
    Provider<MainMenuController> mainMenuControllerProvider;
    public static final KeyCode PAUSE_MENU_KEY = KeyCode.P;

    @Inject
    Provider<IngameTrainerSettingsController> ingameTrainerSettingsControllerProvider;

    @Inject
    Provider<TrainerStorage> trainerStorageProvider;

    /**
     * IngameController is used to show the In-Game screen and to pause the game.
     */

    @Inject
    public IngameController() {
    }

    @Override
    public void init() {
        super.init();
        trainerSettingsController = ingameTrainerSettingsControllerProvider.get();
        trainerSettingsController.init();
    }

    /**
     * This method sets the title of the {@link IngameController}.
     *
     * @return Title of the {@link IngameController}.
     */

    @Override
    public String getTitle() {
        return resources.getString("INGAME.TITLE");
    }

    /**
     * This method plays a timeline animation for the trainer character of the given
     * @param movementImages and displays the
     * @param lastImages after the animation has finished.
     */
    private void playSpriteAnimation(Image[] movementImages, Image lastImages) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(100), e -> {
                    // Calculate the index of the next image
                    int currentIndex = (int) (System.currentTimeMillis() / 100 % 6);
                    playerSpriteImageView.setImage(movementImages[currentIndex]);
                })
        );
        timeline.setOnFinished(c -> playerSpriteImageView.setImage(lastImages));
        timeline.setCycleCount(6);
        timeline.play();
    }

    /**
     * This method is used to render the In-Game screen.
     *
     * @return Parent of the In-Game screen.
     */
    @Override
    public Parent render() {
        final Parent parent = super.render();
        Image[] trainerStandingDown = ImageProcessor.cropTrainerImages(trainerStorageProvider.get().getTrainerSpriteChunk(), "down", false);
        Image[] trainerStandingUp = ImageProcessor.cropTrainerImages(trainerStorageProvider.get().getTrainerSpriteChunk(), "up", false);
        Image[] trainerStandingLeft = ImageProcessor.cropTrainerImages(trainerStorageProvider.get().getTrainerSpriteChunk(), "left", false);
        Image[] trainerStandingRight = ImageProcessor.cropTrainerImages(trainerStorageProvider.get().getTrainerSpriteChunk(), "right", false);

        Image[] trainerWalkingUp = ImageProcessor.cropTrainerImages(trainerStorageProvider.get().getTrainerSpriteChunk(), "up", true);
        Image[] trainerWalkingDown = ImageProcessor.cropTrainerImages(trainerStorageProvider.get().getTrainerSpriteChunk(), "down", true);
        Image[] trainerWalkingLeft = ImageProcessor.cropTrainerImages(trainerStorageProvider.get().getTrainerSpriteChunk(), "left", true);
        Image[] trainerWalkingRight = ImageProcessor.cropTrainerImages(trainerStorageProvider.get().getTrainerSpriteChunk(), "right", true);
        playerSpriteImageView.setImage(trainerStandingDown[0]);
        app.getStage().getScene().setOnKeyPressed(event -> {
            if ((event.getCode() == PAUSE_MENU_KEY)) {
                pauseGame();
            }
            if ((event.getCode() == KeyCode.S)) {
                playSpriteAnimation(trainerWalkingDown, trainerStandingDown[0]);
            }
            if ((event.getCode() == KeyCode.W)) {
                playSpriteAnimation(trainerWalkingUp, trainerStandingUp[0]);
            }
            if ((event.getCode() == KeyCode.A)) {
                playSpriteAnimation(trainerWalkingLeft, trainerStandingLeft[0]);
            }
            if ((event.getCode() == KeyCode.D)) {
                playSpriteAnimation(trainerWalkingRight, trainerStandingRight[0]);
            }
        });
        return parent;
    }

    /**
     * This method is used to show the help screen.
     */

    public void showHelp() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(null);
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.initOwner(app.getStage());
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setContentText(resources.getString("HELP.LABEL"));
        final DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStyleClass().add("comicSans");
        dialogPane.setStyle(FX_STYLE_BORDER_COLOR_BLACK);
        alert.showAndWait();
    }

    /**
     * This method is used to pause the game. It shows a dialog with two buttons.
     * The first button is used to resume the game and the second button is used to save the game and leave.
     * If the user presses the resume button, the dialog will be closed.
     * If the user presses the save and leave button, the game will be saved and
     * the user will be redirected to the main menu.
     */

    public void pauseGame() {
        final Alert alert = new Alert(Alert.AlertType.NONE);
        final DialogPane dialogPane = alert.getDialogPane();
        final ButtonType resume = new ButtonType(resources.getString("RESUME.BUTTON.LABEL"));
        final ButtonType saveAndExit = new ButtonType(resources.getString("SAVE.GAME.AND.LEAVE.BUTTON.LABEL"));
        dialogPane.getButtonTypes().addAll(resume, saveAndExit);
        if (!GraphicsEnvironment.isHeadless()) {
            dialogPane.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("styles.css")).toString());
            dialogPane.getStyleClass().add("comicSans");
        }
        final Button resumeButton = (Button) dialogPane.lookupButton(resume);
        resumeButton.setOnKeyPressed(event -> {
            if (!(event.getCode() == PAUSE_MENU_KEY)) {
                return;
            }
            alert.setResult(resume);
        });

        alert.setTitle(resources.getString("PAUSE.MENU.TITLE"));
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setContentText(resources.getString("PAUSE.MENU.LABEL"));
        alert.initStyle(StageStyle.UNDECORATED);
        dialogPane.setStyle(FX_STYLE_BORDER_COLOR_BLACK);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == resume) {
            alert.close();
        } else if (result.isPresent() && result.get() == saveAndExit) {
            alert.close();
            app.getStage().getScene().setOnKeyPressed(null);
            app.show(mainMenuControllerProvider.get());
        }
    }
    public void showTrainerSettings() {
        Dialog<?> trainerSettingsDialog = new Dialog<>();
        trainerSettingsDialog.setTitle(resources.getString("Trainer.Profil"));
        trainerSettingsDialog.getDialogPane().setContent(trainerSettingsController.render());
        trainerSettingsDialog.getDialogPane().setExpandableContent(null);
        trainerSettingsDialog.getDialogPane().getStylesheets().add(Objects.requireNonNull(Main.class.getResource("styles.css")).toString());
        trainerSettingsDialog.getDialogPane().getStyleClass().add("trainerSettingsDialog");
        Window popUp = trainerSettingsDialog.getDialogPane().getScene().getWindow();
        popUp.setOnCloseRequest(evt ->
                ((Stage) trainerSettingsDialog.getDialogPane().getScene().getWindow()).close()
        );
        trainerSettingsDialog.showAndWait();
    }
}
