package io.github.hugoquinn2.fxpopup.controller;

import io.github.hugoquinn2.fxpopup.constants.Theme;
import io.github.hugoquinn2.fxpopup.control.Message;
import io.github.hugoquinn2.fxpopup.control.ToolTip;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;

public interface FxPopupInterface {
    void add(Node node);
    void add(Pos pos, Node node);
    void addAll(Node... nodes);
    void addAll(Pos pos, Node... nodes);

    void show(Node node);
    void show(Pos pos, Node node);
    void show(double x, double y, Node node);
    void showAll(Node... nodes);
    void showAll(Pos pos, Node... nodes);
    void showAll(double x, double y, Node... nodes);

    void setGlobalTheme(Theme theme);
    Theme getGlobalTheme();
}
