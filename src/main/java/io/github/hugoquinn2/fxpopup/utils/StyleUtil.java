package io.github.hugoquinn2.fxpopup.utils;

import javafx.scene.Node;
import javafx.scene.Parent;

public class StyleUtil {
    public static void setTransparent(Node node) {
        node.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; ");
    }

    public static void setTransparent(Parent parent) {
        parent.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; ");
    }
}