package io.github.hugoquinn2.fxpopup.utils;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Window;

public class MasterUtils {
    public static Scene getPrimaryScene() {
        for (Window window : Window.getWindows()) {
            if (window.isShowing()) {
                return window.getScene();
            }
        }
        return null;
    }

    public static Parent getRoot(){
        Scene scene = getPrimaryScene();
        if (scene != null)
            return scene.getRoot();

        return null;
    }

    public static Node findNodeById(Node node, String targetId) {
        if (node == null) return null;

        if (targetId.equals(node.getId())) {
            return node;
        }

        if (node instanceof Parent) {
            Parent parent = (Parent) node;
            for (Node child : parent.getChildrenUnmodifiable()) {
                Node found = findNodeById(child, targetId);
                if (found != null) {
                    return found;
                }
            }
        }

        return null;
    }

    public static void findAndDeleteById(String id){
        Parent root = getRoot();
        if (root != null) {
            Node targetNode = findNodeById(root, id);

            if (targetNode != null && targetNode.getParent() != null) {
                Pane parent = (Pane) targetNode.getParent();
                parent.getChildren().remove(targetNode);
            }
        }
    }

    public static void requestFocusOnAllFields(Parent parent) {
        for(Node child : parent.getChildrenUnmodifiable()) {
            if (child instanceof Parent)
                requestFocusOnAllFields((Parent) child);

            child.requestFocus();
        }
    }
}
