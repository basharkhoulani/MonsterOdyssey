package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.controller.IngameController;
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
    public ObservableList<String> handyMessages;
    private IngameController ingameController;

    @Inject
    public NotificationListHandyController() {
    }

    public void init(IngameController ingameController) {
        handyMessages = FXCollections.observableArrayList();
        this.ingameController = ingameController;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        ingameNotificationListView.setSelectionModel(null);
        ingameNotificationListView.setFocusModel(null);
        ingameNotificationListView.setCellFactory(param -> new IngameNotificationCell());
        ingameNotificationListView.setItems(handyMessages);
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

    public void displayFirstTimeNotifications(boolean first) {
        Timeline timeline = new Timeline();
        int duration = 1;
        if (first) {
            for (int i = 0; i < 4; i++) {
                int iterator = i;
                KeyFrame keyFrame = new KeyFrame(Duration.seconds(duration), event -> handyMessages.add(this.resources.getString("INGAME.NOTIFICATIONS.NEW." + iterator)));
                timeline.getKeyFrames().add(keyFrame);
                duration++;
            }
        } else {
            for (int i = 0; i < 4; i++) {
                handyMessages.add(this.resources.getString("INGAME.NOTIFICATIONS.NEW." + i));
            }
        }

        //shakeAnimation.play();
        timeline.play();
    }

    public void displayStarterMessages(boolean first) {
        handyMessages.clear();
        Timeline timeline = new Timeline();
        int duration = 1;

        if (first) {
            for (int i = 0; i < 2; i++) {
                int iter = i;
                KeyFrame keyFrame = new KeyFrame(Duration.seconds(duration), event -> handyMessages.add(this.resources.getString("INGAME.NOTIFICATIONS.STARTER." + iter)));
                timeline.getKeyFrames().add(keyFrame);
                duration++;
            }
        } else {
            for (int i = 0; i < 2; i++) {
                handyMessages.add(this.resources.getString("INGAME.NOTIFICATIONS.STARTER." + i));
            }
        }

        timeline.play();
        AnimationBuilder.buildShakeAnimation(ingameController.notificationBell, 500, 20, 2).play();
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
        AnimationBuilder.buildShakeAnimation(ingameController.notificationBell, 500, 20, 2).play();
    }
}
