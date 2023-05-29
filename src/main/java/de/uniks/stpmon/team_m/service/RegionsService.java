package de.uniks.stpmon.team_m.service;

import de.uniks.stpmon.team_m.dto.Region;
import de.uniks.stpmon.team_m.rest.RegionsApiService;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import java.util.List;

public class RegionsService {
    private final RegionsApiService regionsApiService;


    /**
     * RegionsService handles the communication with the backend for the regions.
     */

    @Inject
    public RegionsService(RegionsApiService regionsApiService) {
        this.regionsApiService = regionsApiService;
    }

    /**
     * getRegions returns all regions.
     *
     * @return A list of regions.
     */

    public Observable<List<Region>> getRegions() {
        return regionsApiService.getRegions();
    }

    /**
     * getRegion returns a specific region.
     *
     * @param id The id of the region.
     * @return The region.
     */

    public Observable<Region> getRegion(String id) {
        return regionsApiService.getRegion(id);
    }

}
