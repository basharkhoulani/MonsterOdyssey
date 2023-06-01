package de.uniks.stpmon.team_m.rest;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface PresetsApiSrvice {
    @GET("presets/characters")
    Observable<List<String>> getCharacters();
    @GET("presets/characters/{filename}")
    Observable<ResponseBody> getCharacter(@Path("filename") String filename);
}
