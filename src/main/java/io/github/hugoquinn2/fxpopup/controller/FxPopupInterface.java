package io.github.hugoquinn2.fxpopup.controller;

import io.github.hugoquinn2.fxpopup.model.Message;

public interface FxPopupInterface {
    void add(Message message);
    void remove(Message message);
    void show(Object model);
    void removeMessageForm();
}
