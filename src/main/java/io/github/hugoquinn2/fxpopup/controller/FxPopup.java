package io.github.hugoquinn2.fxpopup.controller;

import io.github.hugoquinn2.fxpopup.constants.Theme;
import io.github.hugoquinn2.fxpopup.utils.MasterUtils;
import io.github.hugoquinn2.fxpopup.utils.MessagePopupUtil;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class FxPopup implements FxPopupInterface {
    /**
     * Create a popup controller. This will apply <code>Message</code>, <code>Form</code> and <code>Tooltip</code>
     * to the main scene.
     */
    public FxPopup() {
        StyleManager.applyAutoStyle();
    }

    /**
     * Add a node to the main scene, by default will be displayed with <code>Pos.BOTTOM_RIGHT</code>. Nodes added at
     * the same position will move.
     * @param node the <code>Node</code> to add to the main <code>Scene</code>.
     */
    @Override
    public void add(Node node) {
        add(Pos.BOTTOM_RIGHT, node);
    }

    /**
     * Add a node to the main scene, it will be displayed with custom <code>Pos</code>. Nodes added at the same
     * position will move.
     * @param pos the position to add.
     * @param node the <code>Node</code> to add to the main <code>Scene</code>.
     */
    @Override
    public void add(Pos pos, Node node) {
        Platform.runLater(() -> {
            MessagePopupUtil.injectFxml(node, MessagePopupUtil.parsePosByPosMessage(pos), pos);
        });
    }

    /**
     * Add a list nodes to the main scene, by default will be displayed with <code>Pos.BOTTOM_RIGHT</code>. Nodes
     * added at the same position will move.
     * @param nodes the list <code>Node</code> to add to the main <code>Scene</code>.
     */
    @Override
    public void addAll(Node... nodes) {
        for (Node node : nodes)
            add(node);
    }

    /**
     * Add a list nodes to the main scene, it will be displayed with custom <code>Pos</code>. Nodes added at the same
     * position will move.
     * @param pos the position to add.
     * @param nodes the list <code>Node</code> to add to the main <code>Scene</code>.
     */
    @Override
    public void addAll(Pos pos, Node... nodes) {
        for (Node node : nodes)
            add(pos, node);
    }

    /**
     * Show a node to the main scene, it will be displayed with default pos <code>Pos.CENTER</code>. Nodes added in the
     * same position will be displayed above the previous one.
     * @param node the <code>Node</code> to add to the main <code>Scene</code>.
     */
    @Override
    public void show(Node node) {
        show(Pos.CENTER, node);
    }

    /**
     * Show a node to the main scene, it will be displayed with default pos <code>Pos.CENTER</code>. Nodes added in the
     * same position will be displayed above the previous one.
     * @param pos the position to show.
     * @param node the <code>Node</code> to add to the main <code>Scene</code>.
     */
    @Override
    public void show(Pos pos, Node node) {
        Platform.runLater(() -> {
            Parent root = MasterUtils.wrapInStackPane(MasterUtils.getRoot());
            StackPane.setAlignment(node, pos);
            ((Pane) root).getChildren().add(node);
        });
    }

    /**
     * Show a node to the main scene, it will be displayed with default pos <code>Pos.CENTER</code>. Nodes added in the
     * same position will be displayed above the previous one.
     * @param x the x coordinates to display.
     * @param y the y coordinates to display.
     * @param node the <code>Node</code> to add to the main <code>Scene</code>.
     */
    @Override
    public void show(double x, double y, Node node) {
        show(Pos.TOP_LEFT, node);
        node.sceneProperty().addListener((observable, oldScene, newScene) -> {
            node.setTranslateX(x);
            node.setTranslateY(y);
        });
    }

    /**
     * Show a list nodes to the main scene, it will be displayed with default pos <code>Pos.CENTER</code>. Nodes added in the
     * same position will be displayed above the previous one.
     * @param nodes the list <code>Node</code> to add to the main <code>Scene</code>.
     */
    @Override
    public void showAll(Node... nodes) {
        for (Node node : nodes)
            show(node);
    }

    /**
     * Show a list nodes to the main scene, it will be displayed with default pos <code>Pos.CENTER</code>. Nodes added in the
     * same position will be displayed above the previous one.
     * @param pos the position to show.
     * @param nodes the list <code>Node</code> to add to the main <code>Scene</code>.
     */
    @Override
    public void showAll(Pos pos, Node... nodes) {
        for (Node node : nodes)
            show(pos, node);
    }

    /**
     * Show a list nodes to the main scene, it will be displayed with default pos <code>Pos.CENTER</code>. Nodes added in the
     * same position will be displayed above the previous one.
     * @param x the x coordinates to display.
     * @param y the y coordinates to display.
     * @param nodes the list <code>Node</code> to add to the main <code>Scene</code>.
     */
    @Override
    public void showAll(double x, double y, Node... nodes) {
        for (Node node : nodes)
            show(x, y, node);
    }

    /**
     * Set global Theme, when changing the theme, the <code>Message</code>, <code>From</code> and <code>ToolTip</code>
     * objects will change themes on their own
     * @param theme the <code>Theme</code> to set.
     */
    @Override
    public void setGlobalTheme(Theme theme) {
       ThemeManager.setGlobalTheme(theme);
    }

    /**
     * Get global Theme
     * @return the global <code>Theme</code>: <code>SYSTEM</code>, <code>DARK</code> or <code>LIGHT</code>.
     */
    @Override
    public Theme getGlobalTheme() {
        return ThemeManager.getGlobalTheme();
    }
}
