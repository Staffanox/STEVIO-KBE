package htwb.ai.stevio.dao;

import htwb.ai.stevio.model.Song;
import javax.persistence.*;
import java.util.List;

/*
 *
 * @author Mario Teklic
 */public class DBSongsDAO implements ISongsDAO{

     private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("STEVIO-KBE");

     public DBSongsDAO() {
     }

    @Override
    public List<Song> getAllSongs() {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();

        Query q = em.createQuery("SELECT s FROM Song s ORDER BY s.id ASC");
        return q.getResultList();
    }

    @Override
    public Song getSongById(int id) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        return em.find(Song.class, id);
    }

    @Override
    public Integer addSong(Song song) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        //Song s = em.find(Song.class, song);

        em.getTransaction().begin();
        em.persist(song);
        em.getTransaction().commit();
        em.close();
        return song.getId();
    }

    @Override
    public void updateSong(Song song) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();

        em.getTransaction().begin();
        em.merge(song);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void deleteSong(Song song) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();

        Song s = em.find(Song.class, song.getId());
        em.getTransaction().begin();
        em.remove(s);
        em.getTransaction().commit();
        em.close();
    }
}
