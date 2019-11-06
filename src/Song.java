import java.util.ArrayList;
import java.util.List;

public class Song {
    private String name;
    private int voices;
    private List<User> voiced = new ArrayList<>();

    public Song() {
        this.name = "";
        this.voices = 0;
    }

    public Song(String name) {
        this.name = name;
        this.voices = 0;
    }

    public Song(String name, int voices) {
        this.name = name;
        this.voices = voices;
    }

    public int getVoices() {
        return voices;
    }

    public String getName() {
        return name;
    }

    public boolean voice (User user) {
        for (User users :voiced) {
            if (user.getLogin().equals(users.getLogin())) {
                System.out.println("Вы уже проголосовали за эту песню");
                return false;
            }
        }
        this.voices++;
        voiced.add(user);
        return true;
    }
}
