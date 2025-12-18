package de.time_tracking.Time_tracking.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.time_tracking.Time_tracking.model.User;

public class UserRepository {

    public void create(User user) {
        String sql = """
            INSERT INTO users (username, password_hash, role)
            VALUES (?, ?, ?)
            """;

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getRole());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("User could not be saved.", e);
        }
    }

    public User findByUsername(String username) {
        String sql = """
            SELECT id, username, password_hash, role
            FROM users
            WHERE username = ?
            """;

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setRole(rs.getString("role"));
                return user;
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException("User could not be loaded.", e);
        }
    }
}
