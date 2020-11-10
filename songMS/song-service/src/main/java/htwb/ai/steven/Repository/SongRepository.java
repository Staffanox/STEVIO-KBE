package htwb.ai.steven.Repository;

import htwb.ai.steven.Model.Song;
import org.springframework.data.repository.CrudRepository;

public interface SongRepository extends CrudRepository<Song, Integer> {

    Iterable<Song> findAll();
}
