package io.github.hugoquinn2.fxpopup.control;

import io.github.hugoquinn2.fxpopup.constants.Theme;
import io.github.hugoquinn2.fxpopup.controller.FxPopup;
import io.github.hugoquinn2.fxpopup.controller.StyleManager;
import io.github.hugoquinn2.fxpopup.controller.ThemeManager;
import io.github.hugoquinn2.fxpopup.service.ThemeDetector;
import io.github.hugoquinn2.fxpopup.utils.ToolTipUtils;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.Objects;

import static io.github.hugoquinn2.fxpopup.config.CssClasses.TOOL_TIP_CLASS;
import static io.github.hugoquinn2.fxpopup.config.CssClasses.TOOL_TIP_LABEL_CLASS;


public class ToolTip extends VBox {
    private FxPopup fxPopup;

    // Tool tip structure
    private Label text;

    // Tool Tips params
    private Node nodeFather;
    private VPos vPosDisplay;
    private HPos hPosDisplay;
    private double space;

    private Theme theme;

    // Customs constuctors
    /**
     * Create a Tool Tip with a <code>text</code> and bound with a <code>Node</code>,
     * by default it is displayed with <code>VPos.BOTTOM</code> and a space between
     * the node bound of <code>10px</code>
     * @param text to show into tool tip.
     * @param nodeFather to bound.
     * */
    public ToolTip(String text, Node nodeFather) {
        this(text, nodeFather, VPos.BOTTOM, null, 10);
    }

    /**
     * Create a Tool Tip with a <code>text</code> and bound with a <code>Node</code>,
     * by default it is displayed with <code>VPos.BOTTOM</code> and a custom space between
     * the node bound.
     * @param text to show into tool tip.
     * @param nodeFather to bound.
     * @param space between <code>nodeFather</code> and Tool Tip.
     * */
    public ToolTip(String text, Node nodeFather, double space) {
        this(text, nodeFather, VPos.BOTTOM, null, space);
    }

    /**
     * Create a Tool Tip with a <code>text</code> and bound with a <code>Node</code>,
     * with custom <code>VPos</code> and a <code>space</code> between the node bound of <code>10px</code>
     * @param text to show into tool tip.
     * @param nodeFather to bound.
     * @param vPosDisplay to displayed.
     * */
    public ToolTip(String text, Node nodeFather, VPos vPosDisplay) {
        this(text, nodeFather, vPosDisplay, null, 10);
    }

    /**
     * Create a Tool Tip with a <code>text</code> and bound with a <code>Node</code>,
     * with custom <code>VPos</code> and a custom space between the node bound.
     * @param text to show into tool tip.
     * @param nodeFather to bound.
     * @param vPosDisplay to displayed.
     * @param space between <code>nodeFather</code> and Tool Tip.
     * */
    public ToolTip(String text, Node nodeFather, VPos vPosDisplay, double space) {
        this(text, nodeFather, vPosDisplay, null, space);
    }

    /**
     * Create a Tool Tip with a <code>text</code> and bound with a <code>Node</code>,
     * with custom <code>HPos</code> and a <code>space</code> between the node bound of <code>10px</code>
     * @param text to show into tool tip.
     * @param nodeFather to bound.
     * @param hPosDisplay to displayed.
     * */
    public ToolTip(String text, Node nodeFather, HPos hPosDisplay) {
        this(text, nodeFather, null, hPosDisplay, 10);
    }

    /**
     * Create a Tool Tip with a <code>text</code> and bound with a <code>Node</code>,
     * with custom <code>HPos</code> and a custom space between the node bound.
     * @param text to show into tool tip.
     * @param nodeFather to bound.
     * @param hPosDisplay to displayed.
     * @param space between <code>nodeFather</code> and Tool Tip.
     * */
    public ToolTip(String text, Node nodeFather, HPos hPosDisplay, double space) {
        this(text, nodeFather, null, hPosDisplay, space);
    }

    // General constructor
    private ToolTip(String text, Node nodeFather, VPos vPosDisplay, HPos hPosDisplay, double space) {
        this.text = new Label(text);
        this.nodeFather = nodeFather;
        this.vPosDisplay = vPosDisplay;
        this.hPosDisplay = hPosDisplay;
        this.space = space;
        fxPopup = new FxPopup();

        // Define tool tip structure
        this.getChildren().add(this.text);

        // Apply styles class for CSS.
        this.getStyleClass().add(TOOL_TIP_CLASS);
        this.text.getStyleClass().add(TOOL_TIP_LABEL_CLASS);

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

        Platform.runLater(() -> this.fxPopup.show(Pos.TOP_LEFT, this));
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
        this.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
    }

    public void show() {
        if (hPosDisplay != null)
            ToolTipUtils.positionToolTip(nodeFather, this, this.hPosDisplay, space);
        else if (vPosDisplay != null)
            ToolTipUtils.positionToolTip(nodeFather, this, this.vPosDisplay, space);

        setVisible(true);
    }

    public void hide() {
        setVisible(false);
    }

    public Label getText() {
        return text;
    }

    public void setText(Label text) {
        this.text = text;
    }

    public Node getNodeFather() {
        return nodeFather;
    }

    public void setNodeFather(Node nodeFather) {
        this.nodeFather = nodeFather;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public HPos gethPosDisplay() {
        return hPosDisplay;
    }

    public void sethPosDisplay(HPos hPosDisplay) {
        this.hPosDisplay = hPosDisplay;
    }

    public VPos getvPosDisplay() {
        return vPosDisplay;
    }

    public void setvPosDisplay(VPos vPosDisplay) {
        this.vPosDisplay = vPosDisplay;
    }
}
