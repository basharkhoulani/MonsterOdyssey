package de.uniks.stpmon.team_m.utils;

import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.dto.MonsterTypeDto;
import de.uniks.stpmon.team_m.dto.Opponent;
import javafx.collections.FXCollections;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Singleton
public class EncounterOpponentStorage {
    private String encounterId;
    private final List<String> opponentsInStorage = new ArrayList<>();
    private Opponent selfOpponent;
    // Die Opponent mit selben trainerId
    private List<Opponent> enemyOpponents = FXCollections.observableArrayList();
    // Die Opponent mit Id der Gegner
    private Opponent coopOpponent;
    // Die Opponent mit Id des Coop-Partners
    private HashMap<String, Monster> currentMonsters = new HashMap<>();
    private HashMap<String, MonsterTypeDto> currentMonsterTypes = new HashMap<>();
    private String regionId;
    private boolean isWild;
    private int encounterSize;
    private boolean isAttacker;
    private boolean isTwoMonster = false;
    private Opponent targetOpponent;
    private Opponent leastTargetOpponent;


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

    public List<Opponent> getEnemyOpponents() {
        return enemyOpponents;
    }

    public void resetEnemyOpponents() {
        this.enemyOpponents.clear();
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

    public void addCurrentMonsters(String opponentId, Monster monster) {
        this.currentMonsters.put(opponentId, monster);
    }

    public Monster getCurrentMonsters(String opponentId) {
        return this.currentMonsters.get(opponentId);
    }

    public MonsterTypeDto getCurrentMonsterType(String opponentId) {
        return this.currentMonsterTypes.get(opponentId);
    }

    public void addCurrentMonsterType(String opponentId, MonsterTypeDto monsterType) {
        this.currentMonsterTypes.put(opponentId, monsterType);
    }

    public List<String> getOpponentsInStorage() {
        return opponentsInStorage;
    }

    public void setOpponentsInStorage(List<Opponent> opponents) {
        this.opponentsInStorage.clear();
        for (Opponent opponent : opponents) {
            this.opponentsInStorage.add(opponent._id());
        }

    }

    public void setTargetOpponent(Opponent opponent) {
        this.leastTargetOpponent = targetOpponent;
        this.targetOpponent = opponent;
    }

    public Opponent getTargetOpponent() {
        return this.targetOpponent;
    }

    public Opponent getLeastTargetOpponent() {
        return this.leastTargetOpponent;
    }

    public boolean isTwoMonster() {
        return isTwoMonster;
    }

    public void setTwoMonster(boolean twoMonster) {
        isTwoMonster = twoMonster;
    }
}
