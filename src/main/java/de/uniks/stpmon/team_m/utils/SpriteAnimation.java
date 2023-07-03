package de.uniks.stpmon.team_m.utils;

import de.uniks.stpmon.team_m.controller.subController.TrainerController;
import de.uniks.stpmon.team_m.dto.Trainer;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import static de.uniks.stpmon.team_m.Constants.*;


public class SpriteAnimation extends AnimationTimer {
    private final TrainerController trainerController;
    private final GraphicsContext alternativeGraphicsContext;
    private final GraphicsContext graphicsContext;

    private final Image spriteChunk;
    private final Trainer trainer;
    public Image currentImage;
    private Image[] images;
    public boolean isPlaying;
    private long duration;
    private Long lastPlayedTimeStamp;
    private int currentIndex = 0;

    private Image[] trainerStandingUp;
    private Image[] trainerStandingDown;
    private Image[] trainerStandingLeft;
    private Image[] trainerStandingRight;
    private Image[] trainerWalkingUp;
    private Image[] trainerWalkingDown;
    private Image[] trainerWalkingLeft;
    private Image[] trainerWalkingRight;

    public SpriteAnimation(TrainerController trainerController, Image spriteChunk, Trainer trainer, long duration, GraphicsContext graphicsContext, GraphicsContext alternativeGraphicsContext) {
        super();
        this.trainerController = trainerController;
        this.spriteChunk = spriteChunk;
        this.trainer = trainer;
        this.duration = duration;
        this.graphicsContext = graphicsContext;
        this.alternativeGraphicsContext = alternativeGraphicsContext;
        init();
    }

    private void init() {
        trainerStandingRight = ImageProcessor.cropTrainerImages(spriteChunk, TRAINER_DIRECTION_RIGHT, false);
        trainerWalkingRight = ImageProcessor.cropTrainerImages(spriteChunk, TRAINER_DIRECTION_RIGHT, true);
        trainerStandingUp = ImageProcessor.cropTrainerImages(spriteChunk, TRAINER_DIRECTION_UP, false);
        trainerWalkingUp = ImageProcessor.cropTrainerImages(spriteChunk, TRAINER_DIRECTION_UP, true);
        trainerStandingLeft = ImageProcessor.cropTrainerImages(spriteChunk, TRAINER_DIRECTION_LEFT, false);
        trainerWalkingLeft = ImageProcessor.cropTrainerImages(spriteChunk, TRAINER_DIRECTION_LEFT, true);
        trainerStandingDown = ImageProcessor.cropTrainerImages(spriteChunk, TRAINER_DIRECTION_RIGHT, false);
        trainerWalkingDown = ImageProcessor.cropTrainerImages(spriteChunk, TRAINER_DIRECTION_RIGHT, true);
        images = trainerWalkingDown;
        currentImage = images[0];
    }

    @Override
    public void handle(long now) {
        if (lastPlayedTimeStamp == null) {
            lastPlayedTimeStamp = System.currentTimeMillis();
        }

        drawTrainer();

        if (System.currentTimeMillis() - lastPlayedTimeStamp < duration + 50) {
            return;
        }
        lastPlayedTimeStamp = System.currentTimeMillis();
        currentIndex = (currentIndex + 1) % 6;
        currentImage = images[currentIndex];
    }

    private void setImages(Image[] images) {
        this.images = images;
    }

    private void setDuration(long duration) {
        this.duration = duration;
    }

    public void walk(int direction) {
        setupAnimation(direction, DELAY, trainerWalkingUp, trainerWalkingRight, trainerWalkingDown, trainerWalkingLeft);
    }

    public void stay(int direction) {
        setupAnimation(direction, DELAY_LONG, trainerStandingUp, trainerStandingRight, trainerStandingDown, trainerStandingLeft);
    }


    /**
     * Sets the images and the duration of the animation
     *
     * @param direction The direction the trainer
     * @param delay     The duration of the animation
     */
    private void setupAnimation(int direction, int delay, Image[] trainerWalkingUp, Image[] trainerWalkingRight, Image[] trainerWalkingDown, Image[] trainerWalkingLeft) {
        setDuration(delay);
        switch (direction) {
            case TRAINER_DIRECTION_UP       -> setImages(trainerWalkingUp);
            case TRAINER_DIRECTION_RIGHT    -> setImages(trainerWalkingRight);
            case TRAINER_DIRECTION_LEFT     -> setImages(trainerWalkingLeft);
            default                         -> setImages(trainerWalkingDown);
        }
    }

    /**
     * Draws the trainer on the canvas at its current position.
     */
    private void drawTrainer() {
        if (trainerController != null) {
            int y = trainerController.getUserTrainerY();
            GraphicsContext drawingContext;
            if (trainerController.getTrainerY() / TILE_SIZE > y && alternativeGraphicsContext != null) {
                graphicsContext.clearRect(trainerController.getTrainerX(), trainerController.getTrainerY(), 16, 27);
                drawingContext = alternativeGraphicsContext;
            } else {
                if (alternativeGraphicsContext != null) {
                    alternativeGraphicsContext.clearRect(trainerController.getTrainerX(), trainerController.getTrainerY(), 16, 27);
                }
                drawingContext = graphicsContext;
            }
            drawingContext.clearRect(trainerController.getTrainerX(), trainerController.getTrainerY(), 16, 27);
            trainerController.walk();
            drawingContext.drawImage(images[currentIndex], trainerController.getTrainerX(), trainerController.getTrainerY(), 16, 27);
        }
    }

    @Override
    public void start() {
        super.start();
        this.isPlaying = true;
    }

    @Override
    public void stop() {
        super.stop();
        this.isPlaying = false;
    }

    public Trainer getTrainer() {
        return trainer;
    }
}
