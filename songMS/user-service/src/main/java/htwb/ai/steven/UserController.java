package htwb.ai.steven;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    UserRepository userRepository;

    //private static Logger log = LoggerFactory.getLogger(UserController.class);


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> signIn(@RequestBody User user) {
        //log.info("POST /auth");
        Optional<User> searchedUser = userRepository.findById(user.getUserId());

        if (searchedUser.isPresent()) {
            User u = searchedUser.get();

            //PW stimmt nicht mit dem PW des Postbefehls überein
            if (!u.getPassword().equals(user.getPassword())) {
                return new ResponseEntity<>("Username or password wrong", HttpStatus.UNAUTHORIZED);
            }
            u.setToken();
            userRepository.save(u);
            // log.info("POST /auth successfully exited");

            return new ResponseEntity<>((u.getToken()), HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }


    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<String> getUserByToken(@PathVariable(value = "id") String id) {
        try {
            Optional<User> user = userRepository.findByToken(id);
            return user.map(value -> new ResponseEntity<>(value.getUserId(), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
        } catch (HttpStatusCodeException ex) {
            return new ResponseEntity<>(ex.getResponseBodyAsString(), ex.getStatusCode());
        }
    }

}
