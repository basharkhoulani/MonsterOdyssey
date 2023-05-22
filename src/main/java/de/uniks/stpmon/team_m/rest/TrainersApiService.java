package de.uniks.stpmon.team_m.rest;

import de.uniks.stpmon.team_m.dto.CreateTrainerDto;
import de.uniks.stpmon.team_m.dto.Trainer;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.*;

import java.util.List;

public interface TrainersApiService {
    @POST("regions/{regionId}/trainers")
    Observable<Trainer> createTrainer(@Path("regionId") String regionId, @Body CreateTrainerDto createTrainerDto);
    @GET("regions/{regionId}/trainers")
    Observable<List<Trainer>> getTrainers(@Path("regionId") String regionId, @Query("area") String area, @Query("user") String id);
    @GET("regions/{regionId}/trainers/{id}")
    Observable<Trainer> getTrainer(@Path("regionId") String regionId, @Path("id") String id);
    @DELETE("regions/{regionId}/trainers/{id}")
    Observable<Trainer> deleteTrainer(@Path("regionId") String regionId, @Path("id") String id);
}
