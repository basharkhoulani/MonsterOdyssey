package de.uniks.stpmon.team_m.rest;

import de.uniks.stpmon.team_m.dto.Item;
import de.uniks.stpmon.team_m.dto.UpdateTrainerDto;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.*;

import java.util.List;

public interface TrainerItemsApiService {
    @POST("regions/{regionId}/trainers/{trainerId]/items")
    Observable<Item> useOrTradeItem(@Path("regionId") String regionId, @Path("trainerId") String trainerId, @Query("action") String action, @Body UpdateTrainerDto dto);

    @GET("regions/{regionId}/trainers/{trainerId}/items")
    Observable<List<Item>> getItems(@Path("regionId") String regionId, @Path("trainerId") String trainerId, @Query("types") String types);

    @GET("regions/{regionId}/trainers/{trainerId}/items/{id}")
    Observable<Item> getItem(@Path("regionId") String regionId, @Path("trainerId") String trainerId, @Path("id") String id);
}
