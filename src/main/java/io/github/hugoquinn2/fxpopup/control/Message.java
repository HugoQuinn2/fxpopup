package io.github.hugoquinn2.fxpopup.control;

import io.github.hugoquinn2.fxpopup.config.FxPopupConfig;
import io.github.hugoquinn2.fxpopup.constants.FxPopIcon;
import io.github.hugoquinn2.fxpopup.constants.MessageType;
import io.github.hugoquinn2.fxpopup.constants.Theme;
import io.github.hugoquinn2.fxpopup.controller.ThemeManager;
import io.github.hugoquinn2.fxpopup.model.Icon;
import io.github.hugoquinn2.fxpopup.service.ThemeDetector;
import io.github.hugoquinn2.fxpopup.utils.MasterUtils;
import io.github.hugoquinn2.fxpopup.utils.MessageFormUtil;
import io.github.hugoquinn2.fxpopup.utils.MessagePopupUtil;
import io.github.hugoquinn2.fxpopup.utils.StyleUtil;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.util.Objects;


public class Message extends HBox {
    // Message Structure
    private Pane messageIndicator;
    private Label title;
    private Label context;
    private VBox containerContext;
    private Button closeButton;

    // Message params
    private MessageType messageType;
    private int duration;
    private Theme theme = Theme.SYSTEM;

    // Effects
    private Transition showTransition;
    private Transition removeTransition;
    private Transition pauseBeforeRemove;

    // Styles class for CSS
    private String MESSAGE_CLASS = "message";
    private String MESSAGE_TITLE_CLASS = "message-title";
    private String MESSAGE_CONTEXT_CLASS = "message-context";
    private String MESSAGE_INDICATOR_CLASS = "message-indicator";
    private String MESSAGE_LABELS_CONTAINER = "message-content-container";
    private String MESSAGE_CLOSE_BUTTON = "message-close-button";

    // Constructor with context
    public Message(String title, String context) {this(title, context, MessageType.NONE, 0);}
    public Message(String title, String context, MessageType messageType) {this(title, context, messageType, 0);}

    // Constructor without context
    public Message(String title) {this(title, null, MessageType.NONE, 0);}
    public Message(String title, MessageType messageType) {this(title, null, messageType, 0);}
    public Message(String title, MessageType messageType, int duration) {this(title, null, messageType, duration);}

    public Message(String title, String context, MessageType messageType, int duration) {
        if (duration < 0)
            throw new IllegalArgumentException("The duration cannot be less than 1");

        this.title = new Label(title);
        this.context = new Label(context);
        setMessageType(messageType);
        this.duration = duration;
        this.messageIndicator = new Pane();
        this.containerContext = new VBox();
        this.closeButton = new Button();

        // Set responsive
        this.title.setWrapText(true);
        this.context.setWrapText(true);
        this.setAlignment(Pos.TOP_CENTER);
        this.containerContext.setAlignment(Pos.CENTER_LEFT);

        HBox.setHgrow(this.containerContext, Priority.ALWAYS);
        VBox.setVgrow(this.context, Priority.ALWAYS);

        this.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        this.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        this.setMaxSize(400, Region.USE_COMPUTED_SIZE);

        this.containerContext.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        this.containerContext.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        this.containerContext.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

        // Create Message structure
        this.closeButton.setGraphic(new Icon(FxPopIcon.CLOSE, 0.5));
        StyleUtil.setTransparent(this.closeButton);

        if (title != null) this.containerContext.getChildren().add(this.title);
        if (context != null) this.containerContext.getChildren().add(this.context);

        this.getChildren().addAll(
          this.messageIndicator,
          this.containerContext,
                this.closeButton
        );

        // Define Styles classes
        this.getStyleClass().add(MESSAGE_CLASS);
        this.title.getStyleClass().add(MESSAGE_TITLE_CLASS);
        this.context.getStyleClass().add(MESSAGE_CONTEXT_CLASS);
        this.messageIndicator.getStyleClass().add(MESSAGE_INDICATOR_CLASS);
        this.containerContext.getStyleClass().add(MESSAGE_LABELS_CONTAINER);
        this.closeButton.getStyleClass().add(MESSAGE_CLOSE_BUTTON);

        // Define basic effect
        defineEffects();

        // Play effect when message is on scene
        this.sceneProperty().addListener((observable, oldScene, newScene) -> {
            showTransition.play();
            if (this.duration != 0)
                pauseBeforeRemove.play();
        });

        // Set Action to CloseButton
        closeButton.setCursor(Cursor.HAND);
        closeButton.setOnAction(event -> {
            MasterUtils.remove(this);
        });

        // Global Theme
        theme = ThemeManager.getGlobalTheme();
        loadStyle(theme);
        ThemeManager.globalTheme().addListener((obs, oldTheme, newTheme) -> loadStyle(newTheme));
    }

    private void defineEffects() {
        setShowTransition(new TranslateTransition(Duration.ZERO, this));
        setRemoveTransition(new FadeTransition(Duration.ZERO, this));
        setPauseBeforeRemove(new PauseTransition(Duration.seconds(duration)));

        // Pause Transition when mouse is on message
        this.setOnMouseEntered(event -> {
            if (duration != 0) {
                pauseBeforeRemove.pause();
                removeTransition.pause();
            }
        });

        // Play Transition when mouse is exited from message
        this.setOnMouseExited(event -> {
            if (duration != 0)
                pauseBeforeRemove.play();
        });
    }

    public Pane getMessageIndicator() {
        return messageIndicator;
    }

    public void setMessageIndicator(Pane messageIndicator) {
        this.messageIndicator = messageIndicator;
    }

    public Label getTitle() {
        return title;
    }

    public void setTitle(Label title) {
        this.title = title;
    }

    public Label getContext() {
        return context;
    }

    public void setContext(Label context) {
        this.context = context;
    }

    public VBox getContainerContext() {
        return containerContext;
    }

    public void setContainerContext(VBox containerContext) {
        this.containerContext = containerContext;
    }

    public Button getCloseButton() {
        return closeButton;
    }

    public void setCloseButton(Button closeButton) {
        this.closeButton = closeButton;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;

        // Remove all style class
        getStyleClass().remove("info");
        getStyleClass().remove("success");
        getStyleClass().remove("warning");
        getStyleClass().remove("error");

        switch (messageType) {
            case INFO -> getStyleClass().add("info");
            case ERROR -> getStyleClass().add("error");
            case SUCCESS -> getStyleClass().add("success");
            case WARNING -> getStyleClass().add("warning");
        }
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Transition getShowTransition() {
        return showTransition;
    }

    public void setShowTransition(Transition showTransition) {
        this.showTransition = showTransition;
    }

    public Transition getRemoveTransition() {
        return removeTransition;
    }

    public void setRemoveTransition(Transition removeTransition) {
        this.removeTransition = removeTransition;
        this.removeTransition.setOnFinished(event -> MasterUtils.remove(this));
    }

    public Transition getPauseBeforeRemove() {
        return pauseBeforeRemove;
    }

    public void setPauseBeforeRemove(Transition pauseBeforeRemove) {
        this.pauseBeforeRemove = pauseBeforeRemove;
        this.pauseBeforeRemove.setOnFinished(event -> removeTransition.play());
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
        loadStyle(theme);
    }

    public void loadStyle(Theme theme) {
        getStylesheets().clear();

        getStylesheets().add(
                Objects.requireNonNull(Message.class.getResource(
                        switch (theme) {
                            case SYSTEM ->
                                    ThemeDetector.isDarkTheme() ? "/themes/dark/message.css" : "/themes/light/message.css";
                            case DARK -> "/themes/dark/message.css";
                            case LIGHT -> "/themes/light/message.css";
                        }
                )).toExternalForm()
        );
    }
}
