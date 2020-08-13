package htwb.ai.steven;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.converters.Auto;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    UserRepository userRepository;


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> signIn(@RequestBody User user) {

        Optional<User> searchedUser = userRepository.findById(user.getUserId());

        if (searchedUser.isPresent()) {
            User u = searchedUser.get();

            //UserId stimmt nicht mit der userId des Postbefehls überein (trivial..)
            if (!u.getUserId().equals(user.getUserId())) {
                return new ResponseEntity<>("Username or password wrong", HttpStatus.UNAUTHORIZED);
            }

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


    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<String> getUserByToken(@PathVariable(value = "id") String id) {
        Optional<User> user = userRepository.findByToken(id);
        return user.map(value -> new ResponseEntity<>(value.getUserId(), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

}
