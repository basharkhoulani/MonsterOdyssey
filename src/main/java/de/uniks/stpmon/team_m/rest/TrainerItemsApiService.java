package de.uniks.stpmon.team_m.rest;

import de.uniks.stpmon.team_m.dto.Item;
import de.uniks.stpmon.team_m.dto.UpdateItemDto;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.*;

import java.util.List;

public interface TrainerItemsApiService {
    @POST("regions/{regionId}/trainers/{trainerId}/items")
    Observable<Item> useOrTradeItem(@Path("regionId") String ignoredRegionId, @Path("trainerId") String ignoredTrainerId, @Query("action") String ignoredAction, @Body UpdateItemDto ignoredDto);

    @GET("regions/{regionId}/trainers/{trainerId}/items")
    Observable<List<Item>> getItems(@Path("regionId") String ignoredRegionId, @Path("trainerId") String ignoredTrainerId, @Query("types") String ignoredTypes);

    @GET("regions/{regionId}/trainers/{trainerId}/items/{id}")
    Observable<Item> getItem(@Path("regionId") String ignoredRegionId, @Path("trainerId") String ignoredTrainerId, @Path("id") String ignoredId);
}
