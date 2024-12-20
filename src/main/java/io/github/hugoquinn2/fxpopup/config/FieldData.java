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
}
