package io.github.hugoquinn2.fxpopup.controller;

import io.github.hugoquinn2.fxpopup.model.Message;
import javafx.geometry.Pos;
import javafx.scene.Parent;

public interface FxPopupInterface {
    void add(Message message);
    void remove(Message message);
    void show(Object model);
    void show(Object model, Parent parent);
    void show(Object model, Pos pos);
    void show(Object model, Parent parent, Pos pos);
    void remove();
}
