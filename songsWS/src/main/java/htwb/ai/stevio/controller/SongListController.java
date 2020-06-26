package htwb.ai.stevio.controller;

import htwb.ai.stevio.dao.DBSongListDAO;
import htwb.ai.stevio.dao.IAuthenticator;
import htwb.ai.stevio.dao.ISongListDAO;
import htwb.ai.stevio.dao.ISongsDAO;
import htwb.ai.stevio.model.Song;
import htwb.ai.stevio.model.SongList;
import htwb.ai.stevio.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(value = "songList")
public class SongListController {

    @Autowired
    ISongListDAO songListDAO;

    @Autowired
    ISongsDAO songsDao;

    @Autowired
    IAuthenticator authenticator;


    public SongListController(ISongsDAO sDAO, IAuthenticator authenticator,ISongListDAO slDAO) {
        this.songsDao = sDAO;
        this.authenticator = authenticator;
        this.songListDAO = slDAO;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postSong(@RequestBody SongList songList, HttpServletRequest request, @RequestHeader("Authorization") String authorization) {
        User user = getUserByToken(authorization);

        if (songList == null) {
            return new ResponseEntity<>("No Songlist", HttpStatus.BAD_REQUEST);
        }
        if (songList.getSongList() == null || songList.getSongList().isEmpty()) {
            return new ResponseEntity<>("No songs selected", HttpStatus.BAD_REQUEST);
        }
        if (songList.getIsPrivate() == null) {
            return new ResponseEntity<>("No State given", HttpStatus.BAD_REQUEST);
        }

        Set<Song> songsFromPayload = songList.getSongList();
        for (Song song : songsFromPayload) {
            Song temp = songsDao.getSongById(song.getId());
            if (temp == null) {
                return new ResponseEntity<>("Song is null", HttpStatus.BAD_REQUEST);
            }
            if (!song.getTitle().equals(temp.getTitle())) {
                return new ResponseEntity<>("Song not in DB", HttpStatus.BAD_REQUEST);
            }
        }

        songList.setUser(user);

        songListDAO.addSongList(songList);

        URI location = URI.create(request.getRequestURI() + "/" + songList.getId());
        return ResponseEntity.created(location).body(null);

    }


    private User getUserByToken(String token) {
        Map<User, String> map = authenticator.getMap();
        return map.keySet().stream().filter(user -> token.equals(authenticator.getMap().get(user))).findFirst().orElse(null);


    }


}
