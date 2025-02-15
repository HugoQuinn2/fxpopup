package io.github.hugoquinn2.fxpopup.controller;

import io.github.hugoquinn2.fxpopup.constants.Theme;
import io.github.hugoquinn2.fxpopup.service.ThemeDetector;
import io.github.hugoquinn2.fxpopup.utils.MasterUtils;
import io.github.hugoquinn2.fxpopup.utils.ThemeDetectorUtil;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class StyleManager {
    private static final String MESSAGE_DARK_STYLE_PATH = "/themes/dark/message.css";
    private static final String MESSAGE_LIGHT_STYLE_PATH = "/themes/light/message.css";
    private static final String FORM_DARK_STYLE_PATH = "/themes/dark/form.css";
    private static final String FORM_LIGHT_STYLE_PATH = "/themes/light/form.css";
    private static final String TOOL_TIP_DARK_STYLE_PATH = "/themes/dark/tool-tip.css";
    private static final String TOOL_TIP_LIGHT_STYLE_PATH = "/themes/light/tool-tip.css";

    public static void applyAutoStyle() {
        Platform.runLater(() -> {
            Scene scene = MasterUtils.getPrimaryScene();

            if (scene != null) {
                loadMessageStyle(scene, ThemeManager.getGlobalTheme());
                loadFormStyle(scene, ThemeManager.getGlobalTheme());
                loadToolTipStyle(scene, ThemeManager.getGlobalTheme());

                ThemeManager.globalTheme().addListener((obs, oldTheme, newTheme) -> {
                    if (oldTheme != null) {
                        removeMessageStyle(scene, oldTheme);
                        removeFormStyle(scene, oldTheme);
                        removeToolTipStyle(scene, oldTheme);
                    }

                    if (newTheme != null) {
                        loadMessageStyle(scene, newTheme);
                        loadFormStyle(scene, newTheme);
                        loadToolTipStyle(scene, newTheme);
                    }
                });
            }
        });
    }

    // --------------- Message Load Style --------------------------

    private static void loadMessageStyle(Scene scene, Theme theme) {
        String style = Objects.requireNonNull(StyleManager.class.getResource(
                switch (theme) {
                    case SYSTEM -> ThemeDetector.isDarkTheme() ? MESSAGE_DARK_STYLE_PATH : MESSAGE_LIGHT_STYLE_PATH ;
                    case DARK -> MESSAGE_DARK_STYLE_PATH;
                    case LIGHT -> MESSAGE_LIGHT_STYLE_PATH;
                }
        )).toExternalForm();

        if (!scene.getStylesheets().contains(style))
            scene.getStylesheets().add(
                    0,
                    style
            );
    }

    private static void removeMessageStyle(Scene scene, Theme theme) {
        scene.getStylesheets().remove(
                Objects.requireNonNull(StyleManager.class.getResource(
                        switch (theme) {
                            case SYSTEM -> ThemeDetector.isDarkTheme() ? MESSAGE_DARK_STYLE_PATH : MESSAGE_LIGHT_STYLE_PATH ;
                            case DARK -> MESSAGE_DARK_STYLE_PATH;
                            case LIGHT -> MESSAGE_LIGHT_STYLE_PATH;
                        }
                )).toExternalForm()
        );
    }

    // ----------------------- Form Load Style --------------------------

    private static void loadFormStyle(Scene scene, Theme theme) {
        String style = Objects.requireNonNull(StyleManager.class.getResource(
                switch (theme) {
                    case SYSTEM -> ThemeDetector.isDarkTheme() ? FORM_DARK_STYLE_PATH : FORM_LIGHT_STYLE_PATH;
                    case DARK -> FORM_DARK_STYLE_PATH;
                    case LIGHT -> FORM_LIGHT_STYLE_PATH;
                }
        )).toExternalForm();

        if (!scene.getStylesheets().contains(style))
            scene.getStylesheets().add(
                    0,
                    style
            );
    }

    private static void removeFormStyle(Scene scene, Theme theme) {
        scene.getStylesheets().remove(
                Objects.requireNonNull(StyleManager.class.getResource(
                        switch (theme) {
                            case SYSTEM -> ThemeDetector.isDarkTheme() ? FORM_DARK_STYLE_PATH : FORM_LIGHT_STYLE_PATH;
                            case DARK -> FORM_DARK_STYLE_PATH;
                            case LIGHT -> FORM_LIGHT_STYLE_PATH;
                        }
                )).toExternalForm()
        );
    }

    // ------------------------- Tool Tip Load Style -------------------------

    private static void loadToolTipStyle(Scene scene, Theme theme) {
        String style = Objects.requireNonNull(StyleManager.class.getResource(
                switch (theme) {
                    case SYSTEM -> ThemeDetector.isDarkTheme() ? TOOL_TIP_DARK_STYLE_PATH : TOOL_TIP_LIGHT_STYLE_PATH ;
                    case DARK -> TOOL_TIP_DARK_STYLE_PATH;
                    case LIGHT -> TOOL_TIP_LIGHT_STYLE_PATH;
                }
        )).toExternalForm();

        if (!scene.getStylesheets().contains(style))
            scene.getStylesheets().add(
                    0,
                    style
            );
    }

    private static void removeToolTipStyle(Scene scene, Theme theme) {
        scene.getStylesheets().remove(
                Objects.requireNonNull(StyleManager.class.getResource(
                        switch (theme) {
                            case SYSTEM -> ThemeDetector.isDarkTheme() ? TOOL_TIP_DARK_STYLE_PATH : TOOL_TIP_LIGHT_STYLE_PATH ;
                            case DARK -> TOOL_TIP_DARK_STYLE_PATH;
                            case LIGHT -> TOOL_TIP_LIGHT_STYLE_PATH;
                        }
                )).toExternalForm()
        );
    }

}
