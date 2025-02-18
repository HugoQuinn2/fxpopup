package io.github.hugoquinn2.fxpopup.utils;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class FxPopupUtils {
    private static final String NODE_MANAGER_ID = "NodeContainer@%s";

    public static void injectFxml(Pos posNode, Node node) {
        Parent root = MasterUtils.wrapInStackPane(MasterUtils.getRoot());

        Parent nodeManager =
                isNodeManager(root, posNode) ?
                        (Parent) getNodeManager(root, posNode) :
                        (Parent) createNodeManager(posNode);

        ((Pane) nodeManager).getChildren().add(node);

        if (!isNodeManager(root, posNode))
            ((Pane) root).getChildren().add(nodeManager);
    }

    private static Node createNodeManager(Pos pos) {
        return new FlowPane() {{
            this.setId(String.format(NODE_MANAGER_ID, pos));
            this.setAlignment(pos);
            StackPane.setAlignment(this, pos);
            this.getStyleClass().add("node-popup-container");
            this.setPickOnBounds(false);
            this.setOrientation(Orientation.VERTICAL);
        }};
    }

    private static boolean isNodeManager(Node node, Pos pos) {
        return MasterUtils.findNodeById(node, String.format(NODE_MANAGER_ID, pos)) != null;
    }

    private static Node getNodeManager(Node node, Pos pos) {
        return MasterUtils.findNodeById(node, String.format(NODE_MANAGER_ID, pos));
    }
}
