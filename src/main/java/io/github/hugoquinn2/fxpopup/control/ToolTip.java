package io.github.hugoquinn2.fxpopup.control;

import io.github.hugoquinn2.fxpopup.controller.FxPopup;
import io.github.hugoquinn2.fxpopup.utils.ToolTipUtils;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import javax.swing.*;


public class ToolTip extends VBox {
    private FxPopup fxPopup;

    // Tool tip structure
    private Label text;

    // Tool Tips params
    private Node nodeFather;
    private VPos vPosDisplay;
    private HPos hPosDisplay;

    // Classes for CSS
    private String TOOL_TIP_CLASS = "tool-tip";
    private String TOOL_TIP_TEXT_CLASS = "tool-tip-text";

    // Customs constuctors
    public ToolTip(String text, Node nodeFather) {this(text, nodeFather, VPos.BOTTOM, null);}
    public ToolTip(String text, Node nodeFather, VPos vPosDisplay) {this(text, nodeFather, vPosDisplay, null);}
    public ToolTip(String text, Node nodeFather, HPos hPosDisplay) {this(text, nodeFather, null, hPosDisplay);}

    // General constructor
    private ToolTip(String text, Node nodeFather, VPos vPosDisplay, HPos hPosDisplay) {
        this.text = new Label(text);
        this.nodeFather = nodeFather;
        this.vPosDisplay = vPosDisplay;
        this.hPosDisplay = hPosDisplay;
        fxPopup = new FxPopup();

        // Define tool tip structure
        this.getChildren().add(this.text);

        // Apply styles class for CSS.
        this.getStyleClass().add(TOOL_TIP_CLASS);
        this.text.getStyleClass().add(TOOL_TIP_TEXT_CLASS);

        // Setting responsive
        setResponsive();
        hide();

        // create Effects
        sceneProperty().addListener((observable, oldScene, newScene) -> {
            nodeFather.hoverProperty().addListener((observable2, oldValue, newValue) -> {
                if (newValue) {
                    show();
                } else {
                    hide();
                }
            });

            nodeFather.focusedProperty().addListener((observable2, oldValue, newValue) -> {
                if (newValue) {
                    show();
                } else {
                    hide();
                }
            });
        });

        Platform.runLater(() -> this.fxPopup.show(this));
    }

    private void setResponsive() {
        // Align the text and parent VBox to the center.
        this.text.setAlignment(Pos.CENTER);
        this.setAlignment(Pos.CENTER);

        // Enable text wrapping and set size constraints.
        this.text.setWrapText(true);

        text.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        text.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        text.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

        this.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        this.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        this.setMaxSize(200, Region.USE_PREF_SIZE);
    }

    public void show() {
        if (hPosDisplay != null)
            ToolTipUtils.positionToolTip(nodeFather, this, this.hPosDisplay);
        else if (vPosDisplay != null)
            ToolTipUtils.positionToolTip(nodeFather, this, this.vPosDisplay);

        setVisible(true);
    }

    public void hide() {
        setVisible(false);
    }
}
