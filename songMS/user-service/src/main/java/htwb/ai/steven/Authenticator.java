package htwb.ai.steven;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

import java.util.HashMap;

@Configuration
@ComponentScan(basePackages = "htwb.ai.steven")
public class Authenticator {

    private HashMap<String, String> tokenMap = new HashMap<String, String>();


    public boolean authenticate(String token) {
        return tokenMap.containsValue(token);
    }


    public String createToken(User user) {

        String token = UUID.randomUUID().toString();

        token = token.replaceAll("-", "");

        if (token.length() > 18) {
            token = token.substring(0, 17);
        }

        if (!tokenMap.containsKey(user.getUserId())) {
            this.putTokenInSession(user, token);
        } else
            tokenMap.replace(user.getUserId(), token);

        return token;
    }


    public Map<String, String> getMap() {
        return tokenMap;
    }


    private void putTokenInSession(User user, String token) {
        this.tokenMap.put(user.getUserId(), token);
    }

}
