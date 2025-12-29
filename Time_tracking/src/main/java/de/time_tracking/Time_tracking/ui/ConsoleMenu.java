package de.time_tracking.Time_tracking.ui;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

import de.time_tracking.Time_tracking.model.User;
import de.time_tracking.Time_tracking.model.Role;
import de.time_tracking.Time_tracking.model.TimeEntry;
import de.time_tracking.Time_tracking.service.UserService;
import de.time_tracking.Time_tracking.service.TimeEntryService;
import de.time_tracking.Time_tracking.repository.TimeEntryRepository;
import de.time_tracking.Time_tracking.service.UsernameAlreadyExistsException;

public class ConsoleMenu {


    private final TimeEntryRepository timeEntryRepository = new TimeEntryRepository();
    private final TimeEntryService timeEntryService = new TimeEntryService(timeEntryRepository);

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
            System.out.println("5. Add time entry");
            System.out.println("6. Show my entries for a date");

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

                case "5":
                    createTimeEntryFlow(currentUser);
                    break;

                case "6":
                    showEntriesForDateFlow(currentUser);
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

    private LocalDate readDate(String prompt) {
    while (true) {
        System.out.print(prompt);
        String s = scanner.nextLine().trim();
        try {
            return LocalDate.parse(s);
        } catch (Exception e) {
            System.out.println("Invalid date. Example: 2025-12-29");
        }
    }
}

private LocalTime readTime(String prompt) {
    while (true) {
        System.out.print(prompt);
        String s = scanner.nextLine().trim();
        try {
            return LocalTime.parse(s);
        } catch (Exception e) {
            System.out.println("Invalid time. Example: 08:30");
        }
    }
}

private int readInt(String prompt) {
    while (true) {
        System.out.print(prompt);
        String s = scanner.nextLine().trim();
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            System.out.println("Please enter a number.");
        }
    }
}


    private void showEntriesForDateFlow(User currentUser) {
    LocalDate date = readDate("Date (YYYY-MM-DD): ");
    List<TimeEntry> entries = timeEntryService.getEntriesForDate(currentUser.getId(), date);

    if (entries.isEmpty()) {
        System.out.println("No entries found for " + date);
        return;
    }

    long totalNetMinutes = 0;

    System.out.println("Entries for " + date + ":");
    for (TimeEntry e : entries) {
        long net = timeEntryService.calculateNetMinutes(
            e.getStartTime(), e.getEndTime(), e.getBreakMinutes()
        );
        totalNetMinutes += net;

        System.out.println(
            "• " + e.getStartTime() + " - " + e.getEndTime() +
            " break " + e.getBreakMinutes() + "min" +
            " net " + (net / 60) + "h " + (net % 60) + "min"
        );
    }

    System.out.println("Total net: " + (totalNetMinutes / 60) + "h " + (totalNetMinutes % 60) + "min");
}

private void createTimeEntryFlow(User currentUser) {
    try {
        LocalDate date = readDate("Date (YYYY-MM-DD): ");
        LocalTime start = readTime("Start time (HH:MM): ");
        LocalTime end = readTime("End time (HH:MM): ");
        int breakMin = readInt("Break minutes: ");

        long id = timeEntryService.create(currentUser.getId(), date, start, end, breakMin);

        long net = timeEntryService.calculateNetMinutes(start, end, breakMin);
        System.out.println("Saved. Entry ID: " + id);
        System.out.println("Net working time: " + (net / 60) + "h " + (net % 60) + "min");

    } catch (IllegalArgumentException ex) {
        System.out.println("Input error: " + ex.getMessage());
    }
}



}
