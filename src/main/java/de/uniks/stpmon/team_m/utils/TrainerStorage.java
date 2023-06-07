package de.uniks.stpmon.team_m.utils;

import de.uniks.stpmon.team_m.dto.Region;
import de.uniks.stpmon.team_m.dto.Trainer;
import javafx.scene.image.Image;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class TrainerStorage {

    private Region region;
    private Trainer trainer;
    private String trainerName;
    private String trainerImageUrl;
    private Image trainerSpriteChunk;

    @Inject
    public TrainerStorage() {
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public void setTrainerName(String name) { this.trainerName = name; }

    public void setTrainerSprite(String url) { this.trainerImageUrl = url; }

    public Trainer getTrainer() {
        return trainer;
    }

    public Region getRegion() {
        return region;
    }

    public String getTrainerName() {
        return this.trainerName;
    }

    public String getTrainerSprite() {
        return this.trainerImageUrl;
    }

    public Image getTrainerSpriteChunk() {
        return trainerSpriteChunk;
    }

    public void setTrainerSpriteChunk(Image trainerSpriteChunk) {
        this.trainerSpriteChunk = trainerSpriteChunk;
    }
}
