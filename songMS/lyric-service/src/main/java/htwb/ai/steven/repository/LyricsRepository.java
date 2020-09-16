package htwb.ai.steven.repository;

import htwb.ai.steven.model.Lyrics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LyricsRepository extends MongoRepository<Lyrics, String> {


}
