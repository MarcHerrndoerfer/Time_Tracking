package de.time_tracking.Time_tracking.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import io.github.cdimascio.dotenv.Dotenv;

public class Database {

    private static final Dotenv dotenv = Dotenv.configure()
        .ignoreIfMissing()
        .load();


    private static final String HOST = dotenv.get("MYSQL_HOST", "localhost");
    private static final String PORT = dotenv.get("MYSQL_PORT", "3306");
    private static final String DB   = dotenv.get("MYSQL_DATABASE", "timetracker");
    private static final String USER = dotenv.get("MYSQL_USER");
    private static final String PASS = dotenv.get("MYSQL_PASSWORD");

    private static final String URL =
            "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB +
            "?useUnicode=true&characterEncoding=utf8&serverTimezone=Europe/Berlin";

    public static Connection getConnection() throws SQLException {
        if (USER == null || PASS == null) {
            throw new IllegalStateException(
                "Missing DB credentials. Create a .env file with MYSQL_USER and MYSQL_PASSWORD."
            );
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static void init() {
        String sqlUsers = """
            CREATE TABLE IF NOT EXISTS users (
                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                username VARCHAR(100) NOT NULL UNIQUE,
                password_hash VARCHAR(255) NOT NULL,
                role VARCHAR(50) NOT NULL
            );
            """;

        String sqlTimeEntries = """
            CREATE TABLE IF NOT EXISTS time_entries (
                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                user_id BIGINT NOT NULL,
                date DATE NOT NULL,
                start_time TIME NOT NULL,
                end_time TIME NOT NULL,
                break_minutes INT NOT NULL DEFAULT 0,
                CONSTRAINT fk_time_user
                    FOREIGN KEY (user_id) REFERENCES users(id)
            );
            """;

        try (Connection con = getConnection();
             Statement st = con.createStatement()) {

            st.execute(sqlUsers);
            st.execute(sqlTimeEntries);

        } catch (SQLException e) {
            throw new RuntimeException("DB init failed", e);
        }
    }
}
