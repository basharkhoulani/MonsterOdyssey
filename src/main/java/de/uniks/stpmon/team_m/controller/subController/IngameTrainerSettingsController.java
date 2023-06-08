package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.IngameController;
import de.uniks.stpmon.team_m.controller.MainMenuController;
import de.uniks.stpmon.team_m.dto.Trainer;
import de.uniks.stpmon.team_m.service.PresetsService;
import de.uniks.stpmon.team_m.service.RegionsService;
import de.uniks.stpmon.team_m.service.TrainersService;
import de.uniks.stpmon.team_m.utils.ImageProcessor;
import de.uniks.stpmon.team_m.utils.UserStorage;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class IngameTrainerSettingsController extends Controller {
    @FXML
    public ImageView trainerAvatarImageView;

    @FXML
    public Button cancelButton;

    @FXML
    public Button deleteTrainerButton;

    @Inject
    Provider<MainMenuController> mainMenuControllerProvider;
    @Inject
    Provider<IngameController> ingameControllerProvider;
    @Inject
    public PresetsService presetsService;

    @Inject
    public RegionsService regionsService;

    @Inject
    public TrainersService trainersService;

    @Inject
    public UserStorage usersStorage;
    private String regionId;
    protected final CompositeDisposable disposables = new CompositeDisposable();
    private Trainer trainer;
    private Image trainerImage;


    @Inject
    public IngameTrainerSettingsController() {
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public Parent render() {
        return super.render();
    }

    public void onCancelButtonClick() {
        ((Stage) cancelButton.getScene().getWindow()).close();
    }

    public void onDeleteTrainerButtonClick() {
        final Alert alert = new Alert(Alert.AlertType.WARNING);
        final DialogPane dialogPane = alert.getDialogPane();
        final ButtonType cancelButton = new ButtonType(resources.getString("Cancel"));
        final ButtonType okButton = alert.getButtonTypes().stream()
                        .filter(buttonType -> buttonType.getButtonData().isDefaultButton()).findFirst().orElse(null);

        dialogPane.getButtonTypes().addAll(cancelButton);

        final Button cancelButton2 = (Button) alert.getDialogPane().lookupButton(cancelButton);
        final Button okButton2 = (Button) alert.getDialogPane().lookupButton(okButton);
        okButton2.setOnAction(event -> onCancelButtonClick());

        alert.setTitle(resources.getString("Delete.your.trainer"));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == cancelButton) {
            alert.close();
        } else if (result.isPresent() && result.get() == okButton) {
            app.show(mainMenuControllerProvider.get());
            alert.close();
        }
    }

    public void setRegion(String regionId) {
        this.regionId = regionId;
        loadTrainer();
    }

    public void loadTrainer() {
        if (this.regionId != null) {
            disposables.add(trainersService.getTrainers(this.regionId, null, usersStorage.get_id()).observeOn(FX_SCHEDULER).subscribe(trainers -> {
                if (!trainers.isEmpty()) {
                    trainer = trainers.get(0);
                    loadAndSetTrainerImage();
                }
            }, error -> this.showError(error.getMessage())));
        }
    }


    private void loadAndSetTrainerImage() {
        disposables.add(presetsService.getCharacter(trainer.image()).observeOn(FX_SCHEDULER).subscribe(responseBody ->  {
            try {
                trainerImage = ImageProcessor.responseBodyToJavaFXImage(responseBody);
                trainerAvatarImageView.setImage(trainerImage);
            }
            catch (Exception e) {
                this.showError(e.getMessage());
            }
        }, error -> this.showError(error.getMessage())));
    }
}
