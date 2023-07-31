package de.uniks.stpmon.team_m.rest;

import de.uniks.stpmon.team_m.dto.Monster;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface MonstersApiService {
    @GET("regions/{regionId}/trainers/{trainerId}/monsters")
    Observable<List<Monster>> getMonsters(@Path("regionId") String regionId, @Path("trainerId") String trainerId);

    @GET("regions/{regionId}/trainers/{trainerId}/monsters/{monsterId}")
    Observable<Monster> getMonster(@Path("regionId") String regionId, @Path("trainerId") String trainerId, @Path("monsterId") String monsterId);

    @DELETE("regions/{regionId}/trainers/{trainerId}/monsters/{monsterId}")
    Observable<Monster> deleteMonster(@Path("regionId") String regionId, @Path("trainerId") String trainerId, @Path("monsterId") String monsterId);
}
