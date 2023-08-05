package de.uniks.stpmon.team_m.service;
import de.uniks.stpmon.team_m.Constants;
import de.uniks.stpmon.team_m.Main;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.inject.Inject;
import java.util.Objects;
import java.util.prefs.Preferences;

import static de.uniks.stpmon.team_m.Constants.SoundEffect.WALKING;

public class AudioService {
    private static AudioService instance;
    private MediaPlayer mediaPlayer;
    private boolean isMuted = false;
    private String currentSound;
    private double soundVolume;
    private MediaPlayer effectPlayer;

    @Inject
    public AudioService() {}

    public static AudioService getInstance() {
        if (instance == null) {
            instance = new AudioService();
        }
        return instance;
    }

    public void playSound(String soundPath) {
        final Media sound = new Media((Objects.requireNonNull(Main.class.getResource("sounds/" + soundPath))).toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
        this.currentSound = soundPath;

    }

    public void playEffect(Constants.SoundEffect effect, Preferences preferences) {
        final Media sound = new Media((Objects.requireNonNull(Main.class.getResource("sounds/" + effect + ".mp3"))).toString());
        effectPlayer = new MediaPlayer(sound);
        if(effect == WALKING) {
            effectPlayer.setRate(1.5);
        } else {
            effectPlayer.setRate(1);
        }
        effectPlayer.setVolume(preferences.getDouble("volume", 0.5));
        effectPlayer.play();
    }

    public void stopEffect() {
        if (effectPlayer != null) {
            effectPlayer.stop();
        }
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
    public void setCurrentSound(String sound) {
        this.currentSound = sound;
    }

    public double getVolume() {
        return this.soundVolume;
    }

    public void setVolume(double volume) {
        this.soundVolume = volume;
        mediaPlayer.setVolume(volume);
    }

    public void muteOrUnmuteSound(Button muteButton, Preferences preferences) {
        if(AudioService.getInstance().checkMuted()) {
            muteButton.getStyleClass().remove("unmuteSymbol");
            muteButton.getStyleClass().add("muteSymbol");
            preferences.putBoolean("mute", false);
            AudioService.getInstance().unmuteSound();
            if (preferences.getDouble("volume", 0.5) <= 0.05) {
                AudioService.getInstance().setVolume(0.5);
                preferences.putDouble("volume", 0.5);
            } else {
                AudioService.getInstance().setVolume(preferences.getDouble("volume", 0.5));
            }
        } else {
            muteButton.getStyleClass().remove("muteSymbol");
            muteButton.getStyleClass().add("unmuteSymbol");
            preferences.putBoolean("mute", true);
            AudioService.getInstance().muteSound();
        }
    }
}
