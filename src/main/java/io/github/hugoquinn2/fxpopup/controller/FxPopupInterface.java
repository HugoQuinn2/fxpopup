package io.github.hugoquinn2.fxpopup.controller;

import io.github.hugoquinn2.fxpopup.model.Message;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;

public interface FxPopupInterface {
    void add(Message message);
    void add(Message message, Pos posMessage);
    void remove(Message message);
    void show(Object model);
    void show(Object model, Parent parent);
    void show(Object model, Pos pos);
    void show(Object model, Parent parent, Pos pos);
    void show(Node node);
    void show(Node node, Pos pos);
    void show(Node node, double x, double y);
    void toolTip(Node node, String s, HPos pos);
    void toolTip(Node node, String s, VPos pos);
    void toolTip(Node node, String s);
    void removeToolTip(Node node);
    Node getToolTip(Node node);
    void remove();
}
