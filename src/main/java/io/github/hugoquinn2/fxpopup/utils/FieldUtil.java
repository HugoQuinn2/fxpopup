package io.github.hugoquinn2.fxpopup.utils;

import io.github.hugoquinn2.fxpopup.model.Icon;
import io.github.hugoquinn2.fxpopup.config.FieldData;
import io.github.hugoquinn2.fxpopup.config.StyleConfig;
import io.github.hugoquinn2.fxpopup.constants.FxPopIcon;
import io.github.hugoquinn2.fxpopup.controller.MessageField;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.lang.reflect.Field;

/**
 * Utility class to handle creation and styling of various field types.
 */
public class FieldUtil {

    // Field Creation Methods

    /**
     * Creates a text field with an icon and links it to the model.
     * @param field the field to bind the value from
     * @param model the object containing the field
     * @param icon the icon to display beside the text field
     * @return a container with the icon and text field
     */
    public static Parent createTextField(Field field, Object model, FxPopIcon icon) {
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

    /**
     * Creates a text field with an icon and a custom match string for validation.
     * @param field the field to bind the value from
     * @param model the object containing the field
     * @param icon the icon to display beside the text field
     * @param match a custom match string for validation
     * @return a container with the icon and text field
     */
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

    /**
     * Creates a password field with an icon and an eye button to toggle visibility.
     * @param field the field to bind the value from
     * @param model the object containing the field
     * @param icon the icon to display beside the password field
     * @return a container with the icon, password field, and eye button
     */
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

    /**
     * Creates a number field with an icon and buttons to increase or decrease the value.
     * @param field the field to bind the value from
     * @param model the object containing the field
     * @param icon the icon to display beside the number field
     * @return a container with the icon, text field, and buttons
     */
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

    /**
     * Creates a phone number field with an icon and a combo box for international dialing codes.
     * @param field the field to bind the value from
     * @param model the object containing the field
     * @param icon the icon to display beside the field
     * @return a container with the icon, combo box, and text field
     */
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

    /**
     * Creates a country field with an icon and a combo box.
     * @param field the field to bind the value from
     * @param model the object containing the field
     * @param icon the icon to display beside the field
     * @return a container with the icon, and combo box
     */
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

    /**
     * Creates a checkbox field.
     * @param field the field to bind the value from
     * @param model the object containing the field
     * @param icon the icon to display beside the phone field
     * @return a container with checkbox, and header
     */
    public static Parent createCheckBoxField(Field field ,Object model, FxPopIcon icon){
        field.setAccessible(true);
        try {
            // Field data.
            MessageField annotation = field.getAnnotation(MessageField.class);
            Object data = field.get(model);

            // Field structure.
            CheckBox checkBox = CustomsFxml.createCustomCheckBox(annotation.placeholder());
            Icon iconLabel = new Icon(icon);
            HBox container = new HBox(iconLabel, checkBox);

            // Select check box if has data
            if (data instanceof Boolean)
                checkBox.setSelected((Boolean) data);

            // Define style
            HBox.setHgrow(checkBox, Priority.ALWAYS);
            container.setAlignment(Pos.CENTER_LEFT);

            setAutoUpdateModel(checkBox, field, model);
            MasterUtils.setRequired(container, annotation.required(), checkBox);
            return container;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Helper Methods

    /**
     * Updates the model automatically whenever the text field is modified.
     * @param textField the text field to monitor
     * @param field the field to bind the text field to
     * @param model the model to update
     */
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

    /**
     * Updates the model automatically whenever the checkbox is modified.
     * @param checkBox the text field to monitor
     * @param field the field to bind the text field to
     * @param model the model to update
     */
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

    /**
     * Updates the model automatically whenever the text field is modified.
     * @param comboBox the text field to monitor
     * @param field the field to bind the text field to
     * @param model the model to update
     */
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

    /**
     * Updates the model automatically whenever the combo box and text field is modified.
     * @param textField the text field to monitor
     * @param comboBox the combo box to monitor
     * @param field the field to bind the text field to
     * @param model the model to update
     */
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

    /**
     * Parses a string to a double, defaulting to 0.0 if the string is empty or invalid.
     * @param text the string to parse
     * @param defaultValue the default value to return in case of error
     * @return the parsed double value
     */
    private static double parseDoubleOrDefault(String text, double defaultValue) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
