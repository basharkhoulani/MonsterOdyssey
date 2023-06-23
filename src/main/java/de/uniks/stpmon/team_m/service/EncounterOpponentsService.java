package de.uniks.stpmon.team_m.service;

import de.uniks.stpmon.team_m.dto.Move;
import de.uniks.stpmon.team_m.dto.Opponent;
import de.uniks.stpmon.team_m.dto.UpdateOpponentDto;
import de.uniks.stpmon.team_m.rest.EncounterOpponentsApiService;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import java.util.List;

public class EncounterOpponentsService {
    private final EncounterOpponentsApiService encounterOpponentsApiService;

    @Inject
    public EncounterOpponentsService(EncounterOpponentsApiService encounterOpponentsApiService) {
        this.encounterOpponentsApiService = encounterOpponentsApiService;
    }

    /**
     * gets returns opponents of a specific trainer.
     *
     * @param regionId  The id of the current region.
     * @param trainerId The id of the current trainer
     * @return A list of opponents.
     */
    public Observable<List<Opponent>> getTrainerOpponents(String regionId, String trainerId) {
        return encounterOpponentsApiService.getTrainerOpponents(regionId, trainerId);
    }

    /**
     * gets returns opponents of a specific encounter.
     *
     * @param regionId    The id of the current region.
     * @param encounterId The id of the current encounter
     * @return A list of opponents.
     */
    public Observable<List<Opponent>> getEncounterOpponents(String regionId, String encounterId) {
        return encounterOpponentsApiService.getEncounterOpponents(regionId, encounterId);
    }

    /**
     * gets returns specific opponent .
     *
     * @param regionId    The id of the current region.
     * @param encounterId The id of the current encounter
     * @param opponentId  The id of this opponent
     * @return The Information about this opponent.
     */
    public Observable<Opponent> getOpponent(String regionId, String encounterId, String opponentId) {
        return encounterOpponentsApiService.getOpponent(regionId, encounterId, opponentId);
    }

    /**
     * update specific opponent .
     *
     * @param regionId    The id of the current region.
     * @param encounterId The id of the current encounter
     * @param opponentId  The id of this opponent
     * @param monsterId   The id of the current monster
     * @param move        whether AbilityMove or ChangeMonsterMove
     * @return The Information about this opponent after update.
     */
    public Observable<Opponent> updateOpponent(String regionId, String encounterId, String opponentId, String monsterId, Move move) {
        UpdateOpponentDto updateOpponentDto = new UpdateOpponentDto(monsterId, move);
        return encounterOpponentsApiService.updateOpponent(regionId, encounterId, opponentId, updateOpponentDto);
    }

    /**
     * delete specific opponent .
     *
     * @param regionId    The id of the current region.
     * @param encounterId The id of the current encounter
     * @param opponentId  The id of this opponent
     * @return The Information about this deleted opponent.
     */
    public Observable<Opponent> deleteOpponent(String regionId, String encounterId, String opponentId) {
        return encounterOpponentsApiService.deleteOpponent(regionId, encounterId, opponentId);
    }


}
