package io.github.hugoquinn2.fxpopup.service;

import static io.github.hugoquinn2.fxpopup.utils.ThemeDetectorUtil.*;

public class ThemeDetector {
    public static boolean isDarkTheme() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return  isDarkThemeWindows();
        } else if (os.contains("mac")) {
            return isDarkThemeMac();
        } else if (os.contains("nux")) {
            return isDarkThemeLinux();
        }
        return false; // Default to light for unsupported OS
    }
}
