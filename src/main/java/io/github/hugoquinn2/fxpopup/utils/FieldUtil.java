package io.github.hugoquinn2.fxpopup.utils;

import io.github.hugoquinn2.fxpopup.constants.FieldType;
import io.github.hugoquinn2.fxpopup.constants.FxPopIcon;
import io.github.hugoquinn2.fxpopup.controller.MessageField;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

public class FieldUtil {
    public static Parent createTextField(Field field ,Object model, FxPopIcon icon) {
        field.setAccessible(true);
        try {
            MessageField annotation = field.getAnnotation(MessageField.class);
            Object data = field.get(model);
            Label iconLabel = new Label();

            iconLabel.setGraphic(SVGUtil.getIcon(icon));

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

            iconLabel.setGraphic(SVGUtil.getIcon(icon));

            StyleUtil.setTransparent(eyeButton);
            StyleUtil.setTransparent(textField);
            StyleUtil.setTransparent(passField);

            textField.setVisible(false);
            textField.setManaged(false);

            eyeButton.setGraphic(SVGUtil.getIcon(FxPopIcon.EYE));

            eyeButton.setCursor(Cursor.HAND);
            passField.textProperty().bindBidirectional(textField.textProperty());

            eyeButton.setOnAction(event -> {
                boolean isTextVisible = textField.isVisible();
                textField.setVisible(!isTextVisible);
                textField.setManaged(!isTextVisible);
                passField.setVisible(isTextVisible);
                passField.setManaged(isTextVisible);
                eyeButton.setGraphic(isTextVisible ? SVGUtil.getIcon(FxPopIcon.EYE) : SVGUtil.getIcon(FxPopIcon.EYE_CLOSE));
            });

            setAutoUpdateModel(textField, field, model);
            applyValidation(passField, annotation);

            HBox.setHgrow(passField, Priority.ALWAYS);
            HBox.setHgrow(textField, Priority.ALWAYS);
            HBox container = new HBox(iconLabel, passField, textField, eyeButton);
            container.getStyleClass().add("field");
            container.setAlignment(Pos.CENTER);

            return container;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void setAutoUpdateModel(TextField textField, Field field, Object model) {
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
}
