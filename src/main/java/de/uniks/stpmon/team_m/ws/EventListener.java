package de.uniks.stpmon.team_m.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.stpmon.team_m.dto.Event;
import de.uniks.stpmon.team_m.utils.TokenStorage;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import static de.uniks.stpmon.team_m.Constants.WS_URL;

@Singleton
public class EventListener {
    private final TokenStorage tokenStorage;
    private final ObjectMapper mapper;
    private ClientEndpoint endpoint;

    @Inject
    public EventListener(TokenStorage tokenStorage, ObjectMapper mapper) {
        this.tokenStorage = tokenStorage;
        this.mapper = mapper;
    }

    private void ensureOpen() {
        if (endpoint != null && endpoint.isOpen()) {
            return;
        }

        final URI endpointURI;
        try {
            endpointURI = new URI(WS_URL + "/events?authToken=" + tokenStorage.getToken());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        endpoint = new ClientEndpoint(endpointURI);
        endpoint.open();
    }

    public <T> Observable<Event<T>> listen(String pattern, Class<T> type) {
        return Observable.create(emitter -> {
            this.ensureOpen();
            send(new Event<>("subscribe", pattern));
            final Consumer<String> handler = createPatternHandler(mapper, pattern, type, emitter);
            endpoint.addMessageHandler(handler);
            emitter.setCancellable(() -> removeEventHandler(pattern, handler));
        });
    }

    public static <T> Consumer<String> createPatternHandler(ObjectMapper mapper, String pattern, Class<T> type, ObservableEmitter<Event<T>> emitter) {
        final Pattern regex = Pattern.compile(pattern.replace(".", "\\.").replace("*", "[^.]*"));
        return eventStr -> {
            try {
                final JsonNode node = mapper.readTree(eventStr);
                final String event = node.get("event").asText();
                if (!regex.matcher(event).matches()) {
                    return;
                }

                final T data = mapper.treeToValue(node.get("data"), type);
                emitter.onNext(new Event<>(event, data));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        };
    }

    private void removeEventHandler(String pattern, Consumer<String> handler) {
        if (endpoint == null) {
            return;
        }

        send(new Event<>("unsubscribe", pattern));
        endpoint.removeMessageHandler(handler);
        if (!endpoint.hasMessageHandler()) {
            close();
        }
    }

    public void send(Object data) {
        this.ensureOpen();
        try {
            final String msg = mapper.writeValueAsString(data);
            endpoint.sendMessage(msg);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void close() {
        if (endpoint != null) {
            endpoint.close();
            endpoint = null;
        }
    }
}
