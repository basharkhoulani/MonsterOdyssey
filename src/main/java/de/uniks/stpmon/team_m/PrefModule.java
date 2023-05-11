package de.uniks.stpmon.team_m;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;
import java.util.prefs.Preferences;

@Module
public class PrefModule {
    @Provides
    @Singleton
    Preferences prefs() {
        return Preferences.userNodeForPackage(Main.class);
    }
}
