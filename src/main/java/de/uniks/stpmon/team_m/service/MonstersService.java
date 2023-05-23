package de.uniks.stpmon.team_m.service;

import de.uniks.stpmon.team_m.dto.Monster;
import de.uniks.stpmon.team_m.rest.MonstersApiService;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import java.util.List;

public class MonstersService {
    private final MonstersApiService monstersApiService;

    @Inject
    public MonstersService(MonstersApiService monstersApiService) {
        this.monstersApiService = monstersApiService;
    }

    public Observable<List<Monster>> getMonsters(String regionId, String trainerId) {
        return monstersApiService.getMonsters(regionId, trainerId);
    }

    public Observable<Monster> getMonster(String regionId, String trainerId, String monsterId) {
        return monstersApiService.getMonster(regionId, trainerId, monsterId);
    }
}
