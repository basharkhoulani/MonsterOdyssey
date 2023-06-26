package de.uniks.stpmon.team_m.service;
import de.uniks.stpmon.team_m.Main;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.inject.Inject;

public class AudioService {
    private static AudioService instance;
    private MediaPlayer mediaPlayer;
    private boolean isMuted = false;
    private String currentSound;

    @Inject
    public AudioService() {}

    public static AudioService getInstance() {
        if (instance == null) {
            instance = new AudioService();
        }
        return instance;
    }

    public void playSound(String soundPath) {
        final Media sound = new Media((Main.class.getResource("sounds/" + soundPath)).toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
        this.currentSound = soundPath;

    }

    public void stopSound() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public void muteSound() {
        this.isMuted = true;
        mediaPlayer.setMute(true);
    }

    public boolean checkMuted() {
        return this.isMuted;
    }

    public void unmuteSound() {
        this.isMuted = false;
        mediaPlayer.setMute(false);
    }

    public String getCurrentSound() {
        return this.currentSound;
    }

    public void pauseSound() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
        }
    }

    public double getVolume() {
        return mediaPlayer.getVolume();
    }

    public void setVolume(double volume) {
        mediaPlayer.setVolume(volume);
    }
}
