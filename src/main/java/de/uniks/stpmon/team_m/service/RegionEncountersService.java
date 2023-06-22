package de.uniks.stpmon.team_m.service;

import de.uniks.stpmon.team_m.dto.Encounter;
import de.uniks.stpmon.team_m.rest.RegionEncountersApiService;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import java.util.List;

public class RegionEncountersService {
    private final RegionEncountersApiService regionEncountersApiService;

    /**
     * RegionEncountersService handles the communication with the backend for the encounters.
     */
    @Inject
    public RegionEncountersService(RegionEncountersApiService regionEncountersApiService) {
        this.regionEncountersApiService = regionEncountersApiService;
    }

    /**
     * getMonsters returns all encounters of a region.
     *
     * @param regionId  The region of the encounters.
     * @return A list of encounters of the region.
     */
    public Observable<List<Encounter>> getEncounters(String regionId) {
        return regionEncountersApiService.getEncounters(regionId);
    }

    /**
     * getMonster returns a specific encounter of a region.
     *
     * @param regionId  The region of the encounter.
     * @param encounterId The trainer of the monster.
     * @return The encounter.
     */
    public Observable<Encounter> getEncounter(String regionId, String encounterId) {
        return regionEncountersApiService.getEncounter(regionId, encounterId);
    }

}
