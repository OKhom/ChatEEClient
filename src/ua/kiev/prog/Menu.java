package ua.kiev.prog;

import ua.kiev.prog.chatroom.Chatroom;
import ua.kiev.prog.messages.Message;
import ua.kiev.prog.users.User;
import ua.kiev.prog.users.UserStatus;
import ua.kiev.prog.utils.Utils;

import java.io.IOException;
import java.util.Scanner;

public class Menu {
    static void mainMenu() throws IOException {
        Scanner scanner = new Scanner(System.in);
        User user = Menu.userLogin(scanner);
        if (user == null) return;
        else System.out.println("You connected as: " + user.getLogin() + "\n");

        for (;;) {
            System.out.println("Select your action:");
            System.out.println("\t1. Messages");
            System.out.println("\t2. Users status");
            System.out.println("\t3. Create new chatroom");
            System.out.println("\t0. Exit");

            String input = scanner.nextLine();
            switch (input) {
                case "1" -> Message.message(user, scanner);
                case "2" -> UserStatus.print(user);
                case "3" -> Chatroom.create(user, scanner);
                case "0" -> {
                    userLogout(user);
                    return;
                }
                default -> System.out.println("Your input is incorrect. Try again...");
            }
        }
    }

    static void userLogout(User user) throws IOException {
        int loginResponse = user.logout(Utils.getURL() + "/logout");
        if (loginResponse != 200) {
            System.out.println("Error " + loginResponse);
        } else {
            System.out.println("User " + user.getLogin() + " logged out...");
        }
    }

    static User userLogin(Scanner scanner) throws IOException {
        for (;;) {
            System.out.println("Input '1' - for SignIn, '2' - for SignUp or 'q' - for Exit: ");
            String signin = scanner.nextLine();
            if (signin.equals("q") || signin.isEmpty()) return null;
            else if (signin.equals("1")) {
                User user = userSignin(scanner);
                if (user != null) return user;
            } else if (signin.equals("2")) {
                userSignup(scanner);
            } else {
                System.out.println("Your input is incorrect. Try again...");
            }
        }
    }

    static User userSignin(Scanner scanner) throws IOException {
        String login;
        String password;
        System.out.println("Enter your login: ");
        login = scanner.nextLine();
        System.out.println("Enter your password: ");
        password = scanner.nextLine();
        User user = new User(login, password);
        int loginResponse = user.send(Utils.getURL() + "/login", "signin");
        if (loginResponse != 200) {
            System.out.println("Error " + loginResponse + " - Your 'Login' or 'Password' is incorrect. Try again...");
            return null;
        } else {
            return user;
        }
    }

    static void userSignup(Scanner scanner) throws IOException {
        String login;
        String password;
        System.out.println("Enter new login: ");
        login = scanner.nextLine();
        System.out.println("Enter new password: ");
        password = scanner.nextLine();
        System.out.println("Repeat new password: ");
        String password2 = scanner.nextLine();
        if (!password2.equals(password)) {
            System.out.println("Your repeated password is incorrect. Try again...");
            return;
        }
        User user = new User(login, password);
        int loginResponse = user.send(Utils.getURL() + "/login", "signup");
        if (loginResponse != 200) {
            System.out.println("Error " + loginResponse + " - Inputted 'Login' exist. Try again...");
        } else {
            System.out.println("Registered OK. Login as new user...");
        }
    }
}
