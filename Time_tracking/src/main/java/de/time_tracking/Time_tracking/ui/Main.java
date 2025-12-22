package de.time_tracking.Time_tracking.ui;
import de.time_tracking.Time_tracking.repository.Database;

public class Main {

    public static void main(String[] args) {
        Database.init();


        ConsoleMenu menu = new ConsoleMenu();
        menu.start();


        
    }
}

