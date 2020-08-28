package htwb.ai.steven;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/songs")
public class SongController {


    @Autowired
    private SongRepository songRepository;

    public SongController() {

    }


    //GET all   ../songs
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Song>> getAll() {
        Iterable<Song> songs = songRepository.findAll();
            List<Song> songsAsList = new ArrayList<>();
            songs.forEach(songsAsList::add);
            return new ResponseEntity<>(songsAsList, HttpStatus.OK);
    }


    //GET by id   ../songs/1
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Song> getSong(@PathVariable(value = "id") Integer id) {
        Optional<Song> song = songRepository.findById(id);
        return song.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));

    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postSong(@RequestBody Song song, HttpServletRequest request) {
        if (song.getReleased() < 0) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        }
        songRepository.save(song);
        URI location = URI.create(request.getRequestURI() + "/" + song.getId());
        return ResponseEntity.created(location).body(null);
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteSong(@PathVariable(value = "id") Integer id) {

        Optional<Song> song = songRepository.findById(id);


        songRepository.deleteById(id);
        return new ResponseEntity<>("Song successfully deleted", HttpStatus.NO_CONTENT);

    }


    @PutMapping(value = "/{id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<String> putSong(@PathVariable(value = "id") Integer id, @RequestBody Song song) {

        if (id != song.getId()) {
            return new ResponseEntity<>("URL ID doesnt match payload ID.", HttpStatus.BAD_REQUEST);
        }
        songRepository.save(song);

        return new ResponseEntity<>("Song with ID '" + song.getId() + "' was updated.", HttpStatus.NO_CONTENT);

    }
}
