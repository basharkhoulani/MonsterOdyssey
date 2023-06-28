package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.controller.subController.BattleMenuController;
import de.uniks.stpmon.team_m.dto.Opponent;
import de.uniks.stpmon.team_m.dto.Trainer;
import de.uniks.stpmon.team_m.service.*;
import de.uniks.stpmon.team_m.utils.EncounterOpponentStorage;
import de.uniks.stpmon.team_m.utils.ImageProcessor;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import de.uniks.stpmon.team_m.ws.EventListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import javax.inject.Inject;
import javax.inject.Provider;
import java.awt.*;
import java.util.Objects;

public class EncounterController extends Controller {
    @FXML
    public Label opponentLevel;
    @FXML
    public ProgressBar opponentHealthBar;
    @FXML
    public Label opponentMonsterName;
    @FXML
    public ImageView opponentTrainer;
    @FXML
    public ImageView opponentMonster;
    @FXML
    public ImageView myMonster;
    @FXML
    public ImageView mySprite;
    @FXML
    public ProgressBar myLevelBar;
    @FXML
    public Label myLevel;
    @FXML
    public ProgressBar myHealthBar;
    @FXML
    public Label myHealth;
    @FXML
    public Label myMonsterName;
    @FXML
    public HBox battleMenu;
    @FXML
    public Text battleDescription;
    @FXML
    public Button goBack;

    @Inject
    EncounterOpponentsService encounterOpponentsService;
    @Inject
    RegionEncountersService regionEncountersService;
    @Inject
    TrainersService trainersService;
    @Inject
    MonstersService monstersService;
    @Inject
    Provider<IngameController> ingameControllerProvider;
    @Inject
    Provider<EventListener> eventListener;
    @Inject
    Provider<EncounterOpponentStorage> encounterOpponentStorageProvider;
    @Inject
    BattleMenuController battleMenuController;
    @Inject
    Provider<TrainerStorage> trainerStorageProvider;

    private ObservableList<Opponent> opponents = FXCollections.observableArrayList();
    String regionId;
    String encounterId;
    String trainerId;

    @Inject
    public EncounterController() {
    }

    public void init() {
        super.init();
        regionId = encounterOpponentStorageProvider.get().getRegionId();
        encounterId = encounterOpponentStorageProvider.get().getEncounterId();
        trainerId = trainerStorageProvider.get().getTrainer()._id();
        disposables.add(regionEncountersService.getEncounter(regionId, encounterId)
                .observeOn(FX_SCHEDULER).subscribe(encounter -> encounterOpponentStorageProvider.get().setWild(encounter.isWild())
                        , error -> showError(error.getMessage())));
        disposables.add(encounterOpponentsService.getEncounterOpponents(regionId, encounterId)
                .observeOn(FX_SCHEDULER).subscribe(os -> {
                    opponents.setAll(os);
                    initEncounterStorage();
                }, error -> showError(error.getMessage())));
        initEncounterStorage();
        battleMenuController.init();
    }

    private void initEncounterStorage() {
        encounterOpponentStorageProvider.get().setOpponentsInStorage(opponents);
        for (Opponent o : opponents) {
            if(o.trainer().equals(trainerStorageProvider.get().getTrainer()._id())){
                encounterOpponentStorageProvider.get().setSelfOpponent(o);
            } else {
                encounterOpponentStorageProvider.get().setEnemyOpponent(o);
            }
        }
        if(!encounterOpponentStorageProvider.get().isWild()){
            String enemyTrainerId = encounterOpponentStorageProvider.get().getEnemyOpponent().trainer();
            disposables.add(trainersService.getTrainer(regionId, enemyTrainerId)
                    .observeOn(FX_SCHEDULER).subscribe(trainer -> {
                        encounterOpponentStorageProvider.get().setOpponentTrainer(trainer);
                    }, Throwable::printStackTrace));
        }
        disposables.add(monstersService.getMonster(regionId, trainerId, encounterOpponentStorageProvider.get().getSelfOpponent().monster())
                .observeOn(FX_SCHEDULER).subscribe(monster -> encounterOpponentStorageProvider.get().setCurrentTrainerMonster(monster),
                        Throwable::printStackTrace));
        disposables.add(monstersService.getMonster(regionId, encounterOpponentStorageProvider.get().getEnemyOpponent().trainer(), encounterOpponentStorageProvider.get().getEnemyOpponent().monster())
                .observeOn(FX_SCHEDULER).subscribe(monster -> encounterOpponentStorageProvider.get().setCurrentTrainerMonster(monster),
                        Throwable::printStackTrace));
    }

    public String getTitle() {
        return resources.getString("ENCOUNTER");
    }

    public Parent render() {
        final Parent parent = super.render();
        if (!GraphicsEnvironment.isHeadless()) {
            // Style sheet
            parent.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../styles.css")).toExternalForm());
            // Sprite
            mySprite.setImage(ImageProcessor.showScaledFrontCharacter(trainerStorageProvider.get().getTrainer().image()));
            //opponentTrainer.setImage(ImageProcessor.showScaledFrontCharacter(encounterOpponentStorageProvider.get().getOpponentTrainer().image()));
            //showMonsterImage();
        }
        // render for subcontroller
        battleMenuController.init(this, battleMenu);
        battleMenu.getChildren().add(battleMenuController.render());

        listenToOpponents(opponents, encounterOpponentStorageProvider.get().getEncounterId());
        return parent;
    }

    private void showMonsterImage() {
    }


    @Override
    public void destroy() {
        super.destroy();
    }

    public void listenToOpponents(ObservableList<Opponent> opponents, String encounterId) {
        disposables.add(eventListener.get().listen("encounters." + encounterId + "opponents.*.*", Opponent.class)
                .observeOn(FX_SCHEDULER).subscribe(event -> {
                    final Opponent opponent = event.data();
                    switch (event.suffix()) {
                        case "created" -> opponents.add(opponent);
                        case "updated" -> opponents.add(opponent);
                        case "deleted" -> app.show(ingameControllerProvider.get());
                    }
                }, error -> showError(error.getMessage())));
    }

    public void showIngameController() {
        app.show(ingameControllerProvider.get());
    }
}
    
