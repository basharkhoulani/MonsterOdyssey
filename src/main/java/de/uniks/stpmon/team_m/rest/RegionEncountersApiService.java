package de.uniks.stpmon.team_m.rest;

import de.uniks.stpmon.team_m.dto.Encounter;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface RegionEncountersApiService {
    @GET("regions/{regionId}/encounters")
    Observable<List<Encounter>> getEncounters(@Path("regionId") String regionId);

    @GET("regions/{regionId}/encounters/{encounterId}")
    Observable<Encounter> getEncounter(@Path("regionId") String regionId, @Path("encounterId") String encounterId);
}
