package de.uniks.stpmon.team_m.utils;

import de.uniks.stpmon.team_m.dto.Region;
import de.uniks.stpmon.team_m.dto.Trainer;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class TrainerStorage {
    private String trainerName;
    private String trainerSprite;
    private int x;
    private int y;
    private int direction;
    private Trainer trainer;
    private Region region;

    @Inject
    public TrainerStorage() {
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }

    public void setTrainerSprite(String trainerSprite) {
        this.trainerSprite = trainerSprite;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public String getTrainerSprite() {
        return trainerSprite;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Region getRegion() {
        return region;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
