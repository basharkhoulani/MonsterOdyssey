package de.uniks.stpmon.team_m.service;

import de.uniks.stpmon.team_m.dto.Region;
import de.uniks.stpmon.team_m.rest.RegionsApiService;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import java.util.List;

public class RegionsService {
    private final RegionsApiService regionsApiService;

    @Inject
    public RegionsService(RegionsApiService regionsApiService) {
        this.regionsApiService = regionsApiService;
    }

    public Observable<List<Region>> getRegions() {
        return regionsApiService.getRegions();
    }

    public Observable<Region> getRegion(String id) {
        return regionsApiService.getRegion(id);
    }

}
