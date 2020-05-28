package htwb.ai.stevio.controller;

import htwb.ai.stevio.dao.ISongsDAO;
import htwb.ai.stevio.model.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    @GetMapping()
    public ResponseEntity<List<Song>> getAll() throws IOException {
        List<Song> songs = songsDAO.getAllSongs();

        if(songs != null && songs.size() > 0){
            return new ResponseEntity<>(songs, HttpStatus.OK);
        }

        return new ResponseEntity<>(songs, HttpStatus.OK);
    }

    //GET id
    @GetMapping(value="/{id}")
    public ResponseEntity<Song> getSong(@PathVariable(value="id") Integer id) throws IOException {
        Song song = songsDAO.getSongById(id);

        if(song != null){
            return new ResponseEntity<>(song, HttpStatus.OK);
        }

        return new ResponseEntity<>(song, HttpStatus.NOT_FOUND);
    }

    @PostMapping(value="")
    public ResponseEntity<Integer> postSong(){
        Song s = new Song(1,"", "", "", 1);
        songsDAO.addSong(s);

        return new ResponseEntity<>(-1, HttpStatus.OK);
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<Song> deleteSong(){
        Song s = new Song(1, "","","",2);
        return new ResponseEntity<Song>(s, HttpStatus.NOT_FOUND);
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<Song> putSong(@PathVariable(value="id") Integer id){
        //songsDAO.updateSong();
        return null;
    }



}
