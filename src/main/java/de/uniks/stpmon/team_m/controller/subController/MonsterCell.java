package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.controller.IngameController;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

import static de.uniks.stpmon.team_m.Constants.ABILITYPALETTE;
import static de.uniks.stpmon.team_m.Constants.TYPESCOLORPALETTE;

public class MonsterCell extends ListCell<Monster> {

    @FXML
    public ImageView arrowUp;
    @FXML
    public ImageView arrowDown;
    @FXML
    public Button removeFromTeamButton;
    @FXML
    public Button viewDetailsButton;
    @FXML
    Label monsterName;
    @FXML
    Label monsterLevel;
    @FXML
    ImageView monsterImageView;
    @FXML
    HBox rootmonsterHBox;
    @FXML
    VBox typeIcon;
    @FXML
    ImageView typeImageView;
    private final ResourceBundle resources;
    @Inject
    Provider<TrainerStorage> trainerStorageProvider;
    @Inject
    Provider<UserStorage> userStorageProvider;
    @Inject
    public TrainersService trainersService;
    @Inject
    public UserStorage usersStorage;
    public final PresetsService presetsService;
    public final IngameController ingameController;
    @Inject
    Provider<TrainersService> trainersServiceProvider;
    final MonstersListController monstersListController;
    private FXMLLoader loader;

    protected final CompositeDisposable disposables = new CompositeDisposable();
    public static final Scheduler FX_SCHEDULER = Schedulers.from(Platform::runLater);
    private MonsterTypeDto monsterTypeDto;
    private Image monsterImage;
    private String typeColor;
    private String typeImagePath;
    private Image typeImage;

    public MonsterCell(ResourceBundle resources, PresetsService presetsService, MonstersListController monstersListController, IngameController ingameController) {
        this.ingameController = ingameController;
        this.resources = resources;
        this.presetsService = presetsService;
        this.monstersListController = monstersListController;
    }

    @Override
    protected void updateItem(Monster monster, boolean empty) {
        super.updateItem(monster, empty);
        StringBuilder type = new StringBuilder();
        if (monster == null || empty) {
            setText(null);
            setGraphic(null);
            setStyle("-fx-background-color: #CFE9DB;");
        } else {
            loadFXML();
            viewDetailsButton.prefWidthProperty().bind(removeFromTeamButton.widthProperty());
            disposables.add(presetsService.getMonster(monster.type()).observeOn(FX_SCHEDULER)
                    .subscribe(monsterType -> {
                        monsterTypeDto = monsterType;
                        monsterName.setText(resources.getString("NAME") + " " + monsterTypeDto.name());
                        for (String s : monsterTypeDto.type()) {
                            type.append("").append(s);
                        }
                        typeColor = TYPESCOLORPALETTE.get(type.toString());
                        String style = "-fx-background-color: " + typeColor + ";";
                        typeIcon.setStyle(style);

                        if(!GraphicsEnvironment.isHeadless()) {
                            typeImagePath = ABILITYPALETTE.get(type.toString());
                            URL resourceType = Main.class.getResource("images/" + typeImagePath);
                            assert resourceType != null;
                            typeImage = new Image(resourceType.toString());
                            typeImageView.setImage(typeImage);
                            typeImageView.setFitHeight(45);
                            typeImageView.setFitWidth(45);

                            URL resourseArrowUp = Main.class.getResource("images/monster-arrange-up.png");
                            assert resourseArrowUp != null;
                            Image arrowUpImage = new Image(resourseArrowUp.toString());
                            arrowUp.setImage(arrowUpImage);
                            arrowDown.setImage(arrowUpImage);
                        }
                    }, error -> monstersListController.showError(error.getMessage())));
            monsterLevel.setText(resources.getString("LEVEL") + " " + monster.level());
            disposables.add(presetsService.getMonsterImage(monster.type()).observeOn(FX_SCHEDULER)
                    .subscribe(monsterImage -> {
                        this.monsterImage = ImageProcessor.resonseBodyToJavaFXImage(monsterImage);
                        monsterImageView.setImage(this.monsterImage);
                    }, error -> monstersListController.showError(error.getMessage())));
            viewDetailsButton.setOnAction(event -> showDetails(monster, type.toString()));
            setGraphic(rootmonsterHBox);
            setText(null);
            setStyle("-fx-background-color: #CFE9DB;  -fx-border-color: #1C701C; -fx-border-width: 2px");
        }
    }

    private void showDetails(Monster monster, String type) {
        this.ingameController.showMonsterDetails(monster, monsterTypeDto, monsterImage, resources, presetsService, type);
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

