package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.IngameController;
import de.uniks.stpmon.team_m.dto.Trainer;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import javax.inject.Inject;
import javax.inject.Provider;
import java.sql.Time;

public class NotificationListHandyController extends Controller {
    @FXML
    public ListView<String> ingameNotificationListView;
    @FXML
    public StackPane notificationHandyStackPane;

    @Inject
    Provider<NotificationListHandyController> notificationListHandyControllerProvider;

    private IngameController ingameController;
    private Trainer trainer;

    @Inject
    public NotificationListHandyController() {
    }

    public void init(IngameController ingameController, Trainer currentTrainer) {
        this.ingameController = ingameController;

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            ingameController.notificationBell.setVisible(true);
        }));

        timeline.play();

        this.trainer = currentTrainer;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        if (trainer.encounteredMonsterTypes().size() == 0) {
            this.displayFirstTimeNotifications();
        }

        ingameNotificationListView.setMouseTransparent(true);

        return parent;
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

    private void displayFirstTimeNotifications() {
        ObservableList<String> newTrainerNotificationMessages = FXCollections.observableArrayList();

        ingameNotificationListView.setSelectionModel(null);
        ingameNotificationListView.setFocusModel(null);
        ingameNotificationListView.setCellFactory(param -> new IngameNotificationCell(this));
        ingameNotificationListView.setItems(newTrainerNotificationMessages);

        Timeline timeline = new Timeline();
        int duration = 1;

        for (int i = 0; i < 4; i++) {
            int iterator = i;
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(duration), event -> {

                newTrainerNotificationMessages.add(this.resources.getString("INGAME.NOTIFICATIONS.NEW."+iterator));
            });
            timeline.getKeyFrames().add(keyFrame);
            duration++;
        }

        timeline.play();
    }
}
