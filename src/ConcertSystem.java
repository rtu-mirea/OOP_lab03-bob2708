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
            System.out.println("Успешно сохранено");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    static private void load() {
        try {
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream("songs.txt"), "UTF-8"));
            songs.clear();
            String temp, name, voices;
            while ((temp = bufferedReader.readLine()) != null) {
                name = temp.substring(0,temp.lastIndexOf(" "));
                voices = temp.substring(temp.lastIndexOf(" ") + 1);
                int voice = Integer.parseInt(voices);
                songs.add(new Song(name, voice));
            }
            System.out.println("Успешно загружено");
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
        if (countOfSongs > songs.size()) {
            countOfSongs = songs.size();
        }
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
                                    if (songs.isEmpty()) {
                                        System.out.println("Отсутствуют песни в списке голосования");
                                        break;
                                    }
                                    for (Integer i = 1; i < songs.size() + 1; i++) {
                                        System.out.println(i.toString() + ". " + songs.get(i - 1).getName());
                                    }
                                    System.out.println("Введите номер песни, за которую хотите проголосовать: ");
                                    try {
                                        int num = in_in.nextInt();
                                        songs.get(num - 1).voice(currentUser);
                                    } catch (IndexOutOfBoundsException ex) {
                                        System.out.println("Такой номер не существует");
                                    } catch (InputMismatchException ex) {
                                        System.out.println("Вводите числа, а не строки");
                                    }
                                    break;
                                case 2:
                                    System.out.println("Введите название песни: ");
                                    String song = in_in.nextLine();
                                    song = song.trim();
                                    if (song.isEmpty()) {
                                        System.out.println("Нельзя добавлять пустую строку");
                                        break;
                                    }
                                    Song newSong = new Song(song);
                                    songs.add(newSong);
                                    System.out.println("Голос засчитан");
                                    break;
                                default:
                                    System.out.println("Неверный пункт");
                                    break;
                            }
                        }
                    } else {
                        int option2 = -1;
                        while (option2 != 0) {
                            System.out.println("1. Изменение количества песен-победителей\n" +
                                    "2. Сохранение списка песен\n" +
                                    "3. Загрузка списка песен\n" +
                                    "0. Выход");
                            System.out.println("Выберите пункт: ");
                            option2 = in.nextInt();
                            switch (option2) {
                                case 1:
                                    System.out.println("Число песен-победителей: ");
                                    if (in_in.hasNextInt()) {
                                        int winnersNum = in_in.nextInt();
                                        if (winnersNum < 1) {
                                            System.out.println("Должен быть хотя бы один победитель");
                                            break;
                                        }
                                        countOfSongs = winnersNum;
                                        System.out.println("Количество песен-победителей изменено успешно");
                                    } else {
                                        System.out.println("Вводите число, а не строку");
                                    }
                                    break;
                                case 2:
                                    save();
                                    break;
                                case 3:
                                    load();
                                    break;
                                default:
                                    System.out.println("Неверный пункт");
                                    break;
                            }
                        }
                    }
            }
        }
        getResults();
    }
}
