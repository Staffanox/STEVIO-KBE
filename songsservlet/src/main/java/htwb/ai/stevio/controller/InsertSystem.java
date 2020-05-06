package htwb.ai.stevio.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;


public class InsertSystem {

    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence
            .createEntityManagerFactory("STEVIO-KBE");


    public static void addSong(Song song) {

        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();

        EntityTransaction et = null;

        try {

            et = em.getTransaction();
            et.begin();
            em.persist(song);
            et.commit();
        } catch (Exception ex) {
            if (et != null) {
                et.rollback();
            }
            ex.printStackTrace();
        } finally {
            em.close();
        }
    }

    public static Song getSong(int id) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();

        // the lowercase c refers to the object
        // :custID is a parameterized query thats value is set below
        String query = "SELECT c FROM Song c WHERE c.id = (:id)";

        // Issue the query and get a matching Customer
        TypedQuery<Song> tq = em.createQuery(query, Song.class);
        tq.setParameter("id", id);

        Song song = null;
        try {
            // Get matching customer object and output
            song = tq.getSingleResult();

        } catch (NoResultException ex) {
            ex.printStackTrace();
        } finally {
            em.close();
        }
        return song;
    }

    public static List<Song> getSongs() {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();


        String strQuery = "SELECT c FROM Song c WHERE c.id IS NOT NULL";

        TypedQuery<Song> tq = em.createQuery(strQuery, Song.class);
        List<Song> songs = null;
        try {
            songs = tq.getResultList();
        } catch (NoResultException ex) {
            ex.printStackTrace();
        } finally {
            em.close();
        }
        return songs;
    }



}
