package io.github.hugoquinn2.fxpopup.controller;

import io.github.hugoquinn2.fxpopup.config.FxPopupConfig;
import io.github.hugoquinn2.fxpopup.constants.Theme;
import io.github.hugoquinn2.fxpopup.model.Message;
import io.github.hugoquinn2.fxpopup.utils.MasterUtils;
import io.github.hugoquinn2.fxpopup.utils.MessageFormUtil;
import io.github.hugoquinn2.fxpopup.utils.MessagePopupUtil;
import javafx.geometry.Pos;
import javafx.scene.Parent;

/**
 * Controller for managing FxPopup messages and forms.
 */
public class FxPopup implements FxPopupInterface {
    private Pos pos;
    private Theme theme;

    /**
     * Default constructor initializing default position and theme.
     */
    public FxPopup() {
        pos = FxPopupConfig.defaultPos;
        theme = FxPopupConfig.defaultTheme;
    }

    /**
     * Adds a message to the popup system, use default Pos (bottom left).
     *
     * @param message the message to be added. Must contain valid properties.
     */
    @Override
    public void add(Message message) {
        add(message, pos);
    }

    /**
     * Adds a message to the popup system.
     *
     * @param message the message to be added. Must contain valid properties.
     * @param posMessage the position where the popup will be visible.
     */
    @Override
    public void add(Message message, Pos posMessage) {
        if (message.getContext() == null)
            MasterUtils.findAndDeleteById(message.getParent(), FxPopupConfig.contextId);
        else
            MasterUtils.findAndEditText(message.getParent(), FxPopupConfig.contextId, message.getContext());

        MasterUtils.findAndEditText(message.getParent(), FxPopupConfig.titleId, message.getTitle());

        MessagePopupUtil.injectTheme(message, theme, message.getMessageType());
        MessagePopupUtil.setClose(message);

        MessagePopupUtil.injectFxml(
                message,
                MessagePopupUtil.parsePosByPosMessage(posMessage),
                posMessage
        );
    }

    /**
     * Removes a message from the popup system.
     *
     * @param message the message to be removed.
     */
    @Override
    public void remove(Message message) {
        MessagePopupUtil.removeMessage(message);
    }

    /**
     * Displays a message form for the given model.
     *
     * @param model the model object to generate the form fields from.
     */
    @Override
    public void show(Object model) {
        show(model, MessageFormUtil.getDefaultContent(), Pos.CENTER);
    }

    /**
     * Displays a message form for the given model with a custom parent.
     *
     * @param model  the model object to generate the form fields from.
     * @param parent the custom parent node for the form.
     */
    @Override
    public void show(Object model, Parent parent) {
        show(model, parent, Pos.CENTER);
    }

    /**
     * Displays a message form for the given model at a specific position.
     *
     * @param model the model object to generate the form fields from.
     * @param pos   the position on the screen where the form will appear.
     */
    @Override
    public void show(Object model, Pos pos) {
        show(model, MessageFormUtil.getDefaultContent(), pos);
    }

    /**
     * Displays a message form for the given model, parent, and position.
     *
     * @param model  the model object to generate the form fields from.
     * @param parent the custom parent node for the form.
     * @param pos    the position on the screen where the form will appear.
     */
    @Override
    public void show(Object model, Parent parent, Pos pos) {
        Class<?> clazz = model.getClass();
        if (isValidObjectForm(model) && MessageFormUtil.isValidParentForm(parent)) {
            MessageFormUtil.generateFieldsForm(parent, model);
            MessageFormUtil.injectTheme(parent, theme);

            MasterUtils.findAndEditText(parent, FxPopupConfig.titleFormId, clazz.getAnnotation(MessageForm.class).name());

            // Set actions to close and submit buttons.
            MessageFormUtil.setClose(parent);
            MessageFormUtil.setSubmit(model, parent);

            MessageFormUtil.injectFxml(parent, pos);
        }
    }

    /**
     * Removes the current message form from the screen.
     */
    @Override
    public void remove() {
        MessageFormUtil.removeMessageForm();
    }

    /**
     * Validates if the given object is a valid form model.
     *
     * @param object the object to validate.
     * @return true if the object is valid; otherwise, throws an exception.
     * @throws NullPointerException if required annotations or parameters are missing.
     */
    private boolean isValidObjectForm(Object object) {
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

    /**
     * Gets the current position for displaying popups.
     *
     * @return the position of the popup.
     */
    public Pos getPos() {
        return pos;
    }

    /**
     * Sets the position for displaying popups.
     *
     * @param pos the new position for the popup.
     */
    public void setPos(Pos pos) {
        this.pos = pos;
    }

    /**
     * Sets the theme for the popup system.
     *
     * @param theme the new theme to be applied.
     */
    public void setTheme(Theme theme) {
        this.theme = theme;
    }
}
