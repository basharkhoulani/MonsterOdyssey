package de.uniks.stpmon.team_m.rest;

import de.uniks.stpmon.team_m.dto.CreateMessageDto;
import de.uniks.stpmon.team_m.dto.Message;
import de.uniks.stpmon.team_m.dto.UpdateMessageDto;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.*;

import java.util.List;

public interface MessagesApiService {
    @POST("{namespace}/{parent}/messages")
    Observable<Message> create(@Path("namespace") String ignoredNamespace, @Path("parent") String ignoredParent, @Body CreateMessageDto ignoredDto);

    // For all groups the current user is member of, pass null as the parameter.
    @GET("{namespace}/{parent}/messages")
    Observable<List<Message>> getMessages(@Path("namespace") String ignoredNamespace, @Path("parent") String ignoredParent);

    @GET("{namespace}/{parent}/messages/{id}")
    Observable<Message> getMessage(@Path("namespace") String ignoredNamespace, @Path("parent") String ignoredParent, @Path("id") String ignoredId);

    @PATCH("{namespace}/{parent}/messages/{id}")
    Observable<Message> update(@Path("namespace") String ignoredNamespace, @Path("parent") String ignoredParent, @Path("id") String ignoredId, @Body UpdateMessageDto ignoredDto);

    @DELETE("{namespace}/{parent}/messages/{id}")
    Observable<Message> delete(@Path("namespace") String ignoredNamespace, @Path("parent") String ignoredParent, @Path("id") String ignoredId);
}
