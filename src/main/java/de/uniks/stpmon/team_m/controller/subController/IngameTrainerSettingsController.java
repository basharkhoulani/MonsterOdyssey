package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.dto.Trainer;
import de.uniks.stpmon.team_m.service.PresetsService;
import de.uniks.stpmon.team_m.service.RegionsService;
import de.uniks.stpmon.team_m.service.TrainersService;
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
import java.util.Objects;


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
    public TrainerStorage trainerStorage;
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
        Parent parent = super.render();
        loadAndSetTrainerImage();
        return parent;
    }

    public void onCancelButtonClick() {
        ((Stage) cancelButton.getScene().getWindow()).close();
    }

    public void onDeleteTrainerButtonClick() {
        // TODO: show alert dialog
    }

    private void loadAndSetTrainerImage() {
        String trainerSprite = trainerStorage.getTrainerSprite();
        trainerSprite = trainerSprite.substring(8);
        String path = Objects.requireNonNull(Main.class.getResource("charactermodels/" + trainerSprite)).toString();
        Image trainerImage = new Image(path);
        trainerAvatarImageView.setImage(trainerImage);
    }
}
