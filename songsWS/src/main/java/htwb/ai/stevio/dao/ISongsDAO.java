package htwb.ai.stevio.dao;

import htwb.ai.stevio.model.Song;

import java.util.List;

/*
 *
 * @author Mario Teklic
 */public interface ISongsDAO {

     List<Song> getAllSongs();
     Song getSongById(int id);
     Integer addSong(Song song);
     void updateSong(Song song);
     void deleteSong(Song song);

 }
