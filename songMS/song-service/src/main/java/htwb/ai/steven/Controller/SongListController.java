package htwb.ai.steven.Controller;


import htwb.ai.steven.Repository.SongListRepository;
import htwb.ai.steven.Repository.SongRepository;
import htwb.ai.steven.Model.Song;
import htwb.ai.steven.Model.SongList;
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
@RequestMapping("/songlist")
public class SongListController {

    private RestTemplate restTemplate;

    @Autowired
    private SongListRepository songListRepository;

    @Autowired
    private SongRepository songRepository;

    public SongListController() {
        this.restTemplate = new RestTemplate();
    }

    //private static Logger log = LoggerFactory.getLogger(SongListController.class);

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<SongList> getId(@PathVariable(value = "id") int id, @RequestHeader("Authorization") String authorization) {
        // log.info("Entered GET /songList" + id);
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
                else {
                    //  log.info("GET songList/" + id + " found and returned");
                    return new ResponseEntity<>(songList, HttpStatus.ACCEPTED);

                }
            }
        } else {
            // log.info("GET /songList/" + id + " not found");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        }

    }


    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<SongList>> getAll(@RequestParam(value = "userId") String userId, @RequestHeader("Authorization") String authorization) {
        // log.info("GET /songList/" + userId + " entered");
        String user = restTemplate.getForObject("http://localhost:8087/auth/" + authorization, String.class);
        if (user != null) {
            List<SongList> songs = songListRepository.findAllByOwnerId(userId);
            if (songs != null) {
                if (user.equals(userId)) {
                    //log.info("GET /songList/" + userId + " successfully");
                    return new ResponseEntity<>(songs, HttpStatus.ACCEPTED);
                } else {
                    List<SongList> returnList = new LinkedList<>();
                    for (SongList songList : songs) {
                        if (!songList.getisPrivate())
                            returnList.add(songList);
                    }
                    //log.info("GET /songList/" + userId + " successfully");
                    return new ResponseEntity<>(returnList, HttpStatus.ACCEPTED);
                }

            }
        }
        // log.info("GET /songList/" + userId + " not found");
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postSong(@RequestBody SongList songList, HttpServletRequest
            request, @RequestHeader("Authorization") String authorization) {
        //log.info("POST /songList");

        try {
            String user = restTemplate.getForObject("http://localhost:8087/auth/" + authorization, String.class);
            if (user != null) {
                Set<Song> songsFromPayload = songList.getSongList();
                for (Song song : songsFromPayload) {
                    if (songRepository.findById(song.getId()).isPresent()) {
                        Song temp = songRepository.findById(song.getId()).get();
                        if (!song.getTitle().equals(temp.getTitle()) || !song.getArtist().equals(temp.getArtist()) || !song.getLabel().equals(temp.getLabel()) || song.getId() != temp.getId() || song.getReleased() != temp.getReleased()) {
                            return new ResponseEntity<>("Song not in DB", HttpStatus.BAD_REQUEST);
                        }
                    }
                }
                songList.setUser(user);
                songListRepository.save(songList);
                URI location = URI.create(request.getRequestURI() + "/" + songList.getId());
                // log.info("POST /songList created with" + songList.getId());
                return ResponseEntity.created(location).body(null);

            }
            // log.info("POST /songList exited, invalid user");
            return new ResponseEntity<>("Can't find user", HttpStatus.BAD_REQUEST);


        } catch (HttpStatusCodeException ex) {
            return new ResponseEntity<>(ex.getResponseBodyAsString(), ex.getStatusCode());
        }
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteSong(@PathVariable(value = "id") int id, @RequestHeader("Authorization") String authorization) {
        //log.info("DELETE /songList/" + id);


        if (id < 0) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        String user = restTemplate.getForObject("http://localhost:8087/auth/" + authorization, String.class);


        Optional<SongList> tempSongList = songListRepository.findById(id);

        if (tempSongList.isPresent()) {
            SongList songList = tempSongList.get();
            if (songList.getOwnerId().equals(user)) {
                songListRepository.delete(songList);
                //   log.info("DELETE /songList/" + id + "successfully exited");
                return new ResponseEntity<>("Songlist was deleted.", HttpStatus.NO_CONTENT);
            }

        }
        //log.info("DELETE /songList/" + id + "forbidden");
        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);

    }


    @PutMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<String> putSongList(@PathVariable(value = "id") int id, @RequestHeader("Authorization") String authorization, @RequestBody SongList songList) {
        // log.info("PUT /songList/" + id);
        try {
            if (id < 0) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            String user = restTemplate.getForObject("http://localhost:8087/auth/" + authorization, String.class);
            Optional<SongList> tempSongList = songListRepository.findById(id);
            if (tempSongList.isPresent()) {
                SongList songListInDB = tempSongList.get();

                if (user == null) {
                    return new ResponseEntity<>("Can't find user", HttpStatus.NOT_FOUND);
                }
                if (user.equals(songList.getOwnerId()) && songList.getOwnerId().equals(songListInDB.getOwnerId())) {


                    Set<Song> songsFromPayload = songList.getSongList();

                    for (Song song : songsFromPayload) {
                        Song temp = restTemplate.getForObject("http://localhost:8082/songs/" + song.getId(), Song.class);
                        if (temp == null) {
                            return new ResponseEntity<>("Song not in DB", HttpStatus.NOT_FOUND);
                        }
                        if (!song.getTitle().equals(temp.getTitle()) || !song.getArtist().equals(temp.getArtist()) || !song.getLabel().equals(temp.getLabel()) || song.getId() != temp.getId() || song.getReleased() != temp.getReleased()) {
                            return new ResponseEntity<>("Song not in DB", HttpStatus.NOT_FOUND);
                        }
                    }
                    songListRepository.save(songList);
                    // log.info("PUT /songList/" + id + "successfully exited");
                    return new ResponseEntity<>("Songlist upated", HttpStatus.OK);
                }

                return new ResponseEntity<>("Ownerid's don't match", HttpStatus.FORBIDDEN);


            }
            return new ResponseEntity<>("Ownerid's don't match", HttpStatus.FORBIDDEN);
        } catch (HttpStatusCodeException ex) {
            return new ResponseEntity<>(ex.getResponseBodyAsString(), ex.getStatusCode());
        }
    }

}



