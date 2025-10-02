import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Chat {
    private String currentUser;
    private String chatFile = "chat.json";

    public Chat(String currentUser) {
        this.currentUser = currentUser;
    }

    public void start() {
        Scanner scan = new Scanner(System.in);

        System.out.println("\n💬 Добро пожаловать в чат, " + currentUser + "!");

        while (true) {
            System.out.println("\nВыберите действие:");
            System.out.println("1 - 📨 Отправить сообщение");
            System.out.println("2 - 📖 Показать все сообщения");
            System.out.println("3 - 🚪 Выйти из чата");
            System.out.print("Ваш выбор: ");

            String choice = scan.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Введите сообщение: ");
                    String message = scan.nextLine();
                    sendMessage(message);
                    break;

                case "2":
                    showMessages();
                    break;

                case "3":
                    System.out.println("👋 Выход из чата...");
                    return;

                default:
                    System.out.println("❌ Неверный выбор!");
            }
        }
    }

    private void sendMessage(String message) {
        try (FileWriter writer = new FileWriter(chatFile, true)) {
            writer.write(currentUser + ": " + message + "\n");
            System.out.println("✅ Сообщение отправлено!");
        } catch (IOException e) {
            System.out.println("❌ Ошибка отправки: " + e.getMessage());
        }
    }

    private void showMessages() {
        System.out.println("\n📨 История чата:");
        System.out.println("────────────────────");

        if (!Files.exists(Paths.get(chatFile))) {
            System.out.println("Сообщений пока нет");
        } else {
            try (BufferedReader reader = Files.newBufferedReader(Paths.get(chatFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                System.out.println("❌ Ошибка чтения: " + e.getMessage());
            }
        }
        System.out.println("────────────────────");
    }
}