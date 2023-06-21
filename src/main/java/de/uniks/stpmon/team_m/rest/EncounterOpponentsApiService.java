package de.uniks.stpmon.team_m.rest;

import de.uniks.stpmon.team_m.dto.Opponent;
import de.uniks.stpmon.team_m.dto.UpdateOpponentDto;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

import java.util.List;

public interface EncounterOpponentsApiService {
    @GET("regions/{regionId}/trainers/{trainerId}/opponents")
    Observable<List<Opponent>> getTrainerOpponents(@Path("regionId") String regionId, @Path("trainerId") String trainerId);

    @GET("regions/{regionId}/trainers/{encounterId}/opponents")
    Observable<List<Opponent>> getEncounterOpponents(@Path("regionId") String regionId, @Path("encounterId") String encounterId);

    @GET("regions/{regionId}/trainers/{encounterId}/opponents/{opponentId}")
    Observable<Opponent> getOpponent(@Path("regionId") String regionId, @Path("encounterId") String encounterId, @Path("opponentId") String opponentId);

    @PATCH("regions/{regionId}/trainers/{encounterId}/opponents/{opponentId}")
    Observable<UpdateOpponentDto> updateOpponent(@Path("regionId") String regionId, @Path("encounterId") String encounterId, @Path("opponentId") String opponentId);

    @DELETE("regions/{regionId}/trainers/{encounterId}/opponents/{opponentId}")
    Observable<Opponent> deleteOpponent(@Path("regionId") String regionId, @Path("encounterId") String encounterId, @Path("opponentId") String opponentId);
}
