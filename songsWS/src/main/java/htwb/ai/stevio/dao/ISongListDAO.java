package htwb.ai.stevio.dao;

/*
 *
 * @author Steven Schuette
 */

import htwb.ai.stevio.model.SongList;

import java.util.List;


public interface ISongListDAO {

    List<SongList> getSongList(String ownerId);

    SongList getSongList(int id);

    void addSongList(SongList songList);

    void deleteSong(SongList songList);


}
