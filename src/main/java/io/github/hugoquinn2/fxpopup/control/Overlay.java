package io.github.hugoquinn2.fxpopup.control;

import io.github.hugoquinn2.fxpopup.utils.MasterUtils;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static io.github.hugoquinn2.fxpopup.config.CssClasses.OVERLAY_CLASS;

public class Overlay extends Rectangle {
    /**
     * Create an Overlay with default background color <code>Color.BLACK</code> and default opacity <code>0.3</code>.
     * The overlay will be linked to the root of the scene.
     */
    public Overlay(){this(Color.BLACK, 0.3);}

    /**
     * Create an Overlay with custom background color and default opacity <code>0.3</code>.
     * The overlay will be linked to the root of the scene.
     * @param color the background color.
     */
    public Overlay(Color color){this(color, 0.3);}

    /**
     * Create an Overlay with default background color <code>Color.BLACK</code> and custom opacity.
     * The overlay will be linked to the root of the scene.
     * @param opacity the opacity value.
     */
    public Overlay(double opacity){this(Color.BLACK, opacity);}

    /**
     * Create an Overlay with custom background color and custom opacity.
     * The overlay will be linked to the root of the scene.
     * @param color the background color.
     * @param opacity the opacity value.
     */
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
