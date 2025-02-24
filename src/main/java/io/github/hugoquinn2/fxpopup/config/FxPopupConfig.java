package io.github.hugoquinn2.fxpopup.config;

import io.github.hugoquinn2.fxpopup.constants.Theme;
import javafx.geometry.Pos;
import javafx.geometry.VPos;

/**
 * Configuration class for the FxPopup library.
 * This class holds static configuration constants such as URLs, paths, theme settings, and form IDs.
 */
public class FxPopupConfig {

    /**
     * URL used for geocoding services via OpenStreetMap's Nominatim API.
     */
    public static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search";

    /**
     * Scale factor for icons in the popup.
     */
    public static final double iconScale = 1;

    /**
     * Path template for the icon used in the popup.
     * Icons are expected to be in the "/icons" directory and in SVG format.
     */
    public static final String iconPath = "/icons/%s.svg";
}
