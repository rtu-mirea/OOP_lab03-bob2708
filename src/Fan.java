import java.util.ArrayList;
import java.util.List;


public class Fan extends User{
    private List <String> songs = new ArrayList<>();

    public Fan(String name, String login, String password) {
        super(name, login, password);
    }
    public void addSong(String song) {
        songs.add(song);
    }
    public List getSongs() {
        return songs;
    }
}
