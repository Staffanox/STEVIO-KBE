package htwb.ai.steven;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/lyrics")
public class LyricController {

    @Autowired
    private LyricsRepository lyricsRepository;


    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Lyrics>> getAll() {
        List<Lyrics> lyrics = this.lyricsRepository.findAll();
        return new ResponseEntity<>(lyrics, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Lyrics> getLyricsToSong(@PathVariable(value = "id") String id) {

        if (id.equals("") || id == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        try {
            Optional<Lyrics> optLyrics = lyricsRepository.findById(id);


            if (optLyrics.isPresent()) {

                Lyrics lyrics = optLyrics.get();
                return new ResponseEntity<>(lyrics, HttpStatus.OK);

            }
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        } catch (
                HttpStatusCodeException ex) {
            return new ResponseEntity<>(null, ex.getStatusCode());
        }
    }


    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<String> postLyrics(@RequestBody Lyrics lyrics, HttpServletRequest request) {

        try {
            Lyrics songLyrics = lyrics;
            System.out.println(songLyrics.getId());
            System.out.println(songLyrics.getLyrics());
            this.lyricsRepository.save(songLyrics);
            return new ResponseEntity<>("Lyric to song created", HttpStatus.CREATED);

        } catch (
                HttpStatusCodeException ex) {
            return new ResponseEntity<>(ex.getResponseBodyAsString(), ex.getStatusCode());
        }


    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteLyrics(@PathVariable(value = "id") String id) {

        Optional<Lyrics> optionalLyrics = lyricsRepository.findById(id);

        if (optionalLyrics.isPresent()) {
            lyricsRepository.deleteById(id);
            return new ResponseEntity<>("Lyrics successfully deleted", HttpStatus.NO_CONTENT);
        } else
            return new ResponseEntity<>("Lyrics is not available in db", HttpStatus.NOT_FOUND);
    }


    @PutMapping(value = "/{id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<String> putLyrics(@PathVariable(value = "id") String id, @RequestBody Lyrics lyrics) {

        if (!id.equals(lyrics.getId())) {
            return new ResponseEntity<>("URL ID doesnt match payload ID.", HttpStatus.BAD_REQUEST);
        }
        Optional<Lyrics> optionalLyrics = lyricsRepository.findById(id);

        if (!optionalLyrics.isPresent())
            return new ResponseEntity<>("No such Lyrics in DB", HttpStatus.BAD_REQUEST);
        lyricsRepository.save(lyrics);

        return new ResponseEntity<>("Lyrics with ID '" + lyrics.getId() + "' was updated.", HttpStatus.NO_CONTENT);

    }

}
