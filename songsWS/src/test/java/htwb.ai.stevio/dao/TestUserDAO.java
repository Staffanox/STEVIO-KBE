package htwb.ai.stevio.dao;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import htwb.ai.stevio.dao.IUsersDAO;
import htwb.ai.stevio.model.User;

public class TestUserDAO implements IUsersDAO {

    private Map<String, User> myUsers;

    public TestUserDAO() {

        myUsers = new ConcurrentHashMap<String, User>();

        User alphonso = new User("1","Z","Alphonso","kek");
        myUsers.put(alphonso.getUserId(),alphonso);

    }

    @Override
    public User getUser(User user) {
        return myUsers.get(user.getUserId());
    }
}
