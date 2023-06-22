package de.uniks.stpmon.team_m.service;

import de.uniks.stpmon.team_m.dto.AbilityMove;
import de.uniks.stpmon.team_m.dto.Opponent;
import de.uniks.stpmon.team_m.dto.Result;
import de.uniks.stpmon.team_m.rest.EncounterOpponentsApiService;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.inject.Inject;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EncounterOpponentsServiceTest {

    @Mock
    EncounterOpponentsApiService encounterOpponentsApiService;
    @InjectMocks
    EncounterOpponentsService encounterOpponentsService;

    @Test
    void getTrainerOpponentsTest() {
        when(encounterOpponentsApiService.getTrainerOpponents("gd5jc6gnbb2sbrz9w8z2jb95", "74k9gkiq22hip5scnaerxqc8")).thenReturn(Observable.just(List.of(
                new Opponent(
                        "2023-06-22T09:51:46.772Z",
                        "2023-06-22T09:51:46.772Z",
                        "dmd4n21l8kadvdlhtr7m3yv3",
                        "fp4v2ifz0n15a3pvvuyejo9i",
                        "74k9gkiq22hip5scnaerxqc8",
                        false,
                        false,
                        "646bac223b4804b87c0b8054",
                        new AbilityMove(
                                "ability",
                                10,
                                "hsvxr8je3qs6qt20oxffy4dw"
                        ),
                        List.of(new Result(
                                "ability-success",
                                1,
                                "super-effective"
                        )),
                        0

                ),
                new Opponent(
                        "2023-06-22T09:56:46.772Z",
                        "2023-06-22T09:56:46.772Z",
                        "avco6ows5t9y596eq067duqa",
                        "cy3gme0xxw0puzats4xytauf",
                        "74k9gkiq22hip5scnaerxqc8",
                        false,
                        false,
                        "e1t81v4bzzqq9fo7l7n080yu",
                        new AbilityMove(
                                "ability",
                                10,
                                "hsvxr8je3qs6qt20oxffy4dw"
                        ),
                        List.of(new Result(
                                "monster-levelup",
                                null,
                                null
                        )),
                        0

                )
        )));

        final List<Opponent> opponents = encounterOpponentsService.getTrainerOpponents("gd5jc6gnbb2sbrz9w8z2jb95", "74k9gkiq22hip5scnaerxqc8").blockingFirst();
        assertNotNull(opponents);
        verify(encounterOpponentsApiService).getTrainerOpponents("gd5jc6gnbb2sbrz9w8z2jb95", "74k9gkiq22hip5scnaerxqc8");
    }

    @Test
    void getEncounterOpponents() {
    }

    @Test
    void getOpponent() {
    }

    @Test
    void updateOpponent() {
    }

    @Test
    void deleteOpponent() {
    }
}