package htwb.ai.steven;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    //private static Logger log = LoggerFactory.getLogger(SongController.class);


    public SongController() {
    }

    //GET all   ../songs
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Song>> getAll() {
        //log.info("GET /songs");
        Iterable<Song> songs = songRepository.findAll();

        if (songs != null) {
            List<Song> songsAsList = new ArrayList<>();
            songs.forEach(songsAsList::add);
            //log.info("GET /songs successfully exited");
            return new ResponseEntity<>(songsAsList, HttpStatus.OK);
        }
        //log.info("GET /songs bad request");

        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }


    //GET by id   ../songs/1
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Optional<Song>> getSong(@PathVariable(value = "id") Integer id) {
        //log.info("GET /songs/" + id);

        Optional<Song> song = songRepository.findById(id);

        if (song.isPresent()) {
           // log.info("GET /songs/" + id + " successfully exited");

            return new ResponseEntity<>(song, HttpStatus.OK);

        } else
           // log.info("GET /songs/" + id + " not found");

        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postSong(@RequestBody Song song, HttpServletRequest request) {
       // log.info("POST /songs");
        songRepository.save(song);
        URI location = URI.create(request.getRequestURI() + "/" + song.getId());
        //log.info("POST /songs exited");
        return ResponseEntity.created(location).body(null);
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteSong(@PathVariable(value = "id") Integer id) {
        //log.info("DELETE /songs/" + id);

        Optional<Song> song = songRepository.findById(id);

        if (song.isPresent()) {
            songRepository.deleteById(id);
            //log.info("DELETE /songs/" + id + " exited");
            return new ResponseEntity<>("Song successfully deleted", HttpStatus.NO_CONTENT);
        } else
            return new ResponseEntity<>("Song is not available in db", HttpStatus.NOT_FOUND);
    }


    @PutMapping(value = "/{id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<String> putSong(@PathVariable(value = "id") Integer id, @RequestBody Song song) {
        //log.info("PUT /songs/" + id);

        if (id != song.getId()) {
            return new ResponseEntity<>("URL ID doesnt match payload ID.", HttpStatus.BAD_REQUEST);
        }
        if (song.getTitle().equals("") || song.getTitle() == null) {
            return new ResponseEntity<>("Wrong body: title is null or has no declaration.", HttpStatus.BAD_REQUEST);
        }
        songRepository.save(song);
       // log.info("PUT /songs/" + id +" exited");

        return new ResponseEntity<>("Song with ID '" + song.getId() + "' was updated.", HttpStatus.NO_CONTENT);

    }
}
