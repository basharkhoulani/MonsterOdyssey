package de.uniks.stpmon.team_m.service;

import de.uniks.stpmon.team_m.dto.Item;
import de.uniks.stpmon.team_m.dto.UpdateTrainerDto;
import de.uniks.stpmon.team_m.rest.TrainerItemsApiService;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import java.util.List;

public class TrainerItemsService {
    private final TrainerItemsApiService trainerItemsApiService;

    /**
     * TrainerItemsService handles the communication with the backend for the trainer items.
     */

    @Inject
    public TrainerItemsService(TrainerItemsApiService trainerItemsApiService) {
        this.trainerItemsApiService = trainerItemsApiService;
    }

    /**
     * useOrTradeItem uses or trades an item.
     *
     * @param regionId The region id.
     * @param trainerId The trainer id.
     * @param action The action.
     * @param dto The update trainer dto.
     * @return The item.
     */

    public Observable<Item> useOrTradeItem(String regionId, String trainerId, String action, UpdateTrainerDto dto) {
        return trainerItemsApiService.useOrTradeItem(regionId, trainerId, action, dto);
    }

    /**
     * getItems returns a list of items.
     *
     * @param regionId The region id.
     * @param trainerId The trainer id.
     * @param types Filter by numeric Item IDs (comma-separated). Can be null.
     * @return The list of items.
     */

    public Observable<List<Item>> getItems(String regionId, String trainerId, String types) {
        return trainerItemsApiService.getItems(regionId, trainerId, types);
    }

    /**
     * getItem returns an item.
     *
     * @param regionId The region id.
     * @param trainerId The trainer id.
     * @param id The item id.
     * @return The item.
     */

    public Observable<Item> getItem(String regionId, String trainerId, String id) {
        return trainerItemsApiService.getItem(regionId, trainerId, id);
    }
}
