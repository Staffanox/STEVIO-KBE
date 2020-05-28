package htwb.ai.stevio.controller;

import htwb.ai.stevio.dao.ISongsDAO;
import htwb.ai.stevio.model.Song;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Member;
import java.util.List;

/*
 *
 * @author Mario Teklic
 */

@RestController
@RequestMapping(value="songs")
public class SongsController {

    @Autowired
    private ISongsDAO songsDAO;

    public SongsController(ISongsDAO sDAO){
        this.songsDAO = sDAO;
    }

    //GET all   ../rest/songs
    @GetMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<List<Song>> getAll() throws IOException {
        List<Song> songs = songsDAO.getAllSongs();

        if(songs != null && songs.size() > 0){
            return new ResponseEntity<>(songs, HttpStatus.OK);
        }

        return new ResponseEntity<>(songs, HttpStatus.OK);
    }

    //GET by id   ../rest/songs/1
    @GetMapping(value="/{id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Song> getSong(@PathVariable(value="id") Integer id) throws IOException {
        if(id < 0){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        Song song = songsDAO.getSongById(id);

        if(song != null){
            return new ResponseEntity<>(song, HttpStatus.OK);
        }

        return new ResponseEntity<>(song, HttpStatus.NOT_FOUND);
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<String> postSong(@RequestBody Song song, HttpServletRequest request){

        if(song.getTitle() == null || song.getTitle() == ""){
            return new ResponseEntity<>("Wrong body: no title", HttpStatus.BAD_REQUEST);
        }
        songsDAO.addSong(song);

        return new ResponseEntity<>(request.getRequestURL() + "/" + song.getId(), HttpStatus.CREATED);
    }

    @DeleteMapping(value="/{id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<String> deleteSong(@PathVariable(value="id") Integer id){

        if(id < 0){
            return new ResponseEntity<>("ID cant be less than 0. Your ID: " + id, HttpStatus.BAD_REQUEST);
        }

        Song s = songsDAO.getSongById(id);

        if(s == null){
            return new ResponseEntity<>("No song with ID '" + id + "' exists.", HttpStatus.NOT_FOUND);
        }

        songsDAO.deleteSong(s);
        return new ResponseEntity<>("Song with ID '" + id + "' was deleted.", HttpStatus.NO_CONTENT);
    }

    @PutMapping(value="/{id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Song> putSong(@PathVariable(value="id") Integer id){
        //songsDAO.updateSong();
        return null;
    }



}
