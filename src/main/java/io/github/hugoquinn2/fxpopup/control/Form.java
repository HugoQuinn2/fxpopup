package io.github.hugoquinn2.fxpopup.control;

import io.github.hugoquinn2.fxpopup.constants.FxPopIcon;
import io.github.hugoquinn2.fxpopup.constants.Theme;
import io.github.hugoquinn2.fxpopup.controller.*;
import io.github.hugoquinn2.fxpopup.model.Icon;
import io.github.hugoquinn2.fxpopup.service.ThemeDetector;
import io.github.hugoquinn2.fxpopup.utils.MasterUtils;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.util.Arrays;
import java.util.Objects;

import static io.github.hugoquinn2.fxpopup.config.CssClasses.*;
import static io.github.hugoquinn2.fxpopup.utils.MessageFormUtil.*;


public class Form extends VBox {
    // Form structure
    private Pane headerContainer;
    private Pane bodyContainer;
    private Pane fieldsContainer;
    private Pane footerContainer;

    private Label titleLabel;
    private Label errorLabel;
    private Button closeButton;
    private Button sendButton;

    // Form Params
    private Object referenceObject;
    private boolean isClosable;
    private Theme theme;

    private final MessageForm messageForm;
    private final Class<?> referenceObjectClazz;
    private String nameForm;

    // Constructors
    /**
     * Create a form with a reference model, this most <code>@MessageForm</code>. By default, the form could be closed
     * <code>False</code>
     * @param referenceObject the reference object.
     */
    public Form(Object referenceObject){
        this(referenceObject, false);
    }

    /**
     * Create a form with a reference model, this most <code>@MessageForm</code> with custom closable status.
     * @param referenceObject the reference object.
     * @param isClosable the closable state.
     */
    public Form(Object referenceObject, boolean isClosable) {
        isValidObjectForm(referenceObject);

        this.isClosable = isClosable;
        this.referenceObject = referenceObject;

        this.closeButton =  new Button();
        closeButton.setGraphic(new Icon(FxPopIcon.CLOSE, 0.7));

        this.sendButton = new Button("Send");
        this.fieldsContainer =  new VBox();
        this.errorLabel = new Label();
        this.footerContainer = new HBox();

        referenceObjectClazz = referenceObject.getClass();
        messageForm = referenceObjectClazz.getAnnotation(MessageForm.class);
        nameForm = messageForm.name();

        this.titleLabel = new Label(nameForm);

        // Make responsive
        setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        setPrefSize(450, USE_COMPUTED_SIZE);
        setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);

        errorLabel.setWrapText(true);

        // Form structure
        Region region =  new Region();
        HBox.setHgrow(region, Priority.ALWAYS);

        headerContainer = new HBox(
                titleLabel, region, closeButton
        );
        ((HBox) headerContainer).setAlignment(Pos.CENTER_LEFT);

        bodyContainer = new VBox(
                fieldsContainer, errorLabel, sendButton
        );

        footerContainer = new HBox();

        getChildren().addAll(
                headerContainer, bodyContainer, footerContainer
        );

        // Define style classes
        getStyleClass().add(FORM_CLASS);
        headerContainer.getStyleClass().add(FORM_HEADER_CONTAINER_CLASS);
        bodyContainer.getStyleClass().add(FORM_BODY_CONTAINER_CLASS);
        footerContainer.getStyleClass().add(FORM_FOOTER_CONTAINER_CLASS);
        titleLabel.getStyleClass().add(FORM_TITLE_LABEL_CLASS);
        fieldsContainer.getStyleClass().add(FORM_FIELD_CONTAINER_CLASS);
        errorLabel.getStyleClass().add(FORM_ERROR_LABEL_CLASS);
        closeButton.getStyleClass().add(FORM_CLOSE_BUTTON_CLASS);
        sendButton.getStyleClass().add(FORM_SEND_BUTTON_CLASS);

        // Generate Fields
        generateFields();

        // Set actions Form
        setSendActions();
        setCloseActions();
    }

    public void generateFields() {
        Arrays.stream(referenceObjectClazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(MessageField.class))
                .forEach(field -> createField(field, referenceObject, fieldsContainer, theme));
    }

    private void setSendActions() {
        try {
            Class<? extends FxPopupForm<?>> validatorClass = messageForm.validator();

            FxPopupForm<Object> validator = (FxPopupForm<Object>) validatorClass.getDeclaredConstructor().newInstance();

            sendButton.setOnAction(e -> {
                sendButton.setDisable(true);
                MasterUtils.requestFocusOnAllFields(this);
                try {
                    if (isAllRequired(this) && validator.validate(referenceObject)) {
                        validator.isValidForm(referenceObject);
                        errorLabel.setText("");
                    } else {
                        sendButton.setDisable(false);
                    }
                } catch (Exception ex) {
                    errorLabel.setText(ex.getMessage());
                    sendButton.setDisable(false);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(String.format("Error setting actions to Send Button: %s", e.getMessage()));
        }
    }
    private void setCloseActions() {
        if (isClosable)
            this.getChildren().remove(closeButton);
        else
            closeButton.setOnAction(event -> {
                Parent parent = getParent();
                if (parent != null)
                    ((Pane) parent).getChildren().remove(this);
            });
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

    public Button getSendButton() {
        return sendButton;
    }

    public void setSendButton(Button sendButton) {
        this.sendButton = sendButton;
    }

    public Button getCloseButton() {
        return closeButton;
    }

    public void setCloseButton(Button closeButton) {
        this.closeButton = closeButton;
    }

    public Label getErrorLabel() {
        return errorLabel;
    }

    public void setErrorLabel(Label errorLabel) {
        this.errorLabel = errorLabel;
    }

    public Parent getFieldsContainer() {
        return fieldsContainer;
    }

    public void setFieldsContainer(VBox fieldsContainer) {
        this.fieldsContainer = fieldsContainer;
    }

    public Label getTitleLabel() {
        return titleLabel;
    }

    public void setTitleLabel(Label titleLabel) {
        this.titleLabel = titleLabel;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public Pane getHeaderContainer() {
        return headerContainer;
    }

    public void setHeaderContainer(Pane headerContainer) {
        this.headerContainer = headerContainer;
    }

    public Pane getBodyContainer() {
        return bodyContainer;
    }

    public void setBodyContainer(Pane bodyContainer) {
        this.bodyContainer = bodyContainer;
    }

    public Pane getFooterContainer() {
        return footerContainer;
    }

    public void setFooterContainer(Pane footerContainer) {
        this.footerContainer = footerContainer;
    }
}
