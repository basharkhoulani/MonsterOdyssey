package de.uniks.stpmon.team_m;

import dagger.BindsInstance;
import dagger.Component;
import de.uniks.stpmon.team_m.controller.LoginController;

import javax.inject.Singleton;

@Component(modules = {MainModule.class, HttpModule.class, PrefModule.class})
@Singleton
public interface MainComponent {
    // LoginService loginService();
    LoginController loginController();
    // LobbyController lobbyController();

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder mainApp(App app);

        MainComponent build();
    }
}

