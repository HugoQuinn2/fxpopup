package io.github.hugoquinn2.fxpopup.utils;

import io.github.hugoquinn2.fxpopup.control.ToolTip;
import io.github.hugoquinn2.fxpopup.config.FieldData;
import io.github.hugoquinn2.fxpopup.config.FxPopupConfig;
import io.github.hugoquinn2.fxpopup.config.StyleConfig;
import io.github.hugoquinn2.fxpopup.constants.Theme;
import io.github.hugoquinn2.fxpopup.controller.FormField;
import io.github.hugoquinn2.fxpopup.controller.Form;
import io.github.hugoquinn2.fxpopup.service.ThemeDetector;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;

import java.lang.reflect.Field;
import java.util.Objects;

public class MessageFormUtil {
    /**
     * Retrieves the default style path for the given theme.
     * @param theme The theme (LIGHT or DARK).
     * @return The stylesheet path as a string.
     */
    public static String getDefaultStyle(Theme theme) {
        return Objects.requireNonNull(MessagePopupUtil.class.getResource(Objects.requireNonNull(getStylePath(theme)))).toExternalForm();
    }

    private static String getStylePath(Theme theme) {
        return switch (theme) {
            case SYSTEM -> ThemeDetector.isDarkTheme() ?
                    FxPopupConfig.pathDarkMessageForm : FxPopupConfig.pathLightMessageForm;
            case LIGHT -> FxPopupConfig.pathLightMessageForm;
            case DARK -> FxPopupConfig.pathDarkMessageForm;
        };
    }

    /**
     * Validates the structure of the parent form.
     * @param parent The parent form to validate.
     * @return True if valid, otherwise throws exceptions.
     */
    public static boolean isValidParentForm(Parent parent) {
        if (parent == null)
            throw new NullPointerException("Parent form can't be null");

        if (MasterUtils.findNodeById(parent, FxPopupConfig.fieldsContainerId) == null)
            throw new NullPointerException("Parent form required fields container with id: #fieldsContainer");

        if (MasterUtils.findNodeById(parent, FxPopupConfig.titleFormId) == null)
            throw new NullPointerException("Parent form required label/text with id: #titleForm");

        return true;
    }

    public static void createField(Field field, Object model, Parent parent, Theme theme) {
        FormField annotation = field.getAnnotation(FormField.class);

        // Header
        Label textRequired = new Label("*");
        Label label = new Label(annotation.label());
        TextFlow header = new TextFlow();

        if (!label.getText().isEmpty()) header.getChildren().add(label);
        if (annotation.required()) header.getChildren().add(textRequired);

        label.setId(field.getName() + "Label");
        textRequired.setId(field.getName() + "TextRequired");

        // Field
        Parent fieldContainer = createFieldByType(field, model);
        Label context = new Label(annotation.context());

        fieldContainer.setId(field.getName() + "FieldContainer");
        context.setId(field.getName() + "ContextField");

        fieldContainer.setDisable(annotation.disable());

        // Field style
        textRequired.getStyleClass().add(StyleConfig.TEXT_REQUIRED);
        context.getStyleClass().add(StyleConfig.CONTEXT);
        label.getStyleClass().add(StyleConfig.LABEL);

        VBox fieldBody = new VBox();
        fieldBody.getStyleClass().add("form-field-container");

        label.setWrapText(true);
        context.setWrapText(true);

        // Tool Tip
        if (!annotation.toolTip().isEmpty()) {
            ToolTip toolTip = new ToolTip(annotation.toolTip(),fieldContainer);
            toolTip.setTheme(theme);
        }

        if (!annotation.label().isEmpty())
            fieldBody.getChildren().add(header);
        if (!fieldContainer.getChildrenUnmodifiable().isEmpty())
            fieldBody.getChildren().add(fieldContainer);
        if (!context.getText().isEmpty() || !context.getText().isBlank())
            fieldBody.getChildren().add(context);

        ((Pane) parent).getChildren().add(fieldBody);
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
            throw new NullPointerException(String.format("Object <%s> required annotation @MessageForm", clazz.getName()));

        if (annotation.validator() == null)
            throw new NullPointerException(String.format("Object <%s> required param validator.", clazz.getName()));

        if (annotation.name() == null)
            throw new NullPointerException(String.format("Object <%s> required param name.", clazz.getName()));

    }
}
