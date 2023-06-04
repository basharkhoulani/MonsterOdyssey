package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.dto.Region;
import de.uniks.stpmon.team_m.dto.Trainer;
import de.uniks.stpmon.team_m.service.PresetsService;
import de.uniks.stpmon.team_m.service.RegionsService;
import de.uniks.stpmon.team_m.service.TrainersService;
import de.uniks.stpmon.team_m.utils.ImageProcessor;
import de.uniks.stpmon.team_m.utils.UserStorage;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.inject.Inject;


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
    private Region region;
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
        // TODO: show alert dialog
    }

    public void setRegion(Region region) {
        this.region = region;
        loadTrainer();
    }

    public void loadTrainer() {
        if (this.region != null) {
            disposables.add(trainersService.getTrainers(this.region._id(), null, usersStorage.get_id()).observeOn(FX_SCHEDULER).subscribe(trainers -> {
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
                trainerImage = ImageProcessor.resonseBodyToJavaFXImage(responseBody);
                trainerAvatarImageView.setImage(trainerImage);
            }
            catch (Exception e) {
                this.showError(e.getMessage());
            }
        }, error -> this.showError(error.getMessage())));
    }
}
