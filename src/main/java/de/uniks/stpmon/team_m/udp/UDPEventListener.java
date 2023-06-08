package de.uniks.stpmon.team_m.udp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.stpmon.team_m.dto.Event;
import de.uniks.stpmon.team_m.dto.MoveTrainerDto;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.InetAddress;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import static de.uniks.stpmon.team_m.Constants.SERVER_PORT;
import static de.uniks.stpmon.team_m.Constants.UDP_URL;

@Singleton
public class UDPEventListener {
    private final ObjectMapper mapper;
    private UDPClient client;

    @Inject
    public UDPEventListener(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    private void ensureOpen() {
        if (client != null && client.isOpen()) {
            return;
        }
        try {
            InetAddress address = InetAddress.getByName(UDP_URL);
            client = new UDPClient(address, SERVER_PORT);
            client.open();
            client.startReceiving();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> Observable<Event<T>> listen(String pattern, Class<T> type) {
        return Observable.create(emitter -> {
            this.ensureOpen();
            send(new Event<>("subscribe", pattern));
            final Consumer<String> handler = createPatternHandler(mapper, pattern, type, emitter);
            client.addMessageHandler(handler);
            emitter.setCancellable(() -> removeEventHandler(pattern, handler));
        });
    }

    public void send(Object data) {
        try {
            String message = mapper.writeValueAsString(data);
            client.sendMessage(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> Consumer<String> createPatternHandler(ObjectMapper mapper, String pattern, Class<T> type, ObservableEmitter<Event<T>> emitter) {
        return eventStr -> {
            final Pattern regex = Pattern.compile(pattern.replace(".", "\\.").replace("*", "[^.]*"));
            try {
                final JsonNode node = mapper.readTree(eventStr);
                final String event = node.get("event").asText();
                if (!regex.matcher(event).matches()) {
                    return;
                }

                final T data = mapper.treeToValue(node.get("data"), type);
                emitter.onNext(new Event<>(event, data));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private void removeEventHandler(String pattern, Consumer<String> handler) {
        if (client == null) {
            return;
        }

        send(new Event<>("unsubscribe", pattern));
        client.removeMessageHandler(handler);
        if (!client.hasMessageHandler()) {
            close();
        }
    }

    private void close() {
        if (client != null) {
            client.close();
            client = null;
        }
    }


    public <T> Observable<Event<T>> move(MoveTrainerDto moveTrainerDto) {
        return Observable.create(emitter -> {
            this.ensureOpen();
            send(new Event<>("areas." + moveTrainerDto.area() + ".trainers." + moveTrainerDto._id() + ".moved", moveTrainerDto));
        });
    }
}
