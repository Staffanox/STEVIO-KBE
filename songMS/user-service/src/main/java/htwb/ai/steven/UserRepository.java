package htwb.ai.steven;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String> {

     Optional<User> findByToken(String token);
}
