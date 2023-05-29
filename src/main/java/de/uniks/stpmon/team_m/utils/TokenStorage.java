package de.uniks.stpmon.team_m.utils;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TokenStorage {
    private String token;

    /**
     * TokenStorage handles the storage of the token that needs to be used when logging the user in
     * with the Remember Me function.
     */

    @Inject
    public TokenStorage() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
