package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.App;
import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.EncounterController;
import de.uniks.stpmon.team_m.controller.IngameController;
import de.uniks.stpmon.team_m.controller.MainMenuController;
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
    private Boolean coinsEarned;
    private Integer coinsAmount;

    @Inject
    public EncounterResultController() {
    }

    public void init(App app) {
        this.app = app;
    }

    public void clickOK() {
        encounterController.get().destroy();
        Trainer trainer = trainerStorageProvider.get().getTrainer();
        if (trainer.settings() != null && trainer.settings().permaDeath() != null && trainer.settings().permaDeath()) {
            disposables.add(trainersService.deleteTrainer(trainer.region(), trainer._id()).observeOn(FX_SCHEDULER).subscribe(trainer1 -> {
                destroy();
                app.show(mainMenuControllerProvider.get());
            }));
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
