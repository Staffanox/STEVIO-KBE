package htwb.ai.stevio.dao;

import htwb.ai.stevio.model.Song;
import htwb.ai.stevio.model.SongList;

import javax.persistence.*;
import java.util.List;

public class DBSongListDAO implements ISongListDAO {
    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("STEVIO-KBE");


    @Override
    public List<SongList> getSongList(String ownerId) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        Query q = em.createQuery("SELECT s FROM SongList s WHERE s.user.id = ownerId");
        return q.getResultList();
    }

    @Override
    public SongList getSongList(int id) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        return em.find(SongList.class, id);
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
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();

        SongList s = em.find(SongList.class, songList.getId());
        em.getTransaction().begin();
        em.remove(s);
        em.getTransaction().commit();
        em.close();
    }
}
