package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.MainMenuController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

import javax.inject.Inject;
import javax.inject.Provider;

public class IngameLoadingController extends Controller {

    @FXML
    public Label loadingLabel;
    @FXML
    public Button goBackButton;
    @Inject
    Provider<MainMenuController> mainMenuControllerProvider;

    @Inject
    public IngameLoadingController() {
    }

    @Override
    public String getTitle() {
        return resources.getString("INGAME.TITLE");
    }


    @Override
    public Parent render() {
        final Parent parent = super.render();
        startLoading();
        return parent;
    }

    public void updateLoadingLabel() {
        String currentLoadingText = loadingLabel.getText();
        if (currentLoadingText.endsWith("...")) {
            currentLoadingText = resources.getString("LOADING.LABEL");
        } else {
            currentLoadingText = currentLoadingText + ".";
        }
        loadingLabel.setText(currentLoadingText);
    }

    public void startLoading() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> updateLoadingLabel()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void changeToMain() {
        app.show(mainMenuControllerProvider.get());
    }
}
