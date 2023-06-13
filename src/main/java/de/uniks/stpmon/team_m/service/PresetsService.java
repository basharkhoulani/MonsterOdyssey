package de.uniks.stpmon.team_m.service;

import com.google.gson.Gson;
import de.uniks.stpmon.team_m.dto.AbilityDto;
import de.uniks.stpmon.team_m.dto.MonsterTypeDto;
import de.uniks.stpmon.team_m.dto.TileSet;
import de.uniks.stpmon.team_m.rest.PresetsApiService;
import de.uniks.stpmon.team_m.utils.ImageProcessor;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.image.Image;
import okhttp3.ResponseBody;

import javax.inject.Inject;
import java.util.List;

public class PresetsService {
    private final PresetsApiService presetsApiService;

    /**
     * MonstersService handles the communication with the backend for the monsters.
     */

    @Inject
    public PresetsService(PresetsApiService presetsApiService) {
        this.presetsApiService = presetsApiService;
    }

    /**
     * getTileset(-Image) returns a tileset in JSON format, or a tile image PNG.
     * Saved in a ResponseBody to be able to processed as needed.
     * Example for image to JavaFX image is found in ImageProcessor.java
     *
     * @param filename The filename of the tileset.
     * @return The tileset.
     */

    public Observable<Image> getTilesetImage(String filename) {
        return presetsApiService.getTileset(filename + ".png").map(ImageProcessor::resonseBodyToJavaFXImage);
    }

    public Observable<TileSet> getTileset(String filename) {
        return presetsApiService.getTileset(filename + ".json").map(responseBody -> new Gson().fromJson(responseBody.string(), TileSet.class));
    }

    /**
     * getCharacters returns a list of character filenames.
     *
     * @return The list of character filenames.
     */

    public Observable<List<String>> getCharacters() {
        return presetsApiService.getCharacters();
    }

    /**
     * getCharacter returns a character image PNG.
     * Saved in a ResponseBody to be able to processed as needed.
     * Example for image to JavaFX image is found in ImageProcessor.java
     * @param filename The filename of the character.
     * @return The character.
     */

    public Observable<ResponseBody> getCharacter(String filename) {
        return presetsApiService.getCharacter(filename);
    }

    /**
     * getMonsters returns a list of monster types.
     *
     * @return The list of monster types.
     */

    public Observable<List<MonsterTypeDto>> getMonsters() {
        return presetsApiService.getMonsters();
    }

    /**
     * getMonster returns a specific monster type.
     *
     * @param id The id of the monster type.
     * @return The monster type.
     */

    public Observable<MonsterTypeDto> getMonster(int id) {
        return presetsApiService.getMonster(id);
    }

    /**
     * getMonsterImage returns a specific monster type image PNG.
     * Saved in a ResponseBody to be able to processed as needed.
     * Example for image to JavaFX image is found in ImageProcessor.java
     * @param id The id of the monster type.
     * @return The monster type image.
     */

    public Observable<ResponseBody> getMonsterImage(int id) {
        return presetsApiService.getMonsterImage(id);
    }

    /**
     * getAbilities returns a list of abilities.
     *
     * @return The list of abilities.
     */

    public Observable<List<AbilityDto>> getAbilities() {
        return presetsApiService.getAbilities();
    }

    /**
     * getAbility returns a specific ability.
     *
     * @param id The id of the ability.
     * @return The ability.
     */

    public Observable<AbilityDto> getAbility(int id) {
        return presetsApiService.getAbility(id);
    }
}
