package de.time_tracking.Time_tracking.ui;
import de.time_tracking.Time_tracking.service.UserService;
import de.time_tracking.Time_tracking.repository.Database;

public class Main {

    public static void main(String[] args) {
        Database.init();
        System.out.println("DB ready");        
        System.out.println("Time_tracking started");

        ConsoleMenu menu = new ConsoleMenu();
        menu.start();


        
    }
}

