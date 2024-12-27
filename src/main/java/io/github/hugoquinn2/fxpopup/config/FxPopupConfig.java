package io.github.hugoquinn2.fxpopup.config;

import io.github.hugoquinn2.fxpopup.constants.Theme;
import javafx.geometry.Pos;

/**
 * Configuration class for the FxPopup library.
 * This class holds static configuration constants such as URLs, paths, theme settings, and form IDs.
 */
public class FxPopupConfig {

    // ===============================
    // General Configuration Constants
    // ===============================

    /**
     * URL used for geocoding services via OpenStreetMap's Nominatim API.
     */
    public static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search";

    /**
     * Maximum width for popup windows.
     */
    public static final int maxWidth = 400;

    /**
     * Insets (padding) for the message manager in the popup.
     */
    public static final int insetsMessageManager = 20;

    /**
     * Default position for the popup window (bottom center).
     */
    public static final Pos defaultPos = Pos.BOTTOM_CENTER;

    /**
     * Scale factor for icons in the popup.
     */
    public static final double iconScale = 1;

    /**
     * Path template for the icon used in the popup.
     * Icons are expected to be in the "/icons" directory and in SVG format.
     */
    public static final String iconPath = "/icons/%s.svg";


    // ============================
    // Form and Layout Configuration
    // ============================

    /**
     * The ID of the message form container in the popup layout.
     */
    public static final String messageFormId = "messageFormBody";

    /**
     * The ID of the title form container in the popup layout.
     */
    public static final String titleFormId = "titleForm";

    /**
     * The ID of the container for the form fields.
     */
    public static final String fieldsContainerId = "fieldsContainer";

    /**
     * The ID of the overlay background element.
     */
    public static final String overlayId = "overlay";

    /**
     * The ID of the title element in the message form.
     */
    public static final String titleId = "#messageTitle";

    /**
     * The ID of the context element in the message form.
     */
    public static final String contextId = "#messageContext";

    /**
     * The ID of the message manager container.
     */
    public static final String messageManagerId = "messageManager";

    /**
     * The ID of the close button for the message popup.
     */
    public static final String buttonDropId = "buttonCloseMessage";


    // ============================
    // File Paths for Layouts and Themes
    // ============================

    /**
     * Path to the basic popup layout FXML file.
     */
    public static final String pathBody = "/layout/popupSimple.fxml";

    /**
     * Path to the message form layout FXML file.
     */
    public static final String pathMessageForm = "/layout/MessageForm.fxml";

    // ============================
    // Theme Configuration
    // ============================

    /**
     * Default theme for the popup (light theme).
     */
    public static final Theme defaultTheme = Theme.LIGHT;

    // Paths to the dark theme CSS files for different message types
    public static final String pathDarkError = "/themes/dark/error.css";
    public static final String pathDarkInfo = "/themes/dark/info.css";
    public static final String pathDarkWarning = "/themes/dark/warning.css";
    public static final String pathDarkSuccess = "/themes/dark/success.css";
    public static final String pathDarkMessageForm = "/themes/dark/messageForm.css";

    // Paths to the light theme CSS files for different message types
    public static final String pathLightError = "/themes/light/error.css";
    public static final String pathLightInfo = "/themes/light/info.css";
    public static final String pathLightWarning = "/themes/light/warning.css";
    public static final String pathLightSuccess = "/themes/light/success.css";
    public static final String pathLightMessageForm = "/themes/light/messageForm.css";
}
