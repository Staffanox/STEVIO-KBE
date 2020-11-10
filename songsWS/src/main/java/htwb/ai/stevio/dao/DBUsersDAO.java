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

    private EntityManagerFactory emf;

    public DBUsersDAO(String PU){
        emf = Persistence.createEntityManagerFactory(PU);
    }

    public DBUsersDAO(){
        emf = Persistence.createEntityManagerFactory("STEVIO-KBE");
    }

    @Override
    public User getUser(User user) {
        EntityManager em = emf.createEntityManager();
        return em.find(User.class, user.getUserId());
    }
    
}
