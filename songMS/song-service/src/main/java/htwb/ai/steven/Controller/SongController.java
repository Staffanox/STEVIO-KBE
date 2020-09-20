package htwb.ai.steven.Controller;


import htwb.ai.steven.Repository.SongRepository;
import htwb.ai.steven.Model.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * RestController which implements REST methods for song-service
 */
@RestController
@RequestMapping("/songs")
public class SongController {


    @Autowired
    private SongRepository songRepository;

    public SongController() {

    }


    /**
     * Endpoint /songs
     *
     * @return all songs currently located in DB
     */
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Song>> getAllSongs() {
        Iterable<Song> songs = songRepository.findAll();
        List<Song> songsAsList = new ArrayList<>();
        songs.forEach(songsAsList::add);
        return new ResponseEntity<>(songsAsList, HttpStatus.OK);
    }


    /**
     * Method to find and return a specific single song in db
     *
     * @param id serial id associated with song
     * @return 200 if song is in db, 404 if not
     */
    //GET by id   ../songs/1
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Song> getSong(@PathVariable(value = "id") Integer id) {
        Optional<Song> song = songRepository.findById(id);
        return song.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));

    }

    /**
     * Method to add a song to db if valid
     *
     * @param song    a hopefully valid song according to model
     * @param request for URI location in header
     * @return 400 if song is invalid (no title, release year not AD), 200 and location to song if successful
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postSong(@RequestBody Song song, HttpServletRequest request) {
        if (song.getReleased() < 0 || song.getTitle().equals("")) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        }
        songRepository.save(song);
        URI location = URI.create(request.getRequestURI() + "/" + song.getId());
        return ResponseEntity.created(location).body(null);
    }


    /**
     * Deletes a song from db
     *
     * @param id serial id of song in db
     * @return 404 if song not in db, 204 if successful
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteSong(@PathVariable(value = "id") Integer id) {
        Optional<Song> searchedSong = songRepository.findById(id);
        if (!searchedSong.isPresent())
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        songRepository.deleteById(id);
        return new ResponseEntity<>("Song successfully deleted", HttpStatus.NO_CONTENT);
    }


    /**
     * Changes a given song in db
     *
     * @param id   serial id of song in db
     * @param song new song which replaces old song in db
     * @return 400 if id in url != id in body, 204 if successful
     */
    @PutMapping(value = "/{id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<String> putSong(@PathVariable(value = "id") Integer id, @RequestBody Song song) {

        if (id != song.getId()) {
            return new ResponseEntity<>("URL ID doesnt match payload ID.", HttpStatus.BAD_REQUEST);
        }
        songRepository.save(song);

        return new ResponseEntity<>("Song with ID '" + song.getId() + "' was updated.", HttpStatus.NO_CONTENT);

    }
}
