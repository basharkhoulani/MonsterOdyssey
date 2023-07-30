package de.uniks.stpmon.team_m.service;

import de.uniks.stpmon.team_m.dto.AbilityDto;
import de.uniks.stpmon.team_m.dto.MonsterTypeDto;
import de.uniks.stpmon.team_m.dto.TileSet;
import de.uniks.stpmon.team_m.rest.PresetsApiService;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.image.Image;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PresetsServiceTest {
    @Mock
    PresetsApiService presetsApiService;
    @InjectMocks
    PresetsService presetsService;

    @Test
    void getCharactersTest() {
        when(presetsApiService.getCharacters()).thenReturn(Observable.just(List.of(

                "Premade_Character_01.png",
                "Premade_Character_02.png",
                "Premade_Character_03.png",
                "Premade_Character_04.png",
                "Premade_Character_05.png"
        )));

        final List<String> characters = presetsService.getCharacters().blockingFirst();

        assertNotNull(characters);
        assertEquals(5, characters.size());

        verify(presetsApiService).getCharacters();
    }

    @Test
    void getCharacterTest() {
        when(presetsApiService.getCharacter("Premade_Character_01.png")).thenReturn(Observable.just(ResponseBody.create(null, new byte[0])));

        final ResponseBody responseBody = presetsService.getCharacter("Premade_Character_01.png").blockingFirst();
        assertNotNull(responseBody);
        verify(presetsApiService).getCharacter("Premade_Character_01.png");
    }

    @Test
    void getMonstersTest() {
        when(presetsApiService.getMonsters()).thenReturn(Observable.just(List.of(
                new MonsterTypeDto(
                        1,
                        "Salamander",
                        "salamander.png",
                        List.of("fire"),
                        "A fire lizard. It's hot."
                ),
                new MonsterTypeDto(
                        2,
                        "Goblin",
                        "goblin.png",
                        List.of("earth"),
                        "A goblin. It's green."
                )
        )));
        final List<MonsterTypeDto> monsters = presetsService.getMonsters().blockingFirst();
        assertNotNull(monsters);
        assertEquals(2, monsters.size());
        verify(presetsApiService).getMonsters();
    }

    @Test
    void getMonsterTest() {
        when(presetsApiService.getMonster(1)).thenReturn(Observable.just(
                new MonsterTypeDto(
                        1,
                        "Salamander",
                        "salamander.png",
                        List.of("fire"),
                        "A fire lizard. It's hot."
                )
        ));
        final MonsterTypeDto monster = presetsService.getMonster(1).blockingFirst();
        assertNotNull(monster);
        assertEquals(1, monster.id());
        assertEquals("Salamander", monster.name());
        assertEquals("salamander.png", monster.image());
        assertEquals("fire", monster.type().get(0));
        assertEquals("A fire lizard. It's hot.", monster.description());
        verify(presetsApiService).getMonster(1);
    }

    @Test
    void getMonsterImageTest() {
        when(presetsApiService.getMonsterImage(2)).thenReturn(Observable.just(ResponseBody.create(null, new byte[0])));
        final ResponseBody responseBody = presetsService.getMonsterImage(2).blockingFirst();
        assertNotNull(responseBody);
        verify(presetsApiService).getMonsterImage(2);
    }

    @Test
    void getAbilitiesTest() {
        when(presetsApiService.getAbilities()).thenReturn(Observable.just(List.of(
                new AbilityDto(
                        1,
                        "Fireball",
                        "A fireball. It's hot.",
                        "fire",
                        15,
                        1,
                        10
                ), new AbilityDto(
                        2,
                        "Earthquake",
                        "An earthquake. It's earthy.",
                        "earth",
                        20,
                        1,
                        10
                ))));
        final List<AbilityDto> abilities = presetsService.getAbilities().blockingFirst();
        assertNotNull(abilities);
        assertEquals(2, abilities.size());
        verify(presetsApiService).getAbilities();
    }

    @Test
    void getAbilityTest() {
        when(presetsApiService.getAbility(1)).thenReturn(Observable.just(
                new AbilityDto(
                        1,
                        "Fireball",
                        "A fireball. It's hot.",
                        "fire",
                        15,
                        1,
                        10
                )
        ));
        final AbilityDto ability = presetsService.getAbility(1).blockingFirst();
        assertNotNull(ability);
        assertEquals(1, ability.id());
        assertEquals("Fireball", ability.name());
        assertEquals("A fireball. It's hot.", ability.description());
        assertEquals("fire", ability.type());
        assertEquals(15, ability.maxUses());
        assertEquals(1, ability.accuracy());
        assertEquals(10, ability.power());
        verify(presetsApiService).getAbility(1);
    }

    @Test
    void getTileSetImageTest() {
        when(presetsApiService.getTileset("tileset.png")).thenReturn(Observable.just(ResponseBody.create(null, new byte[0])));

        Image image = presetsService.getTilesetImage("tileset.png").blockingFirst();
        assertNotNull(image);
        verify(presetsApiService).getTileset("tileset.png");
    }

    @Test
    void getTileSetTest() throws IOException {
        ResponseBody mockResponseBody = mock(ResponseBody.class);
        when(mockResponseBody.string()).thenReturn("{\"name\": \"TileSet A\", \"size\": 64}");

        when(presetsApiService.getTileset("tileset.json")).thenReturn(Observable.just(mockResponseBody));

        TileSet tileSet = presetsService.getTileset("tileset").blockingFirst();
        assertNotNull(tileSet);
        verify(presetsApiService).getTileset("tileset.json");
    }
}
