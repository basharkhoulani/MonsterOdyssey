package de.uniks.stpmon.team_m.rest;

import de.uniks.stpmon.team_m.dto.Opponent;
import de.uniks.stpmon.team_m.dto.UpdateOpponentDto;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.*;

import java.util.List;

public interface EncounterOpponentsApiService {
    @GET("regions/{regionId}/trainers/{trainerId}/opponents")
    Observable<List<Opponent>> getTrainerOpponents(@Path("regionId") String ignoredRegionId, @Path("trainerId") String ignoredTrainerId);

    @GET("regions/{regionId}/encounters/{encounterId}/opponents")
    Observable<List<Opponent>> getEncounterOpponents(@Path("regionId") String ignoredRegionId, @Path("encounterId") String ignoredEncounterId);

    @GET("regions/{regionId}/encounters/{encounterId}/opponents/{opponentId}")
    Observable<Opponent> getOpponent(@Path("regionId") String ignoredRegionId, @Path("encounterId") String ignoredEncounterId, @Path("opponentId") String ignoredOpponentId);

    @PATCH("regions/{regionId}/encounters/{encounterId}/opponents/{opponentId}")
    Observable<Opponent> updateOpponent(@Path("regionId") String ignoredRegionId, @Path("encounterId") String ignoredEncounterId, @Path("opponentId") String ignoredOpponentId, @Body UpdateOpponentDto ignoredDto);

    @DELETE("regions/{regionId}/encounters/{encounterId}/opponents/{opponentId}")
    Observable<Opponent> deleteOpponent(@Path("regionId") String ignoredRegionId, @Path("encounterId") String ignoredEncounterId, @Path("opponentId") String ignoredOpponentId);
}
