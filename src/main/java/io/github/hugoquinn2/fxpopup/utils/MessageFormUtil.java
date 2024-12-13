package io.github.hugoquinn2.fxpopup.utils;

import io.github.hugoquinn2.fxpopup.config.FxPopupConfig;
import io.github.hugoquinn2.fxpopup.constants.FieldType;
import io.github.hugoquinn2.fxpopup.constants.FxPopIcon;
import io.github.hugoquinn2.fxpopup.constants.Theme;
import io.github.hugoquinn2.fxpopup.controller.MessageField;
import io.github.hugoquinn2.fxpopup.service.SVGLoader;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.spi.AbstractResourceBundleProvider;

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

        setSubmit(model, form);

        return form;
    }

    public static void setClose(StackPane root, Parent form) {
        Button close = (Button) form.lookup("#buttonClose");
        close.setOnAction(event -> removeMessageForm(root));

        close.setGraphic(SVGUtil.getIcon(FxPopIcon.CLOSE));
        close.setText("");

        root.setOnMouseClicked(event -> {
            if (!form.getBoundsInParent().contains(event.getX(), event.getY()))
                removeMessageForm(root);
        });
    }

    private static void setSubmit(Object model, Parent form) {
        Button submitButton = (Button) form.lookup("#successButton");
        submitButton.setOnAction(e -> System.out.println("Modelo actualizado: " + model));
    }

    private static void createField(Field field, Object model, VBox form) {
        field.setAccessible(true);
        MessageField annotation = field.getAnnotation(MessageField.class);

        String labelText = annotation.label();
        String fieldName = field.getName();
        boolean required = annotation.required();

        Text textRequired = new Text();
        Text label = new Text(labelText);

        label.setId(String.format("%sLabel", fieldName));
        textRequired.getStyleClass().add("text-required");

        if (required)
            textRequired.setText("*");

        form.getChildren().addAll(new TextFlow(label, textRequired), createFieldByType(field, model));
    }

    private static Parent createFieldByType(Field field, Object model) {
        MessageField annotation = field.getAnnotation(MessageField.class);

        return switch (annotation.type()) {
            case TEXT -> FieldUtil.createTextField(field, model, annotation.icon());
            case PASSWORD -> FieldUtil.createPasswordField(field, model, annotation.icon());
            default -> new HBox();
        };
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
