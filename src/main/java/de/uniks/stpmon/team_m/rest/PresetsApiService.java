package de.uniks.stpmon.team_m.rest;

import de.uniks.stpmon.team_m.dto.AbilityDto;
import de.uniks.stpmon.team_m.dto.MonsterTypeDto;
import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface PresetsApiService {
    @GET("presets/tilesets/{filename}")
    Observable<ResponseBody> getTileset(@Path("filename") String filename);
    @GET("presets/characters")
    Observable<List<String>> getCharacters();
    @GET("presets/characters/{filename}")
    Observable<ResponseBody> getCharacter(@Path("filename") String filename);
    @GET("presets/monsters")
    Observable<List<MonsterTypeDto>> getMonsters();
    @GET("presets/monsters/{id}")
    Observable<MonsterTypeDto> getMonster(@Path("id") int id);
    @GET("presets/monsters/{id}/image")
    Observable<ResponseBody> getMonsterImage(@Path("id") String id);
    @GET("presets/abilities")
    Observable<List<AbilityDto>> getAbilities();
    @GET("presets/abilities/{id}")
    Observable<AbilityDto> getAbility(@Path("id") int id);
}
