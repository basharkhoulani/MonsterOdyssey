package de.uniks.stpmon.team_m.utils;

import de.uniks.stpmon.team_m.dto.MoveTrainerDto;
import de.uniks.stpmon.team_m.dto.Trainer;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class TrainerStorage {

    private String regionId;
    private String trainerName;
    private String trainerSprite;
    private Trainer trainer;
    private MoveTrainerDto moveTrainerDto;

    @Inject
    public TrainerStorage() {
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
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

    public String getRegionId() {
        return regionId;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public String getTrainerSprite() {
        return trainerSprite;
    }

    public MoveTrainerDto getMoveTrainerDto() {
        return moveTrainerDto;
    }

    public void setMoveTrainerDto(MoveTrainerDto moveTrainerDto) {
        this.moveTrainerDto = moveTrainerDto;
    }
}
