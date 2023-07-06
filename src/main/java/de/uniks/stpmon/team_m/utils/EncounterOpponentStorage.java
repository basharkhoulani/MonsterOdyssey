package de.uniks.stpmon.team_m.utils;

import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.dto.MonsterTypeDto;
import de.uniks.stpmon.team_m.dto.Opponent;
import de.uniks.stpmon.team_m.dto.Trainer;
import javafx.collections.FXCollections;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class EncounterOpponentStorage {
    private String encounterId;
    private List<Opponent> opponentsInStorage;
    private Opponent selfOpponent;
    // Die Opponent mit selben trainerId
    private List<Opponent> enemyOpponents = FXCollections.observableArrayList();
    // Die Opponent mit Id der Gegner
    private Opponent coopOpponent;
    // Die Opponent mit Id des Coop-Partners
    private Trainer opponentTrainer;
    private Monster currentTrainerMonster;
    private MonsterTypeDto currentTrainerMonsterType;
    private Monster currentEnemyMonster;
    private MonsterTypeDto currentEnemyMonsterType;
    private String regionId;
    private boolean isWild;
    private int encounterSize;
    private boolean isAttacker;


    @Inject
    public EncounterOpponentStorage() { }

    public String getEncounterId() {
        return encounterId;
    }

    public void setEncounterId(String encounterId) {
        this.encounterId = encounterId;
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


    public void setOpponentTrainer(Trainer opponentTrainer) {
        this.opponentTrainer = opponentTrainer;
    }

    public Opponent getSelfOpponent() {
        return selfOpponent;
    }

    public void setSelfOpponent(Opponent selfOpponent) {
        this.selfOpponent = selfOpponent;
    }

    public Monster getCurrentTrainerMonster() {
        return currentTrainerMonster;
    }

    public void setCurrentTrainerMonster(Monster currentTrainerMonster) {
        this.currentTrainerMonster = currentTrainerMonster;
    }

    public Monster getCurrentEnemyMonster() {
        return currentEnemyMonster;
    }

    public void setCurrentEnemyMonster(Monster currentEnemyMonster) {
        this.currentEnemyMonster = currentEnemyMonster;
    }

    public MonsterTypeDto getCurrentTrainerMonsterType() {
        return currentTrainerMonsterType;
    }

    public void setCurrentTrainerMonsterType(MonsterTypeDto currentTrainerMonsterType) {
        this.currentTrainerMonsterType = currentTrainerMonsterType;
    }

    public MonsterTypeDto getCurrentEnemyMonsterType() {
        return currentEnemyMonsterType;
    }

    public void setCurrentEnemyMonsterType(MonsterTypeDto currentEnemyMonsterType) {
        this.currentEnemyMonsterType = currentEnemyMonsterType;
    }

    public List<Opponent> getEnemyOpponents() {
        return enemyOpponents;
    }

    public void addEnemyOpponent(Opponent enemyOpponent) {
        this.enemyOpponents.add(enemyOpponent);
    }

    public Opponent getCoopOpponent() {
        return coopOpponent;
    }

    public void setCoopOpponent(Opponent coopOpponent) {
        this.coopOpponent = coopOpponent;
    }

    public boolean isAttacker() {
        return isAttacker;
    }

    public void setAttacker(boolean attack) {
        isAttacker = attack;
    }
}
