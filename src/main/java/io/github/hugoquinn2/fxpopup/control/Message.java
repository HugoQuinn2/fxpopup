package io.github.hugoquinn2.fxpopup.control;

import io.github.hugoquinn2.fxpopup.config.FxPopupConfig;
import io.github.hugoquinn2.fxpopup.constants.FxPopIcon;
import io.github.hugoquinn2.fxpopup.constants.MessageType;
import io.github.hugoquinn2.fxpopup.model.Icon;
import io.github.hugoquinn2.fxpopup.utils.MasterUtils;
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
    private Pos posMessage;

    // Effects
    private Transition showTransition;
    private Transition removeTransition;
    private PauseTransition pauseBeforeRemove;

    // Styles class for CSS
    private String MESSAGE_CLASS = "message";
    private String MESSAGE_TITLE_CLASS = "message-title";
    private String MESSAGE_CONTEXT_CLASS = "message-context";
    private String MESSAGE_INDICATOR_CLASS = "message-indicator";
    private String MESSAGE_LABELS_CONTAINER = "message-content-container";
    private String MESSAGE_CLOSE_BUTTON = "message-close-button";

    // Constructor with context
    public Message(String title, String context, MessageType messageType) {this(title, context, messageType, 0, Pos.BOTTOM_RIGHT);}
    public Message(String title, String context, MessageType messageType, Pos posMessage) {this(title, context, messageType, 0, posMessage);}
    public Message(String title, String context, MessageType messageType, int duration) {this(title, context, messageType, duration, Pos.BOTTOM_RIGHT);}
    public Message(String title, String context, MessageType messageType, Pos posMessage,int duration) {this(title, context, messageType, duration, posMessage);}

    // Constructor without context
    public Message(String title, MessageType messageType) {this(title, "", messageType, 0, Pos.BOTTOM_RIGHT);}
    public Message(String title, MessageType messageType, Pos posMessage) {this(title, "", messageType, 0, posMessage);}
    public Message(String title, MessageType messageType, Pos posMessage, int duration) {this(title, "", messageType, duration, posMessage);}
    public Message(String title, MessageType messageType, int duration) {this(title, "", messageType, duration, Pos.BOTTOM_RIGHT);}

    public Message(String title, String context, MessageType messageType, int duration, Pos posMessage) {
        this.title = new Label(title);
        this.context = new Label(context);
        this.messageType = messageType;
        this.duration = duration;
        this.messageIndicator = new Pane();
        this.posMessage = posMessage;
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
        this.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

        this.containerContext.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        this.containerContext.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        this.containerContext.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

        // Create Message structure
        this.closeButton.setGraphic(new Icon(FxPopIcon.CLOSE, 0.5));
        StyleUtil.setTransparent(this.closeButton);

        this.containerContext.getChildren().add(this.title);
        if (!context.isEmpty())
            this.containerContext.getChildren().add(this.context);

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

        // Define times to effects
        setShowEffect(Duration.seconds(0.2));
        setRemoveEffect(Duration.seconds(1));
        setPauseBeforeRemove(Duration.seconds(this.duration > 0 ? this.duration - 1 : 1));

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
    }

    private void setShowEffect(Duration duration) {
        showTransition = new TranslateTransition(duration, this);

        if (posMessage.equals(Pos.CENTER_LEFT) || posMessage.equals(Pos.TOP_LEFT) || posMessage.equals(Pos.BOTTOM_LEFT)) {
            ((TranslateTransition) showTransition).setFromX(-FxPopupConfig.maxWidth);
            ((TranslateTransition) showTransition).setToX(0);
        }
        else {
            ((TranslateTransition) showTransition).setFromX(FxPopupConfig.maxWidth);
            ((TranslateTransition) showTransition).setToX(0);
        }
    }

    private void setRemoveEffect(Duration duration) {
        removeTransition = new FadeTransition(duration, this);
        ((FadeTransition) removeTransition).setFromValue(1.0);
        ((FadeTransition) removeTransition).setToValue(0.0);

        removeTransition.setOnFinished(actionEvent -> ((Pane) this.getParent()).getChildren().remove(this));

        // Pause Transition when mouse is on message
        this.setOnMouseEntered(event -> {
            pauseBeforeRemove.pause();
            removeTransition.pause();
            this.setOpacity(1.0);
        });

        // Play Transition when mouse is existed from message
        this.setOnMouseExited(event -> {
            pauseBeforeRemove.play();
        });
    }

    private void setPauseBeforeRemove(Duration duration) {
        pauseBeforeRemove = new PauseTransition(duration);

        // Play remove effect when pause finished
        pauseBeforeRemove.setOnFinished(event -> {
            removeTransition.play();
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
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Pos getPosMessage() {
        return posMessage;
    }

    public void setPosMessage(Pos posMessage) {
        this.posMessage = posMessage;
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
    }

    public PauseTransition getPauseBeforeRemove() {
        return pauseBeforeRemove;
    }

    public void setPauseBeforeRemove(PauseTransition pauseBeforeRemove) {
        this.pauseBeforeRemove = pauseBeforeRemove;
    }

    private boolean isValid() {
        if (posMessage.equals(Pos.CENTER) || posMessage.equals(Pos.TOP_CENTER) || posMessage.equals(Pos.BOTTOM_CENTER))
            throw new IllegalArgumentException("Unsupported Pos " + posMessage + " to message.");
        if (duration >= 0)
            throw new IllegalArgumentException("Duration message can't be negative.");

        return true;
    }
}
