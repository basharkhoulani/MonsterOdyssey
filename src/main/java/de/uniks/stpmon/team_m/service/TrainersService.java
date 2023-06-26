package de.uniks.stpmon.team_m.service;

import de.uniks.stpmon.team_m.dto.CreateTrainerDto;
import de.uniks.stpmon.team_m.dto.Trainer;
import de.uniks.stpmon.team_m.dto.UpdateTrainerDto;
import de.uniks.stpmon.team_m.rest.TrainersApiService;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import java.util.List;

public class TrainersService {
    private final TrainersApiService trainersApiService;

    /**
     * TrainersService handles the communication with the backend for the trainers.
     */

    @Inject
    public TrainersService(TrainersApiService trainersApiService) {
        this.trainersApiService = trainersApiService;
    }

    /**
     * createTrainer creates a new trainer.
     *
     * @param regionId The region of the trainer.
     * @param name     The name of the trainer.
     * @param image    The image of the trainer.
     * @return The created trainer.
     */

    public Observable<Trainer> createTrainer(String regionId, String name, String image) {
        return trainersApiService.createTrainer(regionId, new CreateTrainerDto(name, image));
    }

    /**
     * getTrainers returns trainers of a region.
     * Area and/or user can be null to get all trainers from all areas,
     * all trainers from one area or one specific trainer from all areas
     *
     * @param regionId The region of the trainers.
     * @param area     The area of the trainers.
     * @param user     The user of the trainers.
     * @return A list of trainers of the region.
     */

    public Observable<List<Trainer>> getTrainers(String regionId, String area, String user) {
        return trainersApiService.getTrainers(regionId, area, user);
    }

    /**
     * getTrainer returns a specific trainer of a region.
     *
     * @param regionId The region of the trainer.
     * @param _id      The id of the trainer.
     * @return The trainer.
     */

    public Observable<Trainer> getTrainer(String regionId, String _id) {
        return trainersApiService.getTrainer(regionId, _id);
    }

    /**
     * deleteTrainer deletes a specific trainer of a region.
     *
     * @param regionId The region of the trainer.
     * @param _id      The id of the trainer.
     * @return The trainer.
     */

    public Observable<Trainer> updateTrainer(String regionId, String _id, String name, String image, List<String> team) {
        UpdateTrainerDto updateTrainerDto = new UpdateTrainerDto(name, image, team);
        return trainersApiService.updateTrainer(regionId, _id, updateTrainerDto);
    }

    public Observable<Trainer> deleteTrainer(String regionId, String _id) {
        return trainersApiService.deleteTrainer(regionId, _id);
    }
}
