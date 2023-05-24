package de.uniks.stpmon.team_m.service;

import de.uniks.stpmon.team_m.dto.Area;
import de.uniks.stpmon.team_m.rest.AreasApiService;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import java.util.List;

public class AreasService {
    private final AreasApiService areasApiService;

    @Inject
    public AreasService(AreasApiService areasApiService) {
        this.areasApiService = areasApiService;
    }

    public Observable<List<Area>> getAreas(String region) {
        return areasApiService.getAreas(region);
    }

    public Observable<Area> getArea(String region, String _id) {
        return areasApiService.getArea(region, _id);
    }
}
