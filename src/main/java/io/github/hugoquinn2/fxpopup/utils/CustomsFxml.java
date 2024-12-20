package io.github.hugoquinn2.fxpopup.utils;

import io.github.hugoquinn2.fxpopup.constants.FxPopIcon;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Cursor;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.function.Function;

public class CustomsFxml {
    public static <T> ComboBox<T> createCustomComboBox(ObservableList<T> items){
        ComboBox<T> comboBox = new ComboBox<>(items);

        StyleUtil.setTransparent(comboBox);

        comboBox.getStyleClass().add("combo-box");
        comboBox.setVisibleRowCount(5);

        comboBox.skinProperty().addListener((obs, oldSkin, newSkin) -> {
            StackPane arrowButton = (StackPane) comboBox.lookup(".arrow-button");
            if (arrowButton != null) {
                arrowButton.setCursor(Cursor.HAND);
                StyleUtil.setTransparent(arrowButton);
                arrowButton.getChildren().clear();
                arrowButton.getChildren().add(SVGUtil.getIcon(FxPopIcon.CHEVRON_DOWN, 0.8));
            }
        });

        return comboBox;
    }

    public static <T> ComboBox<T> createSearchableComboBox(ObservableList<T> items, Function<T, String> toStringFunction) {
        ComboBox<T> comboBox = new ComboBox<>();
        comboBox.setEditable(true);

        // Crear lista filtrada
        FilteredList<T> filteredItems = new FilteredList<>(items);
        comboBox.setItems(filteredItems);

        TextField editor = comboBox.getEditor();
        comboBox.setOnShowing(event -> filteredItems.setPredicate(item -> true)); // Resetear filtro al mostrar el ComboBox

        // Filtro de búsqueda
        editor.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                filteredItems.setPredicate(item -> newValue == null || newValue.isEmpty() ||
                        toStringFunction.apply(item).toLowerCase().contains(newValue.toLowerCase()));
                if (!filteredItems.isEmpty()) {
                    comboBox.show();
                } else {
                    comboBox.hide();
                }
            }
        });

        // Manejar selección
        comboBox.setOnAction(event -> {
            T selectedItem = comboBox.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                editor.setText(toStringFunction.apply(selectedItem));
                filteredItems.setPredicate(null); // Resetear el filtro
            }
        });

        // Mostrar el ComboBox al hacer clic en el editor
        editor.setOnMouseClicked(event -> comboBox.show());

        // Manejar el enfoque del editor para evitar que el contenido se elimine al perder el enfoque
        editor.focusedProperty().addListener((observable, oldFocus, newFocus) -> {
            if (!newFocus && !filteredItems.contains(editor.getText())) {
                T selectedItem = comboBox.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    editor.setText(toStringFunction.apply(selectedItem));
                }
            }
        });

        return comboBox;
    }

}
