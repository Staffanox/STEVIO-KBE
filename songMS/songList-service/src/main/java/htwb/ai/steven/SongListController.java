package htwb.ai.steven;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/songList")
public class SongListController {

    private RestTemplate restTemplate;

    @Autowired
    private SongListRepository songListRepository;

    public SongListController() {
        this.restTemplate = new RestTemplate();
    }


    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<SongList> getId(@PathVariable(value = "id") int id, @RequestHeader("Authorization") String authorization) {
        if (id < 0) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        String user = restTemplate.getForObject("http://localhost:8087/auth/" + authorization, String.class);

        Optional<SongList> tempSongList = songListRepository.findById(id);

        if (tempSongList.isPresent()) {
            SongList songList = tempSongList.get();
            if (songList.getOwnerId().equals(user))
                return new ResponseEntity<>(songList, HttpStatus.ACCEPTED);
            else {
                if (songList.getisPrivate())
                    return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
                else
                    return new ResponseEntity<>(songList, HttpStatus.ACCEPTED);
            }
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        }

    }


    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<SongList>> getAll(@RequestParam(value = "userId") String userId, @RequestHeader("Authorization") String authorization) {

        String user = restTemplate.getForObject("http://localhost:8087/auth/" + authorization, String.class);
        if (user != null) {

            List<SongList> songs = songListRepository.findAllByOwnerId(userId);
            if (songs != null) {
                if (user.equals(userId)) {
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
        }

        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postSong(@RequestBody SongList songList, HttpServletRequest
            request, @RequestHeader("Authorization") String authorization) {

        try {
            String user = restTemplate.getForObject("http://localhost:8087/auth/" + authorization, String.class);
            if (user != null) {
                Set<Song> songsFromPayload = songList.getSongList();
                for (Song song : songsFromPayload) {
                    Song temp = restTemplate.getForObject("http://localhost:8082/songs/" + song.getId(), Song.class);
                    if (temp == null) {
                        return new ResponseEntity<>("Song not in DB", HttpStatus.BAD_REQUEST);
                    }
                    if (!song.getTitle().equals(temp.getTitle()) || !song.getArtist().equals(temp.getArtist()) || !song.getLabel().equals(temp.getLabel()) || song.getId() != temp.getId() || song.getReleased() != temp.getReleased()) {
                        return new ResponseEntity<>("Song not in DB", HttpStatus.BAD_REQUEST);
                    }
                }
                songList.setUser(user);
                songListRepository.save(songList);
                URI location = URI.create(request.getRequestURI() + "/" + songList.getId());
                return ResponseEntity.created(location).body(null);

            }
            return new ResponseEntity<>("Can't find user", HttpStatus.BAD_REQUEST);


        } catch (HttpStatusCodeException ex) {
            return new ResponseEntity<>(ex.getResponseBodyAsString(), ex.getStatusCode());
        }
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteSong(@PathVariable(value = "id") int id, @RequestHeader("Authorization") String authorization) {
        if (id < 0) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        String user = restTemplate.getForObject("http://localhost:8087/auth/" + authorization, String.class);


        Optional<SongList> tempSongList = songListRepository.findById(id);

        if (tempSongList.isPresent()) {
            SongList songList = tempSongList.get();
            if (songList.getOwnerId().equals(user)) {
                songListRepository.delete(songList);
                return new ResponseEntity<>("Songlist was deleted.", HttpStatus.NO_CONTENT);
            }

        }
        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);

    }


    @PutMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<String> putSongList(@PathVariable(value = "id") int id, @RequestHeader("Authorization") String authorization, @RequestBody SongList songList) {
        if (id < 0) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        String user = restTemplate.getForObject("http://localhost:8087/auth/" + authorization, String.class);
        Optional<SongList> tempSongList = songListRepository.findById(id);
        if (tempSongList.isPresent()) {
            SongList songListInDB = tempSongList.get();

            if (user == null) {
                return new ResponseEntity<>("Can't find user", HttpStatus.BAD_REQUEST);
            }
            if (user.equals(songList.getOwnerId()) && songList.getOwnerId().equals(songListInDB.getOwnerId())) {


                Set<Song> songsFromPayload = songList.getSongList();

                for (Song song : songsFromPayload) {
                    Song temp = restTemplate.getForObject("http://localhost:8082/songs/" + song.getId(), Song.class);
                    if (temp == null) {
                        return new ResponseEntity<>("Song not in DB", HttpStatus.BAD_REQUEST);
                    }
                    if (!song.getTitle().equals(temp.getTitle()) || !song.getArtist().equals(temp.getArtist()) || !song.getLabel().equals(temp.getLabel()) || song.getId() != temp.getId() || song.getReleased() != temp.getReleased()) {
                        return new ResponseEntity<>("Song not in DB", HttpStatus.BAD_REQUEST);
                    }
                }
                songListRepository.save(songList);
                return new ResponseEntity<>("Songlist upated", HttpStatus.OK);
            }

            return new ResponseEntity<>("Ownerid's don't match", HttpStatus.OK);


        }
        return new ResponseEntity<>("Ownerid's don't match", HttpStatus.OK);
    }


}



