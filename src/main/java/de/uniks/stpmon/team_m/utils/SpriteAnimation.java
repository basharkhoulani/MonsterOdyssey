package de.uniks.stpmon.team_m.utils;

import de.uniks.stpmon.team_m.dto.Trainer;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.inject.Inject;
import javax.inject.Provider;
import java.awt.*;

import static de.uniks.stpmon.team_m.Constants.TILE_SIZE;


public class SpriteAnimation extends AnimationTimer {
    private static final int DELAY = 100;
    private static final int DELAY_LONG = 500;

    private final Image spriteChunk;
    private final Trainer trainer;
    private ImageView root;
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
        //this.root = root;
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
        graphicsContext.clearRect(trainer.x() * TILE_SIZE, trainer.y() * TILE_SIZE + 10, 16,  28);
        graphicsContext.drawImage(images[currentIndex], trainer.x() * TILE_SIZE, trainer.y() * TILE_SIZE + 10, 16,  28);
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
        if (!GraphicsEnvironment.isHeadless()) {
            setDuration(DELAY);
            switch (direction) {
                case 0 -> {
                    setImages(trainerWalkingUp);
                    //mapMovementTransition = getMapMovementTransition(canvas, 0, SCALE_FACTOR * TILE_SIZE, DELAY);
                }
                case 1 -> {
                    setImages(trainerWalkingRight);
                    //mapMovementTransition = getMapMovementTransition(canvas, -SCALE_FACTOR * TILE_SIZE, 0, DELAY);
                }
                case 2 -> {
                    setImages(trainerWalkingDown);
                    //mapMovementTransition = getMapMovementTransition(canvas, 0, -SCALE_FACTOR * TILE_SIZE, DELAY);
                }
                case 3 -> {
                    setImages(trainerWalkingLeft);
                    //mapMovementTransition = getMapMovementTransition(canvas, SCALE_FACTOR * TILE_SIZE, 0, DELAY);
                }
                default -> {

                }
            }
        }
    }

    public void stay(int direction) {
        if (!GraphicsEnvironment.isHeadless()) {
            setDuration(DELAY_LONG);
            switch (direction) {
                case 0 -> {
                    setImages(trainerStandingUp);
                    //mapMovementTransition = getMapMovementTransition(canvas, 0, SCALE_FACTOR * TILE_SIZE, DELAY);
                }
                case 1 -> {
                    setImages(trainerStandingRight);
                    //mapMovementTransition = getMapMovementTransition(canvas, -SCALE_FACTOR * TILE_SIZE, 0, DELAY);
                }
                case 2 -> {
                    setImages(trainerStandingDown);
                    //mapMovementTransition = getMapMovementTransition(canvas, 0, -SCALE_FACTOR * TILE_SIZE, DELAY);
                }
                case 3 -> {
                    setImages(trainerStandingLeft);
                    //mapMovementTransition = getMapMovementTransition(canvas, SCALE_FACTOR * TILE_SIZE, 0, DELAY);
                }
                default -> {

                }
            }
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
}
