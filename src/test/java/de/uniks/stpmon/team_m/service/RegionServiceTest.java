package de.uniks.stpmon.team_m.service;

import de.uniks.stpmon.team_m.dto.Region;
import de.uniks.stpmon.team_m.dto.Spawn;
import de.uniks.stpmon.team_m.rest.RegionsApiService;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegionServiceTest {
    @Mock
    RegionsApiService regionsApiService;
    @InjectMocks
    RegionsService regionsService;

    @Test
    void getRegionsTest() {
        when(regionsApiService.getRegions()).thenReturn(Observable.just(List.of(
                new Region(
                        "2023-05-22T17:51:46.772Z",
                        "2023-05-22T17:51:46.772Z",
                        "646bc436cfee07c0e408466f",
                        "Albertina",
                        new Spawn("646bc3c0a9ac1b375fb41d93", 1, 1),
                        new Object()
                ),
                new Region(
                        "2023-05-16T17:51:15.772Z",
                        "2023-05-18T11:23:46.772Z",
                        "646bc3c0a9ac1b375fb41d61",
                        "Sanitaire",
                        new Spawn("646bc3c0a9ac1b375fb41d61", 4, 6),
                        new Object()
                )
        )));

        final List<Region> regions = regionsService.getRegions().blockingFirst();

        assertNotNull(regions);
        verify(regionsApiService).getRegions();
    }

    @Test
    void getRegionTest() {
        when(regionsApiService.getRegion("646bc3c0a9ac1b375fb41d93")).thenReturn(Observable.just(
                new Region(
                        "2023-05-22T17:51:46.772Z",
                        "2023-05-22T17:51:46.772Z",
                        "646bc436cfee07c0e408466f",
                        "Albertina",
                        new Spawn("646bc3c0a9ac1b375fb41d93", 1, 1),
                        new Object()
                )));

        final Region region = regionsService.getRegion("646bc3c0a9ac1b375fb41d93").blockingFirst();

        assertEquals("2023-05-22T17:51:46.772Z", region.createdAt());
        assertEquals("2023-05-22T17:51:46.772Z", region.updatedAt());
        assertEquals("646bc436cfee07c0e408466f", region._id());
        assertEquals("Albertina", region.name());
        assertEquals("646bc3c0a9ac1b375fb41d93", region.spawn().area());
        assertEquals(1, region.spawn().x());
        assertEquals(1, region.spawn().y());
        assertNotNull(region.map());

        verify(regionsApiService).getRegion("646bc3c0a9ac1b375fb41d93");
    }
}
