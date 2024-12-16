package io.github.hugoquinn2.fxpopup.utils;

import io.github.hugoquinn2.fxpopup.config.FxPopupConfig;
import io.github.hugoquinn2.fxpopup.constants.MessageType;
import io.github.hugoquinn2.fxpopup.constants.Theme;
import io.github.hugoquinn2.fxpopup.model.Message;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.ListChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

public class FxPopupUtil {

    public static void removeFxml(Message message) {
        FlowPane messageManager = getMessageManager();
        Node parent = message.getContent();

        messageManager.getChildren().remove(parent);
    }

    public static void injectFxml(Message message, Pos pos) {
        Parent root = MasterUtils.wrapInStackPane(MasterUtils.getRoot());

        if (root instanceof StackPane) {
            Parent parent = message.getContent();
            final FlowPane messageManager;
            Button buttonClose = getButtonClose(parent);

            if (isMessageManager()) {
                messageManager = getMessageManager();
            } else {
                messageManager = new FlowPane();
                setMessageManagerConfig(messageManager, pos);
            }

            setTranslateTransition(message);

            if (message.getActionEvent() != null && message.getEventType() != null)
                loadActionEvent(message);

            messageManager.getChildren().add(parent);

            if (buttonClose != null)
                buttonClose.setOnAction(event -> {
                    removeFxml(message);
                });
        }

    }

    private static boolean isMessageManager() {
        return MasterUtils.findNodeById(MasterUtils.getRoot(), FxPopupConfig.messageManagerId) != null;
    }

    private static Button getButtonClose(Parent parent) {
        return (Button) parent.lookup(String.format("#%s", FxPopupConfig.buttonDropId));
    }

    private static FlowPane getMessageManager() {
        return (FlowPane) MasterUtils.findNodeById(MasterUtils.getRoot(), FxPopupConfig.messageManagerId);
    }

    private static void setMessageManagerConfig(FlowPane messageManager, Pos pos) {
        Parent root = MasterUtils.getRoot();
        if (root instanceof StackPane) {
            messageManager.setId(FxPopupConfig.messageManagerId);
            messageManager.setVgap(FxPopupConfig.insetsMessageManager);
            messageManager.setAlignment(pos);
            messageManager.setStyle("-fx-background-color: transparent;");

            messageManager.setMaxWidth(FxPopupConfig.maxWidth);
            messageManager.setMinWidth(FxPopupConfig.maxWidth);

            messageManager.maxHeightProperty().bind(((StackPane) root).heightProperty());
            messageManager.minHeightProperty().bind(((StackPane) root).heightProperty());
            messageManager.prefHeightProperty().bind(((StackPane) root).heightProperty());

            messageManager.setPadding(new Insets(FxPopupConfig.insetsMessageManager));
            StackPane.setAlignment(messageManager, Pos.TOP_RIGHT);
            ((StackPane) root).getChildren().add(messageManager);

//        addHeightListener(stackPane, messageManager);

            // Drop Message Manager if is empty
            addChildListener(((StackPane) root), messageManager);
        }
    }

    public static void addChildListener(StackPane stackPane, FlowPane messageManager) {
        messageManager.getChildren().addListener((ListChangeListener<Node>) change -> {
            while (change.next()) {
                if (change.wasRemoved() && messageManager.getChildren().isEmpty()) {
                    stackPane.getChildren().remove(messageManager);
                }
            }
        });
    }

    public static void addHeightListener(StackPane stackPane, FlowPane messageManager) {
        stackPane.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            messageManager.setMaxHeight(newHeight.doubleValue());
            messageManager.setMinHeight(newHeight.doubleValue());
            messageManager.layout();
            System.out.println("StackPane height changed: Old=" + oldHeight + ", New=" + newHeight);
        });

        messageManager.getChildren().forEach(child -> {
            if (child instanceof Region) {
                ((Region) child).setMaxHeight(Double.MAX_VALUE);
                ((Region) child).setMaxWidth(Double.MAX_VALUE);
            }
        });
    }

    public static void injectTheme(Message message) {
        Parent parent = message.getContent();
        parent.getStylesheets().add(message.getCss());
    }

    public static void loadContent(Message message) {
        Pane parent = (Pane) message.getContent();

        Label title = (Label) parent.lookup(FxPopupConfig.titleId);
        Label context = (Label) parent.lookup(FxPopupConfig.contextId);

        title.setText(message.getTitle());

        if (message.getContext() != null) {
            context.setText(message.getContext());
        } else {
            context.setVisible(false);
            context.setMaxHeight(0);
            context.setMinHeight(0);
        }
    }

    public static Parent getDefaultContent() {
        try {
            return new FXMLLoader(FxPopupUtil.class.getResource(FxPopupConfig.pathBody)).load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getDefaultCss(Message message) {
        return Objects.requireNonNull(FxPopupUtil.class.getResource(Objects.requireNonNull(getCssPath(message)))).toExternalForm();
    }

    public static <T extends Event> void loadActionEvent(Message message) {
        Parent parent = message.getContent();
        EventType<T> eventType = message.getEventType();
        EventHandler<T> eventHandler = message.getActionEvent();

        if (parent == null) {
            throw new IllegalArgumentException("Parent cannot be null");
        }
        if (eventType == null || eventHandler == null) {
            throw new IllegalArgumentException("EventType and EventHandler cannot be null");
        }

        parent.addEventHandler(eventType, eventHandler);
        parent.setCursor(Cursor.HAND);
    }

    private static String getCssPath(Message message) {
        MessageType messageType = message.getMessageType();
        Theme theme = message.getTheme();

        if (theme.equals(Theme.DARK)) {
            if (messageType.equals(MessageType.SUCCESS))
                return FxPopupConfig.pathDarkSuccess;
            if (messageType.equals(MessageType.INFO))
                return FxPopupConfig.pathDarkInfo;
            if (messageType.equals(MessageType.WARNING))
                return FxPopupConfig.pathDarkWarning;
            if (messageType.equals(MessageType.ERROR))
                return FxPopupConfig.pathDarkError;
        }

        if (theme.equals(Theme.LIGHT)) {
            if (messageType.equals(MessageType.SUCCESS))
                return FxPopupConfig.pathLightSuccess;
            if (messageType.equals(MessageType.INFO))
                return FxPopupConfig.pathLightInfo;
            if (messageType.equals(MessageType.WARNING))
                return FxPopupConfig.pathLightWarning;
            if (messageType.equals(MessageType.ERROR))
                return FxPopupConfig.pathLightError;
        }

        return null;
    }

    public static void setFadeTransition(Message message) {
        Parent parent = message.getContent();
        int duration = message.getDuration();

        int fadeStartTime = duration - 1;

        PauseTransition pauseBeforeFade = new PauseTransition(Duration.seconds(fadeStartTime));

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), parent);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(event -> {
            if (isMessageManager())
                removeFxml(message);
        });

        pauseBeforeFade.setOnFinished(event -> fadeOut.play());

        pauseBeforeFade.play();

        parent.setOnMouseEntered(event -> {
            pauseBeforeFade.pause();
            fadeOut.pause();
            parent.setOpacity(1.0);
        });

        parent.setOnMouseExited(event -> {
            pauseBeforeFade.play();
            fadeOut.play();
        });
    }

    public static void setTranslateTransition(Message message) {
        TranslateTransition translate = new TranslateTransition(Duration.seconds(0.2), message.getContent());
        translate.setFromX(FxPopupConfig.maxWidth);
        translate.setToX(0);

        translate.play();
    }
}
