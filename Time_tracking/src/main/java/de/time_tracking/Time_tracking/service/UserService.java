package de.time_tracking.Time_tracking.service;

import java.util.List;

import de.time_tracking.Time_tracking.model.User;
import de.time_tracking.Time_tracking.repository.UserRepository;
import de.time_tracking.Time_tracking.model.Role;

public class UserService {

    private final UserRepository userRepository = new UserRepository();
    private final PasswordPolicyService passwordPolicyService = new PasswordPolicyService();

    public void registerUser(String username, String password, String roleinput) {
        if (userRepository.findByUsername(username) != null) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        passwordPolicyService.validate(password);

        Role role;
        try {
            role = Role.valueOf(roleinput.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role. Use USER or ADMIN.");
        }

        User user = new User(username, password, role);
        userRepository.create(user);
    }

    public User findUser(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers(){
        return userRepository.findAllUsers();
    }

    public boolean login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPasswordHash().equals(password)) {
            return true;
        }
        return false;
    }

    public Role parseRole(String input) {
    try {
        return Role.valueOf(input.trim().toUpperCase());
    } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("Invalid role. Use USER or ADMIN.");
    }
}

}





    

