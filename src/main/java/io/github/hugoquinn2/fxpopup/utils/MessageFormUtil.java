package io.github.hugoquinn2.fxpopup.utils;

import io.github.hugoquinn2.fxpopup.constants.FieldType;
import io.github.hugoquinn2.fxpopup.controller.MessageForm;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;

public class MessageFormUtil {
    public static boolean isValid(String value, FieldType type, String regex) {
        if (value == null || value.isEmpty()) return false;

        return switch (type) {
            case EMAIL -> value.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
            case PHONE -> value.matches("^\\+?[0-9]{7,15}$");
            case NUMBER -> value.matches("^[0-9]+$");
            case CUSTOM -> Pattern.matches(regex, value);
            default -> true;
        };
    }

    public static void injectFxm(StackPane root, VBox form, Pos pos) {
        StackPane.setAlignment(form, pos);
        root.getChildren().add(form);
    }

    public static VBox generateForm(Object model) {
        VBox form = new VBox(10); // Layout con espaciado entre elementos
        Class<?> clazz = model.getClass();

        Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(MessageForm.class)) // Solo campos anotados
                .forEach(field -> createField(field, model, form));

        Button submitButton = new Button("Enviar");
        submitButton.setOnAction(e -> System.out.println("Modelo actualizado: " + model));
        form.getChildren().add(submitButton);

        return form;
    }

    private static void createField(Field field, Object model, VBox form) {
        field.setAccessible(true);
        MessageForm annotation = field.getAnnotation(MessageForm.class);

        String labelText = annotation.label();
        String placeholder = annotation.placeholder();
        FieldType fieldType = annotation.type();
        String fieldName = field.getName();
        boolean required = annotation.required();

        Label label = new Label(labelText);
        label.setId(String.format("%sLabel", fieldName));

        Node textField = createFieldByType(fieldName, placeholder, fieldType);

        // Validar el contenido del campo
        applyValidation((TextField) textField, annotation);

        // Aplicar que los campos siempre se guarden en el modelo
        autoUpdateModel((TextField) textField, field, model);

        form.getChildren().addAll(label, textField);
    }

    private static Node createFieldByType(String fieldName, String placeholder, FieldType type) {
        Node text = null;

        if (type.equals(FieldType.PASSWORD)) {
            PasswordField passwordField = new PasswordField();
            passwordField.setPromptText(placeholder);

            text = (Node) passwordField;
        } else {
            TextField textField = new TextField();
            textField.setPromptText(placeholder);

            text = (Node) textField;
        }

        text.setId(String.format("%sTextField", fieldName));

        return text;
    }

    private static void autoUpdateModel(TextField textField, Field field, Object model) {
        textField.textProperty().addListener((obs, oldVal, newVal) -> {
            try {
                if (field.getType() == int.class || field.getType() == Integer.class) {
                    field.set(model, Integer.parseInt(newVal));
                } else {
                    field.set(model, newVal);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static void applyValidation(TextField textField, MessageForm annotation) {
        FieldType type = annotation.type();
        String regex = annotation.regex();

        textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) { // Validar cuando pierde el foco
                String value = textField.getText();
                if (!isValid(value, type, regex)) {
                    textField.setStyle("-fx-border-color: red;");
                    Tooltip tooltip = new Tooltip("Valor inválido para " + annotation.label());
                    Tooltip.install(textField, tooltip);
                } else {
                    textField.setStyle(null);
                }
            }
        });
    }
}
