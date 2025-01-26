package io.github.hugoquinn2.fxpopup.utils;

import javafx.scene.Node;

public class StyleUtil {
    /**
     * Remove background and border color to Node.
     * @param node Node to apply css code.
     */
    public static void setTransparent(Node node) {
        node.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; ");
    }

    /**
     * Remove background and border color to Node list.
     * @param nodes Nodes list to apply css code.
     */
    public static void setTransparent(Node... nodes) {
        for (Node node : nodes)
            setTransparent(node);
    }
}
