package de.uniks.stpmon.team_m.rest;

import de.uniks.stpmon.team_m.dto.CreateTrainerDto;
import de.uniks.stpmon.team_m.dto.Trainer;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.*;

import java.util.List;

public interface TrainersApiService {
    @POST("regions/{regionId}/trainers")
    Observable<Trainer> create(@Path("regionId") String regionId, @Body CreateTrainerDto createTrainerDto);
    @GET("regions/{regionsId}/trainers")
    Observable<List<Trainer>> getTrainers(@Path("regionId") String regionId, @Query("area") String area, @Query("user") String id);
}
