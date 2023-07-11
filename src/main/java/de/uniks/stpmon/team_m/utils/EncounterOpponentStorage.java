package de.uniks.stpmon.team_m.utils;

import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.dto.MonsterTypeDto;
import de.uniks.stpmon.team_m.dto.Opponent;
import de.uniks.stpmon.team_m.dto.Trainer;
import javafx.collections.FXCollections;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class EncounterOpponentStorage {
    private String encounterId;
    private List<String> opponentsInStorage = new ArrayList<>();
    private Opponent selfOpponent;
    // Die Opponent mit selben trainerId
    private List<Opponent> enemyOpponents = FXCollections.observableArrayList();
    // Die Opponent mit Id der Gegner
    private Opponent coopOpponent;
    // Die Opponent mit Id des Coop-Partners
    private Monster currentTrainerMonster;
    private MonsterTypeDto currentTrainerMonsterType;
    private List<Monster> currentMonsters = FXCollections.observableArrayList();
    private List<MonsterTypeDto> currentMonsterTypes = FXCollections.observableArrayList();
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

    public MonsterTypeDto getCurrentTrainerMonsterType() {
        return currentTrainerMonsterType;
    }

    public void setCurrentTrainerMonsterType(MonsterTypeDto currentTrainerMonsterType) {
        this.currentTrainerMonsterType = currentTrainerMonsterType;
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

    public List<Monster> getCurrentMonsters() {
        return currentMonsters;
    }

    public void addCurrentMonster(Monster monster) {
        this.currentMonsters.add(monster);
    }

    public List<MonsterTypeDto> getCurrentMonsterTypes() {
        return currentMonsterTypes;
    }

    public void addCurrentMonsterType(MonsterTypeDto monsterType) {
        this.currentMonsterTypes.add(monsterType);
    }

    public List<String> getOpponentsInStorage() {
        return opponentsInStorage;
    }

    public void addOpponentsInStorage(String opponentsInStorage) {
        this.opponentsInStorage.add(opponentsInStorage);
    }
}
