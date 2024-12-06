package io.github.hugoquinn2.fxpopup.model;

import io.github.hugoquinn2.fxpopup.constants.MessageType;
import io.github.hugoquinn2.fxpopup.constants.Theme;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Parent;

public class Message <T extends Event> {
    private String title;
    private String context;
    private MessageType messageType;
    private Theme theme;
    private int duration;
    private Parent content;
    private EventHandler<T> actionEvent;
    private EventType<T> eventType;
    private String css;

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

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
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

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public Parent getContent() {
        return content;
    }

    public void setContent(Parent content) {
        this.content = content;
    }

    public EventHandler<T> getActionEvent() {
        return actionEvent;
    }

    public void setActionEvent(EventHandler<T> actionEvent) {
        this.actionEvent = actionEvent;
    }

    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }

    public EventType<T> getEventType() {
        return eventType;
    }

    public void setEventType(EventType<T> eventType) {
        this.eventType = eventType;
    }

    public void setAction(EventType<T> eventType, EventHandler<T> eventHandler) {
        this.eventType = eventType;
        this.actionEvent = eventHandler;
    }


    @Override
    public String toString() {
        return "Message{" +
                "title='" + title + '\'' +
                ", context='" + context + '\'' +
                ", messageType=" + messageType +
                ", theme=" + theme +
                ", duration=" + duration +
                ", content=" + content +
                ", actionEvent=" + actionEvent +
                ", eventType=" + eventType +
                ", css='" + css + '\'' +
                '}';
    }
}
