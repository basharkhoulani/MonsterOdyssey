package de.uniks.stpmon.team_m.utils;

import de.uniks.stpmon.team_m.dto.Trainer;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.awt.*;

import static de.uniks.stpmon.team_m.Constants.TILE_SIZE;


public class SpriteAnimation extends AnimationTimer {
    private static final int DELAY = 100;
    private static final int DELAY_LONG = 500;

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

    public SpriteAnimation(Image spriteChunk, Trainer trainer, long duration, GraphicsContext graphicsContext) { //ImageView root) {
        super();
        this.spriteChunk = spriteChunk;
        this.trainer = trainer;
        this.duration = duration;
        this.graphicsContext = graphicsContext;
        init();
    }

    private void init() {
        trainerStandingDown = ImageProcessor.cropTrainerImages(spriteChunk, 2, false);
        trainerStandingUp = ImageProcessor.cropTrainerImages(spriteChunk, 0, false);
        trainerStandingLeft = ImageProcessor.cropTrainerImages(spriteChunk, 3, false);
        trainerStandingRight = ImageProcessor.cropTrainerImages(spriteChunk, 1, false);
        trainerWalkingUp = ImageProcessor.cropTrainerImages(spriteChunk, 0, true);
        trainerWalkingDown = ImageProcessor.cropTrainerImages(spriteChunk, 2, true);
        trainerWalkingLeft = ImageProcessor.cropTrainerImages(spriteChunk, 3, true);
        trainerWalkingRight = ImageProcessor.cropTrainerImages(spriteChunk, 1, true);
        images = trainerWalkingDown;
        currentPosition = new Position(trainer.x(), trainer.y(), trainer.direction());
        currentImage = images[0];

    }

    @Override
    public void handle(long now) {
        if (lastPlayedTimeStamp == null) {
            lastPlayedTimeStamp = System.currentTimeMillis();
        }
        if (System.currentTimeMillis() - lastPlayedTimeStamp < duration) {
            return;
        }
        lastPlayedTimeStamp = System.currentTimeMillis();
        if (lastPosition != null) {
            graphicsContext.clearRect(lastPosition.getX() * TILE_SIZE, lastPosition.getY() * TILE_SIZE, 16,  25);
        }
        graphicsContext.clearRect(currentPosition.getX() * TILE_SIZE, currentPosition.getY() * TILE_SIZE, 16,  25);
        graphicsContext.drawImage(images[currentIndex], currentPosition.getX() * TILE_SIZE, currentPosition.getY() * TILE_SIZE, 16,  25);
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
        setupWalkAnimation(direction, DELAY, trainerWalkingUp, trainerWalkingRight, trainerWalkingDown, trainerWalkingLeft);
    }

    private void setupWalkAnimation(int direction, int delay, Image[] trainerWalkingUp, Image[] trainerWalkingRight, Image[] trainerWalkingDown, Image[] trainerWalkingLeft) {
        if (!GraphicsEnvironment.isHeadless()) {
            setDuration(delay);
            switch (direction) {
                case 0 -> setImages(trainerWalkingUp);
                case 1 -> setImages(trainerWalkingRight);
                case 2 -> setImages(trainerWalkingDown);
                case 3 -> setImages(trainerWalkingLeft);
                default -> {}
            }
        }
    }

    public void stay(int direction) {
        setupWalkAnimation(direction, DELAY_LONG, trainerStandingUp, trainerStandingRight, trainerStandingDown, trainerStandingLeft);
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
