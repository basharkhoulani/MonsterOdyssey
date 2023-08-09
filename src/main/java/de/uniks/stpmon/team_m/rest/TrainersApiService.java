package de.uniks.stpmon.team_m.rest;

import de.uniks.stpmon.team_m.dto.CreateTrainerDto;
import de.uniks.stpmon.team_m.dto.Trainer;
import de.uniks.stpmon.team_m.dto.UpdateTrainerDto;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.*;

import java.util.List;

public interface TrainersApiService {
    @POST("regions/{regionId}/trainers")
    Observable<Trainer> createTrainer(@Path("regionId") String ignoredRegionId, @Body CreateTrainerDto ignoredCreateTrainerDto);
    @GET("regions/{regionId}/trainers")
    Observable<List<Trainer>> getTrainers(@Path("regionId") String ignoredRegionId, @Query("area") String ignoredArea, @Query("user") String ignoredId);
    @GET("regions/{regionId}/trainers/{id}")
    Observable<Trainer> getTrainer(@Path("regionId") String ignoredRegionId, @Path("id") String ignoredIgnoredIgnored_id);
    @PATCH("regions/{regionId}/trainers/{id}")
    Observable<Trainer> updateTrainer(@Path("regionId") String ignoredRegionId, @Path("id") String ignoredIgnoredIgnored_id, @Body UpdateTrainerDto ignoredDto);
    @DELETE("regions/{regionId}/trainers/{id}")
    Observable<Trainer> deleteTrainer(@Path("regionId") String ignoredRegionId, @Path("id") String ignoredIgnoredIgnored_id);
}
