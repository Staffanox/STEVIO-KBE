package htwb.ai.stevio.dao;

import htwb.ai.stevio.model.User;

import java.util.HashMap;

public interface IAuthenticator {

    boolean authenticate(String token);
    String createToken(User user);
    HashMap<User,String> getMap();

}
