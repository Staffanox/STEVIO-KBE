package htwb.ai.steven.Repository;

import htwb.ai.steven.Model.SongList;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SongListRepository extends CrudRepository<SongList, Integer> {

    List<SongList> findAllByOwnerId(String ownerid);

}
