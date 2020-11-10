package htwb.ai.stevio.dao;

import htwb.ai.stevio.model.Song;
import javax.persistence.*;
import java.util.List;

/*
 *
 * @author Mario Teklic
 */public class DBSongsDAO implements ISongsDAO{

     private EntityManagerFactory emf;

     public DBSongsDAO() {
         emf = Persistence.createEntityManagerFactory("STEVIO-KBE");
     }

     public DBSongsDAO(String PU){
         emf = Persistence.createEntityManagerFactory(PU);
     }

    @Override
    public List<Song> getAllSongs() {
        EntityManager em = emf.createEntityManager();

        Query q = em.createQuery("SELECT s FROM Song s ORDER BY s.id ASC");
        return q.getResultList();
    }

    @Override
    public Song getSongById(int id) {
        EntityManager em = emf.createEntityManager();
        return em.find(Song.class, id);
    }

    @Override
    public Integer addSong(Song song) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(song);
        em.getTransaction().commit();
        em.close();
        return song.getId();
    }

    @Override
    public void updateSong(Song song) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(song);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void deleteSong(Song song) {
        EntityManager em = emf.createEntityManager();

        Song s = em.find(Song.class, song.getId());
        em.getTransaction().begin();
        em.remove(s);
        em.getTransaction().commit();
        em.close();
    }
}
