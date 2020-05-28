package htwb.ai.stevio.dao;

import htwb.ai.stevio.model.Song;

import java.util.List;

/*
 *
 * @author Mario Teklic
 */public interface ISongsDAO {

     public List<Song> getAllSongs();
     public Song getSongById(int id);
     public Integer addSong(Song song);
     public void updateSong(Song song);
     public void deleteSong(Song song);

 }
