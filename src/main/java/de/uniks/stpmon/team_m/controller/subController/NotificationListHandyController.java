package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.IngameController;
import de.uniks.stpmon.team_m.dto.Trainer;
import de.uniks.stpmon.team_m.utils.AnimationBuilder;
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

import static de.uniks.stpmon.team_m.Constants.spaceBetweenPhoneAndWindowEdge;

public class NotificationListHandyController extends Controller {
    @FXML
    public ListView<String> ingameNotificationListView;
    @FXML
    public StackPane notificationHandyStackPane;

    @Inject
    Provider<NotificationListHandyController> notificationListHandyControllerProvider;
    ObservableList<String> handyMessages;
    private IngameController ingameController;
    private Trainer trainer;
    private Timeline shakeAnimation;


    @Inject
    public NotificationListHandyController() {
    }

    public void init(IngameController ingameController, Trainer currentTrainer) {
        handyMessages = FXCollections.observableArrayList();
        this.ingameController = ingameController;
        shakeAnimation = AnimationBuilder.buildShakeAnimation(ingameController.notificationBell, 500, 30, 2);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> ingameController.notificationBell.setVisible(true)));

        timeline.play();

        this.trainer = currentTrainer;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        ingameNotificationListView.setSelectionModel(null);
        ingameNotificationListView.setFocusModel(null);
        ingameNotificationListView.setCellFactory(param -> new IngameNotificationCell());
        ingameNotificationListView.setItems(handyMessages);

        if (trainer.encounteredMonsterTypes().size() == 0) {
            this.displayFirstTimeNotifications();
        }
        if (trainer.encounteredMonsterTypes().size() == 1) {
            this.displayStarterMessages();
        }
        ingameNotificationListView.setMouseTransparent(true);

        return parent;
    }

    public void closeNotificationListHandy() {
        for (int i = 0; i < notificationHandyStackPane.getWidth() + spaceBetweenPhoneAndWindowEdge; i++) {
            int iterator = i;

            PauseTransition pause = new PauseTransition(Duration.millis(1));
            pause.setOnFinished(event -> notificationHandyStackPane.translateXProperty().bind(
                    ingameController.anchorPane.
                            widthProperty().
                            add(notificationHandyStackPane.widthProperty()).
                            divide(2).
                            subtract(notificationHandyStackPane.widthProperty()).
                            add(iterator)
            ));
            pause.setDelay(Duration.millis(i));
            pause.play();
        }

        ingameController.smallHandyButton.setVisible(true);
    }

    private void displayFirstTimeNotifications() {
        Timeline timeline = new Timeline();
        int duration = 1;

        for (int i = 0; i < 4; i++) {
            int iterator = i;
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(duration), event -> handyMessages.add(this.resources.getString("INGAME.NOTIFICATIONS.NEW." + iterator)));
            timeline.getKeyFrames().add(keyFrame);
            duration++;
        }
        shakeAnimation.play();
        timeline.play();
    }

    public void displayStarterMessages() {
        handyMessages.clear();
        Timeline timeline = new Timeline();
        int duration = 1;

        for (int i = 0; i < 2; i++) {
            int iter = i;
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(duration), event -> handyMessages.add(this.resources.getString("INGAME.NOTIFICATIONS.STARTER." + iter)));
            timeline.getKeyFrames().add(keyFrame);
            duration++;
        }
        timeline.play();
        shakeAnimation.play();
    }

    public void displayLowHealthMessages() {
        Timeline timeline = new Timeline();
        int duration = 1;

        for (int i = 0; i < 2; i++) {
            int iter = i;
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(duration), event -> handyMessages.add(this.resources.getString("INGAME.NOTIFICATIONS.LOWHEALTH." + iter)));
            timeline.getKeyFrames().add(keyFrame);
            duration++;
        }
        timeline.play();
        shakeAnimation.play();
    }
}
