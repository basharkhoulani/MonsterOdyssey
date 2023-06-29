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
import javafx.scene.image.Image;
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
    PresetsService presetsService;
    @Inject
    Provider<IngameController> ingameControllerProvider;
    @Inject
    Provider<EventListener> eventListener;
    @Inject
    EncounterOpponentStorage encounterOpponentStorage;
    @Inject
    BattleMenuController battleMenuController;
    @Inject
    Provider<TrainerStorage> trainerStorageProvider;

    private String regionId;
    private String encounterId;
    private String trainerId;
    private Image myMonsterImage;
    private Image enemyMonsterImage;

    @Inject
    public EncounterController() {
    }

    public void init() {
        super.init();
        regionId = encounterOpponentStorage.getRegionId();
        encounterId = encounterOpponentStorage.getEncounterId();
        trainerId = trainerStorageProvider.get().getTrainer()._id();
        disposables.add(regionEncountersService.getEncounter(regionId, encounterId)
                .observeOn(FX_SCHEDULER).subscribe(encounter -> {
                    encounterOpponentStorage.setWild(encounter.isWild());
                }, Throwable::printStackTrace));
        battleMenuController.init();
    }

    public String getTitle() {
        return resources.getString("ENCOUNTER");
    }

    public Parent render() {
        final Parent parent = super.render();
        System.out.println("EncounterController.render");
        if (!GraphicsEnvironment.isHeadless()) {
            // Sprite
            showTrainerImage();
            showMonsterImage();
        }
        // render for subcontroller
        battleMenuController.init(this, battleMenu);
        battleMenu.getChildren().add(battleMenuController.render());

        listenToOpponents(encounterOpponentStorage.getEncounterId());
        return parent;
    }

    private void showTrainerImage(){
        mySprite.setImage(ImageProcessor.showScaledFrontCharacter(trainerStorageProvider.get().getTrainer().image()));
        if(!encounterOpponentStorage.isWild()){
            String enemyTrainerId = encounterOpponentStorage.getEnemyOpponent().trainer();
            disposables.add(trainersService.getTrainer(regionId, enemyTrainerId)
                    .observeOn(FX_SCHEDULER).subscribe(trainer -> {
                        encounterOpponentStorage.setOpponentTrainer(trainer);
                        opponentTrainer.setImage(ImageProcessor.showScaledFrontCharacter(trainer.image()));
                    }, Throwable::printStackTrace));
        }
    }

    public void initEncounterOpponentStorage(){
        if(!encounterOpponentStorage.isWild()){
            String enemyTrainerId = encounterOpponentStorage.getEnemyOpponent().trainer();
            disposables.add(trainersService.getTrainer(regionId, enemyTrainerId)
                    .observeOn(FX_SCHEDULER).subscribe(trainer -> {
                        encounterOpponentStorage.setOpponentTrainer(trainer);
                    }, Throwable::printStackTrace));
        }
        disposables.add(monstersService.getMonster(regionId, trainerId, encounterOpponentStorage.getSelfOpponent().monster())
                .observeOn(FX_SCHEDULER).subscribe(monster -> encounterOpponentStorage.setCurrentTrainerMonster(monster),
                        Throwable::printStackTrace));
        disposables.add(monstersService.getMonster(regionId, encounterOpponentStorage.getEnemyOpponent().trainer(), encounterOpponentStorage.getEnemyOpponent().monster())
                .observeOn(FX_SCHEDULER).subscribe(monster -> encounterOpponentStorage.setCurrentTrainerMonster(monster),
                        Throwable::printStackTrace));
    }

    private void showMonsterImage() {
        disposables.add(monstersService.getMonster(regionId, trainerId, encounterOpponentStorage.getSelfOpponent().monster())
                .observeOn(FX_SCHEDULER).subscribe(monster -> {
                    encounterOpponentStorage.setCurrentTrainerMonster(monster);
                    disposables.add(presetsService.getMonsterImage(monster.type())
                            .observeOn(FX_SCHEDULER).subscribe(mImage -> {
                                myMonsterImage = ImageProcessor.resonseBodyToJavaFXImage(mImage);
                                myMonster.setImage(myMonsterImage);
                            }, Throwable::printStackTrace));
                        }, Throwable::printStackTrace));

        disposables.add(monstersService.getMonster(regionId, encounterOpponentStorage.getEnemyOpponent().trainer(), encounterOpponentStorage.getEnemyOpponent().monster())
                .observeOn(FX_SCHEDULER).subscribe(monster -> {
                    encounterOpponentStorage.setCurrentEnemyMonster(monster);
                    disposables.add(presetsService.getMonsterImage(monster.type())
                            .observeOn(FX_SCHEDULER).subscribe(mImage -> {
                                enemyMonsterImage = ImageProcessor.resonseBodyToJavaFXImage(mImage);
                                opponentMonster.setImage(enemyMonsterImage);
                            }, Throwable::printStackTrace));
                        }, Throwable::printStackTrace));
    }


    @Override
    public void destroy() {
        super.destroy();
    }

    public void listenToOpponents(String encounterId) {
        disposables.add(eventListener.get().listen("encounters." + encounterId + "opponents.*.*", Opponent.class)
                .observeOn(FX_SCHEDULER).subscribe(event -> {
                    final Opponent opponent = event.data();
                    switch (event.suffix()) {
                        case "deleted" -> app.show(ingameControllerProvider.get());
                    }
                }, error -> showError(error.getMessage())));
    }

    public void showIngameController() {
        app.show(ingameControllerProvider.get());
    }
}
    
