package ui;

public class StartupChecker {
    public static boolean checkJavaVersion() {
        String version = System.getProperty("java.version");
        System.out.println("Java runtime version: " + version);
        // project compiled for Java 17 (class file version 61)
        try {
            String[] parts = version.split("[._-]");
            int major = Integer.parseInt(parts[0]);
            if (major >= 17) return true;
        } catch (Exception e) {
            // ignore
        }
        System.err.println("ERROR: Java 17+ is required to run this application. Please use a Java 17 (or newer) runtime.");
        return false;
    }
}
