package de.uniks.stpmon.team_m.rest;

import de.uniks.stpmon.team_m.dto.*;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.*;

import java.util.List;

public interface MessagesApiService {
    @POST("{namespace}/{parent}/messages")
    Observable<Message> create(@Path("namespace") String namespace, @Path("parent") String parent, @Body CreateMessageDto dto);

    // For all groups the current user is member of, pass null as the parameter.
    @GET("{namespace}/{parent}/messages")
    Observable<List<Message>> getMessages(@Path("namespace") String namespace, @Path("parent") String parent);

    @GET("{namespace}/{parent}/messages/{id}")
    Observable<Message> getMessage(@Path("namespace") String namespace, @Path("parent") String parent, @Path("id") String id);

    @PATCH("{namespace}/{parent}/messages/{id}")
    Observable<Message> update(@Path("namespace") String namespace, @Path("parent") String parent,@Path("id") String id, @Body UpdateMessageDto dto);

    @DELETE("{namespace}/{parent}/messages/{id}")
    Observable<Message> delete(@Path("namespace") String namespace, @Path("parent") String parent,@Path("id") String id);
}
