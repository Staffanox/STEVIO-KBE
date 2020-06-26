package htwb.ai.stevio.controller;

import htwb.ai.stevio.dao.IAuthenticator;
import htwb.ai.stevio.dao.ISongsDAO;
import htwb.ai.stevio.model.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

/*
 *
 * @author Steven Schuette
 */

@RestController
@RequestMapping(value = "songs")
public class SongsController {

    @Autowired
    private final ISongsDAO songsDAO;

    private final IAuthenticator authenticator;

    public SongsController(ISongsDAO sDAO, IAuthenticator authenticator) {
        this.songsDAO = sDAO;
        this.authenticator = authenticator;
    }

    //GET all   ../rest/songs
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Song>> getAll(@RequestHeader("Authorization") String authorization) {
        if (authorization != null) {

            if (authenticator.authenticate(authorization)) {
                List<Song> songs = songsDAO.getAllSongs();
                if (songs != null && songs.size() > 0) {
                    return new ResponseEntity<>(songs, HttpStatus.OK);
                }

                return new ResponseEntity<>(songs, HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

    }


    //GET by id   ../rest/songs/1
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Song> getSong(@PathVariable(value = "id") Integer id, @RequestHeader("Authorization") String authorization) {
        if (id < 0) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        if (authorization != null && authenticator.authenticate(authorization)) {
            Song song = songsDAO.getSongById(id);

            if (song != null) {
                return new ResponseEntity<>(song, HttpStatus.OK);
            }
            return new ResponseEntity<>(song, HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postSong(@RequestBody Song song, HttpServletRequest request, @RequestHeader("Authorization") String authorization) {

        if (song.getTitle() == null || song.getTitle().equals("")) {
            return new ResponseEntity<>("Wrong body: no title", HttpStatus.BAD_REQUEST);
        }
        if (authenticator.authenticate(authorization)) {
            songsDAO.addSong(song);
            URI location = URI.create(request.getRequestURI() + "/" + song.getId());
            return ResponseEntity.created(location).body(null);
            // return new ResponseEntity<>(request.getRequestURL() + "/" + song.getId(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteSong(@PathVariable(value = "id") Integer id, @RequestHeader("Authorization") String authorization) {

        if (id < 0) {
            return new ResponseEntity<>("ID cant be less than 0. Your ID: " + id, HttpStatus.BAD_REQUEST);
        }

        Song s = songsDAO.getSongById(id);

        if (s == null) {
            return new ResponseEntity<>("No song with ID '" + id + "' exists.", HttpStatus.NOT_FOUND);
        }
        if (authenticator.authenticate(authorization)) {
            songsDAO.deleteSong(s);
            return new ResponseEntity<>("Song with ID '" + id + "' was deleted.", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

    }

    @PutMapping(value = "/{id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<String> putSong(@PathVariable(value = "id") Integer id, @RequestBody Song song,@RequestHeader("Authorization") String authorization) {

        if (id != song.getId()) {
            return new ResponseEntity<>("URL ID doesnt match payload ID.", HttpStatus.BAD_REQUEST);
        }

        if (song.getTitle().equals("") || song.getTitle() == null) {
            return new ResponseEntity<>("Wrong body: title is null or has no declaration.", HttpStatus.BAD_REQUEST);
        }
        if (authenticator.authenticate(authorization)) {

            songsDAO.updateSong(song);

            return new ResponseEntity<>("Song with ID '" + song.getId() + "' was updated.", HttpStatus.NO_CONTENT);
        }
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

    }

}
