package htwb.ai.stevio.controller;

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
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "songLists")
public class SongListController {

    @Autowired
    private ISongListDAO songListDAO;

    @Autowired
    private ISongsDAO songsDao;

    @Autowired
    private IAuthenticator authenticator;

    public SongListController(ISongsDAO sDAO, IAuthenticator authenticator, ISongListDAO slDAO) {
        this.songsDao = sDAO;
        this.authenticator = authenticator;
        this.songListDAO = slDAO;
    }

    private void sortSongList(SongList songs) {
        List<Song> songliste = songs.getSongList().stream().collect(Collectors.toList());
        Collections.sort(songliste);
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<SongList> getId(@PathVariable(value = "id") int id, @RequestHeader("Authorization") String authorization) {
        if (id < 0) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        User user = getUserByToken(authorization);

        if (user != null) {

            SongList songs = songListDAO.getSongList(id);

            if (songs != null) {

                sortSongList(songs);

                if (songs.getOwnerId().equals(user.getUserId()))
                    return new ResponseEntity<>(songs, HttpStatus.ACCEPTED);
                else {
                    if (songs.getisPrivate())
                        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
                    else
                        return new ResponseEntity<>(songs, HttpStatus.ACCEPTED);
                }
            }
        }
        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }

    // todo right path
    //songLists?userId=xyz
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<SongList>> getAll(@RequestParam(value = "userId") String userId, @RequestHeader("Authorization") String authorization) {

        User user = getUserByToken(authorization);
        if (user != null) {

            List<SongList> songs = songListDAO.getSongList(userId);

            if(songs != null){
                for(SongList sl : songs){
                    sortSongList(sl);
                }
            }

            if(songs.size() == 0){
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            if (user.getUserId().equals(userId)) {
                return new ResponseEntity<>(songs, HttpStatus.ACCEPTED);
            } else {
                List<SongList> returnList = new LinkedList<>();
                for (SongList songList : songs) {
                    if (!songList.getisPrivate())
                        returnList.add(songList);
                }
                return new ResponseEntity<>(returnList, HttpStatus.ACCEPTED);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    //POST ../rest/songList
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postSong(@RequestBody SongList songList, HttpServletRequest
            request, @RequestHeader("Authorization") String authorization) {
        User user = getUserByToken(authorization);

        if (user == null) {
            return new ResponseEntity<>("Can't find user", HttpStatus.BAD_REQUEST);

        }
        if (songList == null) {
            return new ResponseEntity<>("No songlist provided", HttpStatus.BAD_REQUEST);
        }
        if (songList.getSongList() == null || songList.getSongList().isEmpty()) {
            return new ResponseEntity<>("Songlist is empty", HttpStatus.BAD_REQUEST);
        }
        if (songList.getisPrivate() == null) {
            return new ResponseEntity<>("No visibility provided", HttpStatus.BAD_REQUEST);
        }

        Set<Song> songsFromPayload = songList.getSongList();
        for (Song song : songsFromPayload) {
            Song temp = songsDao.getSongById(song.getId());
            if (temp == null) {
                return new ResponseEntity<>("Song not in DB", HttpStatus.BAD_REQUEST);
            }
            if (!song.getTitle().equals(temp.getTitle()) || !song.getArtist().equals(temp.getArtist()) || !song.getLabel().equals(temp.getLabel()) || song.getId() != temp.getId() || song.getReleased() != temp.getReleased()) {
                return new ResponseEntity<>("Song not in DB", HttpStatus.BAD_REQUEST);
            }
        }

        songList.setUser(user.getUserId());
        songListDAO.addSongList(songList);

        URI location = URI.create(request.getRequestURI() + "/" + songList.getId());
        return ResponseEntity.created(location).body(null);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteSong(@PathVariable(value = "id") Integer
                                                     id, @RequestHeader("Authorization") String authorization) {
        if (id < 0) {
            return new ResponseEntity<>("ID cant be less than 0. Your ID: " + id, HttpStatus.BAD_REQUEST);
        }

        User user = getUserByToken(authorization);
        if (user == null) {
            return new ResponseEntity<>("Invalid user", HttpStatus.BAD_REQUEST);
        }

        SongList songList = songListDAO.getSongList(id);
        if (songList != null) {
            if (user.getUserId().equals(songList.getOwnerId())) {
                songListDAO.deleteSong(songList);
                return new ResponseEntity<>("Song with ID '" + id + "' was deleted.", HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    private User getUserByToken(String token) {
        Map<User, String> map = authenticator.getMap();
        return map.keySet().stream().filter(user -> token.equals(authenticator.getMap().get(user))).findFirst().orElse(null);
    }
}
