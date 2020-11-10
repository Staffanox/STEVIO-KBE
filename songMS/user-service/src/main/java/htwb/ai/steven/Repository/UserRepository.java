package htwb.ai.steven.Repository;

import htwb.ai.steven.Model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String> {

     Optional<User> findByToken(String token);
}
