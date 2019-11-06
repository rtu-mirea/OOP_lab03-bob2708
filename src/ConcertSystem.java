import java.io.*;
import java.util.*;

public class ConcertSystem {
    private static List<Song> songs = new ArrayList<>();
    private static List<User> users = new ArrayList<>();
    static private int countOfSongs;
    static private User currentUser;
    static private void save() {
        try {
            FileWriter fileWriter = new FileWriter("songs.txt",false);
            for (Song song: songs) {
                Integer voices = song.getVoices();
                String temp = song.getName() + " " + voices.toString() + "\n";
                fileWriter.write(temp);
            }
            fileWriter.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    static private void load() {
        try {
            FileReader fileReader = new FileReader("songs.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            songs.clear();
            String temp, name, voices;
            while ((temp = bufferedReader.readLine())!=null) {
                name = temp.substring(0,temp.lastIndexOf(" "));
                voices = temp.substring(temp.lastIndexOf(" ") + 1);
                int voice = Integer.parseInt(voices);
                songs.add(new Song(name, voice));
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    static private List getResults() {
        songs.sort(new Comparator<Song>() {
            @Override
            public int compare(Song o1, Song o2) {
                if (o1.getVoices() > o2.getVoices()) {
                    return -1;
                }
                if ( o1.getVoices() < o2.getVoices()) {
                    return 1;
                }
                return 0;
            }
        });
        List<Song> winners = new ArrayList<>();
        for (int i = 0; i < countOfSongs; i++) {
            winners.add(songs.get(i));
            System.out.println(songs.get(i).getName());
        }
        return winners;
    }
    static private void addUser() {
        int done = 0;
        while (done != 1) {
            Scanner in = new Scanner(System.in);
            System.out.print("Введите имя: ");
            String name = in.nextLine();
            System.out.print("Введите логин: ");
            String login = in.nextLine();
            System.out.print("Введите пароль: ");
            String password = in.nextLine();
            for (User user : users) {
                if (login.equals(user.getLogin())) {
                    System.out.print("Логин уже существует\n");
                    done--;
                }
            }
            Fan fan = new Fan(name, login, password);
            users.add(fan);
            done++;
        }
    }
    static private User findUser() {
        boolean done = true;
        while (done) {
            Scanner in = new Scanner(System.in);
            System.out.print("Введите логин: ");
            String login = in.nextLine();
            System.out.print("Введите пароль: ");
            String password = in.nextLine();
            for (User user : users) {
                if (user.enter(login, password)) {
                    currentUser = user;
                    System.out.println("Добро пожаловать, " + user.getName());
                    done = false;
                }
            }
            if (done) { System.out.print("Неверный логин или пароль\n"); }
        }
        return currentUser;
    }
    public static void main(String[] args) {
        Admin admin = new Admin("admin", "admin", "admin");
        users.add(admin);
        Scanner in = new Scanner(System.in);
        int option1 = -1;
        while (option1 != 0) {
            System.out.println("1. Регистрация\n" +
                    "2. Вход\n" +
                    "0. Выход");
            System.out.println("Выберите пункт: ");
            option1 = in.nextInt();
            switch (option1) {
                case 1:
                    addUser();
                    break;
                case 2:
                    findUser();
                    Scanner in_in = new Scanner(System.in);
                    if (currentUser.getClass().getName().equals("Fan")) {
                        int option2 = -1;
                        while (option2 != 0) {
                            System.out.println("1. Голосование за песни\n" +
                                    "2. Добавление своих песен\n" +
                                    "0. Выход");
                            System.out.println("Выберите пункт: ");
                            option2 = in.nextInt();
                            switch (option2) {
                                case 1:
                                    for (Integer i = 1; i < songs.size() + 1; i++) {
                                        System.out.println(i.toString() + ". " + songs.get(i - 1).getName());
                                    }
                                    System.out.println("Введите номер песни, за которую хотите проголосовать: ");
                                    int num = in_in.nextInt();
                                    songs.get(num - 1).voice(currentUser);
                                    break;
                                case 2:
                                    System.out.println("Введите название песни: ");
                                    String song = in_in.nextLine();
                                    Song newSong = new Song(song);
                                    songs.add(newSong);
                                    break;
                                default:
                                    System.out.println("Неверный номер");
                                    break;
                            }
                        }
                    } else {
                        int option2 = -1;
                        while (option2 != 0) {
                            System.out.println("1. Изменение количества песен-победителей\n" +
                                    "0. Выход");
                            System.out.println("Выберите пункт: ");
                            option2 = in.nextInt();
                            switch (option2) {
                                case 1:
                                    System.out.println("Число песен-победителей: ");
                                    int winnersNum = in_in.nextInt();
                                    countOfSongs = winnersNum;
                                    break;
                                case 1000:
                                    save();
                                    break;
                                case 2000:
                                    load();
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
            }
        }
        getResults();
    }
}
