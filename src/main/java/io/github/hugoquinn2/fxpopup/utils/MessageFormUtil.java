package io.github.hugoquinn2.fxpopup.utils;

import io.github.hugoquinn2.fxpopup.control.ToolTip;
import io.github.hugoquinn2.fxpopup.config.FieldData;
import io.github.hugoquinn2.fxpopup.config.StyleConfig;
import io.github.hugoquinn2.fxpopup.controller.FormField;
import io.github.hugoquinn2.fxpopup.controller.Form;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;

import java.lang.reflect.Field;

import static io.github.hugoquinn2.fxpopup.config.CssClasses.*;

public class MessageFormUtil {
    public static void createField(Field field, Object model, Parent parent) {
        FormField annotation = field.getAnnotation(FormField.class);

        // Header
        Label textRequired = new Label("*");
        Label label = new Label(annotation.label());
        TextFlow header = new TextFlow();

        if (!label.getText().isEmpty()) header.getChildren().add(label);
        if (annotation.required()) header.getChildren().add(textRequired);

        // Field
        Parent customField = createFieldByType(field, model);
        Label context = new Label(annotation.context());

        customField.setDisable(annotation.disable());

        // Field style
        textRequired.getStyleClass().add(StyleConfig.TEXT_REQUIRED);
        context.getStyleClass().add(StyleConfig.CONTEXT);
        label.getStyleClass().add(StyleConfig.LABEL);

        VBox fieldContainer = new VBox();
        fieldContainer.getStyleClass().add("form-field-container");

        // Set id to field
        fieldContainer.setId(String.format(FORM_FIELD_CONTAINER_ID, field.getName()));
        customField.setId(String.format(FORM_CUSTOM_FIELD_ID, field.getName()));
        context.setId(String.format(FORM_FIELD_CONTEXT_ID, field.getName()));
        label.setId(String.format(FORM_FIELD_LABEL_ID, field.getName()));
        textRequired.setId(String.format(FORM_FIELD_LABEL_REQUIRED_ID, field.getName()));

        label.setWrapText(true);
        context.setWrapText(true);

        // Tool Tip
        if (!annotation.toolTip().isEmpty()) {
            ToolTip toolTip = new ToolTip(annotation.toolTip(),customField);
        }

        if (!annotation.label().isEmpty())
            fieldContainer.getChildren().add(header);
        if (!customField.getChildrenUnmodifiable().isEmpty())
            fieldContainer.getChildren().add(customField);
        if (!context.getText().isEmpty() || !context.getText().isBlank())
            fieldContainer.getChildren().add(context);

        ((Pane) parent).getChildren().add(fieldContainer);
    }

    private static Parent createFieldByType(Field field, Object model) {
        FormField annotation = field.getAnnotation(FormField.class);

        return switch (annotation.type()) {
            case TEXT -> FieldUtil.createTextField(field, model, annotation.icon());
            case EMAIL -> FieldUtil.createTextField(field, model, annotation.icon(), FieldData.emailMatch);
            case GPS -> FieldUtil.createTextField(field, model, annotation.icon(), FieldData.gpsMatch);
            case HEX -> FieldUtil.createTextField(field, model, annotation.icon(), FieldData.hexMatch);
            case URL -> FieldUtil.createTextField(field, model, annotation.icon(), FieldData.urlMatch);
            case BYTE -> FieldUtil.createTextField(field, model, annotation.icon(), FieldData.byteMatch);
            case IP -> FieldUtil.createTextField(field, model, annotation.icon(), FieldData.ipMatch);
            case MAC -> FieldUtil.createTextField(field, model, annotation.icon(), FieldData.macMatch);

            // Fields with special features
            case PASSWORD -> FieldUtil.createPasswordField(field, model, annotation.icon());
            case NUMBER -> FieldUtil.createNumberField(field, model, annotation.icon());
            case PHONE -> FieldUtil.createPhoneField(field, model, annotation.icon());
            case COUNTRY -> FieldUtil.createCountryField(field, model, annotation.icon());
            case CHECK -> FieldUtil.createCheckBoxField(field, model, annotation.icon());
            case CARD -> FieldUtil.createCardField(field, model, annotation.icon());
        };
    }

    /**
     * Extract all nodes with class REQUIRED and validate if si empty.
     * @param form Parent where nodes with class REQUIRED will be searched.
     * @return True if no Node was found.
     */
    public static boolean isAllRequired(Parent form) {
        return MasterUtils.searchNodesWithClass(form, StyleConfig.REQUIRED).isEmpty();
    }

    public static void isValidObjectForm(Object object) {
        Class<?> clazz = object.getClass();
        Form annotation = clazz.getAnnotation(Form.class);

        if (!clazz.isAnnotationPresent(Form.class))
            throw new NullPointerException(String.format("Object <%s> required annotation @Form", clazz.getName()));

        if (annotation.validator() == null)
            throw new NullPointerException(String.format("Object <%s> required param validator.", clazz.getName()));

        if (annotation.name() == null)
            throw new NullPointerException(String.format("Object <%s> required param name.", clazz.getName()));

    }
}
