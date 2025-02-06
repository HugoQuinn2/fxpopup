package io.github.hugoquinn2.fxpopup.control;

import io.github.hugoquinn2.fxpopup.constants.FxPopIcon;
import io.github.hugoquinn2.fxpopup.constants.Theme;
import io.github.hugoquinn2.fxpopup.controller.MessageField;
import io.github.hugoquinn2.fxpopup.controller.MessageForm;
import io.github.hugoquinn2.fxpopup.model.Icon;
import io.github.hugoquinn2.fxpopup.utils.MessageFormUtil;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.Arrays;

import static io.github.hugoquinn2.fxpopup.utils.MessageFormUtil.createField;
import static io.github.hugoquinn2.fxpopup.utils.MessageFormUtil.isValidObjectForm;

public class Form extends VBox {
    // Form structure
    private HBox header;
    private HBox footer;
    private Label title;
    private VBox fieldsContainer;
    private Label error;
    private Button close;
    private Button send;

    // Form Params
    private Object referenceObject;
    private boolean isClosable;
    private Theme theme = Theme.SYSTEM;

    private MessageForm messageForm;
    private Class<?> referenceObjectClazz;
    private String nameForm;

    // Style class for CSS
    private String FORM_CLASS = "form";
    private String HEADER_CLASS = "form-header";
    private String FOOTER_CLASS = "form-footer";
    private String TITLE_CLASS = "form-title";
    private String FIELD_CONTAINER_CLASS = "form-fields-container";
    private String CLOSE_BUTTON_CLASS = "form-close-button";
    private String SEND_BUTTON_CLASS = "form-send-button";
    private String ERROR_LABEL_CLASS = "form-error";

    // Constructors
    public Form(Object referenceObject){this(referenceObject, false);}

    public Form(Object referenceObject, boolean isClosable) {
        isValidObjectForm(referenceObject);

        this.isClosable = isClosable;
        this.referenceObject = referenceObject;

        this.close =  new Button();
        close.setGraphic(new Icon(FxPopIcon.CLOSE, 0.5));

        this.send = new Button("Send");
        this.fieldsContainer =  new VBox();
        this.error = new Label();
        this.footer = new HBox();

        referenceObjectClazz = referenceObject.getClass();
        messageForm = referenceObjectClazz.getAnnotation(MessageForm.class);
        nameForm = messageForm.name();

        this.title = new Label(nameForm);

        // Form structure
        Region region =  new Region();
        HBox.setHgrow(region, Priority.ALWAYS);

        header = new HBox(
                title, region, close
        );

        footer = new HBox(
                region, send
        );

        getChildren().addAll(
                header, fieldsContainer, error, footer
        );

        // Define style classes
        getStyleClass().add(FORM_CLASS);
        header.getStyleClass().add(HEADER_CLASS);
        footer.getStyleClass().add(FOOTER_CLASS);
        title.getStyleClass().add(TITLE_CLASS);
        fieldsContainer.getStyleClass().add(FIELD_CONTAINER_CLASS);
        error.getStyleClass().add(ERROR_LABEL_CLASS);
        close.getStyleClass().add(CLOSE_BUTTON_CLASS);
        send.getStyleClass().add(SEND_BUTTON_CLASS);

        // Inject style by theme
        MessageFormUtil.injectTheme(this, theme);

        // Generate Fields
        generateFields();
    }

    public void generateFields() {
        Arrays.stream(referenceObjectClazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(MessageField.class))
                .forEach(field -> createField(field, referenceObject, fieldsContainer, theme));
    }

    public boolean isClosable() {
        return isClosable;
    }

    public void setClosable(boolean closable) {
        isClosable = closable;
    }

    public Object getReferenceObject() {
        return referenceObject;
    }

    public void setReferenceObject(Object referenceObject) {
        this.referenceObject = referenceObject;
    }

    public Button getSend() {
        return send;
    }

    public void setSend(Button send) {
        this.send = send;
    }

    public Button getClose() {
        return close;
    }

    public void setClose(Button close) {
        this.close = close;
    }

    public Label getError() {
        return error;
    }

    public void setError(Label error) {
        this.error = error;
    }

    public VBox getFieldsContainer() {
        return fieldsContainer;
    }

    public void setFieldsContainer(VBox fieldsContainer) {
        this.fieldsContainer = fieldsContainer;
    }

    public Label getTitle() {
        return title;
    }

    public void setTitle(Label title) {
        this.title = title;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
        MessageFormUtil.injectTheme(this, this.theme);
    }
}
