package de.uniks.stpmon.team_m.service;

import de.uniks.stpmon.team_m.dto.CreateTrainerDto;
import de.uniks.stpmon.team_m.dto.NPCInfo;
import de.uniks.stpmon.team_m.dto.Trainer;
import de.uniks.stpmon.team_m.dto.UpdateTrainerDto;
import de.uniks.stpmon.team_m.rest.TrainersApiService;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainersServiceTest {
    @Mock
    TrainersApiService trainersApiService;
    @InjectMocks
    TrainersService trainersService;

    @Test
    void createTrainerTest() {
        when(trainersApiService.createTrainer(
                "646bab5cecf584e1be02598a",
                new CreateTrainerDto("Hans", "Premade_Character_01.png")))
                .thenReturn(Observable.just(new Trainer(
                        "2023-05-22T17:51:46.772Z",
                        "2023-05-22T17:51:46.772Z",
                        "646bac223b4804b87c0b8054",
                        "646bab5cecf584e1be02598a",
                        "646bac8c1a74032c70fffe24",
                        "Hans",
                        "Premade_Character_01.png",
                        0,
                        null,
                        null,
                        List.of("646bacc568933551792bf3d5"),
                        "646bacc568933551792bf3d5",
                        0,
                        0,
                        0,
                        new NPCInfo(false, false, false, false, null, null, null),
                        null
                )));

        final Trainer trainer = trainersService.createTrainer("646bab5cecf584e1be02598a", "Hans", "Premade_Character_01.png").blockingFirst();

        assertEquals("2023-05-22T17:51:46.772Z", trainer.createdAt());
        assertEquals("2023-05-22T17:51:46.772Z", trainer.updatedAt());
        assertEquals("646bac223b4804b87c0b8054", trainer._id());
        assertEquals("646bab5cecf584e1be02598a", trainer.region());
        assertEquals("646bac8c1a74032c70fffe24", trainer.user());
        assertEquals("Hans", trainer.name());
        assertEquals("Premade_Character_01.png", trainer.image());
        assertEquals(0, trainer.coins());
        assertEquals("646bacc568933551792bf3d5", trainer.area());
        assertEquals(0, trainer.x());
        assertEquals(0, trainer.y());
        assertEquals(0, trainer.direction());
        assertFalse(trainer.npc().walkRandomly());

        verify(trainersApiService).createTrainer("646bab5cecf584e1be02598a", new CreateTrainerDto("Hans", "Premade_Character_01.png"));
    }

    @Test
    void getTrainersTest() {
        when(trainersApiService.getTrainers("646bab5cecf584e1be02598a", null, null)).thenReturn(Observable.just(List.of(
                new Trainer(
                        "2023-05-22T17:51:46.772Z",
                        "2023-05-22T17:51:46.772Z",
                        "646bac223b4804b87c0b8054",
                        "646bab5cecf584e1be02598a",
                        "646bac8c1a74032c70fffe24",
                        "Hans",
                        "Premade_Character_01.png",
                        0,
                        List.of("63va3w6d11sj2hq0nzpsa20w", "86m1imksu4jkrxuep2gtpi4a"),
                        List.of(1, 2),
                        List.of("646bacc568933551792bf3d5"),
                        "646bacc568933551792bf3d5",
                        0,
                        0,
                        0,
                        new NPCInfo(false, false, false, false, null, null, null),
                        null),
                new Trainer(
                        "2023-05-21T13:43:12.742Z",
                        "2023-05-21T13:43:12.742Z",
                        "646baf531f097a36fc1b8bc5",
                        "646bab5cecf584e1be02598a",
                        "646baf778eceac8ef458cc34",
                        "Peter",
                        "Premade_Character_03.png",
                        0,
                        null,
                        null,
                        List.of("646baf8096dc75bef5ab7cae"),
                        "646baf8096dc75bef5ab7cae",
                        0,
                        0,
                        0,
                        new NPCInfo(true, false, false, true, null, null, null),
                        null)
        )));

        final List<Trainer> trainers = trainersService.getTrainers("646bab5cecf584e1be02598a", null, null).blockingFirst();

        assertNotNull(trainers);

        verify(trainersApiService).getTrainers("646bab5cecf584e1be02598a", null, null);
    }

    @Test
    void getTrainerTest() {
        when(trainersApiService.getTrainer("646bab5cecf584e1be02598a", "646baf531f097a36fc1b8bc5")).thenReturn(Observable.just(
                new Trainer(
                        "2023-05-21T13:43:12.742Z",
                        "2023-05-21T13:43:12.742Z",
                        "646baf531f097a36fc1b8bc5",
                        "646bab5cecf584e1be02598a",
                        "646baf778eceac8ef458cc34",
                        "Peter",
                        "Premade_Character_03.png",
                        0,
                        List.of("63va3w6d11sj2hq0nzpsa20w", "86m1imksu4jkrxuep2gtpi4a"),
                        List.of(1, 2),
                        List.of("646baf8096dc75bef5ab7cae"),
                        "646baf8096dc75bef5ab7cae",
                        0,
                        0,
                        0,
                        new NPCInfo(true, false, false, true, null, null, null),
                        null)
        ));

        final Trainer trainer = trainersService.getTrainer("646bab5cecf584e1be02598a", "646baf531f097a36fc1b8bc5").blockingFirst();

        assertNotNull(trainer);
        assertEquals("Peter", trainer.name());

        verify(trainersApiService).getTrainer("646bab5cecf584e1be02598a", "646baf531f097a36fc1b8bc5");
    }

    @Test
    void deleteTrainerTest() {
        when(trainersApiService.deleteTrainer("646bab5cecf584e1be02598a", "646baf531f097a36fc1b8bc5")).thenReturn(Observable.just(
                new Trainer(
                        "2023-05-21T13:43:12.742Z",
                        "2023-05-21T13:43:12.742Z",
                        "646baf531f097a36fc1b8bc5",
                        "646bab5cecf584e1be02598a",
                        "646baf778eceac8ef458cc34",
                        "Peter",
                        "Premade_Character_03.png",
                        0,
                        List.of("63va3w6d11sj2hq0nzpsa20w", "86m1imksu4jkrxuep2gtpi4a"),
                        List.of(1, 2),
                        List.of("646baf8096dc75bef5ab7cae"),
                        "646baf8096dc75bef5ab7cae",
                        0,
                        0,
                        0,
                        new NPCInfo(false, false, false, false, null, null, null),
                        null)
        ));

        final Trainer trainer = trainersService.deleteTrainer("646bab5cecf584e1be02598a", "646baf531f097a36fc1b8bc5").blockingFirst();

        assertNotNull(trainer);
        assertEquals("Peter", trainer.name());

        verify(trainersApiService).deleteTrainer("646bab5cecf584e1be02598a", "646baf531f097a36fc1b8bc5");
    }

    @Test
    void updateTrainerTest(){
        when(trainersApiService.updateTrainer(anyString(), anyString(), any())).thenReturn(Observable.just(new Trainer(
                "2023-05-21T13:43:12.742Z",
                "2023-05-21T13:43:12.742Z",
                "646baf531f097a36fc1b8bc5",
                "646bab5cecf584e1be02598a",
                "646baf778eceac8ef458cc34",
                "Hallo",
                "Premade_Character_03.png",
                0,
                List.of("63va3w6d11sj2hq0nzpsa20w", "86m1imksu4jkrxuep2gtpi4a"),
                List.of(1, 2),
                List.of("646baf8096dc75bef5ab7cae"),
                "646baf8096dc75bef5ab7cae",
                0,
                0,
                0,
                new NPCInfo(false, false, false, false, null, null, null),
                null)
        ));

        final Trainer trainer = trainersService.updateTrainer("646bab5cecf584e1be02598a", "646baf531f097a36fc1b8bc5", "Hallo", "Premade_Character_03.png", List.of("63va3w6d11sj2hq0nzpsa20w", "86m1imksu4jkrxuep2gtpi4a"), null, null).blockingFirst();

        assertNotNull(trainer);
        assertEquals("Hallo", trainer.name());

        verify(trainersApiService).updateTrainer("646bab5cecf584e1be02598a", "646baf531f097a36fc1b8bc5",
                new UpdateTrainerDto("Hallo", "Premade_Character_03.png", List.of("63va3w6d11sj2hq0nzpsa20w", "86m1imksu4jkrxuep2gtpi4a"), null, null));

    }

}
