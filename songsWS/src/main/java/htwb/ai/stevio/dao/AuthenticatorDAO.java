package htwb.ai.stevio.dao;

import htwb.ai.stevio.model.User;

import java.util.Map;
import java.util.UUID;

import java.util.HashMap;

public class AuthenticatorDAO implements IAuthenticator {

    private HashMap<User, String> tokenMap = new HashMap<User, String>();


    @Override
    public boolean authenticate(String token) {
        return tokenMap.containsValue(token);
    }

    @Override
    public String createToken(User user) {

        String token = UUID.randomUUID().toString();

        token = token.replaceAll("-", "");

        if (token.length() > 18) {
            token = token.substring(0, 17);
        }

        if (!tokenMap.containsKey(user)) {
            this.putTokenInSession(user, token);
        } else
            tokenMap.replace(user, token);

        return token;
    }

    @Override
    public Map<User, String> getMap() {
        return tokenMap;
    }


    private void putTokenInSession(User user, String token) {
        this.tokenMap.put(user, token);
    }

}
