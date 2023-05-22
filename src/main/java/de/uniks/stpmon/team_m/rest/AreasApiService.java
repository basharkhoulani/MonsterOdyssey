package de.uniks.stpmon.team_m.rest;

import de.uniks.stpmon.team_m.dto.Area;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface AreasApiService {
    @GET("regions/{region}/areas")
    Observable<List<Area>> getAreas(@Path("region") String region);

    @GET("regions/{region}/areas/{id}")
    Observable<Area> getArea(@Path("region") String region, @Path("id") String _id);
}