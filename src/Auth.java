import java.io.BufferedReader;
import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Auth {

    static class User {
        String login;
        String password;

        User(String login, String password) {
            this.login = login;
            this.password = password;
        }
    }

    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("db.json"))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }

            // Простой парсинг JSON (для базового формата)
            String json = content.toString().trim();
            if (json.startsWith("[") && json.endsWith("]")) {
                json = json.substring(1, json.length() - 1);
                String[] userEntries = json.split("\\},\\s*\\{");

                for (String entry : userEntries) {
                    entry = entry.replace("{", "").replace("}", "");
                    String[] fields = entry.split(",");
                    String userLogin = null;
                    String userPassword = null;

                    for (String field : fields) {
                        String[] keyValue = field.split(":");
                        if (keyValue.length == 2) {
                            String key = keyValue[0].trim().replace("\"", "");
                            String value = keyValue[1].trim().replace("\"", "");

                            if ("login".equals(key)) {
                                userLogin = value;
                            } else if ("password".equals(key)) {
                                userPassword = value;
                            }
                        }
                    }

                    if (userLogin != null && userPassword != null) {
                        users.add(new User(userLogin, userPassword));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static void auth() {
        Scanner scan = new Scanner(System.in);
        List<User> users = loadUsers();

        System.out.println("✅ Добро пожаловать в консольный мессенджер!");
        System.out.println("Пожалуйста войдите в свою учетную запись!");

        System.out.print("Логин: ");
        String login = scan.nextLine();
        System.out.print("Пароль: ");
        String password = scan.nextLine();

        boolean authenticated = false;
        for (User user : users) {
            if (user.login.equals(login) && user.password.equals(password)) {
                authenticated = true;
                break;
            }
        }

        if (authenticated) {
            System.out.println("✅ Авторизация успешна! Добро пожаловать, " + login + "!");
            // кидать на общий чат
            Chat chat = new Chat(login);
            chat.start();

        } else {
            System.out.println("❌ Ошибка авторизации! Неверный логин или пароль.");
        }
    }

    public static void main(String[] args) {
        auth();
    }
}