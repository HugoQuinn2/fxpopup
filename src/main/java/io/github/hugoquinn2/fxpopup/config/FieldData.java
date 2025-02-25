package io.github.hugoquinn2.fxpopup.config;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Class that contains field data for the FxPopup library, including predefined lists
 * of countries, phone number prefixes, and regular expressions for validating different types of input.
 */
public class FieldData {

    // ===============================
    // Predefined Lists for Countries and Lada Codes
    // ===============================

    /**
     * A list of country names.
     */
    public static ObservableList<String> countryList = FXCollections.observableArrayList(
            "Argentina", "Australia", "Brazil", "Canada", "Chile", "China", "Colombia",
            "Egypt", "France", "Germany", "India", "Italy", "Japan", "Mexico", "Nigeria", "Russia",
            "South Africa", "South Korea", "Spain", "Sweden", "Switzerland", "United Kingdom",
            "United States", "Venezuela"
    );

    /**
     * A list of Lada (country calling) codes.
     */
    public static ObservableList<String> ladaList = FXCollections.observableArrayList(
            "+52", "+54", "+55", "+57", "+58", "+59", "+60", "+61", "+62", "+63",
            "+64", "+65", "+66", "+67", "+68", "+69", "+70", "+71", "+72", "+73", "+74",
            "+75", "+76", "+77", "+78", "+79", "+81", "+82", "+84", "+86", "+90", "+91",
            "+93", "+94", "+95", "+96", "+97", "+98", "+99"
    );

    // ============================
    // Regular Expressions for Field Validation
    // ============================

    /**
     * Regular expression for validating an IP address.
     */
    public static final String ipMatch = "^((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)$";

    /**
     * Regular expression for validating a MAC address.
     */
    public static final String macMatch = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})|([0-9A-Fa-f]{4}\\.[0-9A-Fa-f]{4}\\.[0-9A-Fa-f]{4})$\n";

    /**
     * Regular expression for validating a URL.
     */
    public static final String urlMatch = "^(https?|ftp)://[\\w.-]+(:\\d+)?(/[\\w.-]*)*/?$";

    /**
     * Regular expression for validating GPS coordinates.
     */
    public static final String gpsMatch = "^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?)\\s*,\\s*[-+]?(180(\\.0+)?|(1[0-7]\\d|\\d{1,2})(\\.\\d+)?)$";

    /**
     * Regular expression for validating byte data.
     */
    public static final String byteMatch = "^([0-9A-Fa-f]{2}\\s*)+$";

    /**
     * Regular expression for validating hexadecimal data.
     */
    public static final String hexMatch = "^(0[xX])?[0-9A-Fa-f]+(\\s+(0[xX])?[0-9A-Fa-f]+)*$";

    /**
     * Regular expression for validating an email address.
     */
    public static final String emailMatch = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,7}$";

    /**
     * Regular expression for validating a phone number.
     */
    public static final String phoneMatch = "\\d{3}[-\\s]?\\d{3}[-\\s]?\\d{4}";

    /**
     * Regular expression for validating a number (integer or decimal).
     */
    public static final String numberMatch = "^[-+]?\\d*\\.?\\d+$";

    public static final String cardMatch = "^(?:4[0-9]{12}(?:[0-9]{3})?|[25][1-7][0-9]{14}|6(?:011|5[0-9][0-9])[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\\d{3})\\d{11})$";

    public static final String visaMatch = "^4[0-9]{12}(?:[0-9]{3})?$";

    public static final String mcMatch = "^(?:5[1-5][0-9]{2}|222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)[0-9]{12}$";

    public static final String amexMatch = "^3[47][0-9]{13}$";
}
