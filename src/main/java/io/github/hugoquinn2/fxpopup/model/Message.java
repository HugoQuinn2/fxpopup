package io.github.hugoquinn2.fxpopup.model;

import io.github.hugoquinn2.fxpopup.constants.MessageType;
import io.github.hugoquinn2.fxpopup.constants.Theme;
import io.github.hugoquinn2.fxpopup.utils.MessagePopupUtil;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Parent;

public class Message {
    private String title;
    private String context;
    private MessageType messageType;
    private int duration;
    private Parent parent = MessagePopupUtil.getDefaultParent();

    public Message() {
    }

    public Message(String title, String context, MessageType messageType, int duration) {
        this.title = title;
        this.duration = duration;
        this.messageType = messageType;
        this.context = context;
    }

    public Message(String title, MessageType messageType, int duration) {
        this.title = title;
        this.messageType = messageType;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "Message{" +
                "title='" + title + '\'' +
                ", context='" + context + '\'' +
                ", messageType=" + messageType +
                ", duration=" + duration +
                ", parent=" + parent +
                '}';
    }
}
