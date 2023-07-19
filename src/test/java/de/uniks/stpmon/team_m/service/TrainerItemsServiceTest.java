package de.uniks.stpmon.team_m.service;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import de.uniks.stpmon.team_m.dto.UpdateItemDto;
import de.uniks.stpmon.team_m.dto.UpdateTrainerDto;
import de.uniks.stpmon.team_m.rest.TrainerItemsApiService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.inject.Inject;

import static org.mockito.Mockito.when;

public class TrainerItemsServiceTest {

    @Mock
    TrainerItemsApiService trainerItemsApiService;
    @InjectMocks
    TrainerItemsService trainerItemsService;

    @Test
    void useItem() {
        when(trainerItemsApiService.useOrTradeItem(
                "kh9ohbkrff6xgv4e1d36ogea",
                "pnrhch2baloqk1c7wjlzjmw3",
                "use",
                new UpdateItemDto(
                    2,
                        1,
                        "507f191e810c19729de860ea"
                ))).thenReturn(null);
    }
}
