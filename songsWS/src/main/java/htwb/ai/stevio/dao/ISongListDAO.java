package htwb.ai.stevio.dao;

/*
 *
 * @author Steven Schuette
 */

import htwb.ai.stevio.model.SongList;


public interface ISongListDAO {

    SongList getSongList(String ownerId);

    SongList getSongList(int id);

    void addSongList(SongList songList);

    void deleteSong(SongList songList);


}
