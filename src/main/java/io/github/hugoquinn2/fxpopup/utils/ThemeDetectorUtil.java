package io.github.hugoquinn2.fxpopup.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ThemeDetectorUtil {
    public static boolean isDarkThemeLinux() {
        try {
            ProcessBuilder pb = new ProcessBuilder("gsettings", "get", "org.gnome.desktop.interface", "gtk-theme");
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            return line != null && line.toLowerCase().contains("dark"); // Checks if the theme contains "dark"
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Default to light if detection fails
        }
    }

    public static boolean isDarkThemeMac() {
        try {
            ProcessBuilder pb = new ProcessBuilder("defaults", "read", "-g", "AppleInterfaceStyle");
            Process process = pb.start();
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            return "Dark".equalsIgnoreCase(line); // Returns true if "Dark" theme is enabled
        } catch (Exception e) {
            // If the command fails or output is null, assume light theme
            return false;
        }
    }

    public static boolean isDarkThemeWindows() {
        try {
            ProcessBuilder pb = new ProcessBuilder("reg", "query",
                    "HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize",
                    "/v", "AppsUseLightTheme");
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("AppsUseLightTheme")) {
                    String[] parts = line.trim().split("\\s+");
                    return "0x0".equals(parts[parts.length - 1]); // 0x0 = dark, 0x1 = light
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Default to light if detection fails
    }

}
