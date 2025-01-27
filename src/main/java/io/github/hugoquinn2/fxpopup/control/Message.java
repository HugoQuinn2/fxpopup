package io.github.hugoquinn2.fxpopup.control;

import io.github.hugoquinn2.fxpopup.config.FxPopupConfig;
import io.github.hugoquinn2.fxpopup.constants.MessageType;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Duration;


public class Message extends HBox {
    // Message Structure
    private Pane messageIndicator;
    private Label title;
    private Label context;
    private HBox containerContext;
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
    private String MESSAGE_LABELS_CONTAINER = "message-container-context";
    private String MESSAGE_CLOSE_BUTTON = "message-close-button";

    public Message(String title, MessageType messageType, int duration, Pos posMessage) {
        this(title, "", messageType, duration, posMessage);
    }

    public Message(String title, String context, MessageType messageType, int duration, Pos posMessage) {
        this.title = new Label(title);
        this.context = new Label(context);
        this.messageType = messageType;
        this.duration = duration;
        this.messageIndicator = new Pane();
        this.posMessage = posMessage;
        this.containerContext = new HBox();
        this.closeButton = new Button();

        // Set responsive
        this.title.setWrapText(true);
        this.context.setWrapText(true);
        this.setAlignment(Pos.TOP_CENTER);
        this.containerContext.setAlignment(Pos.TOP_LEFT);

        // Create Message structure
        this.containerContext.getChildren().addAll(
                this.title,
                !context.isEmpty() ? this.context : null
        );

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
        setPauseBeforeRemove(Duration.seconds(this.duration - 1));

        // Play effect when message is on scene
        this.sceneProperty().addListener((observable, oldScene, newScene) -> {
            showTransition.play();
            pauseBeforeRemove.play();
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
            if (this.duration != 0)
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

    public HBox getContainerContext() {
        return containerContext;
    }

    public void setContainerContext(HBox containerContext) {
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
}
