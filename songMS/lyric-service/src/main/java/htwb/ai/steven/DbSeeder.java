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


        String MacarthusPark = "[Verse 1]\n" +
                "Step one, you say we need to talk\n" +
                "He walks, you say \"sit down; it's just a talk\"\n" +
                "He smiles politely back at you\n" +
                "You stare politely right on through\n" +
                "Some sort of window to your right\n" +
                "As he goes left and you stay right\n" +
                "Between the lines of fear and blame\n" +
                "You begin to wonder why you came\n" +
                "\n" +
                "[Chorus]\n" +
                "Where did I go wrong? I lost a friend\n" +
                "Somewhere along in the bitterness\n" +
                "And I would have stayed up with you all night\n" +
                "Had I known how to save a life\n" +
                "\n" +
                "[Verse 2]\n" +
                "Let him know that you know best\n" +
                "'Cause after all, you do know best\n" +
                "Try to slip past his defense\n" +
                "Without granting innocence\n" +
                "Lay down a list of what is wrong\n" +
                "The things you've told him all along\n" +
                "And pray to God He hears you\n" +
                "And I pray to God He hears you, and\n" +
                "\n" +
                "[Chorus]\n" +
                "Where did I go wrong? I lost a friend\n" +
                "Somewhere along in the bitterness\n" +
                "And I would have stayed up with you all night\n" +
                "Had I known how to save a life\n" +
                "\n" +
                "[Verse 3]\n" +
                "As he begins to raise his voice\n" +
                "You lower yours and grant him one last choice\n" +
                "Drive until you lose the road\n" +
                "Or break with the ones you've followed\n" +
                "He will do one of two things\n" +
                "He will admit to everything\n" +
                "Or he'll say he's just not the same\n" +
                "And you'll begin to wonder why you came\n" +
                "\n" +
                "[Chorus]\n" +
                "Where did I go wrong? I lost a friend\n" +
                "Somewhere along in the bitterness\n" +
                "And I would have stayed up with you all night\n" +
                "Had I known how to save a life\n" +
                "\n" +
                "Where did I go wrong? I lost a friend\n" +
                "Somewhere along in the bitterness\n" +
                "And I would have stayed up with you all night\n" +
                "Had I known how to save a life\n" +
                "\n" +
                "[Refrain]\n" +
                "How to save a life\n" +
                "How to save a life\n" +
                "\n" +
                "[Chorus]\n" +
                "Where did I go wrong? I lost a friend\n" +
                "Somewhere along in the bitterness\n" +
                "And I would have stayed up with you all night\n" +
                "Had I known how to save a life\n" +
                "\n" +
                "Where did I go wrong? I lost a friend\n" +
                "Somewhere along in the bitterness\n" +
                "And I would have stayed up with you all night\n" +
                "Had I known how to save a life\n" +
                "\n" +
                "[Outro]\n" +
                "How to save a life\n" +
                "How to save a life ";

        Lyrics macarthus_park = new Lyrics("How to Save a Life", MacarthusPark);
        //this.lyricsRepository.deleteAll();
       //this.lyricsRepository.save(macarthus_park);
    }
}
