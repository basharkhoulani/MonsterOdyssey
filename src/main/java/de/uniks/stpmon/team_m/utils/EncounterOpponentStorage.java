package de.uniks.stpmon.team_m.utils;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class EncounterOpponentStorage {
    private String encounterId;
    private String trainerId;
    private String opponentId;
    private String trainerMonsterId;
    private String opponentMonsterId;
    private String regionId;


    @Inject
    public EncounterOpponentStorage() {

    }

    public String getEncounterId() {
        return encounterId;
    }

    public void setEncounterId(String encounterId) {
        this.encounterId = encounterId;
    }

    // Get the Id of self own trainer
    public String getTrainerId() {
        return trainerId;
    }

    //Set the Id of self own trainer
    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }

    // Get the Id of oppenent
    public String getOpponentId() {
        return opponentId;
    }

    // Set the Id of Opponent
    public void setOpponentId(String opponentId) {
        this.opponentId = opponentId;
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
}
