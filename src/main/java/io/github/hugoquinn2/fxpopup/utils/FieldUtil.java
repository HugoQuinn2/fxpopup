package io.github.hugoquinn2.fxpopup.utils;

import io.github.hugoquinn2.fxpopup.model.Icon;
import io.github.hugoquinn2.fxpopup.config.FieldData;
import io.github.hugoquinn2.fxpopup.config.FxPopupConfig;
import io.github.hugoquinn2.fxpopup.config.StyleConfig;
import io.github.hugoquinn2.fxpopup.constants.FxPopIcon;
import io.github.hugoquinn2.fxpopup.controller.MessageField;
import io.github.hugoquinn2.fxpopup.service.OpenStreetMapService;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.UnaryOperator;

public class FieldUtil {
    public static Parent createTextField(Field field ,Object model, FxPopIcon icon) {
        field.setAccessible(true);
        try {
            // Field information
            MessageField annotation = field.getAnnotation(MessageField.class);
            Object data = field.get(model);

            // Field structure
            Icon iconLabel = new Icon(icon);
            TextField textField = new TextField(data != null ? data.toString() : null);
            HBox container = new HBox(iconLabel, textField);

            // Define style
            StyleUtil.setTransparent(textField);
            container.getStyleClass().add(StyleConfig.FIELD);
            textField.setPromptText(annotation.placeholder());

            HBox.setHgrow(textField, Priority.ALWAYS);
            container.setAlignment(Pos.CENTER);

            setAutoUpdateModel(textField, field, model);
            MasterUtils.setRequired(container, annotation.required(), textField);
            return container;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Parent createTextField(Field field ,Object model, FxPopIcon icon, String match) {
        field.setAccessible(true);
        try {
            // Field information
            MessageField annotation = field.getAnnotation(MessageField.class);
            Object data = field.get(model);

            // Field structure
            Icon iconLabel = new Icon(icon);
            TextField textField = new TextField(data != null ? data.toString() : null);
            HBox container = new HBox(iconLabel, textField);

            // Define style
            StyleUtil.setTransparent(textField);
            container.getStyleClass().add(StyleConfig.FIELD);
            textField.setPromptText(annotation.placeholder());

            HBox.setHgrow(textField, Priority.ALWAYS);
            container.setAlignment(Pos.CENTER);

            setAutoUpdateModel(textField, field, model);
            MasterUtils.setRequired(container, annotation.required(), match, textField);
            return container;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static Parent createPasswordField(Field field, Object model, FxPopIcon icon) {
        field.setAccessible(true);
        try {
            // Field Information
            MessageField annotation = field.getAnnotation(MessageField.class);
            Object data = field.get(model);

            // Field Structure
            Icon iconLabel = new Icon(icon);
            Button eyeButton = new Button();
            PasswordField passField = new PasswordField();
            TextField textField = new TextField(data != null ? data.toString() : null);
            HBox container = new HBox(iconLabel, passField, textField, eyeButton);

            // Define Style
            StyleUtil.setTransparent(eyeButton, textField, passField);
            container.getStyleClass().add(StyleConfig.FIELD);

            passField.setPromptText(annotation.placeholder());
            textField.setPromptText(annotation.placeholder());

            HBox.setHgrow(passField, Priority.ALWAYS);
            HBox.setHgrow(textField, Priority.ALWAYS);

            container.setAlignment(Pos.CENTER);

            // Special features to Password Field
            textField.setVisible(false);
            textField.setManaged(false);

            eyeButton.setGraphic(new Icon(FxPopIcon.EYE));
            eyeButton.setCursor(Cursor.HAND);

            passField.textProperty().bindBidirectional(textField.textProperty());
            eyeButton.setOnAction(event -> {
                boolean isTextVisible = textField.isVisible();
                textField.setVisible(!isTextVisible);
                textField.setManaged(!isTextVisible);
                passField.setVisible(isTextVisible);
                passField.setManaged(isTextVisible);
                eyeButton.setGraphic(isTextVisible ? new Icon(FxPopIcon.EYE) : new Icon(FxPopIcon.EYE_CLOSE));
            });

            setAutoUpdateModel(textField, field, model);
            MasterUtils.setRequired(container, annotation.required(), passField, textField);
            return container;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static Parent createIPField(Field field, Object model, FxPopIcon icon){
        field.setAccessible(true);
        try {
            // Field Information
            MessageField annotation = field.getAnnotation(MessageField.class);
            Object data = field.get(model);

            // Field structure
            Icon iconLabel = new Icon(icon);
            TextField textField = new TextField(data != null ? data.toString() : null);
            HBox container = new HBox(iconLabel, textField);

            // Define style
            StyleUtil.setTransparent(textField);
            container.getStyleClass().add(StyleConfig.FIELD);
            textField.setPromptText(annotation.placeholder());

            HBox.setHgrow(textField, Priority.ALWAYS);
            container.setAlignment(Pos.CENTER);

            // Special feature to ip field.
            TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
                String newText = change.getControlNewText();
                if (newText.matches("\\d{0,3}(\\.\\d{0,3}){0,3}") && validateIpParts(newText)) {
                    return change;
                } else {
                    return null;
                }
            });

            textField.setTextFormatter(textFormatter);

            setAutoUpdateModel(textField, field, model);
            MasterUtils.setRequired(container, annotation.required(), FieldData.ipMatch, textField);
            return container;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static Parent createMACField(Field field, Object model, FxPopIcon icon){
        field.setAccessible(true);
        try {
            // Field Information
            MessageField annotation = field.getAnnotation(MessageField.class);
            Object data = field.get(model);

            // Field structure
            Icon iconLabel = new Icon(icon);
            TextField textField = new TextField(data != null ? data.toString() : null);
            HBox container = new HBox(iconLabel, textField);

            // Define style
            StyleUtil.setTransparent(textField);
            container.getStyleClass().add(StyleConfig.FIELD);
            textField.setPromptText(annotation.placeholder());

            HBox.setHgrow(textField, Priority.ALWAYS);
            container.setAlignment(Pos.CENTER);


            MasterUtils.setRequired(container, annotation.required(), FieldData.macMatch, textField);
            return container;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static Parent createNumberField(Field field, Object model, FxPopIcon icon){
        field.setAccessible(true);
        try {
            // Field information.
            MessageField annotation = field.getAnnotation(MessageField.class);
            Object data = field.get(model);

            // Field structure.
            Icon iconLabel = new Icon(icon);
            Button less = new Button();
            Button plus = new Button();
            TextField textField = new TextField(
                    data != null ?
                            data.toString() :
                            null
            );
            HBox container = new HBox(iconLabel, textField, less, plus);

            // Field style
            container.getStyleClass().add(StyleConfig.FIELD);
            StyleUtil.setTransparent(textField);
            StyleUtil.setTransparent(less);
            StyleUtil.setTransparent(plus);
            textField.setPromptText(annotation.placeholder());

            less.setCursor(Cursor.HAND);
            plus.setCursor(Cursor.HAND);

            less.setGraphic(new Icon(FxPopIcon.LEFT));
            plus.setGraphic(new Icon(FxPopIcon.RIGHT));

            HBox.setHgrow(textField, Priority.ALWAYS);
            container.setAlignment(Pos.CENTER);

            // Special features to number field.
            less.setOnAction(event -> {
                double value = parseDoubleOrDefault(textField.getText(), 0);
                textField.setText(String.valueOf(value - 1.0));
            });

            plus.setOnAction(event -> {
                double value = parseDoubleOrDefault(textField.getText(), 0);
                textField.setText(String.valueOf(value + 1.0));
            });

            setAutoUpdateModel(textField, field, model);
            MasterUtils.setRequired(container, annotation.required(), FieldData.numberMatch, textField);
            return container;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static Parent createPhoneField(Field field ,Object model, FxPopIcon icon) {
        field.setAccessible(true);
        try {
            // Field Information
            MessageField annotation = field.getAnnotation(MessageField.class);
            Object data = field.get(model);
            String lada = null;
            String phone = null;

            if (data != null) {
                String input = data.toString();
                lada = input.startsWith("+") ? input.substring(0, 3) : null;
                phone = lada != null ?
                        input.replace(lada, "").replaceFirst("^\\s+", "") :
                        input.replaceFirst("^\\s+", "");
            }

            // Field structure
            Icon iconLabel = new Icon(icon);
            TextField textField = new TextField(phone);
            ComboBox<String> ladaList = CustomsFxml.createCustomComboBox(
                    annotation.data().length == 0 ?
                            FieldData.ladaList :
                            FXCollections.observableArrayList(annotation.data())
            );
            HBox container = new HBox(iconLabel, ladaList, textField);

            // Select lada if data has a lada
            if (lada == null)
                ladaList.getSelectionModel().select(0);
            else
                ladaList.getSelectionModel().select(lada);

            // Define style
            StyleUtil.setTransparent(textField);
            container.getStyleClass().add(StyleConfig.FIELD);
            textField.setPromptText(annotation.placeholder());

            HBox.setHgrow(textField, Priority.ALWAYS);
            container.setAlignment(Pos.CENTER);

            setAutoUpdateModel(textField, ladaList, field, model);
            MasterUtils.setRequired(container, annotation.required(), FieldData.phoneMatch, textField);
            return container;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static Parent createCountryField(Field field, Object model, FxPopIcon icon) {
        field.setAccessible(true);
        try {
            // Field data.
            MessageField annotation = field.getAnnotation(MessageField.class);
            Object data = field.get(model);

            // Field structure
            Icon iconLabel = new Icon(icon);
            ComboBox<String> countryBox = CustomsFxml.createCustomComboBox(
                    annotation.data().length == 0 ?
                            FieldData.countryList :
                            FXCollections.observableArrayList(annotation.data())
            );
            HBox container = new HBox(iconLabel, countryBox);

            // Select country if field has data
            if (data != null)
                countryBox.getSelectionModel().select(data.toString());

            // Define style
            countryBox.setPromptText(annotation.placeholder());
            countryBox.setCursor(Cursor.HAND);

            HBox.setHgrow(countryBox, Priority.ALWAYS);
            countryBox.setMaxWidth(Double.MAX_VALUE);

            container.getStyleClass().add(StyleConfig.FIELD);
            container.setAlignment(Pos.CENTER);

            setAutoUpdateModel(countryBox, field, model);
            MasterUtils.setRequired(container, annotation.required(), countryBox);
            return container;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static Parent createAddressField(Field field ,Object model, FxPopIcon icon) {
        field.setAccessible(true);
        PauseTransition pauseTransition = new PauseTransition(Duration.millis(500));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            // Field data.
            MessageField annotation = field.getAnnotation(MessageField.class);
            Object data = field.get(model);

            // Field structure
            ObservableList<String> filteredItems = FXCollections.observableArrayList();
            Icon iconLabel = new Icon(icon);
            ComboBox<String> addressBox = CustomsFxml.createCustomComboBox(filteredItems);
            HBox container = new HBox(iconLabel, addressBox);
            TextField addressField = addressBox.getEditor();
            AtomicBoolean isAddressBoxChange = new AtomicBoolean(false);

            if (data != null)
                addressField.setText(data.toString());

            // Style field
            StyleUtil.setTransparent(addressBox);
            StyleUtil.setTransparent(addressField);
            addressBox.setMaxWidth(Double.MAX_VALUE);

            addressBox.setEditable(true);
            HBox.setHgrow(addressBox, Priority.ALWAYS);
            addressBox.setPromptText(annotation.placeholder());

            container.getStyleClass().add(StyleConfig.FIELD);
            container.setAlignment(Pos.CENTER);

            // Special feature to address field.
            pauseTransition.setOnFinished(event -> {
                String query = addressField.getText();
                if (query != null) {
                    List<String> results = OpenStreetMapService.searchAddress(query, 5);

                    if (results.isEmpty())
                        results.add(query);

                    filteredItems.setAll(results);

                    if (filteredItems.isEmpty()) {
                        addressBox.hide();
                    } else {
                        addressBox.show();
                    }
                }
                else
                    filteredItems.clear();

            });

            addressField.textProperty().addListener((obs, oldValue, newValue) -> {
                if (addressBox.isShowing())
                    addressBox.hide();

                if (!Objects.equals(newValue, oldValue) && !isAddressBoxChange.get())
                    pauseTransition.play();
            });

            addressBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    isAddressBoxChange.set(true);
                    addressField.setText(newValue);
                    isAddressBoxChange.set(false);
                }
            });

            addressField.setOnKeyReleased(event -> {
                String text = addressBox.getEditor().getText();
                if (!text.isEmpty() && !filteredItems.contains(text)) {
                    addressBox.hide();
                }
            });

            setAutoUpdateModel(addressBox, field, model);
            MasterUtils.setRequired(container, annotation.required(), addressBox);
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

    private static TextFormatter<String> createMACAddressFormatter() {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getControlNewText().toUpperCase();

            if (change.isDeleted()) {
                return change;
            }

            if (text.length() > 17) {
                return null;
            }

            if (!text.matches("[0-9A-F:]*")) {
                return null;
            }

            StringBuilder formattedText = new StringBuilder();
            int len = Math.min(text.length(), 12);
            for (int i = 0; i < len; i++) {
                formattedText.append(text.charAt(i));
                if (i % 2 == 1 && i < 11) {
                    formattedText.append(':');
                }
            }

            if (text.length() > 12) {
                formattedText.append(text.substring(12));
            }

            change.setText(formattedText.toString());
            change.setRange(0, change.getControlText().length());
            return change;
        };

        return new TextFormatter<>(filter);
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

    private static double parseDoubleOrDefault(String text, double defaultValue) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
