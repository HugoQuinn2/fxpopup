package io.github.hugoquinn2.fxpopup.utils;

import io.github.hugoquinn2.fxpopup.config.FxPopupConfig;
import io.github.hugoquinn2.fxpopup.constants.FieldType;
import io.github.hugoquinn2.fxpopup.constants.Theme;
import io.github.hugoquinn2.fxpopup.controller.MessageField;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;

public class MessageFormUtil {

    public static void injectFxml(StackPane root, Parent form, Pos pos) {
        StackPane.setAlignment(form, pos);
        root.getChildren().remove(form);
        root.getChildren().add(form);
    }

    public static void removeMessageForm(StackPane root) {
        Node messageForm = root.lookup(String.format("#%s", FxPopupConfig.messageFormId));

        if (messageForm != null)
            root.getChildren().remove(messageForm);
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

    private static void injectTheme(VBox form, Theme theme) {
        form.getStylesheets().add(getDefaultStyle(theme));
    }

    public static Parent generateForm(Object model, Theme theme) {
        VBox form = getDefaultContent();

        if (form == null)
            return null;

        VBox fieldsContainer = (VBox) form.lookup("#fieldsContainer");

        Class<?> clazz = model.getClass();

        injectTheme(form, theme);

        Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(MessageField.class))
                .forEach(field -> createField(field, model, fieldsContainer));

        form.getChildren().add(getSubmit(model));

        return form;
    }

    public static void setClose(StackPane root, Parent form) {
        Button close = (Button) form.lookup("#buttonClose");
        close.setOnAction(event -> removeMessageForm(root));

        root.setOnMouseClicked(event -> {
            if (!form.getBoundsInParent().contains(event.getX(), event.getY()))
                removeMessageForm(root);
        });
    }

    private static Button getSubmit(Object model) {
        Button submitButton = new Button("Enviar");
        submitButton.setId("submitButton");
        submitButton.setOnAction(e -> System.out.println("Modelo actualizado: " + model));

        return submitButton;
    }

    private static void createField(Field field, Object model, VBox form) {
        field.setAccessible(true);
        MessageField annotation = field.getAnnotation(MessageField.class);

        String labelText = annotation.label();
        String placeholder = annotation.placeholder();
        FieldType fieldType = annotation.type();
        String fieldName = field.getName();
        boolean required = annotation.required();

        if (required)
            labelText = labelText + "*";

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

    private static void applyValidation(TextField textField, MessageField annotation) {
        FieldType type = annotation.type();
        String regex = annotation.regex();

        textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) { // Validar cuando pierde el foco
                String value = textField.getText();
                if (!isValid(value, type, regex, annotation)) {
                    textField.setStyle("-fx-border-color: red;");
                    Tooltip tooltip = new Tooltip("Valor inválido para " + annotation.label());
                    Tooltip.install(textField, tooltip);
                } else {
                    textField.setStyle(null);
                }
            }
        });
    }

    public static VBox getDefaultContent() {
        try {
            return new FXMLLoader(FxPopupUtil.class.getResource(FxPopupConfig.pathMessageForm)).load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String getStylePath(Theme theme) {
        return switch (theme){
            case LIGHT -> FxPopupConfig.pathLightMessageForm;
            case DARK -> FxPopupConfig.pathDarkMessageForm;
        };
    }

    public static String getDefaultStyle(Theme theme) {
        return Objects.requireNonNull(FxPopupUtil.class.getResource(Objects.requireNonNull(getStylePath(theme)))).toExternalForm();
    }
}
