package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.controller.*;
import de.uniks.stpmon.team_m.dto.Trainer;
import de.uniks.stpmon.team_m.service.TrainersService;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.inject.Inject;
import javax.inject.Provider;

public class EncounterResultController extends Controller {
    @FXML
    public VBox resultVBox;
    @FXML
    public Text informationText;
    @FXML
    public Button okButton;
    @Inject
    Provider<EncounterController> encounterController;
    @Inject
    Provider<IngameController> ingameControllerProvider;
    @Inject
    Provider<MainMenuController> mainMenuControllerProvider;
    @Inject
    Provider<TrainerStorage> trainerStorageProvider;
    @Inject
    TrainersService trainersService;
    @Inject
    Provider<HardcoreDeathScreenController> hardcoreDeathScreenControllerProvider;

    private Boolean coinsEarned;
    private Integer coinsAmount;
    private HardcoreDeathScreenController hardcoreDeathScreenController;

    @Inject
    public EncounterResultController() {
    }

    public void init(App app) {
        this.app = app;
    }

    public void clickOK() {
        encounterController.get().destroy();
        Trainer trainer = trainerStorageProvider.get().getTrainer();
        if (trainer.settings() != null && trainer.settings().permaDeath() != null && trainer.settings().permaDeath()
                && informationText.getText().equals(resources.getString("YOU.FAILED"))) {
            disposables.add(trainersService.deleteTrainer(trainerStorageProvider.get().getRegion()._id(), trainerStorageProvider.get().getTrainer()._id()).
                    observeOn(FX_SCHEDULER).subscribe(end -> {
                        trainerStorageProvider.get().setTrainer(null);
                        trainerStorageProvider.get().setTrainerSprite(null);
                        trainerStorageProvider.get().setTrainerName(null);
                        trainerStorageProvider.get().setRegion(null);
                        hardcoreDeathScreenController = hardcoreDeathScreenControllerProvider.get();
                        hardcoreDeathScreenController.setValues(resources, preferences, resourceBundleProvider, hardcoreDeathScreenController, app);
                        destroy();
                        app.show(hardcoreDeathScreenController);
                    }));
            MainMenuController mainMenuController = mainMenuControllerProvider.get();
            mainMenuController.setTrainerDeletion();
            app.show(mainMenuController);
        } else {
            IngameController ingameController = ingameControllerProvider.get();
            ingameController.setCoinsAmount(getCoinsAmount());
            ingameController.setCoinsEarned(getCoinsEarned());
            ingameController.setIsNewStart(false);
            app.show(ingameController);
        }
    }

    public void setInformationText(String text) {
        informationText.setText(text);
    }

    public Boolean getCoinsEarned() {
        return coinsEarned;
    }

    public void setCoinsEarned(Boolean coinsEarned) {
        this.coinsEarned = coinsEarned;
    }

    public Integer getCoinsAmount() {
        return coinsAmount;
    }

    public void setCoinsAmount(Integer coinsAmount) {
        this.coinsAmount = coinsAmount;
    }
}
