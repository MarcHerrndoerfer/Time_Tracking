package de.time_tracking.Time_tracking.ui;

import java.util.List;
import java.util.Scanner;

import de.time_tracking.Time_tracking.model.User;
import de.time_tracking.Time_tracking.service.UserService;

public class ConsoleMenu {
    
    private final Scanner scanner = new Scanner(System.in);
    private final UserService userService = new UserService();


    public void start() {
        boolean running = true;

        while (running) {
            System.out.println("1. Create user");
            System.out.println("2. Login");
            System.out.println("3. Show all user");
            System.out.println("4. Select user");
            System.out.println("0. Exit");

            System.out.print("Selection: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    System.out.print("Username: ");
                    String username = scanner.nextLine();

                    System.out.print("Password: ");
                    String password = scanner.nextLine();

                    boolean created = userService.registerUser(username, password, "USER");

                    if (created) {
                        System.out.println("User successfully created");
                    } else {
                        System.out.println("Username already exists");
                    }
                    break;
                    case "2":
                    System.out.print("Username: ");
                    String Loginusername = scanner.nextLine();

                    System.out.print("Password: ");
                    String Loginpassword = scanner.nextLine();

                    boolean Loggedin = userService.UserLogin(Loginusername, Loginpassword);

                    if (Loggedin) {
                        System.out.println("Login successfully");
                    } else {
                        System.out.println("Incorrect username or password");
                    }
                    break; 

                
                case "3":
                    System.out.println("list of all users:");
                    List<User> users = userService.getAllUsers();
                    if (users.isEmpty()) {
                        System.out.println("No Users available ");
                    } else {
                        for (User u : users) {
                            System.out.println(u.getUsername());
                        }
                    } break;
                case "4":
                    System.out.print("Which user would you like to find? ");
                    String searchUsername = scanner.nextLine();

                    User person = userService.findUser(searchUsername);

                    if (person == null) {
                        System.out.println("User not found");
                    } else {
                        System.out.println("username " + person.getUsername() + " " + "id: " + person.getId() + " Role: " + person.getRole() + " passwort: " + person.getPasswordHash());

                    }
                    break;

                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid input");
            }
        }
    }

}
