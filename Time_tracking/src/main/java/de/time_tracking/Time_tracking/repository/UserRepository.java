package de.time_tracking.Time_tracking.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.time_tracking.Time_tracking.model.User;
import de.time_tracking.Time_tracking.model.Role;


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
            ps.setString(3, user.getRole().name());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("User could not be saved.", e);
        }
    }

    public User login(String username, String passwordHash) {
        String sql = """
            SELECT id, username, password_hash, role
            FROM users
            WHERE username = ? AND password_hash = ?
            """;

        try (Connection con = Database.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, passwordHash);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setRole(Role.valueOf(rs.getString("role")));
                return user;
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to login", e);
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
                user.setRole(Role.valueOf(rs.getString("role")));
                return user;
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException("User could not be loaded.", e);
        }
    }

    public void deleteUser(User user) throws SQLException {
        String sql = """
            DELETE FROM users
            WHERE username = ?
            """;
        try (Connection con = Database.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("User could not be loaded.", e);
        }
    }

    public List<User> findAllUsers() {
    String sql = """
        SELECT username FROM users
                """;

    List<User> users = new ArrayList<>();

    try (Connection con = Database.getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            User user = new User();
            user.setUsername(rs.getString("username"));
            users.add(user);
        }

        return users;

    } catch (SQLException e) {
        throw new RuntimeException("Users could not be loaded.", e);
    }
}
  
        
    }



