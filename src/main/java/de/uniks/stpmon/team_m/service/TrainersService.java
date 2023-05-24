package de.uniks.stpmon.team_m.service;

import de.uniks.stpmon.team_m.dto.CreateTrainerDto;
import de.uniks.stpmon.team_m.dto.Trainer;
import de.uniks.stpmon.team_m.rest.TrainersApiService;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import java.util.List;

public class TrainersService {
    private final TrainersApiService trainersApiService;

    @Inject
    public TrainersService(TrainersApiService trainersApiService) {
        this.trainersApiService = trainersApiService;
    }

    public Observable<Trainer> createTrainer(String regionId, String name, String image) {
        return trainersApiService.createTrainer(regionId, new CreateTrainerDto(name, image));
    }

    /**
     * Area and/or user can be null to get all trainers from all areas,
     * all trainers from one area or one specific trainer from all areas
     */
    public Observable<List<Trainer>> getTrainers(String regionId, String area, String user) {
        return trainersApiService.getTrainers(regionId, area, user);
    }

    public Observable<Trainer> getTrainer(String regionId, String _id) {
        return trainersApiService.getTrainer(regionId, _id);
    }

    public Observable<Trainer> deleteTrainer(String regionId, String _id) {
        return trainersApiService.deleteTrainer(regionId, _id);
    }
}
