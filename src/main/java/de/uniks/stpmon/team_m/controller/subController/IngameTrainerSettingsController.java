package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.IngameController;
import de.uniks.stpmon.team_m.controller.MainMenuController;
import de.uniks.stpmon.team_m.dto.Trainer;
import de.uniks.stpmon.team_m.service.PresetsService;
import de.uniks.stpmon.team_m.service.RegionsService;
import de.uniks.stpmon.team_m.service.TrainersService;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import de.uniks.stpmon.team_m.utils.ImageProcessor;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
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
import java.awt.*;
import java.util.Objects;
import javax.inject.Singleton;

import java.text.MessageFormat;
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
    public Provider<TrainerStorage> trainerStorageProvider;
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
        final Alert alert = new Alert(Alert.AlertType.WARNING);
        final DialogPane dialogPane = alert.getDialogPane();
        final ButtonType cancelButton = new ButtonType(resources.getString("CANCEL"));
        final ButtonType okButton = alert.getButtonTypes().stream()
                        .filter(buttonType -> buttonType.getButtonData().isDefaultButton()).findFirst().orElse(null);

        dialogPane.getButtonTypes().addAll(cancelButton);

        final String trainerName = trainerStorageProvider.get().getTrainer().name();
        final String deleteTrainerText = resources.getString("DELETE.TRAINER.TEXT");
        dialogPane.setContentText(MessageFormat.format(deleteTrainerText, trainerName));

        alert.setTitle(resources.getString("DELETE.YOUR.TRAINER"));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == cancelButton) {
            alert.close();
        } else if (result.isPresent() && result.get() == okButton) {
            onCancelButtonClick();
            disposables.add(trainersService.deleteTrainer(trainerStorageProvider.get().getRegionId(), trainerStorageProvider.get().getTrainer()._id()).
                    observeOn(FX_SCHEDULER).subscribe(end -> {
                        trainerStorageProvider.get().setTrainer(null);
                        trainerStorageProvider.get().setTrainerSprite(null);
                        trainerStorageProvider.get().setTrainerName(null);
                        trainerStorageProvider.get().setRegionId(null);
                    }, error -> this.showError(error.getMessage())));
            MainMenuController mainMenuController = mainMenuControllerProvider.get();
            mainMenuController.setTrainerDeletion();
            app.show(mainMenuController);
            alert.close();
        }

    }

    private void loadAndSetTrainerImage() {
        String trainerSprite = trainerStorageProvider.get().getTrainerSprite();
        if (!GraphicsEnvironment.isHeadless()) {
            trainerSprite = trainerSprite.substring(8);
            String path = Objects.requireNonNull(Main.class.getResource("charactermodels/" + trainerSprite)).toString();
            Image trainerImage = new Image(path);
            trainerAvatarImageView.setImage(trainerImage);
        }

    }
}
