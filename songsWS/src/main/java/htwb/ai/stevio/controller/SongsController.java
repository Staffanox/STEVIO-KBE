package htwb.ai.stevio.controller;

import htwb.ai.stevio.dao.ISongsDAO;
import htwb.ai.stevio.model.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.util.List;

/*
 *
 * @author Mario Teklic
 */

@RestController
@RequestMapping(value = "songs")
public class SongsController {

    @Autowired
    private ISongsDAO songsDAO;

    public SongsController(ISongsDAO sDAO) {
        this.songsDAO = sDAO;
    }

    //GET all   ../rest/songs
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Song>> getAll() throws IOException {
        List<Song> songs = songsDAO.getAllSongs();

        if (songs != null && songs.size() > 0) {
            return new ResponseEntity<>(songs, HttpStatus.OK);
        }

        return new ResponseEntity<>(songs, HttpStatus.BAD_REQUEST);
    }

    //GET by id   ../rest/songs/1
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Song> getSong(@PathVariable(value = "id") Integer id) throws IOException {
        if (id < 0) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        Song song = songsDAO.getSongById(id);

        if (song != null) {
            return new ResponseEntity<>(song, HttpStatus.OK);
        }

        return new ResponseEntity<>(song, HttpStatus.NOT_FOUND);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postSong(@RequestBody Song song, HttpServletRequest request) {

        if (song.getTitle() == null || song.getTitle().equals("")) {
            return new ResponseEntity<>("Wrong body: no title", HttpStatus.BAD_REQUEST);
        }

        songsDAO.addSong(song);
        URI location = URI.create(request.getRequestURI() + "/" + song.getId());
        return ResponseEntity.created(location).body(null);
        // return new ResponseEntity<>(request.getRequestURL() + "/" + song.getId(), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteSong(@PathVariable(value = "id") Integer id) {

        if (id < 0) {
            return new ResponseEntity<>("ID cant be less than 0. Your ID: " + id, HttpStatus.BAD_REQUEST);
        }

        Song s = songsDAO.getSongById(id);

        if (s == null) {
            return new ResponseEntity<>("No song with ID '" + id + "' exists.", HttpStatus.NOT_FOUND);
        }

        songsDAO.deleteSong(s);
        return new ResponseEntity<>("Song with ID '" + id + "' was deleted.", HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/{id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<String> putSong(@PathVariable(value = "id") Integer id, @RequestBody Song song) {

        if (id != song.getId()) {
            return new ResponseEntity<>("URL ID doesnt match payload ID.", HttpStatus.BAD_REQUEST);
        }

        if (song.getTitle().equals("") || song.getTitle() == null) {
            return new ResponseEntity<>("Wrong body: title is null or has no declaration.", HttpStatus.BAD_REQUEST);
        }

        songsDAO.updateSong(song);

        return new ResponseEntity<>("Song with ID '" + song.getId() + "' was updated.", HttpStatus.NO_CONTENT);
    }
}
