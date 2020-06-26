package htwb.ai.stevio.dao;

import htwb.ai.stevio.model.User;

import java.util.Map;

/*
 *
 * @author Steven Schuette
 */

public interface IAuthenticator {

    boolean authenticate(String token);
    String createToken(User user);
    Map<User,String> getMap();

}
