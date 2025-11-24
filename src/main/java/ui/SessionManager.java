package ui;

public class SessionManager {

    public enum Role {
        ADMIN, STAFF
    }

    private static String currentUsername;
    private static Role currentRole;

    public static void login(String username, Role role) {
        currentUsername = username;
        currentRole = role;
    }

    public static void logout() {
        currentUsername = null;
        currentRole = null;
    }

    public static boolean isLoggedIn() {
        return currentUsername != null && currentRole != null;
    }

    public static String getCurrentUsername() {
        return currentUsername;
    }

    public static Role getCurrentRole() {
        return currentRole;
    }
}
