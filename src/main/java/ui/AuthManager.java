package ui;

import ui.SessionManager.Role;

import java.util.ArrayList;
import java.util.List;

public class AuthManager {

    // Simple in-memory user model
    public static class User {
        private final String username;
        private final String password;
        private final Role role;

        public User(String username, String password, Role role) {
            this.username = username;
            this.password = password;
            this.role = role;
        }

        public String getUsername() { return username; }
        public String getPassword() { return password; }
        public Role getRole() { return role; }
    }

    // In-memory user list (lost when app closes)
    private static final List<User> users = new ArrayList<>();

    static {
        // Demo accounts
        users.add(new User("admin", "admin123", Role.ADMIN));
        users.add(new User("staff", "staff123", Role.STAFF));
    }

    public static User authenticate(String username, String password) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username)
                        && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public static boolean exists(String username) {
        return users.stream()
                .anyMatch(u -> u.getUsername().equals(username));
    }

    public static void registerStaff(String username, String password) {
        if (exists(username)) {
            throw new IllegalArgumentException("Username already exists.");
        }
        users.add(new User(username, password, Role.STAFF));
    }
}
