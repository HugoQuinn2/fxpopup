package io.github.hugoquinn2.fxpopup.controller;

import io.github.hugoquinn2.fxpopup.config.FxPopupConfig;
import io.github.hugoquinn2.fxpopup.constants.Theme;
import io.github.hugoquinn2.fxpopup.control.Form;
import io.github.hugoquinn2.fxpopup.control.Message;
import io.github.hugoquinn2.fxpopup.utils.MasterUtils;
import io.github.hugoquinn2.fxpopup.utils.MessagePopupUtil;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * Controller for managing FxPopup messages and forms.
 */
public class FxPopup implements FxPopupInterface {
    /**
     * Default constructor initializing default position and theme.
     */
    public FxPopup() {}

    @Override
    public void add(Node node) {
        add(Pos.BOTTOM_RIGHT, node);
    }

    @Override
    public void add(Pos pos, Node node) {
        Platform.runLater(() -> {
            MessagePopupUtil.injectFxml(node, MessagePopupUtil.parsePosByPosMessage(pos), pos);
        });
    }

    @Override
    public void addAll(Node... nodes) {
        for (Node node : nodes)
            add(node);
    }

    @Override
    public void addAll(Pos pos, Node... nodes) {
        for (Node node : nodes)
            add(pos, node);
    }

    @Override
    public void show(Node node) {
        show(Pos.CENTER, node);
    }

    @Override
    public void show(Pos pos, Node node) {
        Platform.runLater(() -> {
            Parent root = MasterUtils.wrapInStackPane(MasterUtils.getRoot());
            StackPane.setAlignment(node, pos);
            ((Pane) root).getChildren().add(node);
        });
    }

    @Override
    public void show(double x, double y, Node node) {
        show(Pos.TOP_LEFT, node);
        node.sceneProperty().addListener((observable, oldScene, newScene) -> {
            node.setTranslateX(x);
            node.setTranslateY(y);
        });
    }

    @Override
    public void showAll(Node... nodes) {
        for (Node node : nodes)
            show(node);
    }

    @Override
    public void showAll(Pos pos, Node... nodes) {
        for (Node node : nodes)
            show(pos, node);
    }

    @Override
    public void showAll(double x, double y, Node... nodes) {
        for (Node node : nodes)
            show(x, y, node);
    }

    @Override
    public ObjectProperty<Theme> themeManager() {
        return ThemeManager.globalTheme();
    }

    @Override
    public void setGlobalTheme(Theme theme) {
       ThemeManager.setGlobalTheme(theme);
    }

    @Override
    public Theme getGlobalTheme() {
        return ThemeManager.getGlobalTheme();
    }
}
