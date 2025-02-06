package io.github.hugoquinn2.fxpopup.controller;

import io.github.hugoquinn2.fxpopup.config.FxPopupConfig;
import io.github.hugoquinn2.fxpopup.constants.Theme;
import io.github.hugoquinn2.fxpopup.control.Form;
import io.github.hugoquinn2.fxpopup.control.Message;
import io.github.hugoquinn2.fxpopup.control.ToolTip;
import io.github.hugoquinn2.fxpopup.utils.MasterUtils;
import io.github.hugoquinn2.fxpopup.utils.MessageFormUtil;
import io.github.hugoquinn2.fxpopup.utils.MessagePopupUtil;
import io.github.hugoquinn2.fxpopup.utils.ToolTipUtils;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.Objects;

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

    @Override
    public void add(Node node) {
        add(Pos.BOTTOM_RIGHT, node);
    }

    @Override
    public void add(Pos pos, Node node) {
        Platform.runLater(() -> {
            if (node instanceof Message)
                ((Message) node).setTheme(theme);
            else if (node instanceof Form)
                ((Form) node).setTheme(theme);

            MessagePopupUtil.injectFxml(node, MessagePopupUtil.parsePosByPosMessage(pos), pos);
        });
    }

    @Override
    public void addAll(Node... nodes) {
        for (Node node : nodes)
            add(node);
    }

    @Override
    public void addAll(Pos pos, Node... nodes) {
        for (Node node : nodes)
            add(pos, node);
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
            MessageFormUtil.generateFieldsForm(parent, model, theme);
            MessageFormUtil.injectTheme(parent, theme);

            MasterUtils.findAndEditText(parent, FxPopupConfig.titleFormId, clazz.getAnnotation(MessageForm.class).name());

            // Set actions to close and submit buttons.
            MessageFormUtil.setClose(parent);
            MessageFormUtil.setSubmit(model, parent);

            MessageFormUtil.injectFxml(parent, pos);
        }
    }

    /**
     * Displays a custom popup on Pos.TOP_LEFT by default.
     *
     * @param node  the node object to display.
     */
    @Override
    public void show(Node node) {
        show(node, Pos.TOP_LEFT);
    }

    /**
     * Displays a custom popup on custom Pos.
     *
     * @param node  the node object to display.
     * @param pos the pos.
     */
    @Override
    public void show(Node node, Pos pos) {
        Platform.runLater(() -> {
            Parent root = MasterUtils.wrapInStackPane(MasterUtils.getRoot());
            StackPane.setAlignment(node, pos);
            ((Pane) root).getChildren().add(node);
        });
    }

    /**
     * Displays a custom popup on coordinates x & y.
     *
     * @param node  the node object to display.
     * @param x the coordinates X.
     * @param y the coordinates Y.
     */
    @Override
    public void show(Node node, double x, double y) {
        Platform.runLater(() -> {
            show(node);

            node.setTranslateX(x);
            node.setTranslateY(y);
        });
    }

    /**
     * Displays a Tool Tip on a Node with custom <code>HPos</code>.
     *
     * @param node  the node object to display.
     * @param s the text on tool tip.
     * @param pos the HPos to show.
     */
    @Override
    public void toolTip(Node node, String s, HPos pos) {
        String nodeId = ToolTipUtils.getToolTipIdByNode(node);

        if (ToolTipUtils.isToolTip(node))
            MasterUtils.remove(ToolTipUtils.getToolTipByNode(node));

        Node toolTip = ToolTipUtils.createToolTip(s, theme);
        toolTip.setId(nodeId);

        // a delay on first run to ensure rendering
        Platform.runLater(() -> {
            PauseTransition delay = new PauseTransition(Duration.millis(200));
            delay.setOnFinished(event -> {
                toolTip.setVisible(false);

                if (!ToolTipUtils.isToolTip(node))
                    show(toolTip);

                // show tool tip with mouse and hover
                node.hoverProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        ToolTipUtils.positionToolTip(node, toolTip, pos);
                        toolTip.setVisible(true);
                    } else {
                        toolTip.setVisible(false);
                    }
                });

                // show tool tip with focused
                node.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        ToolTipUtils.positionToolTip(node, toolTip, pos);
                        toolTip.setVisible(true);
                    } else {
                        toolTip.setVisible(false);
                    }
                });
            });
            delay.play();
        });
    }

    /**
     * Displays a Tool Tip on a Node with custom VPos.
     *
     * @param node  the node object to display.
     * @param s the text on tool tip.
     * @param pos the VPos to show.
     */
    @Override
    public void toolTip(Node node, String s, VPos pos) {
        String nodeId = Integer.toString(node.hashCode());

        if (ToolTipUtils.isToolTip(node))
            MasterUtils.remove(ToolTipUtils.getToolTipByNode(node));

        Node toolTip = ToolTipUtils.createToolTip(s, theme);
        toolTip.setId(nodeId);

        // a delay on first run to ensure rendering
        Platform.runLater(() -> {
            PauseTransition delay = new PauseTransition(Duration.millis(200));
            delay.setOnFinished(event -> {
                toolTip.setVisible(false);

                if (!ToolTipUtils.isToolTip(node))
                    show(toolTip);

                // show tool tip with mouse and hover
                node.hoverProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        ToolTipUtils.positionToolTip(node, toolTip, pos);
                        toolTip.setVisible(true);
                    } else {
                        toolTip.setVisible(false);
                    }
                });

                // show tool tip with focused
                node.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        ToolTipUtils.positionToolTip(node, toolTip, pos);
                        toolTip.setVisible(true);
                    } else {
                        toolTip.setVisible(false);
                    }
                });
            });
            delay.play();
        });
    }

    /**
     * Displays a Tool Tip on a Node with default <code>VPos.BOTTOM</code>.
     *
     * @param node  the node object to display.
     * @param s the text on tool tip.
     */
    @Override
    public void toolTip(Node node, String s) {
        toolTip(node,s, FxPopupConfig.defaultToolTipPos);
    }

    @Override
    public void removeToolTip(Node node) {
        MasterUtils.remove(ToolTipUtils.getToolTipByNode(node));
    }

    @Override
    public Node getToolTip(Node node) {
        return ToolTipUtils.getToolTipByNode(node);
    }

    /**
     * Removes the current message form from the screen.
     */
    @Override
    public void remove() {
        MessageFormUtil.removeMessageForm();
    }

    @Override
    public void show(ToolTip toolTip) {
        ToolTipUtils.injectTheme(toolTip, theme);
        show(((Pane) toolTip));
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
