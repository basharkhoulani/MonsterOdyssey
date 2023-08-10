package de.uniks.stpmon.team_m.rest;

import de.uniks.stpmon.team_m.dto.Region;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface RegionsApiService {
    @GET("regions")
    Observable<List<Region>> getRegions();

    @GET("regions/{id}")
    Observable<Region> getRegion(@Path("id") String ignoredId);
}
