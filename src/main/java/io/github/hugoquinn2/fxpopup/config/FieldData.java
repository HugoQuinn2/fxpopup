package io.github.hugoquinn2.fxpopup.config;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FieldData {
    public static ObservableList<String> countryList = FXCollections.observableArrayList(
            "Argentina", "Australia", "Brazil", "Canada", "Chile", "China", "Colombia",
            "Egypt", "France", "Germany", "India", "Italy", "Japan", "Mexico", "Nigeria", "Russia",
            "South Africa", "South Korea", "Spain", "Sweden", "Switzerland", "United Kingdom",
            "United States", "Venezuela"
    );

    public static ObservableList<String> ladaList = FXCollections.observableArrayList(
            "+52", "+54", "+55", "+57", "+58", "+59", "+60", "+61", "+62", "+63",
            "+64", "+65", "+66", "+67", "+68", "+69", "+70", "+71", "+72", "+73", "+74",
            "+75", "+76", "+77", "+78", "+79", "+81", "+82", "+84", "+86", "+90", "+91",
            "+93", "+94", "+95", "+96", "+97", "+98", "+99"
    );

    public static final String ipMatch = "^((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)$";
    public static final String macMatch = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})|([0-9A-Fa-f]{4}\\.[0-9A-Fa-f]{4}\\.[0-9A-Fa-f]{4})$\n";
    public static final String urlMatch = "^(https?|ftp)://[\\w.-]+(:\\d+)?(/[\\w.-]*)*/?$";
    public static final String gpsMatch = "^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?)\\s*,\\s*[-+]?(180(\\.0+)?|(1[0-7]\\d|\\d{1,2})(\\.\\d+)?)$";
    public static final String byteMatch = "^([0-9A-Fa-f]{2}\\s*)+$";
    public static final String hexMatch = "^(0[xX])?[0-9A-Fa-f]+(\\s+(0[xX])?[0-9A-Fa-f]+)*$";
    public static final String emailMatch = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,7}$";
    public static final String phoneMatch = "\\d{3}[-\\s]?\\d{3}[-\\s]?\\d{4}";
    public static final String numberMatch = "^[-+]?\\d*\\.?\\d+$";
}
