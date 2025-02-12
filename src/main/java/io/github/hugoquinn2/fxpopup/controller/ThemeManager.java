package io.github.hugoquinn2.fxpopup.controller;

import io.github.hugoquinn2.fxpopup.constants.Theme;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ThemeManager {
    private static final ObjectProperty<Theme> globalTheme = new SimpleObjectProperty<>(Theme.SYSTEM);

    public static ObjectProperty<Theme> globalTheme() {
        return globalTheme;
    }

    public static Theme getGlobalTheme() {
        return globalTheme().get();
    }

    public static void setGlobalTheme(Theme theme) {
        globalTheme.set(theme);
    }

}
