package io.github.hugoquinn2.fxpopup.utils;

import javafx.scene.Node;
import javafx.scene.Parent;

public class StyleUtil {
    public static Node setTransparent(Node node) {
        node.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; ");
        return node;
    }

    public static Parent setTransparent(Parent parent) {
        parent.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; ");
        return parent;
    }

    public static void setTransparent(Parent... parents) {
        for (Parent parent : parents)
            setTransparent(parent);
    }
}
