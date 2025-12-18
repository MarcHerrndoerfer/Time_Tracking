package de.time_tracking.Time_tracking.ui;

import java.util.Scanner;

import de.time_tracking.Time_tracking.service.UserService;

public class ConsoleMenu {
    
    private final Scanner scanner = new Scanner(System.in);
    private final UserService userService = new UserService();


    public void start() {
        boolean running = true;

        while (running) {
            System.out.println("1. Create user");
            System.out.println("2. Login");
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
                    System.out.println("Login");
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
