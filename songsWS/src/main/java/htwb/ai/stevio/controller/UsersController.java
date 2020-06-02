package htwb.ai.stevio.controller;

/*
 *
 * @author Mario Teklic
 */

import htwb.ai.stevio.dao.IUsersDAO;
import htwb.ai.stevio.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(value="auth")
public class UsersController {

    @Autowired
    private IUsersDAO usersDAO;

    public UsersController(IUsersDAO usersDAO){
        this.usersDAO = usersDAO;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> signIn(@RequestBody User user){

        if(user.getUserId() == null || user.getUserId() == "" ||
                user.getPassword() == null || user.getPassword() == ""){
            return new ResponseEntity<>("Username AND password is needed.", HttpStatus.UNAUTHORIZED);
        }

        User u = usersDAO.getUser(user);

        if(u == null){
            return new ResponseEntity<>("No user found", HttpStatus.UNAUTHORIZED);
        }
        
        return new ResponseEntity<>(this.generateToken(), HttpStatus.OK);
    }

    public String generateToken(){

        String token = UUID.randomUUID().toString();
        token.replace("-", "");

        if(token.length() > 18){
            token = token.substring(0, 17);
        }

        return token;
    }

}
