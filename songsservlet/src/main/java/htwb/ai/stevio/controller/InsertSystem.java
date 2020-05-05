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

    public static void main(String[] args) {
        addSong("Sog","Son1","lel",20);
        ENTITY_MANAGER_FACTORY.close();
    }

    public static void addSong(String title, String artist, String label, int released) {

        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();

        EntityTransaction et = null;

        try {
            // Get transaction and start
            et = em.getTransaction();
            et.begin();

            // Create and set values for new customer
            Song song = new Song();
            song.setTitle(title);
            song.setArtist(artist);
            song.setLabel(label);
            song.setReleased(released);

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

    public static void getSong(int id) {
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
            System.out.println(song.getTitle() + " " + song.getArtist() + " " + song.getLabel() + " " + song.getReleased());
        } catch (NoResultException ex) {
            ex.printStackTrace();
        } finally {
            em.close();
        }
    }

    public static void getSongs() {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();


        String strQuery = "SELECT c FROM Song c WHERE c.id IS NOT NULL";

        TypedQuery<Song> tq = em.createQuery(strQuery, Song.class);
        List<Song> songs;
        try {
            songs = tq.getResultList();
            songs.forEach(song -> System.out.println(song.getTitle() + " " + song.getArtist() + " " + song.getLabel() + " " + song.getReleased()));
        } catch (NoResultException ex) {
            ex.printStackTrace();
        } finally {
            em.close();
        }
    }
}
