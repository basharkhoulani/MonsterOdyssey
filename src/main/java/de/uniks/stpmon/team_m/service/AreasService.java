package de.uniks.stpmon.team_m.service;

import de.uniks.stpmon.team_m.dto.Area;
import de.uniks.stpmon.team_m.rest.AreasApiService;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import java.util.List;

public class AreasService {
    private final AreasApiService areasApiService;

    /**
     * AreasService handles the communication with the backend for the areas.
     */

    @Inject
    public AreasService(AreasApiService areasApiService) {
        this.areasApiService = areasApiService;
    }

    /**
     * getAreas returns all areas of a region.
     *
     * @param region The region of the areas.
     * @return A list of areas of the region.
     */

    public Observable<List<Area>> getAreas(String region) {
        return areasApiService.getAreas(region);
    }

    /**
     * getArea returns a specific area of a region.
     *
     * @param region The region of the area.
     * @param _id    The id of the area.
     * @return The area.
     */

    public Observable<Area> getArea(String region, String _id) {
        return areasApiService.getArea(region, _id);
    }
}
