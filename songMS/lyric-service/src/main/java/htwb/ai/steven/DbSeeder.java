package htwb.ai.steven;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DbSeeder implements CommandLineRunner {
    @Autowired
    private LyricsRepository lyricsRepository;


    @Override
    public void run(String... args) throws Exception {


        String MacarthusPark = "Spring was never waiting for us, dear " +
                "It ran one step ahead " +
                "As we followed in the dance " +
                "MacArthur's Park is melting in the dark " +
                "All the sweet, green icing flowing down " +
                "Someone left the cake out in the rain " +
                "I don't think that I can take it " +
                "'Cause it took so long to bake it " +
                "And I'll never have that recipe again " +
                "Oh, no " +
                "I recall the yellow cotton dress " +
                "Foaming like a wave " +
                "On the ground beneath your knees " +
                "The birds, like tender babies in your hands " +
                "And the old men playing Chinese checkers by the trees " +
                "MacArthur's Park is melting in the dark " +
                "All the sweet, green icing flowing down " +
                "Someone left the cake out in the rain " +
                "I don't think that I can take it " +
                "'Cause it took so long to bake it " +
                "And I'll never have that recipe again " +
                "Oh, no " +
                "MacArthur's Park is melting in the dark " +
                "All the sweet, green icing flowing down " +
                "Someone left my cake out in the rain " +
                "And I don't think that I can take it " +
                "'Cause it took so long to bake it " +
                "And I'll never have that recipe again " +
                "Oh, no, oh ";

        Lyrics macarthus_park = new Lyrics("Macarthus Park", MacarthusPark);
        //this.lyricsRepository.deleteAll();
       // this.lyricsRepository.save(macarthus_park);
    }
}
