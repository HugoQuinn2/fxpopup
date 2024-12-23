package io.github.hugoquinn2.fxpopup.config;

import io.github.hugoquinn2.fxpopup.constants.Theme;
import javafx.geometry.Pos;

public class FxPopupConfig {
    public static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search?format=json&q=";

    public static final int maxWidth = 400;
    public static final int insetsMessageManager = 20;
    public static final Pos defaultPos = Pos.BOTTOM_CENTER;
    public static final double iconScale = 1;

    public static final String iconPath = "/icons/%s.svg";

    public static final String titleId = "#messageTitle";
    public static final String contextId = "#messageContext";
    public static final String messageManagerId = "messageManager";
    public static final String buttonDropId = "buttonCloseMessage";
    public static final String messageFormId = "messageFormBody";

    public static final String pathBody = "/layout/popupSimple.fxml";
    public static final String pathMessageForm = "/layout/MessageForm.fxml";

    public static final Theme defaultTheme = Theme.LIGHT;
    public static final String pathDarkError = "/themes/dark/error.css";
    public static final String pathDarkInfo = "/themes/dark/info.css";
    public static final String pathDarkWarning = "/themes/dark/warning.css";
    public static final String pathDarkSuccess = "/themes/dark/success.css";
    public static final String pathDarkMessageForm = "/themes/dark/messageForm.css";

    public static final String pathLightError = "/themes/light/error.css";
    public static final String pathLightInfo = "/themes/light/info.css";
    public static final String pathLightWarning = "/themes/light/warning.css";
    public static final String pathLightSuccess = "/themes/light/success.css";
    public static final String pathLightMessageForm = "/themes/light/messageForm.css";
}
