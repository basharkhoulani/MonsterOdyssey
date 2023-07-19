package de.uniks.stpmon.team_m.utils;

import de.uniks.stpmon.team_m.Constants;
import de.uniks.stpmon.team_m.Main;
import de.uniks.stpmon.team_m.controller.subController.EncounterOpponentController;
import javafx.animation.*;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.Objects;

import static de.uniks.stpmon.team_m.Constants.TRAINER_DIRECTION_DOWN;

public class AnimationBuilder {
    /**
     * Builds a sequential transition for the trainer to walk to the opponent
     *
     * @param trainerChunk         : The imageChunk of the trainer
     * @param ownTrainerController : The controller of the trainer
     * @return : The sequential transition
     */
    public static SequentialTransition buildFleeAnimation(Image trainerChunk, EncounterOpponentController ownTrainerController) {
        SequentialTransition transition = new SequentialTransition();
        var ref = new Object() {
            int currentImageIndex = 0;
        };

        Image[] images = ImageProcessor.cropTrainerImages(trainerChunk, TRAINER_DIRECTION_DOWN, true);

        KeyFrame animationFrame = new KeyFrame(Duration.millis(Constants.DELAY), event -> {
            ownTrainerController.setTrainerImage(images[ref.currentImageIndex]);
            ref.currentImageIndex = (ref.currentImageIndex + 1) % 6;
        });

        KeyFrame movementFrame = new KeyFrame(Duration.millis(Constants.DELAY), evt -> {
            TranslateTransition translateTransition = new TranslateTransition();
            translateTransition.setNode(ownTrainerController.trainerMonsterVBox);
            translateTransition.setByY(16);
            translateTransition.setDuration(Duration.millis(Constants.DELAY));
            translateTransition.setCycleCount(1);
            translateTransition.play();
        });
        for (int i = 0; i < 12; i++) {
            Timeline fleeAnimation = new Timeline(animationFrame);
            Timeline trainerMovement = new Timeline(movementFrame);
            ParallelTransition parallelTransition = new ParallelTransition(fleeAnimation, trainerMovement);
            transition.getChildren().add(parallelTransition);
        }
        return transition;
    }

    /**
     * Builds a transition for a progressbar value to a targeted value
     *
     * @param progressBar    : The progressbar to be animated
     * @param durationMillis : The duration of the animation in milliseconds
     * @param targetValue    : The target value of the progressbar
     * @return : The timeline of the animation
     */
    public static Timeline buildProgressBarAnimation(ProgressBar progressBar, double durationMillis, double targetValue) {
        return new Timeline(
                new KeyFrame(
                        Duration.millis(durationMillis),
                        new KeyValue(progressBar.progressProperty(), targetValue)
                )
        );
    }

    /**
     * Builds a shake animation for a given imageView
     *
     * @param imageView      : The imageView to be animated
     * @param durationMillis : The duration of the animation in milliseconds
     * @param shakeAngle     : The angle of the shake
     * @param cycleCount     : The number of repetitions
     * @return : The timeline of the animation
     */
    public static Timeline buildShakeAnimation(ImageView imageView, int durationMillis, int shakeAngle, int cycleCount) {
        Timeline timeline = new Timeline();
        KeyFrame keyFrame1 = new KeyFrame(Duration.ZERO, new KeyValue(imageView.rotateProperty(), 0));
        KeyFrame keyFrame2 = new KeyFrame(Duration.millis(durationMillis), new KeyValue(imageView.rotateProperty(), shakeAngle));
        KeyFrame keyFrame3 = new KeyFrame(Duration.millis(durationMillis).multiply(2), new KeyValue(imageView.rotateProperty(), -shakeAngle));
        KeyFrame keyFrame4 = new KeyFrame(Duration.millis(durationMillis).multiply(3), new KeyValue(imageView.rotateProperty(), shakeAngle));
        KeyFrame keyFrame5 = new KeyFrame(Duration.millis(durationMillis).multiply(4), new KeyValue(imageView.rotateProperty(), 0));

        timeline.getKeyFrames().addAll(keyFrame1, keyFrame2, keyFrame3, keyFrame4, keyFrame5);
        timeline.setCycleCount(cycleCount);
        return timeline;
    }

    public static Timeline buildTrainerWalkAnimation(Image trainerChunk, ImageView target, int durationMillis, int cycleCount, int direction) {
        var ref = new Object() {
            int currentImageIndex = 0;
        };

        Image[] images = ImageProcessor.cropTrainerImages(trainerChunk, direction, true);

        KeyFrame animationFrame = new KeyFrame(Duration.millis(durationMillis), event -> {
            //ownTrainerController.setTrainerImage(images[ref.currentImageIndex]);
            target.setImage(images[ref.currentImageIndex]);
            ref.currentImageIndex = (ref.currentImageIndex + 1) % 6;
        });

        Timeline trainerWalkAnimation = new Timeline(animationFrame);
        trainerWalkAnimation.setCycleCount(cycleCount);
        return trainerWalkAnimation;
    }


    public static void throwMonBall(StackPane root, ImageView source, ImageView target) {
        // Setup image and imageView for monball
        ImageView ballImageView = new ImageView(new Image(Objects.requireNonNull(Main.class.getResource("images/monball.png")).toExternalForm()));
        ballImageView.setFitWidth(64);
        ballImageView.setFitHeight(64);
        ballImageView.setVisible(false);

        // Init transitions
        TranslateTransition initialTransition = new TranslateTransition(Duration.millis(10), ballImageView);
        TranslateTransition throwTransition = new TranslateTransition(Duration.millis(300), ballImageView);
        ScaleTransition growthTransition = new ScaleTransition(Duration.millis(200), ballImageView);
        ScaleTransition shrinkTransition = new ScaleTransition(Duration.millis(200), ballImageView);
        SequentialTransition sequentialScaleTransition = new SequentialTransition(growthTransition, shrinkTransition);
        RotateTransition rotateTransition = new RotateTransition(Duration.millis(400), ballImageView);
        ParallelTransition parallelTransition = new ParallelTransition(sequentialScaleTransition, rotateTransition);
        PauseTransition pauseTransition = new PauseTransition(Duration.millis(50));
        TranslateTransition monballFallTransition = new TranslateTransition(Duration.millis(100), ballImageView);

        // Move monball imageView to own trainer, then start throw ball animation
        initialTransition.setToY(source.layoutYProperty().intValue());
        root.getChildren().add(ballImageView);

        // Setup other animations
        ballImageView.setVisible(true);
        ballImageView.setEffect(new Glow(0.8));
        throwTransition.setToY(target.layoutYProperty().get() + target.layoutYProperty().intValue() - source.layoutYProperty().intValue() - 50);

        growthTransition.setByX(1.5);
        growthTransition.setByY(1.5);

        shrinkTransition.setToX(1.0);
        shrinkTransition.setToY(1.0);

        rotateTransition.setByAngle(360);

        monballFallTransition.setByY(25);

        // Set callbacks on finished
        initialTransition.setOnFinished(evt  -> throwTransition.play());
        throwTransition.setOnFinished(evt    -> parallelTransition.play());
        growthTransition.setOnFinished(evt   -> target.setVisible(false));
        shrinkTransition.setOnFinished(evt   -> ballImageView.setEffect(null));
        parallelTransition.setOnFinished(evt -> pauseTransition.play());
        pauseTransition.setOnFinished(evt    -> monballFallTransition.play());

        // Start callback chain
        initialTransition.play();
    }
}
