package htwb.ai.stevio.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import htwb.ai.stevio.dao.ISongsDAO;
import htwb.ai.stevio.model.Song;

public class TestSongDAO implements ISongsDAO {

    private final Map<Integer, Song> mySongs;

    public TestSongDAO() {

        mySongs = new ConcurrentHashMap<>();

        Song song = new Song(10, "7 Years", "Lukas Graham", "Lukas Graham (Blue Album)", 2015);
        mySongs.put(song.getId(), song);

    }

    @Override
    public List<Song> getAllSongs() {
        return new ArrayList<>(mySongs.values());
    }

    @Override
    public Song getSongById(int id) {
        Collection<Song> allSongs = mySongs.values();
        for(Song u : allSongs) {
            if (u.getId() == id) {return u;}
        }
        return null;
    }

    @Override
    public Integer addSong(Song song) {
        mySongs.put(song.getId(), song);
        return song.getId();
    }

    @Override
    public void updateSong(Song song) {
        mySongs.put(song.getId(), song);
    }

    @Override
    public void deleteSong(Song song) {
        mySongs.remove(song.getId());
    }
}
