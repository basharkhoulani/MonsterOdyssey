package de.uniks.stpmon.team_m.utils;

import de.uniks.stpmon.team_m.dto.Opponent;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class EncounterOpponentStorage {
    private String encounterId;
    private List<Opponent> opponentsInStorage;
    private String trainerId;
    private String opponentTrainerId;
    private String trainerMonsterId;
    private String opponentMonsterId;
    private String regionId;
    private boolean isWild;
    private int encounterSize;


    @Inject
    public EncounterOpponentStorage() { }

    public String getEncounterId() {
        return encounterId;
    }

    public void setEncounterId(String encounterId) {
        this.encounterId = encounterId;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }

    public String getTrainerMonsterId() {
        return trainerMonsterId;
    }

    public void setTrainerMonsterId(String trainerMonsterId) {
        this.trainerMonsterId = trainerMonsterId;
    }

    public String getOpponentMonsterId() {
        return opponentMonsterId;
    }

    public void setOpponentMonsterId(String opponentMonsterId) {
        this.opponentMonsterId = opponentMonsterId;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public boolean isWild() {
        return isWild;
    }

    public void setWild(boolean wild) {
        isWild = wild;
    }

    public int getEncounterSize() {
        return encounterSize;
    }

    public void setEncounterSize(int encounterSize) {
        this.encounterSize = encounterSize;
    }

    public List<Opponent> getOpponentsInStorage() {
        return opponentsInStorage;
    }

    public void setOpponentsInStorage(List<Opponent> opponentsInStorage) {
        this.opponentsInStorage = opponentsInStorage;
    }

    public String getOpponentTrainerId() {
        return opponentTrainerId;
    }

    public void setOpponentTrainerId(String opponentTrainerId) {
        this.opponentTrainerId = opponentTrainerId;
    }
}
