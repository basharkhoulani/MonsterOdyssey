package de.uniks.stpmon.team_m;

import dagger.BindsInstance;
import dagger.Component;
import de.uniks.stpmon.team_m.controller.LoginController;
import de.uniks.stpmon.team_m.controller.MainMenuController;
import de.uniks.stpmon.team_m.service.AuthenticationService;

import javax.inject.Singleton;

@Component(modules = {MainModule.class, HttpModule.class, PrefModule.class})
@Singleton
public interface MainComponent {
    AuthenticationService authenticationService();

    LoginController loginController();

    MainMenuController mainMenuController();




    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder mainApp(App app);

        MainComponent build();
    }
}

