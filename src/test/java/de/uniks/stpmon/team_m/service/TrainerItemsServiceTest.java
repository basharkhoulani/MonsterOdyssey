package de.uniks.stpmon.team_m.service;

import de.uniks.stpmon.team_m.dto.Item;
import de.uniks.stpmon.team_m.dto.UpdateItemDto;
import de.uniks.stpmon.team_m.rest.TrainerItemsApiService;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
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
                ))).thenReturn(Observable.just(new Item(
                "507f191e810c19729de860ea",
                "507f191e810c19729de860ea",
                2,
                1
        )));

        final Item item = trainerItemsService.useOrTradeItem(
                "kh9ohbkrff6xgv4e1d36ogea",
                "pnrhch2baloqk1c7wjlzjmw3",
                "use",
                new UpdateItemDto(
                        2,
                        1,
                        "507f191e810c19729de860ea"
                )
        ).blockingFirst();

        assertNotNull(item);
        verify(trainerItemsApiService).useOrTradeItem("kh9ohbkrff6xgv4e1d36ogea",
                "pnrhch2baloqk1c7wjlzjmw3",
                "use",
                new UpdateItemDto(
                        2,
                        1,
                        "507f191e810c19729de860ea"
                ));
    }
}
