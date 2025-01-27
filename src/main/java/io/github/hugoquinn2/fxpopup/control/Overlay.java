package io.github.hugoquinn2.fxpopup.control;

import io.github.hugoquinn2.fxpopup.utils.MasterUtils;
import javafx.application.Platform;
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
        Platform.runLater(() -> {
            this.setFill(color);
            this.setOpacity(opacity);

            this.widthProperty().bind(((Pane) MasterUtils.getRoot()).widthProperty());
            this.heightProperty().bind(((Pane) MasterUtils.getRoot()).heightProperty());

            this.getStyleClass().add(OVERLAY_CLASS);
        });
    }
}
