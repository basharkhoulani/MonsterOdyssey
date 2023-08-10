package de.uniks.stpmon.team_m.utils;

import de.uniks.stpmon.team_m.Constants;
import de.uniks.stpmon.team_m.Constants.BallType;
import de.uniks.stpmon.team_m.controller.subController.EncounterOpponentController;
import javafx.animation.*;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

import static de.uniks.stpmon.team_m.Constants.BallType.*;
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

    public static ImageView throwMonBall(BallType type, StackPane root, ImageView source, ImageView target, int ticks, Runnable onFinished) {
        String imageUrl = "ball_";
        if (type == NORMAL)        { imageUrl += "normal"; }
        else if (type == SUPER)    { imageUrl += "super";  }
        else if (type == HYPER)    { imageUrl += "hyper";  }
        else if (type == MASTER)   { imageUrl += "master"; }
        else if (type == WATER)    { imageUrl += "water";  }
        else if (type == NET)      { imageUrl += "net";    }
        else                       { imageUrl += "heal";   }
        imageUrl += ".png";

        // Setup image and imageView for monball
        Image image = ImageProcessor.showScaledItemImage(imageUrl);
        ImageView ballImageView = new ImageView(image);
        ballImageView.setFitWidth(50);
        ballImageView.setFitHeight(50);
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

        List<PauseTransition> pauseTransitions = new ArrayList<>();
        List<Timeline> shakeTransitions = new ArrayList<>();
        for (int i = 0; i < ticks; i++) {
            PauseTransition pause = new PauseTransition(Duration.millis(1000));
            Timeline shakeTransition = buildShakeAnimation(ballImageView, 100, 5, 1);
            pause.setOnFinished(evt -> shakeTransition.play());
            pauseTransitions.add(pause);
            shakeTransitions.add(shakeTransition);
        }
        for (int i = 0; i < ticks; i++) {
            int finalI = i;
            if (finalI + 1 == ticks) {
                shakeTransitions.get(i).setOnFinished(evt -> onFinished.run());
            }
            else {
                shakeTransitions.get(i).setOnFinished(evt -> pauseTransitions.get(finalI +1).play());
            }
        }
        // Move monball imageView to own trainer, then start throw ball animation
        initialTransition.setToY(source.layoutYProperty().intValue());
        initialTransition.setToX(source.layoutXProperty().intValue() - 50);
        root.getChildren().add(ballImageView);

        // Setup other animations
        ballImageView.setVisible(true);
        ballImageView.setEffect(new Glow(0.8));
        throwTransition.setToX(target.layoutXProperty().get() + target.layoutXProperty().intValue() - source.layoutXProperty().intValue() + 50);
        throwTransition.setToY(target.layoutYProperty().get() + target.layoutYProperty().intValue() - source.layoutYProperty().intValue() - 50);

        growthTransition.setByX(1.5);
        growthTransition.setByY(1.5);

        shrinkTransition.setToX(1.0);
        shrinkTransition.setToY(1.0);

        rotateTransition.setByAngle(360);

        monballFallTransition.setByY(50);


        // Set callbacks on finished
        initialTransition.setOnFinished(evt     -> throwTransition.play());
        throwTransition.setOnFinished(evt       -> parallelTransition.play());
        growthTransition.setOnFinished(evt      -> target.setVisible(false));
        shrinkTransition.setOnFinished(evt      -> ballImageView.setEffect(null));
        parallelTransition.setOnFinished(evt    -> pauseTransition.play());
        pauseTransition.setOnFinished(evt       -> monballFallTransition.play());
        monballFallTransition.setOnFinished(evt -> pauseTransitions.get(0).play());

        // Start callback chain
        initialTransition.play();

        return ballImageView;
    }
}
