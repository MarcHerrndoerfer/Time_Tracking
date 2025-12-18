package de.time_tracking.Time_tracking.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String URL = "jdbc:sqlite:timetracker.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void init() {
        String sqlUsers = """
            CREATE TABLE IF NOT EXISTS users (
              id INTEGER PRIMARY KEY AUTOINCREMENT,
              username TEXT NOT NULL UNIQUE,
              password_hash TEXT NOT NULL,
              role TEXT NOT NULL
            );
            """;

        String sqlTimeEntries = """
            CREATE TABLE IF NOT EXISTS time_entries (
              id INTEGER PRIMARY KEY AUTOINCREMENT,
              user_id INTEGER NOT NULL,
              date TEXT NOT NULL,
              start_time TEXT NOT NULL,
              end_time TEXT NOT NULL,
              break_minutes INTEGER NOT NULL DEFAULT 0,
              FOREIGN KEY(user_id) REFERENCES users(id)
            );
            """;

        try (Connection con = getConnection(); Statement st = con.createStatement()) {
            st.execute(sqlUsers);
            st.execute(sqlTimeEntries);
        } catch (SQLException e) {
            throw new RuntimeException("DB init failed", e);
        }
    }
}
