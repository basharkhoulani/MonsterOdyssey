package de.uniks.stpmon.team_m.rest;

import de.uniks.stpmon.team_m.dto.AbilityDto;
import de.uniks.stpmon.team_m.dto.ItemTypeDto;
import de.uniks.stpmon.team_m.dto.MonsterTypeDto;
import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface PresetsApiService {
    @GET("presets/tilesets/{filename}")
    Observable<ResponseBody> getTileset(@Path("filename") String ignoredFilename);
    @GET("presets/characters")
    Observable<List<String>> getCharacters();
    @GET("presets/characters/{filename}")
    Observable<ResponseBody> getCharacter(@Path("filename") String ignoredFilename);
    @GET("presets/items")
    Observable<List<ItemTypeDto>> getItems();
    @GET("presets/items/{id}")
    Observable<ItemTypeDto> getItem(@Path("id") int ignoredId);
    @GET("presets/items/{id}/image")
    Observable<ResponseBody> getItemImage(@Path("id") int ignoredId);
    @GET("presets/monsters")
    Observable<List<MonsterTypeDto>> getMonsters();
    @GET("presets/monsters/{id}")
    Observable<MonsterTypeDto> getMonster(@Path("id") int ignoredId);
    @GET("presets/monsters/{id}/image")
    Observable<ResponseBody> getMonsterImage(@Path("id") int ignoredId);
    @GET("presets/abilities")
    Observable<List<AbilityDto>> getAbilities();
    @GET("presets/abilities/{id}")
    Observable<AbilityDto> getAbility(@Path("id") int ignoredId);
}
