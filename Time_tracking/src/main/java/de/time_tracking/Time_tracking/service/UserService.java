package de.time_tracking.Time_tracking.service;

import de.time_tracking.Time_tracking.model.User;
import de.time_tracking.Time_tracking.repository.UserRepository;

public class UserService {

    private final UserRepository userRepository = new UserRepository();

    public boolean registerUser(String username, String password, String role) {
        if (userRepository.findByUsername(username) != null) {
            return false;
        }

        User user = new User(username, password, role);
        userRepository.create(user);
        return true;
    }

    public User findUser(String username) {
        return userRepository.findByUsername(username);
    }
}
    

