package io.github.hugoquinn2.fxpopup.utils;

import io.github.hugoquinn2.fxpopup.config.StyleConfig;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Window;

import java.util.ArrayList;
import java.util.List;

public class MasterUtils {

    /**
     * Retrieves the primary scene of the application.
     *
     * @return the primary Scene if found, otherwise null.
     */
    public static Scene getPrimaryScene() {
        for (Window window : Window.getWindows()) {
            if (window.isShowing()) {
                return window.getScene();
            }
        }
        return null;
    }

    /**
     * Retrieves the root node of the primary scene.
     *
     * @return the root node of the primary Scene if available, otherwise null.
     */
    public static Parent getRoot() {
        Scene scene = getPrimaryScene();
        if (scene != null)
            return scene.getRoot();

        return null;
    }

    /**
     * Finds a node by its ID within a given node hierarchy.
     *
     * @param node the root node to start the search.
     * @param targetId the ID of the node to find.
     * @return the node with the matching ID, or null if not found.
     */
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

    /**
     * Finds a node by its CSS class within a given node hierarchy.
     *
     * @param node the root node to start the search.
     * @param targetClass the CSS class of the node to find.
     * @return the node with the matching CSS class, or null if not found.
     */
    public static Node findNodeByClass(Node node, String targetClass) {
        if (node == null) return null;

        for (String clazz : node.getStyleClass()) {
            if (targetClass.equals(clazz))
                return node;
        }

        if (node instanceof Parent) {
            Parent parent = (Parent) node;
            for (Node child : parent.getChildrenUnmodifiable()) {
                Node found = findNodeByClass(child, targetClass);
                if (found != null)
                    return found;
            }
        }

        return null;
    }

    /**
     * Deletes a node by its ID if it exists within the root node's hierarchy.
     *
     * @param id the ID of the node to delete.
     */
    public static void findAndDeleteById(String id) {
        Parent root = getRoot();
        if (root != null) {
            Node targetNode = findNodeById(root, id);

            if (targetNode != null && targetNode.getParent() != null) {
                Pane parent = (Pane) targetNode.getParent();
                parent.getChildren().remove(targetNode);
            }
        }
    }

    /**
     * Deletes a node by its ID if it exists within the root node's hierarchy.
     * @param root the root parent where the node will bew deleted.
     * @param id the ID of the node to delete.
     */
    public static void findAndDeleteById(Parent root, String id) {
        if (root != null) {
            Node targetNode = findNodeById(root, id);

            if (targetNode != null && targetNode.getParent() != null) {
                Pane parent = (Pane) targetNode.getParent();
                parent.getChildren().remove(targetNode);
            }
        }
    }

    /**
     * Extract parent node and remove.
     * @param node the node to remove.
     */
    public static void remove(Node node) {
        Parent parent = node.getParent();
        ((Pane) parent).getChildren().remove(node);
    }

    /**
     * Recursively requests focus on all child nodes of a given parent node.
     *
     * @param parent the parent node to process.
     */
    public static void requestFocusOnAllFields(Parent parent) {
        for (Node child : parent.getChildrenUnmodifiable()) {
            if (child instanceof Parent)
                requestFocusOnAllFields((Parent) child);

            child.requestFocus();
        }
    }

    /**
     * Wraps the given parent node in a StackPane.
     *
     * @param parent the parent node to wrap.
     * @return the new StackPane containing the parent node.
     */
    public static Parent wrapInStackPane(Parent parent) {
        if (parent instanceof StackPane)
            return parent;

        StackPane newRoot = new StackPane();
        newRoot.getChildren().add(parent);

        Scene scene = getPrimaryScene();
        scene.setRoot(newRoot);

        return newRoot;
    }

    /**
     * Finds a Label by ID and updates its text.
     *
     * @param parent the root node to search within.
     * @param id the ID of the Label to update.
     * @param text the new text to set.
     */
    public static void findAndEditText(Parent parent, String id, String text) {
        if (findNodeById(parent, id) instanceof Label)
            ((Label) findNodeById(parent, id)).setText(text);
    }

    /**
     * Sets a parent node's style to indicate required fields based on child nodes' states.
     *
     * @param parent the parent node to update styles for.
     * @param required whether the fields are required.
     * @param children the child nodes to monitor.
     */
    public static void setRequired(Parent parent, boolean required, Node... children) {
        if (!required)
            return;

        for (Node child : children) {
            child.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                if (!isNowFocused) {
                    if (child instanceof TextField) {
                        if (((TextField) child).getText() == null || ((TextField) child).getText().isBlank()) {
                            if (!parent.getStyleClass().contains(StyleConfig.REQUIRED))
                                parent.getStyleClass().add(StyleConfig.REQUIRED);
                        } else {
                            parent.getStyleClass().remove(StyleConfig.REQUIRED);
                        }
                    } else if (child instanceof ComboBox<?>) {
                        if (((ComboBox<?>) child).getSelectionModel().getSelectedItem() == null) {
                            if (!parent.getStyleClass().contains(StyleConfig.REQUIRED))
                                parent.getStyleClass().add(StyleConfig.REQUIRED);
                        } else {
                            parent.getStyleClass().remove(StyleConfig.REQUIRED);
                        }
                    }
                }
            });
        }
    }

    /**
     * Sets a parent node's style to indicate required fields based on child nodes' text matching a property.
     *
     * @param parent the parent node to update styles for.
     * @param required whether the fields are required.
     * @param textProperty the text property to validate against.
     * @param children the child nodes to monitor.
     */
    public static void setRequired(Parent parent, boolean required, String textProperty, Node... children) {
        setRequired(parent, required, children);

        for (Node child : children) {
            child.focusedProperty().addListener((obs, oldValue, newValue) -> {
                if (child instanceof TextField) {
                    String data = ((TextField) child).getText();
                    if (data != null) {
                        if (!data.matches(textProperty) && !data.trim().isEmpty()) {
                            if (!parent.getStyleClass().contains(StyleConfig.REQUIRED))
                                parent.getStyleClass().add(StyleConfig.REQUIRED);
                        } else {
                            if (!required)
                                parent.getStyleClass().remove(StyleConfig.REQUIRED);
                        }
                    }
                }
            });
        }
    }

    /**
     * Searches for nodes with a specific CSS class within a node hierarchy.
     *
     * @param node the root node to start the search.
     * @param cssClass the CSS class to match.
     * @return a list of nodes that match the specified CSS class.
     */
    public static List<Node> searchNodesWithClass(Node node, String cssClass) {
        List<Node> nodes = new ArrayList<>();
        if (node.getStyleClass().contains(cssClass)) {
            nodes.add(node);
        }

        if (node instanceof Parent) {
            Parent parent = (Parent) node;

            for (Node child : parent.getChildrenUnmodifiable()) {
                nodes.addAll(searchNodesWithClass(child, cssClass));
            }
        }

        return nodes;
    }
}
