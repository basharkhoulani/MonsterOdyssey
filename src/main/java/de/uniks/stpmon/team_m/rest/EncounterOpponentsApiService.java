package de.uniks.stpmon.team_m.rest;

import de.uniks.stpmon.team_m.dto.Opponent;
import de.uniks.stpmon.team_m.dto.UpdateOpponentDto;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.*;

import java.util.List;

public interface EncounterOpponentsApiService {
    @GET("regions/{regionId}/trainers/{trainerId}/opponents")
    Observable<List<Opponent>> getTrainerOpponents(@Path("regionId") String regionId, @Path("trainerId") String trainerId);

    @GET("regions/{regionId}/trainers/{encounterId}/opponents")
    Observable<List<Opponent>> getEncounterOpponents(@Path("regionId") String regionId, @Path("encounterId") String encounterId);

    @GET("regions/{regionId}/trainers/{encounterId}/opponents/{opponentId}")
    Observable<Opponent> getOpponent(@Path("regionId") String regionId, @Path("encounterId") String encounterId, @Path("opponentId") String opponentId);

    @PATCH("regions/{regionId}/trainers/{encounterId}/opponents/{opponentId}")
    Observable<Opponent> updateOpponent(@Path("regionId") String regionId, @Path("encounterId") String encounterId, @Path("opponentId") String opponentId, @Body UpdateOpponentDto dto);

    @DELETE("regions/{regionId}/trainers/{encounterId}/opponents/{opponentId}")
    Observable<Opponent> deleteOpponent(@Path("regionId") String regionId, @Path("encounterId") String encounterId, @Path("opponentId") String opponentId);
}
