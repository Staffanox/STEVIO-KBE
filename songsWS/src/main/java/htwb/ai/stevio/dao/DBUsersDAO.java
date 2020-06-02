package htwb.ai.stevio.dao;

import htwb.ai.stevio.model.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/*
 *
 * @author Mario Teklic
 */

public class DBUsersDAO implements IUsersDAO {

    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("STEVIO-KBE-USER");

    @Override
    public User getUser(User user) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        return em.find(User.class, user.getUserId());
    }
}
