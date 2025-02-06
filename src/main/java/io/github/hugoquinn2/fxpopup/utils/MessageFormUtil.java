package io.github.hugoquinn2.fxpopup.utils;

import io.github.hugoquinn2.fxpopup.controller.FxPopup;
import io.github.hugoquinn2.fxpopup.model.Icon;
import io.github.hugoquinn2.fxpopup.config.FieldData;
import io.github.hugoquinn2.fxpopup.config.FxPopupConfig;
import io.github.hugoquinn2.fxpopup.config.StyleConfig;
import io.github.hugoquinn2.fxpopup.constants.FxPopIcon;
import io.github.hugoquinn2.fxpopup.constants.Theme;
import io.github.hugoquinn2.fxpopup.controller.FxPopupForm;
import io.github.hugoquinn2.fxpopup.controller.MessageField;
import io.github.hugoquinn2.fxpopup.controller.MessageForm;
import io.github.hugoquinn2.fxpopup.service.ThemeDetector;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class MessageFormUtil {

    // ** Inject and remove forms **

    /**
     * Injects an FXML form into a root container.
     * @param form The form to inject.
     * @param pos The position alignment for the form within the container.
     */
    public static void injectFxml(Parent form, Pos pos) {
        Parent root = MasterUtils.wrapInStackPane(MasterUtils.getRoot());
        Parent formInRoot = (Parent) MasterUtils.findNodeById(root, FxPopupConfig.messageFormId);

        // Overlay
        Rectangle overlay = new Rectangle();
        overlay.setFill(Color.BLACK);
        overlay.setOpacity(0.2);
        overlay.setId(FxPopupConfig.overlayId);

        if (formInRoot == null) {
            if (root instanceof StackPane) {
                overlay.widthProperty().bind(((StackPane) root).widthProperty());
                overlay.heightProperty().bind(((StackPane) root).heightProperty());

                StackPane.setAlignment(form, pos);
                ((StackPane) root).getChildren().remove(form);
                ((StackPane) root).getChildren().addAll(overlay, form);
            }
        } else {
            MasterUtils.findAndDeleteById(FxPopupConfig.messageFormId);
        }
    }

    /**
     * Removes the currently injected message form from the root container.
     */
    public static void removeMessageForm() {
        MasterUtils.findAndDeleteById(FxPopupConfig.messageFormId);
        MasterUtils.findAndDeleteById(FxPopupConfig.overlayId);
    }

    // ** Theming and styles **

    /**
     * Injects a theme stylesheet into a form.
     * @param form The form to style.
     * @param theme The theme to apply.
     */
    public static void injectTheme(Parent form, Theme theme) {
        form.getStylesheets().add(getDefaultStyle(theme));
    }

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

    // ** Form generation **

    /**
     * Dynamically generates fields in the form based on the model's annotations.
     * @param parent The parent form.
     * @param model The model containing the annotated fields.
     */
    public static void generateFieldsForm(Parent parent, Object model, Theme theme) {
        if (isValidParentForm(parent)) {
            Parent fieldsContainer = (VBox) MasterUtils.findNodeById(parent, FxPopupConfig.fieldsContainerId);
            Class<?> clazz = model.getClass();

            Arrays.stream(clazz.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(MessageField.class))
                    .forEach(field -> createField(field, model, fieldsContainer, theme));
        }
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
        MessageField annotation = field.getAnnotation(MessageField.class);

        // Header
        Text textRequired = new Text(annotation.required() && !annotation.label().isEmpty() ? "*" : null);
        Text label = new Text(!annotation.label().isEmpty() ? annotation.label() : null);
        TextFlow header = new TextFlow(
                label.getText() != null ?
                        label : null,
                textRequired.getText() != null ?
                        textRequired : null
        );

        label.setId(field.getName() + "Label");
        textRequired.setId(field.getName() + "TextRequired");

        // Field
        Parent fieldContainer = createFieldByType(field, model);
        Text context = new Text(annotation.context());

        fieldContainer.setId(field.getName() + "FieldContainer");
        context.setId(field.getName() + "ContextField");

        fieldContainer.setDisable(annotation.disable());

        // Field style
        textRequired.getStyleClass().add(StyleConfig.TEXT_REQUIRED);
        context.getStyleClass().add(StyleConfig.CONTEXT);
        label.getStyleClass().add(StyleConfig.LABEL);

        // Tool Tip
        if (!annotation.toolTip().isEmpty()) {
            FxPopup fxPopup = new FxPopup();
            fxPopup.setTheme(theme);
            fxPopup.toolTip(fieldContainer, annotation.toolTip());
        }

        if (!annotation.label().isEmpty())
            ((Pane) parent).getChildren().add(header);
        if (!fieldContainer.getChildrenUnmodifiable().isEmpty())
            ((Pane) parent).getChildren().add(fieldContainer);
        if (!context.getText().isEmpty() || !context.getText().isBlank())
            ((Pane) parent).getChildren().add(context);
    }

    private static Parent createFieldByType(Field field, Object model) {
        MessageField annotation = field.getAnnotation(MessageField.class);

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

    // ** Button and action handling **

    /**
     * Sets a close action for the form's close button.
     * @param form The form containing the close button.
     */
    public static void setClose(Parent form) {
        Button close = (Button) MasterUtils.findNodeById(form, "buttonClose");

        if (close == null)
            throw new NullPointerException(String.format("Parent form <%s> required button close: #buttonClose", form.getId()));

        close.setOnAction(event -> removeMessageForm());

        close.setGraphic(new Icon(FxPopIcon.CLOSE, 0.8));
        close.setText("");
    }

    /**
     * Sets a submit action for the form's success button.
     * @param model The model to validate.
     * @param form The form containing the success button.
     */
    public static void setSubmit(Object model, Parent form) {
        try {
            Class<?> formClass = model.getClass();

            MessageForm messageForm = formClass.getAnnotation(MessageForm.class);
            Class<? extends FxPopupForm<?>> validatorClass = messageForm.validator();

            FxPopupForm<Object> validador = (FxPopupForm<Object>) validatorClass.getDeclaredConstructor().newInstance();

            Button submitButton = (Button) form.lookup("#successButton");
            submitButton.setOnAction(e -> {
                submitButton.setDisable(true);
                MasterUtils.requestFocusOnAllFields(form);
                try {
                    if (isAllRequired(form) && validador.validate(model)) {
                        validador.isValidForm(model);
                        removeMessageForm();
                    } else {
                        submitButton.setDisable(false);
                    }
                } catch (Exception ex) {
                    MasterUtils.findAndEditText(form, "messageError", ex.getMessage());
                    submitButton.setDisable(false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ** Utility **

    /**
     * Loads the default content for the message form.
     * @return The default VBox content or null if an error occurs.
     */
    public static VBox getDefaultContent() {
        try {
            return new FXMLLoader(MessageFormUtil.class.getResource(FxPopupConfig.pathMessageForm)).load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Extract all nodes with class REQUIRED and validate if si empty.
     * @param form Parent where nodes with class REQUIRED will be searched.
     * @return True if no Node was found.
     */
    private static boolean isAllRequired(Parent form) {
        return MasterUtils.searchNodesWithClass(form, StyleConfig.REQUIRED).isEmpty();
    }

    public static boolean isValidObjectForm(Object object) {
        Class<?> clazz = object.getClass();
        MessageForm annotation = clazz.getAnnotation(MessageForm.class);

        if (!clazz.isAnnotationPresent(MessageForm.class))
            throw new NullPointerException(String.format("Object <%s> required annotation @MessageForm", clazz.getName()));

        if (annotation.validator() == null)
            throw new NullPointerException(String.format("Object <%s> required param validator.", clazz.getName()));

        if (annotation.name() == null)
            throw new NullPointerException(String.format("Object <%s> required param name.", clazz.getName()));

        return true;
    }
}
