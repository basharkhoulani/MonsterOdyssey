package de.uniks.stpmon.team_m.rest;

import de.uniks.stpmon.team_m.dto.Monster;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface MonstersApiService {
    @GET("regions/{regionId}/trainers/{trainerId}/monsters")
    Observable<List<Monster>> getMonsters(@Path("regionId") String ignoredRegionId, @Path("trainerId") String ignoredTrainerId);

    @GET("regions/{regionId}/trainers/{trainerId}/monsters/{monsterId}")
    Observable<Monster> getMonster(@Path("regionId") String ignoredRegionId, @Path("trainerId") String ignoredTrainerId, @Path("monsterId") String ignoredMonsterId);

    @DELETE("regions/{regionId}/trainers/{trainerId}/monsters/{monsterId}")
    Observable<Monster> deleteMonster(@Path("regionId") String ignoredRegionId, @Path("trainerId") String ignoredTrainerId, @Path("monsterId") String ignoredMonsterId);
}
