package io.github.hugoquinn2.fxpopup.controller;

import io.github.hugoquinn2.fxpopup.control.Message;
import io.github.hugoquinn2.fxpopup.control.ToolTip;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;

public interface FxPopupInterface {
    // Add functions this function are placed one on top of the other so that they do not block each other.
    void add(Node node);
    void add(Pos pos, Node node);

    // Add All functions
    void addAll(Node... nodes);
    void addAll(Pos pos, Node... nodes);


//    void add(Message message, Pos posMessage);
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

    // New features to controls
    void show(ToolTip toolTip);
}
