package htwb.ai.steven.Controller;


import htwb.ai.steven.Model.User;
import htwb.ai.steven.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Optional;

/**
 * RestController which implements REST methods for user-service
 */
@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    UserRepository userRepository;


    /**
     * @param user userId and password for given user
     * @return a newly generated random token
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> signIn(@RequestBody User user) {
        Optional<User> searchedUser = userRepository.findById(user.getUserId());

        if (searchedUser.isPresent()) {
            User u = searchedUser.get();

            //PW stimmt nicht mit dem PW des Postbefehls überein
            if (!u.getPassword().equals(user.getPassword())) {
                return new ResponseEntity<>("Username or password wrong", HttpStatus.UNAUTHORIZED);
            }
            u.setToken();
            userRepository.save(u);

            return new ResponseEntity<>((u.getToken()), HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }


    /**
     * @param token authentication token
     * @return userId which belongs to token
     */
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<String> getUserByToken(@PathVariable(value = "id") String token) {
        try {
            Optional<User> user = userRepository.findByToken(token);
            return user.map(value -> new ResponseEntity<>(value.getUserId(), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
        } catch (HttpStatusCodeException ex) {
            return new ResponseEntity<>(ex.getResponseBodyAsString(), ex.getStatusCode());
        }
    }

}
