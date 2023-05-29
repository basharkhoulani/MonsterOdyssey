package de.uniks.stpmon.team_m.service;

import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.rest.MonstersApiService;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import java.util.List;

public class MonstersService {
    private final MonstersApiService monstersApiService;


    /**
     * MonstersService handles the communication with the backend for the monsters.
     */

    @Inject
    public MonstersService(MonstersApiService monstersApiService) {
        this.monstersApiService = monstersApiService;
    }

    /**
     * getMonsters returns all monsters of a region.
     *
     * @param regionId  The region of the monsters.
     * @param trainerId The trainer of the monsters.
     * @return A list of monsters of the region.
     */

    public Observable<List<Monster>> getMonsters(String regionId, String trainerId) {
        return monstersApiService.getMonsters(regionId, trainerId);
    }

    /**
     * getMonster returns a specific monster of a region.
     *
     * @param regionId  The region of the monster.
     * @param trainerId The trainer of the monster.
     * @param monsterId The id of the monster.
     * @return The monster.
     */

    public Observable<Monster> getMonster(String regionId, String trainerId, String monsterId) {
        return monstersApiService.getMonster(regionId, trainerId, monsterId);
    }
}
