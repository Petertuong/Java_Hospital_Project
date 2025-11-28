package ui;

import java.util.HashMap;
import java.util.Map;

public class AuthService {

    private static final Map<String, String> USERS = new HashMap<>();

    static {
        // default account
        USERS.put("admin", "admin123");
    }

    public static boolean login(String username, String password) {
        if (username == null || password == null) return false;
        String stored = USERS.get(username);
        return stored != null && stored.equals(password);
    }

    public static boolean register(String username, String password) {
        if (username == null || password == null) return false;

        // JDK 8 không có isBlank() => dùng trim().isEmpty()
        if (username.trim().isEmpty() || password.trim().isEmpty()) return false;

        if (USERS.containsKey(username)) return false;
        USERS.put(username, password);
        return true;
    }
}
