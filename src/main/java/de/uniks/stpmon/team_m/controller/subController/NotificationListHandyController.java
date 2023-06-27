package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.IngameController;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import javax.inject.Inject;
import javax.inject.Provider;

public class NotificationListHandyController extends Controller {
    @FXML
    public ListView ingameNotificationListView;
    @FXML
    public StackPane notificationHandyStackPane;

    @Inject
    Provider<NotificationListHandyController> notificationListHandyControllerProvider;

    private IngameController ingameController;

    @Inject
    public NotificationListHandyController() {
    }

    public void init(IngameController ingameController) {
        this.ingameController = ingameController;

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            ingameController.notificationBell.setVisible(true);
        }));

        timeline.play();
    }

    public void closeNotificationListHandy() {
        for (int i = 0; i < notificationHandyStackPane.getWidth() + 13; i++) {
            int iterator = i;

            PauseTransition pause = new PauseTransition(Duration.millis(1));
            pause.setOnFinished(event -> {
                notificationHandyStackPane.translateXProperty().bind(
                        ingameController.anchorPane.
                                widthProperty().
                                add(notificationHandyStackPane.widthProperty()).
                                divide(2).
                                add(17).
                                subtract(notificationHandyStackPane.widthProperty()).
                                add(iterator)
                );
            });
            pause.setDelay(Duration.millis(i));
            pause.play();
        }

        ingameController.smallHandyButton.setVisible(true);
    }
}
