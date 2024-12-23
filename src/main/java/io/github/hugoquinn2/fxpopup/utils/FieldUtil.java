package io.github.hugoquinn2.fxpopup.utils;

import io.github.hugoquinn2.fxpopup.config.FieldData;
import io.github.hugoquinn2.fxpopup.config.FxPopupConfig;
import io.github.hugoquinn2.fxpopup.config.StyleConfig;
import io.github.hugoquinn2.fxpopup.constants.FieldType;
import io.github.hugoquinn2.fxpopup.constants.FxPopIcon;
import io.github.hugoquinn2.fxpopup.controller.MessageField;
import io.github.hugoquinn2.fxpopup.service.OpenStreetMapService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

public class FieldUtil {
    public static Parent createTextField(Field field ,Object model, FxPopIcon icon) {
        field.setAccessible(true);
        try {
            MessageField annotation = field.getAnnotation(MessageField.class);
            Object data = field.get(model);
            Label iconLabel = new Label();

            iconLabel.setGraphic(SVGUtil.getIcon(icon, FxPopupConfig.iconScale));

            TextField textField;
            if (data != null)
                textField = new TextField(data.toString());
            else
                textField = new TextField();

            StyleUtil.setTransparent(textField);
            textField.setPromptText(annotation.placeholder());

            setAutoUpdateModel(textField, field, model);
            applyValidation(textField, annotation);

            HBox.setHgrow(textField, Priority.ALWAYS);

            HBox container = new HBox(iconLabel, textField);
            container.getStyleClass().add("field");
            container.setAlignment(Pos.CENTER);

            textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                if (!isNowFocused && annotation.required()) {
                    if (textField.getText().isEmpty() || textField.getText().isBlank())
                        container.getStyleClass().add("required");
                    else
                        container.getStyleClass().remove("required");
                }
            });

            return container;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static Parent createPasswordField(Field field, Object model, FxPopIcon icon) {
        field.setAccessible(true);
        try {
            MessageField annotation = field.getAnnotation(MessageField.class);

            Button eyeButton = new Button();
            PasswordField passField = new PasswordField();
            TextField textField = new TextField();
            passField.setPromptText(annotation.placeholder());
            textField.setPromptText(annotation.placeholder());
            Label iconLabel = new Label();

            iconLabel.setGraphic(SVGUtil.getIcon(icon, FxPopupConfig.iconScale));

            StyleUtil.setTransparent(eyeButton);
            StyleUtil.setTransparent(textField);
            StyleUtil.setTransparent(passField);

            textField.setVisible(false);
            textField.setManaged(false);

            eyeButton.setGraphic(SVGUtil.getIcon(FxPopIcon.EYE, FxPopupConfig.iconScale));

            eyeButton.setCursor(Cursor.HAND);
            passField.textProperty().bindBidirectional(textField.textProperty());

            eyeButton.setOnAction(event -> {
                boolean isTextVisible = textField.isVisible();
                textField.setVisible(!isTextVisible);
                textField.setManaged(!isTextVisible);
                passField.setVisible(isTextVisible);
                passField.setManaged(isTextVisible);
                eyeButton.setGraphic(isTextVisible ? SVGUtil.getIcon(FxPopIcon.EYE, FxPopupConfig.iconScale) : SVGUtil.getIcon(FxPopIcon.EYE_CLOSE, FxPopupConfig.iconScale));
            });

            setAutoUpdateModel(textField, field, model);
            applyValidation(passField, annotation);

            HBox.setHgrow(passField, Priority.ALWAYS);
            HBox.setHgrow(textField, Priority.ALWAYS);

            HBox container = new HBox(iconLabel, passField, textField, eyeButton);
            container.getStyleClass().add("field");
            container.setAlignment(Pos.CENTER);

            textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                if (!isNowFocused && annotation.required()) {
                    if (textField.getText().isEmpty() || textField.getText().isBlank())
                        container.getStyleClass().add("required");
                    else
                        container.getStyleClass().remove("required");
                }
            });

            passField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                if (!isNowFocused && annotation.required()) {
                    if (passField.getText().isEmpty() || passField.getText().isBlank())
                        container.getStyleClass().add("required");
                    else
                        container.getStyleClass().remove("required");
                }
            });

            return container;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static Parent createIPField(Field field, Object model, FxPopIcon icon){
        field.setAccessible(true);
        try {
            MessageField annotation = field.getAnnotation(MessageField.class);
            Object data = field.get(model);
            Label iconLabel = new Label();

            iconLabel.setGraphic(SVGUtil.getIcon(icon,FxPopupConfig.iconScale));

            TextField textField;
            if (data != null)
                textField = new TextField(data.toString());
            else
                textField = new TextField();

            StyleUtil.setTransparent(textField);
            textField.setPromptText(annotation.placeholder());

            setAutoUpdateModel(textField, field, model);
            applyValidation(textField, annotation);

            HBox.setHgrow(textField, Priority.ALWAYS);

            HBox container = new HBox(iconLabel, textField);
            container.getStyleClass().add("field");
            container.setAlignment(Pos.CENTER);

            TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
                String newText = change.getControlNewText();
                if (newText.matches("\\d{0,3}(\\.\\d{0,3}){0,3}") && validateIpParts(newText)) {
                    return change;
                } else {
                    return null;
                }
            });

            textField.setTextFormatter(textFormatter);

            textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                if (!isNowFocused && annotation.required()) {
                    if (textField.getText().isEmpty() || textField.getText().isBlank())
                        container.getStyleClass().add("required");
                    else
                        container.getStyleClass().remove("required");
                }
            });

            textField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.length() > oldValue.length()) {
                    if (newValue.matches("\\d{3}(?!\\.)")) {
                        textField.setText(newValue + ".");
                        textField.positionCaret(newValue.length() + 1);
                    } else if (newValue.matches("\\d{3}\\.\\d{3}(?!\\.)")) {
                        textField.setText(newValue + ".");
                        textField.positionCaret(newValue.length() + 1);
                    } else if (newValue.matches("\\d{3}\\.\\d{3}\\.\\d{3}(?!\\.)")) {
                        textField.setText(newValue + ".");
                        textField.positionCaret(newValue.length() + 1);
                    }
                } else {
                    if (oldValue.endsWith(".") && !newValue.endsWith(".")) {
                        textField.setText(oldValue.substring(0, oldValue.length() - 1));
                        textField.positionCaret(oldValue.length() - 1);
                    }
                }
            });

            return container;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static Parent createMACField(Field field, Object model, FxPopIcon icon){
        field.setAccessible(true);
        try {
            MessageField annotation = field.getAnnotation(MessageField.class);
            Object data = field.get(model);
            Label iconLabel = new Label();

            iconLabel.setGraphic(SVGUtil.getIcon(icon, FxPopupConfig.iconScale));

            TextField textField;
            if (data != null)
                textField = new TextField(data.toString());
            else
                textField = new TextField();

            StyleUtil.setTransparent(textField);
            textField.setPromptText(annotation.placeholder());

            setAutoUpdateModel(textField, field, model);
            applyValidation(textField, annotation);

            HBox.setHgrow(textField, Priority.ALWAYS);

            HBox container = new HBox(iconLabel, textField);
            container.getStyleClass().add("field");
            container.setAlignment(Pos.CENTER);

            TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
                String newText = change.getControlNewText();
                if (newText.matches("([0-9A-Fa-f]{0,2}[:]?){0,5}[0-9A-Fa-f]{0,2}") && validateMacSegments(newText)) {
                    return change;
                } else {
                    return null;
                }
            });

            textField.setTextFormatter(textFormatter);

            textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                if (!isNowFocused && annotation.required()) {
                    if (textField.getText().isEmpty() || textField.getText().isBlank())
                        container.getStyleClass().add("required");
                    else
                        container.getStyleClass().remove("required");
                }
            });

            textField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.length() > oldValue.length()) {
                    if (newValue.length() == 2 || newValue.length() == 5 || newValue.length() == 8 || newValue.length() == 11 || newValue.length() == 14) {
                        textField.setText(newValue + ":");
                        textField.positionCaret(newValue.length() + 1);
                    }
                } else {
                    if (oldValue.endsWith(":") && !newValue.endsWith(":")) {
                        textField.setText(oldValue.substring(0, oldValue.length() - 1));
                        textField.positionCaret(oldValue.length() - 1);
                    }
                }
            });

            return container;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static Parent createIntField(Field field, Object model, FxPopIcon icon){
        field.setAccessible(true);
        try {
            MessageField annotation = field.getAnnotation(MessageField.class);
            Object data = field.get(model);
            Label iconLabel = new Label();
            Button less = new Button();
            Button plus = new Button();

            iconLabel.setGraphic(SVGUtil.getIcon(icon, FxPopupConfig.iconScale));

            TextField textField;
            if (data != null)
                textField = new TextField(data.toString());
            else
                textField = new TextField("0");

            StyleUtil.setTransparent(textField);
            StyleUtil.setTransparent(less);
            StyleUtil.setTransparent(plus);
            textField.setPromptText(annotation.placeholder());

            less.setCursor(Cursor.HAND);
            plus.setCursor(Cursor.HAND);

            less.setGraphic(SVGUtil.getIcon(FxPopIcon.LEFT, FxPopupConfig.iconScale));
            plus.setGraphic(SVGUtil.getIcon(FxPopIcon.RIGHT, FxPopupConfig.iconScale));

            less.setOnAction(event -> {
                double value = parseDoubleOrDefault(textField.getText(), 0);
                textField.setText(String.valueOf(value - 1.0));
            });

            plus.setOnAction(event -> {
                double value = parseDoubleOrDefault(textField.getText(), 0);
                textField.setText(String.valueOf(value + 1.0));
            });

            setAutoUpdateModel(textField, field, model);
            applyValidation(textField, annotation);

            HBox.setHgrow(textField, Priority.ALWAYS);

            HBox container = new HBox(iconLabel, textField, less, plus);
            container.getStyleClass().add("field");
            container.setAlignment(Pos.CENTER);

            TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
                String newText = change.getControlNewText();
                if (newText.matches("-?\\d*(\\.\\d*)?")) {
                    return change;
                } else {
                    return null;
                }
            });

            textField.setTextFormatter(textFormatter);

            textField.textProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    textField.setText("0");
                }
            });

            textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                if (!isNowFocused && annotation.required()) {
                    if (textField.getText().isEmpty() || textField.getText().isBlank())
                        container.getStyleClass().add("required");
                    else
                        container.getStyleClass().remove("required");
                }
            });

            return container;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static Parent createPhoneField(Field field ,Object model, FxPopIcon icon) {
        field.setAccessible(true);
        try {
            MessageField annotation = field.getAnnotation(MessageField.class);
            Object data = field.get(model);
            Label iconLabel = new Label();

            ComboBox<String> ladaList;
            if (annotation.data().length == 0) {
                ladaList = CustomsFxml.createCustomComboBox(FieldData.ladaList);
                ladaList.getSelectionModel().select(FieldData.ladaList.get(0));
            }
            else {
                ladaList = CustomsFxml.createCustomComboBox(FXCollections.observableArrayList(annotation.data()));
                ladaList.getSelectionModel().select(FXCollections.observableArrayList(annotation.data()).get(0));
            }

            iconLabel.setGraphic(SVGUtil.getIcon(icon, FxPopupConfig.iconScale));

            TextField textField;
            if (data != null)
                textField = new TextField(data.toString());
            else
                textField = new TextField();

            StyleUtil.setTransparent(textField);
            textField.setPromptText(annotation.placeholder());

            setAutoUpdateModel(textField, ladaList, field, model);
            applyValidation(textField, annotation);

            HBox.setHgrow(textField, Priority.ALWAYS);

            HBox container = new HBox(iconLabel, ladaList, textField);
            container.getStyleClass().add("field");
            container.setAlignment(Pos.CENTER);

            TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
                String newText = change.getControlNewText();

                // Validar que solo se permita el formato: XXX-XXX-XXXX
                if (newText.matches("^\\d{0,3}(-\\d{0,3}){0,1}(-\\d{0,4}){0,1}$")) {
                    if (newText.replace("-", "").length() <= 10) { // No más de 10 dígitos
                        return change;
                    }
                }
                return null; // Rechazar cambios que no cumplan el patrón
            });

            textField.setTextFormatter(textFormatter);

            textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                if (!isNowFocused && annotation.required()) {
                    if (textField.getText().isEmpty() || textField.getText().isBlank())
                        container.getStyleClass().add("required");
                    else
                        container.getStyleClass().remove("required");
                }
            });

            textField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.length() > oldValue.length()) {
                    if (newValue.matches("\\d{3}(?!-)")) {
                        textField.setText(newValue + "-");
                        textField.positionCaret(newValue.length() + 1);
                    } else if (newValue.matches("\\d{3}-\\d{3}(?!-)")) {
                        textField.setText(newValue + "-");
                        textField.positionCaret(newValue.length() + 1);
                    }
                } else {
                    if (oldValue.endsWith("-") && !newValue.endsWith("-")) {
                        textField.setText(oldValue.substring(0, oldValue.length() - 1));
                        textField.positionCaret(oldValue.length() - 1);
                    }
                }
            });

            return container;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static Parent createCountryField(Field field, Object model, FxPopIcon icon) {
        field.setAccessible(true);
        try {
            MessageField annotation = field.getAnnotation(MessageField.class);
            ComboBox<String> countryBox;

            if (annotation.data().length == 0)
                countryBox = CustomsFxml.createCustomComboBox(FieldData.countryList);
            else
                countryBox = CustomsFxml.createCustomComboBox(FXCollections.observableArrayList(annotation.data()));

            countryBox.setPromptText(annotation.placeholder());

            Label iconLabel = new Label();

            iconLabel.setGraphic(SVGUtil.getIcon(icon, FxPopupConfig.iconScale));

            countryBox.setCursor(Cursor.HAND);

            setAutoUpdateModel(countryBox, field, model);
//            applyValidation(countryBox, annotation);

            HBox.setHgrow(countryBox, Priority.ALWAYS);
            countryBox.setMaxWidth(Double.MAX_VALUE);

            HBox container = new HBox(iconLabel, countryBox);
            container.getStyleClass().add("field");
            container.setAlignment(Pos.CENTER);

            return container;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static Parent createAddressField(Field field ,Object model, FxPopIcon icon) {
        field.setAccessible(true);
        try {
            MessageField annotation = field.getAnnotation(MessageField.class);
            Object data = field.get(model);
            Label iconLabel = new Label();
            ListView<String> suggestionsList = new ListView<>();
            suggestionsList.setVisible(false);

            iconLabel.setGraphic(SVGUtil.getIcon(icon, FxPopupConfig.iconScale));

            TextField textField;
            if (data != null)
                textField = new TextField(data.toString());
            else
                textField = new TextField();

            StyleUtil.setTransparent(textField);
            textField.setPromptText(annotation.placeholder());

            setAutoUpdateModel(textField, field, model);
            applyValidation(textField, annotation);

            HBox.setHgrow(textField, Priority.ALWAYS);

            HBox container = new HBox(iconLabel, textField, suggestionsList);
            container.getStyleClass().add("field");
            container.setAlignment(Pos.CENTER);

            textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                if (!isNowFocused && annotation.required()) {
                    if (textField.getText().isEmpty() || textField.getText().isBlank())
                        container.getStyleClass().add("required");
                    else
                        container.getStyleClass().remove("required");
                }
            });

            textField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.length() > 2) {
                    suggestionsList.setVisible(true);
                    suggestionsList.getItems().clear();
                    // Obtener sugerencias de OpenStreetMap
                    String[] suggestions = OpenStreetMapService.fetchSuggestions(newValue);
                    if (suggestions != null) {
                        suggestionsList.getItems().addAll(suggestions);
                    }
                } else {
                    suggestionsList.setVisible(false);
                }
            });

            suggestionsList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (newValue != null) {
                        textField.setText(newValue);
                        suggestionsList.setVisible(false);
                    }
                }
            });

            return container;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static Parent createCheckBoxField(Field field ,Object model, FxPopIcon icon){
        field.setAccessible(true);
        MessageField annotation = field.getAnnotation(MessageField.class);
        boolean required = annotation.required();
        try {
            Object data = field.get(model);
            Label iconLabel = new Label();

            iconLabel.setGraphic(SVGUtil.getIcon(icon, FxPopupConfig.iconScale));

            CheckBox checkBox = CustomsFxml.createCustomCheckBox(annotation.placeholder());

            if (data instanceof Boolean)
                checkBox.setSelected((Boolean) data);

            setAutoUpdateModel(checkBox, field, model);

            HBox.setHgrow(checkBox, Priority.ALWAYS);

            HBox container = new HBox(iconLabel, checkBox);
            container.setAlignment(Pos.CENTER_LEFT);

            checkBox.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
                StackPane box = (StackPane) checkBox.lookup(".box");
                if (box != null) {
                    if (!checkBox.isSelected() && required && wasFocused)
                        box.getStyleClass().add(StyleConfig.REQUIRED);
                    else
                        box.getStyleClass().remove(StyleConfig.REQUIRED);
                }
            });

            checkBox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
                StackPane box = (StackPane) checkBox.lookup(".box");
                if (box != null) {
                    if (!isSelected && required)
                        box.getStyleClass().add(StyleConfig.REQUIRED);
                    else
                        box.getStyleClass().remove(StyleConfig.REQUIRED);
                }
            });

            return container;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void setAutoUpdateModel(TextField textField, Field field, Object model) {
        textField.textProperty().addListener((obs, oldVal, newVal) -> {
            try {
                if (field.getType() == int.class || field.getType() == Integer.class)
                    field.set(model, Integer.parseInt(newVal));
                if (field.getType() == String.class)
                    field.set(model, newVal);
                if (field.getType() == long.class || field.getType() == Long.class)
                    field.set(model, Long.parseLong(newVal));
                if (field.getType() == double.class || field.getType() == Double.class)
                    field.set(model, Double.parseDouble(newVal));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static void setAutoUpdateModel(CheckBox checkBox, Field field, Object model) {
        checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            try {
                // Verificar que el campo sea de tipo boolean o Boolean
                if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                    field.set(model, newVal);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static void setAutoUpdateModel(ComboBox<?> comboBox, Field field, Object model) {
        comboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.equals(oldVal)) {  // Asegúrate de que el valor sea distinto
                try {
                    // Actualizar el modelo solo si el valor realmente cambia
                    if (field.getType() == int.class || field.getType() == Integer.class) {
                        field.set(model, Integer.parseInt(newVal.toString()));
                    } else if (field.getType() == String.class) {
                        field.set(model, newVal.toString());
                    } else if (field.getType() == long.class || field.getType() == Long.class) {
                        field.set(model, Long.parseLong(newVal.toString()));
                    } else if (field.getType() == double.class || field.getType() == Double.class) {
                        field.set(model, Double.parseDouble(newVal.toString()));
                    }
                } catch (Exception e) {
                    System.err.println("Error al actualizar el campo: " + e.getMessage());
                }
            }
        });
    }

    public static void setAutoUpdateModel(TextField textField, ComboBox<String> comboBox, Field field, Object model) {
        textField.textProperty().addListener((obs, oldVal, newVal) -> updateField(comboBox, textField, field, model));

        comboBox.valueProperty().addListener((obs, oldVal, newVal) -> updateField(comboBox, textField, field, model));
    }

    private static void updateField(ComboBox<String> comboBox, TextField textField, Field field, Object model) {
        try {
            String combinedValue = comboBox.getValue() + " " + textField.getText(); // Concatenar los valores

            if (field.getType() == String.class) {
                field.set(model, combinedValue);
            } else {
                throw new IllegalArgumentException("Tipo de campo no compatible: " + field.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isValid(String value, FieldType type, String regex, MessageField annotation) {
        if (annotation.required() && value == null) return false;

        return switch (type) {
            case EMAIL -> value.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
            case PHONE -> value.matches("^\\+?[0-9]{7,15}$");
            case NUMBER -> value.matches("^[0-9]+$");
            case CUSTOM -> Pattern.matches(regex, value);
            default -> true;
        };
    }

    private static void applyValidation(TextField textField, MessageField annotation) {
        FieldType type = annotation.type();
        String regex = annotation.regex();

        textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) { // Validar cuando pierde el foco
                String value = textField.getText();
                if (!isValid(value, type, regex, annotation)) {
                    Tooltip tooltip = new Tooltip("Valor inválido para " + annotation.label());
                    Tooltip.install(textField, tooltip);
                }
            }
        });
    }

    private static boolean validateIpParts(String ip) {
        String[] parts = ip.split("\\.");
        for (String part : parts) {
            if (!part.isEmpty() && Integer.parseInt(part) > 255) {
                return false;
            }
        }
        return true;
    }

    private static boolean validateMacSegments(String mac) {
        String[] parts = mac.split(":");
        for (String part : parts) {
            if (!part.isEmpty() && Integer.parseInt(part, 16) > 255) {
                return false;
            }
        }
        return true;
    }
//
//    private static ComboBox<String> createLadaList() {
//        ObservableList<String> countryCodes = FXCollections.observableArrayList("+52", "+54", "+55", "+57", "+58", "+59", "+60", "+61", "+62", "+63", "+64", "+65", "+66", "+67", "+68", "+69", "+70", "+71", "+72", "+73", "+74", "+75", "+76", "+77", "+78", "+79", "+81", "+82", "+84", "+86", "+90", "+91", "+93", "+94", "+95", "+96", "+97", "+98", "+99");
//        ComboBox<String> countryCodeListView = CustomsFxml.createCustomComboBox();
//        countryCodes.addAll(countryCodes);
//        String countryPhone = getCountryPhoneCode(Locale.getDefault().getCountry());
//
//        StyleUtil.setTransparent(countryCodeListView);
//
//        if (countryPhone.isBlank() && countryPhone.isEmpty())
//            countryCodeListView.getSelectionModel().select(countryPhone);
//        else
//            countryCodeListView.getSelectionModel().select("+52");
//
//        countryCodeListView.skinProperty().addListener((obs, oldSkin, newSkin) -> {
//            StackPane arrowButton = (StackPane) countryCodeListView.lookup(".arrow-button");
//
//            if (arrowButton != null) {
//                arrowButton.getChildren().clear();
//                arrowButton.getChildren().add(SVGUtil.getIcon(FxPopIcon.CHEVRON_DOWN, 0.8));
//            }
//        });
//
//        return countryCodeListView;
//    }

    private static double parseDoubleOrDefault(String text, double defaultValue) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
