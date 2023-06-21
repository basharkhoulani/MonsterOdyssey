package de.uniks.stpmon.team_m.service;

import de.uniks.stpmon.team_m.dto.Encounter;
import de.uniks.stpmon.team_m.rest.RegionEncountersApiService;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegionEncountersServiceTest {

    @Mock
    RegionEncountersApiService regionEncountersApiService;
    @InjectMocks
    RegionEncountersService regionEncountersService;

    @Test
    void getEncountersTest() {
        when(regionEncountersApiService.getEncounters("kh9ohbkrff6xgv4e1d36ogea")).thenReturn(Observable.just(List.of(
                new Encounter(
                        "2023-06-21T17:51:46.772Z",
                        "2023-06-21T17:51:46.772Z",
                        "kh9ohbkrff6xgv4e1d36ogea",
                        "kh9ohbkrff6xgv4e1d36ogea",
                        true),
                new Encounter(
                        "2023-06-21T19:51:46.772Z",
                        "2023-06-21T19:51:46.772Z",
                        "pnrhch2baloqk1c7wjlzjmw3",
                        "kh9ohbkrff6xgv4e1d36ogea",
                        false)
        )));

        final List<Encounter> encounters = regionEncountersService.getEncounters("kh9ohbkrff6xgv4e1d36ogea").blockingFirst();

        assertNotNull(encounters);

        verify(regionEncountersApiService).getEncounters("kh9ohbkrff6xgv4e1d36ogea");

    }

    @Test
    void getEncounterTest() {
        when(regionEncountersApiService.getEncounter("kh9ohbkrff6xgv4e1d36ogea", "pnrhch2baloqk1c7wjlzjmw3")).thenReturn(Observable.just(
                new Encounter("2023-06-21T17:51:46.772Z",
                        "2023-06-21T17:51:46.772Z",
                        "pnrhch2baloqk1c7wjlzjmw3",
                        "kh9ohbkrff6xgv4e1d36ogea",
                        true)
        ));

        final Encounter encounter = regionEncountersService.getEncounter("kh9ohbkrff6xgv4e1d36ogea", "pnrhch2baloqk1c7wjlzjmw3").blockingFirst();

        assertNotNull(encounter);
        assertEquals("pnrhch2baloqk1c7wjlzjmw3", encounter._id());

        verify(regionEncountersApiService).getEncounter("kh9ohbkrff6xgv4e1d36ogea", "pnrhch2baloqk1c7wjlzjmw3");
    }
}