package de.time_tracking.Time_tracking.ui;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import de.time_tracking.Time_tracking.model.User;
import de.time_tracking.Time_tracking.model.Role;
import de.time_tracking.Time_tracking.service.UserService;
import de.time_tracking.Time_tracking.service.UsernameAlreadyExistsException;

public class ConsoleMenu {

    private final Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
    private final UserService userService = new UserService();

    public void start() {
        System.out.println("######### Welcome to your Timetracker ###########");

        while (true) {
            User currentUser = authenticate();
            if (currentUser == null) {
                System.out.println("Bye.");
                return;
            }

            System.out.println("Login successfully. Hello " + currentUser.getUsername());
            mainMenu(currentUser);
        }
    }


    private User authenticate() {
        while (true) {
            System.out.println();
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("0. Exit");

            System.out.print("Selection: ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    return handleLogin();

                case "2":
                    handleRegister();
                    break;

                case "0":
                    return null;

                default:
                    System.out.println("Invalid input");
            }
        }
    }

    private User handleLogin() {
        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        boolean loggedIn = userService.login(username, password);
        if (!loggedIn) {
            System.out.println("Incorrect username or password");
            return null;
        }

        return userService.findUser(username);
    }

    private void handleRegister() {
        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        System.out.print("Role (USER / ADMIN): ");
        String roleInput = scanner.nextLine();

        try {
            userService.registerUser(username, password, roleInput);
            System.out.println("User created. You can now login.");
        } catch (UsernameAlreadyExistsException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }


    private void mainMenu(User currentUser) {
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("1. Show all users");
            System.out.println("2. Select user");

            if (currentUser.getRole() == Role.ADMIN) {
                System.out.println("3. Delete user");
                System.out.println("4. Delete all users");
            }

            System.out.println("9. Logout");
            System.out.println("0. Exit");

            System.out.print("Selection: ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    handleShowAllUsers();
                    break;

                case "2":
                    handleSelectUser();
                    break;

                case "3":
                    if (currentUser.getRole() == Role.ADMIN) {
                        handleDeleteUser();
                    } else {
                        System.out.println("Access denied");
                    }
                    break;

                case "4":
                    if (currentUser.getRole() == Role.ADMIN) {
                        handleDeleteAllUsers();
                    } else {
                        System.out.println("Access denied");
                    }
                    break;

                case "9":
                    System.out.println("Logged out.");
                    running = false;
                    break;

                case "0":
                    System.out.println("Bye.");
                    System.exit(0);

                default:
                    System.out.println("Invalid input");
            }
        }
    }


    private void handleShowAllUsers() {
        System.out.println("List of all users:");
        List<User> users = userService.getAllUsers();

        if (users.isEmpty()) {
            System.out.println("No users available");
            return;
        }

        for (User u : users) {
            System.out.println(u.getUsername());
        }
    }

    private void handleSelectUser() {
        System.out.print("Which user would you like to find? ");
        String username = scanner.nextLine();

        User user = userService.findUser(username);
        if (user == null) {
            System.out.println("User not found");
            return;
        }

        System.out.println(
            "Username: " + user.getUsername() +
            " | ID: " + user.getId() +
            " | Role: " + user.getRole()
        );
    }

    private void handleDeleteUser() {
        System.out.print("Which user do you want to delete? ");
        String username = scanner.nextLine();

        User user = userService.findUser(username);
        if (user == null) {
            System.out.println("User not found");
            return;
        }

        try {
            userService.deleteUser(user);
            System.out.println("User " + username + " successfully deleted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleDeleteAllUsers() {
        System.out.print("Do you really want to delete all users? Y/N ");
        String answer = scanner.nextLine().trim();

        if (!"Y".equalsIgnoreCase(answer)) {
            System.out.println("Canceled");
            return;
        }

        try {
            userService.deleteAllUSers();
            System.out.println("All users deleted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
