package de.time_tracking.Time_tracking.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import de.time_tracking.Time_tracking.model.User;
import de.time_tracking.Time_tracking.repository.UserRepository;
import de.time_tracking.Time_tracking.model.Role;

public class UserService {

    private final UserRepository userRepository = new UserRepository();
    private final PasswordPolicyService passwordPolicyService = new PasswordPolicyService();
    private final Argon2PasswordEncoder encoder = new Argon2PasswordEncoder(32, 64, 1, 15 * 1024, 2);

    public void registerUser(String username, String rawPassword, String roleinput) {
        if (userRepository.findByUsername(username) != null) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        passwordPolicyService.validate(rawPassword);

        Role role = parseRole(roleinput);
        String passwordHash = encoder.encode(rawPassword);

        User user = new User(username, passwordHash, role);
        userRepository.create(user);
    }


    public User findUser(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers(){
        return userRepository.findAllUsers();
    }

    public boolean login(String username, String rawPassword) {
            User user = userRepository.findByUsername(username);
            if (user == null) {
                return false;
            }
            return encoder.matches(rawPassword, user.getPasswordHash());
        }

    public Role parseRole(String input) {
    try {
        return Role.valueOf(input.trim().toUpperCase());
    } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("Invalid role. Use USER or ADMIN.");
    }
    }

    public void deleteUser(User user) throws SQLException{
        userRepository.deleteUser(user);

    }

    public void deleteAllUSers()throws SQLException{
        userRepository.deleteAllUSers();
    }

}





    

