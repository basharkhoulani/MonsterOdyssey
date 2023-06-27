package de.uniks.stpmon.team_m.controller;

import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.controller.subController.BattleMenuController;
import de.uniks.stpmon.team_m.dto.Message;
import de.uniks.stpmon.team_m.dto.Opponent;
import de.uniks.stpmon.team_m.service.EncounterOpponentsService;
import de.uniks.stpmon.team_m.service.PresetsService;
import de.uniks.stpmon.team_m.utils.EncounterOpponentStorage;
import de.uniks.stpmon.team_m.ws.EventListener;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import javax.inject.Inject;
import javax.inject.Provider;
import java.awt.*;
import java.util.Objects;
import java.util.ResourceBundle;

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

    private BattleMenuController battleMenuController;
    @Inject
    EncounterOpponentsService encounterOpponentsService;
    @Inject
    Provider<IngameController> ingameControllerProvider;
    @Inject
    Provider<PresetsService> presetsServiceProvider;
    @Inject
    Provider<EventListener> eventListener;
    @Inject
    Provider<EncounterOpponentStorage> encounterOpponentStorageProvider;

    private ObservableList<Opponent> opponents = FXCollections.observableArrayList();

    @Inject
    public EncounterController() {
    }

    public void init(){
        super.init();
        String regionId = encounterOpponentStorageProvider.get().getRegionId();
        String encounterId = encounterOpponentStorageProvider.get().getEncounterId();
        disposables.add(encounterOpponentsService.getEncounterOpponents(regionId, encounterId)
                .observeOn(FX_SCHEDULER).subscribe(this.opponents::setAll, error -> showError(error.getMessage())));
        battleMenuController = new BattleMenuController();
        battleMenuController.init();
    }

    public String getTitle() {return resources.getString("ENCOUNTER"); }

    public Parent render() {
        final Parent parent = super.render();
        if(!GraphicsEnvironment.isHeadless()){
            parent.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../styles.css")).toExternalForm());
        }
        battleMenuController.init(this, battleMenu);
        battleMenu.getChildren().add(battleMenuController.render());
        return parent;
    }

    @Override
    public void destroy(){
        super.destroy();
    }

    public void listenToOpponents(ObservableList<Opponent> opponents, String encounterId) {
        disposables.add(eventListener.get().listen("encounters." + encounterId + "opponents.*.*", Opponent.class)
                .observeOn(FX_SCHEDULER).subscribe(event -> {
                    final Opponent opponent = event.data();
                    switch (event.suffix()) {
                        case "created" -> opponents.add(opponent);
                        case "updated" -> opponents.add(opponent);
                        case "deleted" -> opponents.removeIf(o -> o._id().equals(opponent._id()));
                    }
                }, error -> showError(error.getMessage())));
    }

}
    
