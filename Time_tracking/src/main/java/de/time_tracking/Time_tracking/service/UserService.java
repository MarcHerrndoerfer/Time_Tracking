package de.time_tracking.Time_tracking.service;

import java.util.List;

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

    public List<User> getAllUsers(){
        return userRepository.findAllUsers();
    }

    public boolean UserLogin(String username, String password) {
        User user = userRepository.findByUsername(username);
        if(user != null && user.getPasswordHash().equals((password))) 
            { return true; 

            } return false; 
        }

    



}


    

