package htwb.ai.stevio.dao;

import htwb.ai.stevio.model.Song;
import htwb.ai.stevio.model.SongList;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

public class DBSongListDAO implements ISongListDAO {

    private EntityManagerFactory emf;

    public DBSongListDAO(){
        this.emf = Persistence.createEntityManagerFactory("STEVIO-KBE");
    }

    public DBSongListDAO(String persistenceUnit){
        this.emf = Persistence.createEntityManagerFactory(persistenceUnit);
    }

    @Override
    public List<SongList> getSongList(String ownerId) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT s FROM SongList s WHERE s.ownerId= :ownerId")
                .setParameter("ownerId", ownerId);

        return q.getResultList();
    }


    @Override
    public SongList getSongList(int id) {
        EntityManager em = emf.createEntityManager();

        SongList songList = em.find(SongList.class, id);

        if(songList != null){
            songList.getSongList().stream().sorted().map(o -> o.getId()).collect(Collectors.toSet());
        }

        for(Song s : songList.getSongList()){
            System.out.println("**********aaa");
            System.out.println(s.getId() + " == ID");
            System.out.println("**********aaa");
        }

        return songList;
    }

    @Override
    public int addSongList(SongList songList) {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction entityTransaction = null;

        try {
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();

            entityManager.persist(songList);
            entityTransaction.commit();

            return songList.getId();
        } catch (Exception ex) {
            if (entityTransaction != null) {
                entityTransaction.rollback();
            }
            ex.printStackTrace();

        } finally {
            entityManager.close();
        }
        return -1;
    }

    @Override
    public void deleteSong(SongList songList) {
        EntityManager em = emf.createEntityManager();

        SongList s = em.find(SongList.class, songList.getId());
        em.getTransaction().begin();
        em.remove(s);
        em.getTransaction().commit();
        em.close();
    }
}
