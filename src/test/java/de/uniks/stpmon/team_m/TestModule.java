package de.uniks.stpmon.team_m;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.Module;
import dagger.Provides;
import de.uniks.stpmon.team_m.dto.*;
import de.uniks.stpmon.team_m.rest.*;
import de.uniks.stpmon.team_m.utils.ImageProcessor;
import de.uniks.stpmon.team_m.utils.TrainerStorage;
import de.uniks.stpmon.team_m.utils.UserStorage;
import de.uniks.stpmon.team_m.ws.EventListener;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.inject.Provider;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import static org.mockito.Mockito.mock;

@Module
public class TestModule {
    @Provides
    static Preferences prefs() {
        return mock(Preferences.class);
    }

    @Provides
    static ResourceBundle resourceBundle() {
        return ResourceBundle.getBundle("de/uniks/stpmon/team_m/lang/lang", Locale.forLanguageTag("en"));
    }

    @Provides
    ObjectMapper mapper() {
        return new ObjectMapper()
                .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .setSerializationInclusion(JsonInclude.Include.NON_ABSENT);
    }

    @Provides
    static EventListener eventListener() {
        return new EventListener(null, null) {
            @Override
            public <T> Observable<Event<T>> listen(String pattern, Class<T> type) {
                Subject<Event<T>> eventSubject;
                if (pattern.equals(("groups." + "64610ec8420b3d786212aea8" + "messages" + "6475e50ed2b996624960cc2c" + "created"))) {
                    eventSubject = PublishSubject.create();
                    return eventSubject;
                }
                return Observable.empty();
            }

            @Override
            public void send(Object object) {
            }
        };
    }

    @Provides
    static AuthApiService authApiService() {
        return new AuthApiService() {
            @Override
            public Observable<LoginResult> login(LoginDto dto) {
                return Observable.just(new LoginResult("6475e51abff65ded36a854ae",
                        "TestUser",
                        "online",
                        null,
                        null,
                        "6475ee91c3cd3c45a009060d",
                        "6475ee9744d0553374489d56"));
            }

            @Override
            public Observable<LoginResult> refresh(RefreshDto dto) {
                return Observable.just(new LoginResult("6475e51abff65ded36a854ae", "TestUser", "online", null, null, "6475ee91c3cd3c45a009060d", "6475ee9744d0553374489d56"));
            }

            @Override
            public Observable<LogoutResult> logout() {
                return Observable.just(new LogoutResult());
            }
        };
    }

    @Provides
    static GroupsApiService groupsApiService() {
        return new GroupsApiService() {
            @Override
            public Observable<Group> create(CreateGroupDto dto) {
                return Observable.just(new Group("6475e39e5ec1ca327fad76cc", "TestGroup", List.of("6475e51abff65ded36a854ae", "6475e423f0c71676ff8a9e16")));
            }

            @Override
            public Observable<List<Group>> getGroups(String ids) {
                return Observable.just(
                        List.of(new Group("64610ec8420b3d786212aea8", "AlreadyCreatedGroup", List.of("6475e51abff65ded36a854ae", "6475e45f6aa7df2539471876")))
                );
            }

            @Override
            public Observable<Group> getGroup(String id) {
                return Observable.just(new Group("64610ec8420b3d786212aea8", "AlreadyCreatedGroup", List.of("6475e51abff65ded36a854ae", "6475e45f6aa7df2539471876")));
            }

            @Override
            public Observable<Group> update(String _id, UpdateGroupDto dto) {
                return Observable.just(new Group("6475e39e5ec1ca327fad76cc", "TestGroupUpdated", List.of("6475e51abff65ded36a854ae", "6475e423f0c71676ff8a9e16")));
            }

            @Override
            public Observable<Group> delete(String _id) {
                return Observable.just(new Group("6475e39e5ec1ca327fad76cc", "TestGroup", List.of("6475e51abff65ded36a854ae")));
            }
        };
    }

    @Provides
    static MessagesApiService messagesApiService() {
        return new MessagesApiService() {
            @Override
            public Observable<Message> create(String namespace, String parent, CreateMessageDto dto) {
                return Observable.just(new Message("2023-05-31T12:50:57.510Z", "2023-05-31T12:50:57.510Z", "6475e58f40b88eaff651b164",
                        "6475e51abff65ded36a854ae", "This is a created test message 1."));
            }

            @Override
            public Observable<List<Message>> getMessages(String namespace, String parent) {
                return Observable.just(List.of(
                        new Message("2023-05-30T12:01:57.510Z", "2023-05-30T12:01:57.510Z", "6475e595ac3946b6a812d863",
                                "6475e51abff65ded36a854ae", "This is an already created test message 1."),
                        new Message("2023-05-31T12:01:57.510Z", "2023-05-31T12:01:57.510Z", "6475e59dd60f90d6f550dd2d",
                                "6475e59dd60f90d6f550dd2d", "This is an already created test message 2.")
                ));
            }

            @Override
            public Observable<Message> getMessage(String namespace, String parent, String id) {
                return Observable.just(
                        new Message("2023-05-30T12:00:57.510Z", "2023-05-30T12:00:57.510Z", "6475e58f40b88eaff651b164",
                                "6475e51abff65ded36a854ae", "This is an already created test message 1.")
                );
            }

            @Override
            public Observable<Message> update(String namespace, String parent, String id, UpdateMessageDto dto) {
                return Observable.just(
                        new Message("2023-05-30T12:01:57.510Z", "2023-05-30T12:01:57.510Z", "6475e595ac3946b6a812d863",
                                "6475e51abff65ded36a854ae", "This is an already created test message 2.")
                );
            }

            @Override
            public Observable<Message> delete(String namespace, String parent, String id) {
                return Observable.just(
                        new Message("2023-05-31T12:01:57.510Z", "2023-05-31T12:01:57.510Z", "6475e59dd60f90d6f550dd2d",
                                "6475e59dd60f90d6f550dd2d", "This is an already created test message 3.")
                );
            }
        };
    }

    @Provides
    static RegionsApiService regionsApiService() {
        return new RegionsApiService() {
            @Override
            public Observable<List<Region>> getRegions() {
                return Observable.just(List.of(new Region("2023-05-22T17:51:46.772Z",
                        "2023-05-22T17:51:46.772Z", "646bc436cfee07c0e408466f", "Albertina", new Spawn("Albertina", 1, 1), new Map(
                        -1,
                        true,
                        1,
                        1,
                        "orthogonal",
                        "right-down",
                        "1.6.1",
                        "map",
                        "1.6",
                        32,
                        32,
                        List.of(),
                        16,
                        16,
                        List.of(),
                        List.of()))));
            }

            @Override
            public Observable<Region> getRegion(String id) {
                return Observable.just(new Region("2023-05-22T17:51:46.772Z",
                        "2023-05-22T17:51:46.772Z", "646bc436cfee07c0e408466f", "Albertina", new Spawn("Albertina", 1, 1), new Map(
                        -1,
                        true,
                        1,
                        1,
                        "orthogonal",
                        "right-down",
                        "1.6.1",
                        "map",
                        "1.6",
                        32,
                        32,
                        List.of(),
                        16,
                        16,
                        List.of(),
                        List.of())));
            }
        };
    }

    @Provides
    static UsersApiService usersApiService() {
        return new UsersApiService() {
            @Override
            public Observable<User> createUser(CreateUserDto dto) {
                return Observable.just(new User("6475e51abff65ded36a854ae", "TestUser", "online", null, null));
            }

            @Override
            public Observable<List<User>> getUsers(List<String> ids, String status) {
                return Observable.just(List.of(
                        new User("6475e6121a0f21b9cd9fa708", "TestFriend 1", "online", null, null),
                        new User("6475e6259cb7e1e7606c0dc6", "TestFriend 2", "online", null, null),
                        new User("6475e6325ec09749507c3848", "TestFriend 3", "offline", null, null),
                        new User("6475e51abff65ded36a854ae", "TestUser", "online", null, null)
                ));
            }

            @Override
            public Observable<User> getUser(String id) {
                if (id.equals("6475e6121a0f21b9cd9fa708")) {
                    return Observable.just(new User("6475e6121a0f21b9cd9fa708", "TestFriend 1", "online", null, null));
                } else if (id.equals("6475e6259cb7e1e7606c0dc6")) {
                    return Observable.just(new User("6475e6259cb7e1e7606c0dc6", "TestFriend 2", "online", null, null));
                } else {
                    return Observable.just(new User("6475e6325ec09749507c3848", "TestFriend 3", "offline", null, null));
                }
            }

            @Override
            public Observable<User> updateUser(String id, UpdateUserDto dto) {
                if (dto.status().equals("offline")) {
                    return Observable.just(new User("6475e51abff65ded36a854ae", "TestUser", "online", null, List.of(
                            "6475e6259cb7e1e7606c0dc6", "6475e6121a0f21b9cd9fa708")));
                } else {
                    return Observable.just(new User("6475e51abff65ded36a854ae", "TestUser", "offline", null, List.of(
                            "6475e6259cb7e1e7606c0dc6", "6475e6121a0f21b9cd9fa708")));
                }
            }

            @Override
            public Observable<User> deleteUser(String id) {
                return Observable.just(new User("6475e51abff65ded36a854ae", "TestUser", "offline", null, List.of(
                        "6475e6259cb7e1e7606c0dc6", "6475e6121a0f21b9cd9fa708")));
            }
        };
    }

    @Provides
    static UserStorage userStorage() {
        return new UserStorage() {
            @Override
            public List<String> getFriends() {
                return List.of("645cd04c11b590456276e9d9", "645cd086f249626b1eefa92e", "645cd0a34389d5c06620fe64");
            }

            @Override
            public String get_id() {
                return "6475e51abff65ded36a854ae";
            }
        };
    }

    @Provides
    static FileChooser fileChooser() {
        return new FileChooser();
    }

    @Provides
    static PresetsApiService presetsApiService() {
        return new PresetsApiService() {
            @Override
            public Observable<ResponseBody> getTileset(String filename) {
                return null;
            }

            @Override
            public Observable<List<String>> getCharacters() {
                return Observable.just(List.of());
            }

            @Override
            public Observable<ResponseBody> getCharacter(String filename) {
                return Observable.just(new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                });
            }

            @Override
            public Observable<List<MonsterTypeDto>> getMonsters() {
                return null;
            }

            @Override
            public Observable<MonsterTypeDto> getMonster(int id) {
                return null;
            }

            @Override
            public Observable<ResponseBody> getMonsterImage(int id) {
                return null;
            }

            @Override
            public Observable<List<AbilityDto>> getAbilities() {
                return null;
            }

            @Override
            public Observable<AbilityDto> getAbility(int id) {
                return null;
            }
        };
    }

    @Provides
    static TrainerStorage trainerStorage() {
        return new TrainerStorage() {
            @Override
            public void setTrainer(Trainer trainer) {
            }

            @Override
            public void setRegion(Region region) {
            }

            @Override
            public void setTrainerName(String name) {
            }

            @Override
            public void setTrainerSprite(String url) {
            }

            @Override
            public Trainer getTrainer() {
                return new Trainer("123", "456", "789", "test", "max", "mustermann", getTrainerSprite(), 0, "Testina", 0, 0, 0, null);
            }

            @Override
            public Region getRegion() {
                return new Region("123", "456", "789", "test", new Spawn("Testina", 0, 0), null);
            }

            @Override
            public String getTrainerName() {
                return "Test";
            }

            @Override
            public String getTrainerSprite() {
                return Objects.requireNonNull(Main.class.getResource("charactermodels/Premade_Character_01.png")).toString();
            }

            @Override
            public Image getTrainerSpriteChunk() {
                String path = Objects.requireNonNull(Main.class.getResource("charactermodels/Premade_Character_01.png")).toString();
                return new Image(path);
            }

            @Override
            public void setTrainerSpriteChunk(Image trainerSpriteChunk) {
            }
        };
    }

    @Provides
    static TrainersApiService trainersApiService() {
        return new TrainersApiService() {
            @Override
            public Observable<Trainer> createTrainer(String regionId, CreateTrainerDto createTrainerDto) {
                return Observable.just(new Trainer("2023-05-22T17:51:46.772Z",
                        "2023-05-22T17:51:46.772Z",
                        "646bac223b4804b87c0b8054",
                        "646bab5cecf584e1be02598a",
                        "646bac8c1a74032c70fffe24",
                        "Hans",
                        "Premade_Character_01.png",
                        0,
                        "646bacc568933551792bf3d5",
                        0,
                        0,
                        0,
                        new NPCInfo(false)));
            }

            @Override
            public Observable<List<Trainer>> getTrainers(String regionId, String area, String id) {
                return Observable.just(List.of());
            }

            @Override
            public Observable<Trainer> getTrainer(String regionId, String _id) {
                return Observable.just(new Trainer("123", "456", "789", "test", "max", "mustermann", Objects.requireNonNull(Main.class.getResource("charactermodels/Premade_Character_01.png")).toString(), 0, "Testina", 0, 0, 0, null));
            }

            @Override
            public Observable<Trainer> deleteTrainer(String regionId, String _id) {
                return null;
            }
        };
    }

    @Provides
    static AreasApiService areasApiService() {
        return new AreasApiService() {

            @Override
            public Observable<List<Area>> getAreas(String regionId) {
                return Observable.just(List.of(
                        new Area(
                                "2023-05-22T17:51:46.772Z",
                                "2023-05-22T17:51:46.772Z",
                                "646bc3c0a9ac1b375fb41d93",
                                "646bc436cfee07c0e408466f",
                                "Albertina",
                                new Map(
                                        -1,
                                        true,
                                        1,
                                        1,
                                        "orthogonal",
                                        "right-down",
                                        "1.6.1",
                                        "map",
                                        "1.6",
                                        32,
                                        32,
                                        List.of(),
                                        16,
                                        16,
                                        List.of(),
                                        List.of()))

                ));
            }

            @Override
            public Observable<Area> getArea(String regionId, String _id) {
                return Observable.just(
                        new Area(
                                "2023-05-22T17:51:46.772Z",
                                "2023-05-22T17:51:46.772Z",
                                "646bc3c0a9ac1b375fb41d93",
                                "646bc436cfee07c0e408466f",
                                "Albertina",
                                new Map(
                                        -1,
                                        true,
                                        1,
                                        1,
                                        "orthogonal",
                                        "right-down",
                                        "1.6.1",
                                        "map",
                                        "1.6",
                                        32,
                                        32,
                                        List.of(),
                                        16,
                                        16,
                                        List.of(),
                                        List.of()))

                );
            }
        };
    }
}
