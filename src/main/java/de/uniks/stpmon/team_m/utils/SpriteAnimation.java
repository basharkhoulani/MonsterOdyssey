package de.uniks.stpmon.team_m.utils;

import de.uniks.stpmon.team_m.controller.subController.TrainerController;
import de.uniks.stpmon.team_m.dto.Trainer;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.awt.*;

import static de.uniks.stpmon.team_m.Constants.TILE_SIZE;


public class SpriteAnimation extends AnimationTimer {
    private static final int DELAY = 100;
    private static final int DELAY_LONG = 500;
    private final TrainerController trainerController;

    private Position currentPosition;
    private Position lastPosition;
    private final Image spriteChunk;
    private final Trainer trainer;
    public Image currentImage;
    private Image[] images;
    public boolean isPlaying;
    private long duration;
    private Long lastPlayedTimeStamp;
    private int currentIndex = 0;
    GraphicsContext graphicsContext;

    private Image[] trainerStandingUp;
    private Image[] trainerStandingDown;
    private Image[] trainerStandingLeft;
    private Image[] trainerStandingRight;
    private Image[] trainerWalkingUp;
    private Image[] trainerWalkingDown;
    private Image[] trainerWalkingLeft;
    private Image[] trainerWalkingRight;
    private boolean isWalking;
    public SpriteAnimation(TrainerController trainerController, Image spriteChunk, Trainer trainer, long duration, GraphicsContext graphicsContext) { //ImageView root) {
        super();
        this.trainerController = trainerController;
        this.spriteChunk = spriteChunk;
        this.trainer = trainer;
        this.duration = duration;
        this.graphicsContext = graphicsContext;
        init();
    }

    private void init() {
        // right up left down
        trainerStandingRight = ImageProcessor.cropTrainerImages(spriteChunk, 0, false);
        trainerWalkingRight = ImageProcessor.cropTrainerImages(spriteChunk, 0, true);
        trainerStandingUp = ImageProcessor.cropTrainerImages(spriteChunk, 1, false);
        trainerWalkingUp = ImageProcessor.cropTrainerImages(spriteChunk, 1, true);
        trainerStandingLeft = ImageProcessor.cropTrainerImages(spriteChunk, 2, false);
        trainerWalkingLeft = ImageProcessor.cropTrainerImages(spriteChunk, 2, true);
        trainerStandingDown = ImageProcessor.cropTrainerImages(spriteChunk, 3, false);
        trainerWalkingDown = ImageProcessor.cropTrainerImages(spriteChunk, 3, true);
        images = trainerWalkingDown;
        currentPosition = new Position(trainer.x(), trainer.y(), trainer.direction());
        currentImage = images[0];

    }

    @Override
    public void handle(long now) {
        if (lastPlayedTimeStamp == null) {
            lastPlayedTimeStamp = System.currentTimeMillis();
        }
        if (lastPosition != null) {
            graphicsContext.clearRect(lastPosition.getX() * TILE_SIZE, lastPosition.getY() * TILE_SIZE, 16,  25);
        }
        if (trainerController != null) {
            graphicsContext.clearRect(trainerController.getTrainerX(), trainerController.getTrainerY(), 16,  25);
            trainerController.walk();
            graphicsContext.drawImage(images[currentIndex], trainerController.getTrainerX(), trainerController.getTrainerY(), 16,  25);
            if (trainer.name().equals("Adam")) {
                System.out.println("X: " + trainerController.getTrainerX() + " Y: " + trainerController.getTrainerY());
            }
        }
        else {
            //graphicsContext.clearRect(currentPosition.getX() * TILE_SIZE, currentPosition.getY() * TILE_SIZE, 16,  25);
            //graphicsContext.drawImage(images[currentIndex], currentPosition.getX() * TILE_SIZE, currentPosition.getY() * TILE_SIZE, 16,  25);
            if (isWalking && currentIndex == 0) {
                isWalking = false;
                stay(currentPosition.getDirection());
            }
        }
        if (System.currentTimeMillis() - lastPlayedTimeStamp < duration) {
            return;
        }
        lastPlayedTimeStamp = System.currentTimeMillis();
        currentIndex = (currentIndex + 1) % 6;
        currentImage = images[currentIndex];
    }

    private void setImages(Image[] images) {
        if (isWalking) {
            return;
        }
        this.images = images;
    }

    private void setDuration(long duration) {
        this.duration = duration;
    }

    public void walk(int direction) {
        setupAnimation(direction, DELAY, trainerWalkingUp, trainerWalkingRight, trainerWalkingDown, trainerWalkingLeft);
        isWalking = true;
    }

    private void setupAnimation(int direction, int delay, Image[] trainerWalkingUp, Image[] trainerWalkingRight, Image[] trainerWalkingDown, Image[] trainerWalkingLeft) {
        if (!GraphicsEnvironment.isHeadless()) {
            setDuration(delay);
            switch (direction) {
                case 1 -> setImages(trainerWalkingUp);
                case 0 -> setImages(trainerWalkingRight);
                case 2 -> setImages(trainerWalkingLeft);
                case 3 -> setImages(trainerWalkingDown);
                default -> {}
            }
        }
    }

    public void stay(int direction) {
        isWalking = false;
        setupAnimation(direction, DELAY_LONG, trainerStandingUp, trainerStandingRight, trainerStandingDown, trainerStandingLeft);
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

    public Position getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Position currentPosition) {
        this.lastPosition = this.currentPosition;
        this.currentPosition = currentPosition;
    }
}
