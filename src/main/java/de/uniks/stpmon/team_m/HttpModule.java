package de.uniks.stpmon.team_m;

import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.Module;
import dagger.Provides;
import de.uniks.stpmon.team_m.rest.*;
import de.uniks.stpmon.team_m.utils.TokenStorage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.inject.Singleton;
import java.io.FileOutputStream;
import java.io.IOException;

@Module
public class HttpModule {
    @Provides
    @Singleton
    static OkHttpClient client(TokenStorage tokenStorage) {
        return new OkHttpClient.Builder().addInterceptor(chain -> {
            final String token = tokenStorage.getToken();
            if (token == null) {
                return chain.proceed(chain.request());
            }
            final Request newRequest = chain
                    .request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer " + token)
                    .build();
            return chain.proceed(newRequest);
        }).build();
    }

    @Provides
    @Singleton
    Retrofit retrofit(OkHttpClient client, ObjectMapper mapper) {
        return new Retrofit.Builder()
                .baseUrl(Constants.API_URL + "/")
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    AuthApiService auth(Retrofit retrofit) {
        return retrofit.create(AuthApiService.class);
    }

    @Provides
    @Singleton
    UsersApiService users(Retrofit retrofit) {
        return retrofit.create(UsersApiService.class);
    }

    @Provides
    @Singleton
    GroupsApiService groups(Retrofit retrofit) {
        return retrofit.create(GroupsApiService.class);
    }

    @Provides
    @Singleton
    MessagesApiService messages(Retrofit retrofit) {
        return retrofit.create(MessagesApiService.class);
    }

    @Provides
    @Singleton
    RegionsApiService regions(Retrofit retrofit) {
        return retrofit.create(RegionsApiService.class);
    }

    @Provides
    @Singleton
    TrainersApiService trainers(Retrofit retrofit) {
        return retrofit.create(TrainersApiService.class);
    }

    @Provides
    @Singleton
    AreasApiService areas(Retrofit retrofit) {
        return retrofit.create(AreasApiService.class);
    }

    @Provides
    @Singleton
    MonstersApiService monsters(Retrofit retrofit) {
        return retrofit.create(MonstersApiService.class);
    }
}
