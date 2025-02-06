package io.github.hugoquinn2.fxpopup.utils;

import io.github.hugoquinn2.fxpopup.config.FxPopupConfig;
import io.github.hugoquinn2.fxpopup.constants.FxPopIcon;
import io.github.hugoquinn2.fxpopup.constants.MessageType;
import io.github.hugoquinn2.fxpopup.constants.Theme;
import io.github.hugoquinn2.fxpopup.control.Message;
import io.github.hugoquinn2.fxpopup.model.Icon;
import io.github.hugoquinn2.fxpopup.service.ThemeDetector;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;

/**
 * Utility class for managing message popups.
 */
public class MessagePopupUtil {

    /**
     * Injects an FXML message into the specified position.
     *
     * @param message          the message to inject
     * @param posMessageManager the position of the message manager
     * @param posMessage       the position of the message
     */
    public static void injectFxml(Node node, Pos posMessageManager, Pos posMessage) {
        Parent root = MasterUtils.wrapInStackPane(MasterUtils.getRoot());
        Parent messageManager = !isMessageManager(posMessageManager, posMessage) ?
                createMessageManager(posMessageManager, posMessage) :
                (Parent) MasterUtils.findNodeById(root, FxPopupConfig.messageManagerId + posMessageManager + posMessage);

//        setFadeTransition(message);
//        setTranslateTransition(message, posMessage);
        ((Pane) messageManager).getChildren().add(node);
    }

    /**
     * Removes a message from its parent.
     *
     * @param message the message to remove
     */
    public static void removeMessage(Message message) {
        if (message.getParent() != null)
            MasterUtils.remove(message.getParent());
    }

    /**
     * Sets a fade-out transition for a message.
     *
     * @param message the message to apply the fade-out transition to
     */
    public static void setFadeTransition(Message message) {
//        Parent parent = message.getParent();
//        if (parent == null) {
//            return;
//        }
//
//        int fadeStartTime = message.getDuration() - 1;
//
//        PauseTransition pauseBeforeFade = new PauseTransition(Duration.seconds(fadeStartTime));
//
//        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), parent);
//        fadeOut.setFromValue(1.0);
//        fadeOut.setToValue(0.0);
//
//        pauseBeforeFade.setOnFinished(event -> {
//            if (parent.getParent() != null) {
//                fadeOut.play();
//            }
//        });
//
//        pauseBeforeFade.play();
//
//        fadeOut.setOnFinished(actionEvent -> removeMessage(message));
//
//        parent.setOnMouseEntered(event -> {
//            pauseBeforeFade.pause();
//            fadeOut.pause();
//            parent.setOpacity(1.0);
//        });
//
//        parent.setOnMouseExited(event -> {
//            if (parent.getParent() != null) {
//                pauseBeforeFade.play();
//            }
//        });
    }

    /**
     * Sets a translate transition for a message.
     *
     * @param message   the message to apply the translate transition to
     * @param posMessage the position of the message
     */
    public static void setTranslateTransition(Message message, Pos posMessage) {
//        TranslateTransition translate = new TranslateTransition(Duration.seconds(0.2), message.getParent());
//
//        if (posMessage.equals(Pos.CENTER_LEFT) || posMessage.equals(Pos.TOP_LEFT) || posMessage.equals(Pos.BOTTOM_LEFT)) {
//            translate.setFromX(-FxPopupConfig.maxWidth);
//            translate.setToX(0);
//        }
//        else {
//            translate.setFromX(FxPopupConfig.maxWidth);
//            translate.setToX(0);
//        }
//
//        translate.play();
    }

    /**
     * Checks if the message manager exists for a given position.
     *
     * @param posMessageManager the position of the message manager
     * @param posMessage        the position of the message
     * @return true if the message manager exists; false otherwise
     */
    public static boolean isMessageManager(Pos posMessageManager, Pos posMessage) {
        return MasterUtils.findNodeById(
                MasterUtils.getRoot(), FxPopupConfig.messageManagerId + posMessageManager + posMessage
        ) != null;
    }

    /**
     * Sets up a close button for the message.
     *
     * @param message the message to set the close button for
     */
    public static void setClose(Message message) {
        Button close = (Button) MasterUtils.findNodeById(message.getParent(), FxPopupConfig.buttonDropId);

        if (close == null)
            throw new NullPointerException("Button close #" + FxPopupConfig.buttonDropId + " not founded on message.");

        close.setCursor(Cursor.HAND);
        close.setGraphic(new Icon(FxPopIcon.CLOSE, 0.5));
        close.setOnAction(actionEvent -> removeMessage(message));
    }

    /**
     * Creates a message manager for the specified positions.
     *
     * @param posMessageManager the position of the message manager
     * @param posMessage        the position of the message
     * @return the created message manager
     */
    public static Parent createMessageManager(Pos posMessageManager, Pos posMessage) {
        Parent root = MasterUtils.getRoot();
        VBox parent = new VBox();

        parent.setId(FxPopupConfig.messageManagerId + posMessageManager + posMessage);
        parent.setAlignment(posMessage);

        StackPane.setAlignment(parent, posMessageManager);
        ((Pane) root).getChildren().add(parent);

        parent.setMaxWidth(FxPopupConfig.maxWidth);
        parent.setMinWidth(FxPopupConfig.maxWidth);

        parent.setPadding(new Insets(FxPopupConfig.messageContainerPadding));
        parent.setSpacing(20);

        addChildListener(parent);

        // set parent message transparent when mouse is over a message.
        parent.setPickOnBounds(false);

        return parent;
    }

    /**
     * Injects a theme into a message.
     *
     * @param message     the message to inject the theme into
     * @param theme       the theme to apply
     * @param messageType the type of the message
     */
    public static void injectTheme(Message message, Theme theme) {
        message.getStylesheets().add(
                MessageFormUtil.class.getResource(
                        getStylePath(theme, message.getMessageType())).toExternalForm()
        );
    }

    /**
     * Gets the style path based on the theme and message type.
     *
     * @param theme       the theme to use
     * @param messageType the type of the message
     * @return the style path
     */
    public static String getStylePath(Theme theme, MessageType messageType) {

        return switch (theme) {
            case SYSTEM -> switch (messageType) {
                case INFO -> ThemeDetector.isDarkTheme() ? FxPopupConfig.pathDarkInfo : FxPopupConfig.pathLightInfo;
                case ERROR -> ThemeDetector.isDarkTheme() ? FxPopupConfig.pathDarkError : FxPopupConfig.pathLightError;
                case SUCCESS -> ThemeDetector.isDarkTheme() ? FxPopupConfig.pathDarkSuccess : FxPopupConfig.pathLightSuccess;
                case WARNING -> ThemeDetector.isDarkTheme() ? FxPopupConfig.pathDarkWarning : FxPopupConfig.pathLightWarning;
            };
            case LIGHT -> switch (messageType) {
                case INFO -> FxPopupConfig.pathLightInfo;
                case ERROR -> FxPopupConfig.pathLightError;
                case SUCCESS -> FxPopupConfig.pathLightSuccess;
                case WARNING -> FxPopupConfig.pathLightWarning;
            };
            case DARK -> switch (messageType) {
                case INFO -> FxPopupConfig.pathDarkInfo;
                case ERROR -> FxPopupConfig.pathDarkError;
                case SUCCESS -> FxPopupConfig.pathDarkSuccess;
                case WARNING -> FxPopupConfig.pathDarkWarning;
            };
        };
    }

    /**
     * Gets the default parent for the popup message.
     *
     * @return the default parent
     */
    public static Parent getDefaultParent() {
        try {
            return new FXMLLoader(MessagePopupUtil.class.getResource(FxPopupConfig.pathPopupMessage)).load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Adds a listener to monitor child changes in the message manager.
     *
     * @param messageManager the message manager to monitor
     */
    public static void addChildListener(Parent messageManager) {
        ((Pane) messageManager).getChildren().addListener((ListChangeListener<Node>) change -> {
            if (messageManager != null) {
                while (change.next()) {
                    if (change.wasRemoved() && ((Pane) messageManager).getChildren().isEmpty()) {
                        ((Pane) MasterUtils.getRoot()).getChildren().remove(messageManager);
                    }
                }
            }
        });
    }

    /**
     * Parses a position to determine the appropriate center alignment.
     *
     * @param pos the position to parse
     * @return the parsed position
     */
    public static Pos parsePosByPosMessage(Pos pos) {
        if (pos.equals(Pos.BOTTOM_LEFT) || pos.equals(Pos.TOP_LEFT) || pos.equals(Pos.CENTER_LEFT))
            return Pos.CENTER_LEFT;
        if (pos.equals(Pos.BOTTOM_RIGHT) || pos.equals(Pos.TOP_RIGHT) || pos.equals(Pos.CENTER_RIGHT))
            return Pos.CENTER_RIGHT;

        throw new IllegalArgumentException("Unsupported Pos " + pos + " to message popup.");
    }

    public static Transition createShowEffect(Pos pos, Duration duration, Node node) {
        TranslateTransition showTransition = new TranslateTransition(duration, node);

        if (pos.equals(Pos.CENTER_LEFT) || pos.equals(Pos.TOP_LEFT) || pos.equals(Pos.BOTTOM_LEFT)) {
            showTransition.setFromX(-FxPopupConfig.maxWidth);
            showTransition.setToX(0);
        }
        else {
            showTransition.setFromX(FxPopupConfig.maxWidth);
            showTransition.setToX(0);
        }

        return showTransition;
    }

    public static Transition createRemoveEffect(Duration duration, Node node) {
        FadeTransition removeTransition = new FadeTransition(duration, node);

        // Fade effect, opacity from 1 to 0
        removeTransition.setFromValue(1.0);
        removeTransition.setToValue(0.0);

        return removeTransition;
    }
}
