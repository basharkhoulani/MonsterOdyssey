package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.dto.Trainer;
import de.uniks.stpmon.team_m.service.PresetsService;
import de.uniks.stpmon.team_m.service.RegionsService;
import de.uniks.stpmon.team_m.service.TrainersService;
import de.uniks.stpmon.team_m.utils.ImageProcessor;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import de.uniks.stpmon.team_m.utils.UserStorage;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.inject.Inject;
import javax.inject.Provider;


public class IngameTrainerSettingsController extends Controller {
    @FXML
    public ImageView trainerAvatarImageView;

    @FXML
    public Button cancelButton;

    @FXML
    public Button deleteTrainerButton;

    @Inject
    public PresetsService presetsService;

    @Inject
    public RegionsService regionsService;

    @Inject
    public TrainersService trainersService;

    @Inject
    public UserStorage usersStorage;
    @Inject
    public Provider<TrainerStorage> trainerStorageProvider;
    private String regionId;
    protected final CompositeDisposable disposables = new CompositeDisposable();
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
        disposables.add(trainersService.deleteTrainer(trainerStorageProvider.get().getRegionId(), trainerStorageProvider.get().getTrainer()._id()).
                observeOn(FX_SCHEDULER).subscribe(result -> {
                 trainerStorageProvider.get().setTrainer(null);
                 trainerStorageProvider.get().setTrainerSprite(null);
                 trainerStorageProvider.get().setTrainerName(null);
                 trainerStorageProvider.get().setRegionId(null);
        }, error -> this.showError(error.getMessage())));
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
        disposables.add(presetsService.getCharacter(trainerStorageProvider.get().getTrainer().image()).observeOn(FX_SCHEDULER).subscribe(responseBody ->  {
            try {
                trainerImage = ImageProcessor.resonseBodyToJavaFXImage(responseBody);
                trainerAvatarImageView.setImage(trainerImage);
            }
            catch (Exception e) {
                this.showError(e.getMessage());
            }
        }, error -> this.showError(error.getMessage())));
    }
}
