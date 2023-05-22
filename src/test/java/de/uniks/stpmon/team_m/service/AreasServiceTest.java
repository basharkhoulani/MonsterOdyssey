package de.uniks.stpmon.team_m.service;

import de.uniks.stpmon.team_m.dto.Area;
import de.uniks.stpmon.team_m.rest.AreasApiService;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapeditor.core.Map;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AreasServiceTest {
    @Mock
    AreasApiService areasApiService;
    @InjectMocks
    AreasService areasService;

    @Test
    void getAreasTest() {
        when(areasApiService.getAreas("646bc436cfee07c0e408466f")).thenReturn(Observable.just(List.of(
                new Area(
                        "2023-05-22T17:51:46.772Z",
                        "2023-05-22T17:51:46.772Z",
                        "646bc3c0a9ac1b375fb41d93",
                        "646bc436cfee07c0e408466f",
                        "Albertina",
                        new Map()
                ),
                new Area(
                        "2023-05-16T17:51:15.772Z",
                        "2023-05-18T11:23:46.772Z",
                        "646bc3c0a9ac1b375fb41d61",
                        "646bc436cfee07c0e408466f",
                        "Sanitaire",
                        new Map()
                )
        )));

        final List<Area> areas = areasService.getAreas("646bc436cfee07c0e408466f").blockingFirst();

        assertNotNull(areas);
        verify(areasApiService).getAreas("646bc436cfee07c0e408466f");
    }

    @Test
    void getAreaTest() {

    }
}
