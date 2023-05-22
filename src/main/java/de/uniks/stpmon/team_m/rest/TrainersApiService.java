package de.uniks.stpmon.team_m.rest;

import de.uniks.stpmon.team_m.dto.CreateTrainerDto;
import de.uniks.stpmon.team_m.dto.Trainer;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TrainersApiService {
    @POST("regions/{regionId}/trainers")
    Observable<Trainer> create(@Path("regionId") String regionId, @Body CreateTrainerDto createTrainerDto);
}
