package htwb.ai.stevio.dao;

import htwb.ai.stevio.model.SongList;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class DBSongListDAO implements ISongListDAO {
         private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("STEVIO-KBE");


    @Override
    public SongList getSongList(String ownerId) {
        return null;
    }

    @Override
    public SongList getSongList(int id) {
        return null;
    }

   @Override
    public void addSongList(SongList songList) {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction entityTransaction = null;

        try {
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();

            entityManager.persist(songList);
            entityTransaction.commit();
        } catch (Exception ex) {
            if (entityTransaction != null) {
                entityTransaction.rollback();
            }
            ex.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void deleteSong(SongList songList) {

    }
}
