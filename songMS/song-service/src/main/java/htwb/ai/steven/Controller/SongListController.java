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

/**
 * RestController which implements REST methods for songlist-service
 */
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


    /**
     * Gets a songList by a given ID, provided request is authorized
     *
     * @param id            serial ID in DB
     * @param authorization random token associated with user account
     * @return 200 if successful, 403 if access is forbidden (private songList and no Access), 404 if ID is not in DB
     */
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<SongList> getSongListByID(@PathVariable(value = "id") int id, @RequestHeader("Authorization") String authorization) {
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
                    return new ResponseEntity<>(songList, HttpStatus.ACCEPTED);

                }
            }
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        }

    }


    /**
     * Gets all songLists from a user which are viewable (all public and private from self, only public from another)
     *
     * @param userId        ownerId of users, basically username like "mmuster"
     * @param authorization token of enquirer
     * @return All songLists from a user viewable to enquirer
     */
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<SongList>> getAllUserSongLists(@RequestParam(value = "userId") String userId, @RequestHeader("Authorization") String authorization) {
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


    /**
     * Posts a songList for a user in db
     *
     * @param songList      a hopefully valid songList according to model
     * @param request       for URI to be returned in header
     * @param authorization token of enquirer
     * @return 400 if song or user is invalid, URI to newly posted SongList in header if correct
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postSongList(@RequestBody SongList songList, HttpServletRequest
            request, @RequestHeader("Authorization") String authorization) {

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
                return ResponseEntity.created(location).body(null);

            }
            return new ResponseEntity<>("Can't find user", HttpStatus.BAD_REQUEST);


        } catch (HttpStatusCodeException ex) {
            return new ResponseEntity<>(ex.getResponseBodyAsString(), ex.getStatusCode());
        }
    }


    /**
     * Deletes a songList if it's yours and is existing
     *
     * @param id            serial ID of SongList in DB
     * @param authorization token of enquirer
     * @return 400 if id invalid, 403 if userId of enquirer != userId of owner, 204 if successful
     */
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


    /**
     * Changes a songLists properties if the enquirer is the owner of given songList
     *
     * @param id            serial id of songList to be changed
     * @param authorization token for authorization  of user
     * @param songList      to be changed
     * @return 400 if id invalid, 404 if user or songList not found, 403 if enquirerId != ownerId, 200 if successful
     */
    @PutMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<String> putSongList(@PathVariable(value = "id") int id, @RequestHeader("Authorization") String authorization, @RequestBody SongList songList) {
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



