package de.uniks.stpmon.team_m.utils;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SpriteAnimation extends AnimationTimer {
    private ImageView root;
    private Image[] images;
    public boolean isPlaying;
    private long duration;
    private Long lastPlayedTimeStamp;
    private int currentIndex = 0;

    public SpriteAnimation(long duration, ImageView root, Image[] images) {
        super();
        this.duration = duration;
        this.root = root;
        this.images = images;
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
        root.setImage(images[currentIndex]);
        currentIndex = (currentIndex + 1) % 6;
    }

    public void setImages(Image[] images) {
        this.images = images;
    }

    public void setDuration(long duration) {
        this.duration = duration;
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
