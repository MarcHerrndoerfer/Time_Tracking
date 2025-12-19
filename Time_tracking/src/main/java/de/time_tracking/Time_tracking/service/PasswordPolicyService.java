package de.time_tracking.Time_tracking.service;

public class PasswordPolicyService  {
    public void validate(String password) {

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password must not be empty.");
        }

        if (password.length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters long.");
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Password must contain an uppercase letter.");
        }

        if (!password.matches(".*[0-9].*")) {
            throw new IllegalArgumentException("Password must contain a number.");
        }
    }

}
