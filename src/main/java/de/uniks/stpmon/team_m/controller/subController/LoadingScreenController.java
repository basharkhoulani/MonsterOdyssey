package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class LoadingScreenController extends Controller {
    @FXML
    public BorderPane loadingScreenRootBorderPane;
    @FXML
    public Label loadingScreenLoadingLabel;
    @FXML
    public Label loadingScreenDidYouKnowLabel;
    @FXML
    public Label loadingScreenTippLabel;
    @FXML
    public ImageView loadingScreenTrainerImageView;
    private Timer timer;

    public LoadingScreenController() {
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        //loadingScreenRootBorderPane.setStyle("-fx-background-color: lightgray; -fx-background-radius: 10;" );
        loadingScreenRootBorderPane.getStyleClass().add("ForegroundContainer");
        loadingScreenLoadingLabel.setText(resources.getString("LOADING.LABEL"));
        loadingScreenDidYouKnowLabel.setText(resources.getString("DID.YOU.KNOW"));

        timer = new Timer();
        Random random = new Random();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                int i = random.nextInt(16) + 1;
                Platform.runLater(() -> {
                    loadingScreenTippLabel.setText(resources.getString("LOADING.SCREEN.TEXT" + i));
                });

            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 7000);
        return parent;
    }

    @Override
    public void destroy() {
        super.destroy();
        timer.cancel();
    }
}
