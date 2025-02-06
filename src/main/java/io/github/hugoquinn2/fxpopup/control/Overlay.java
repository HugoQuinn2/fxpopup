package io.github.hugoquinn2.fxpopup.control;

import io.github.hugoquinn2.fxpopup.utils.MasterUtils;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Overlay extends Rectangle {
    // Confing Overlay
    private final String OVERLAY_CLASS = "overlay";

    public Overlay(){this(Color.BLACK, 0.3);}
    public Overlay(Color color){this(color, 0.3);}
    public Overlay(double opacity){this(Color.BLACK, opacity);}

    public Overlay(Color color, double opacity) {
        this.setFill(color);
        this.setOpacity(opacity);

        // Bind width and height when overlay is on scene
        this.sceneProperty().addListener((observable, oldScene, newScene) -> {
            Parent root = getParent();

            if (root != null) {
                this.widthProperty().bind(((Pane) root).widthProperty());
                this.heightProperty().bind(((Pane) root).heightProperty());
            }
        });

        this.getStyleClass().add(OVERLAY_CLASS);
    }
}
