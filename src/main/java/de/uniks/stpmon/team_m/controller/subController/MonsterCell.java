package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.Constants;
import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.dto.MonsterTypeDto;
import de.uniks.stpmon.team_m.service.PresetsService;
import de.uniks.stpmon.team_m.service.TrainersService;
import de.uniks.stpmon.team_m.utils.ImageProcessor;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import de.uniks.stpmon.team_m.utils.UserStorage;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import static de.uniks.stpmon.team_m.Constants.ABILITYPALETTE;
import static de.uniks.stpmon.team_m.Constants.TYPESCOLORPALETTE;

public class MonsterCell extends ListCell<Monster> {

    @FXML
    public Button removeFromTeamButton;
    @FXML
    public Button viewDetailsButton;
    @FXML
    Label monsterName;
    @FXML
    Label monsterType;
    @FXML
    Label monsterLevel;
    @FXML
    ImageView monsterImageView;
    @FXML
    HBox rootmonsterHBox;
    @FXML
    VBox abilityIcon;
    @FXML
    ImageView abilityImageView;
    private final ResourceBundle resources;
    @Inject
    Provider<TrainerStorage> trainerStorageProvider;
    @Inject
    Provider<UserStorage> userStorageProvider;
    @Inject
    public TrainersService trainersService;
    @Inject
    public UserStorage usersStorage;
    public PresetsService presetsService;
    @Inject
    Provider<TrainersService> trainersServiceProvider;
    MonstersListController monstersListController;
    private FXMLLoader loader;

    protected final CompositeDisposable disposables = new CompositeDisposable();
    public static final Scheduler FX_SCHEDULER = Schedulers.from(Platform::runLater);
    private MonsterTypeDto monsterTypeDto;
    private Image monsterImage;
    private String abilityColor;
    private String abilityImagePath;
    private Image abilityImage;

    public MonsterCell(ResourceBundle resources, PresetsService presetsService, MonstersListController monstersListController) {
        this.resources = resources;
        this.presetsService = presetsService;
        this.monstersListController = monstersListController;
    }

    @Override
    protected void updateItem(Monster monster, boolean empty) {
        super.updateItem(monster, empty);
        if (monster == null || empty) {
            setText(null);
            setGraphic(null);
        } else {
            loadFXML();
            viewDetailsButton.prefWidthProperty().bind(removeFromTeamButton.widthProperty());
            disposables.add(presetsService.getMonster(monster.type()).observeOn(FX_SCHEDULER)
                    .subscribe(monsterType -> {
                        monsterTypeDto = monsterType;
                        monsterName.setText(resources.getString("NAME") + " " + monsterTypeDto.name());
                        StringBuilder ability = new StringBuilder();
                        for (String s : monsterTypeDto.type()) {
                            ability.append("").append(s);
                        }
                        abilityColor = TYPESCOLORPALETTE.get(ability.toString());
                        String style = "-fx-background-color: " + abilityColor + ";";
                        abilityIcon.setStyle(style);

                        abilityImagePath = ABILITYPALETTE.get(ability.toString());
                        URL resource = Main.class.getResource("images/" + abilityImagePath);
                        abilityImage = new Image(resource.toString());
                        abilityImageView.setImage(abilityImage);
                    }, error -> monstersListController.showError(error.getMessage())));
            monsterLevel.setText(resources.getString("LEVEL") + " " + monster.level());
            disposables.add(presetsService.getMonsterImage(monster.type()).observeOn(FX_SCHEDULER)
                    .subscribe(monsterImage -> {
                        this.monsterImage = ImageProcessor.resonseBodyToJavaFXImage(monsterImage);
                        monsterImageView.setImage(this.monsterImage);
                    }, error -> monstersListController.showError(error.getMessage())));
            setGraphic(rootmonsterHBox);
            setText(null);
        }
    }

    private void showDetails(Monster monster) {
        Stage popup = (Stage) rootmonsterHBox.getScene().getWindow();
        popup.close();
        MonstersDetailController monstersDetailController = new MonstersDetailController();
        monstersDetailController.init(monstersListController, monster, monsterTypeDto, monsterImage, resources, presetsService);
        Scene scene = new Scene(monstersDetailController.render());
        popup.setScene(scene);
        popup.show();
    }

    private void loadFXML() {
        if (loader == null) {
            loader = new FXMLLoader(Main.class.getResource("views/MonsterCell.fxml"));
            loader.setResources(resources);
            loader.setControllerFactory(c -> this);
            try {
                loader.load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

