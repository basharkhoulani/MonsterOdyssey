package de.uniks.stpmon.team_m.utils;

import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.dto.MonsterTypeDto;
import de.uniks.stpmon.team_m.dto.Region;
import de.uniks.stpmon.team_m.dto.Trainer;
import javafx.scene.image.Image;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;


@Singleton
public class TrainerStorage {

    private Region region;
    private String trainerName;
    private int x;
    private int y;
    private int direction;
    private Trainer trainer;
    private String trainerImageUrl;
    private Image trainerSpriteChunk;
    private List<Monster> monsters;
    private List<MonsterTypeDto> monsterTypes;

    @Inject
    public TrainerStorage() {
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }

    public void setTrainerSprite(String url) {
        this.trainerImageUrl = url;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
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

    public List<Monster> getMonsters() {
        return monsters;
    }

    public void setMonsters(List<Monster> monsters) {
        this.monsters = monsters;
    }
}
