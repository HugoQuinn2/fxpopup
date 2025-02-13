package io.github.hugoquinn2.fxpopup.utils;

import io.github.hugoquinn2.fxpopup.config.FxPopupConfig;
import io.github.hugoquinn2.fxpopup.config.StyleConfig;
import io.github.hugoquinn2.fxpopup.constants.Theme;
import io.github.hugoquinn2.fxpopup.service.ThemeDetector;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.util.Objects;

public class ToolTipUtils {

    /**
     * Creates a tooltip styled as a VBox containing a Label with the specified text.
     * The tooltip will be styled and sized dynamically based on its content.
     *
     * @param s     The text to display in the tooltip.
     * @param theme The theme to apply to the tooltip (LIGHT, DARK, or SYSTEM).
     * @return The styled tooltip as a Parent.
     */
    public static Parent createToolTip(String s, Theme theme) {
        Label text = new Label(s);
        VBox parent = new VBox(text);

        // Add styles to the tooltip and its text.
        parent.getStyleClass().add(StyleConfig.TOOL_TIP);
        text.getStyleClass().add(StyleConfig.TOOL_TIP_TEXT);

        // Align the text and parent VBox to the center.
        text.setAlignment(Pos.CENTER);
        parent.setAlignment(Pos.CENTER);

        // Enable text wrapping and set size constraints.
        text.setWrapText(true);

        text.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        text.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        text.setMaxSize(200, Region.USE_COMPUTED_SIZE);

        parent.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        parent.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        parent.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        // Make the tooltip non-interactive.
        parent.setMouseTransparent(true);

        // Apply the theme to the tooltip.
        injectTheme(parent, theme);

        return parent;
    }

    /**
     * Injects the appropriate theme stylesheet into the given Parent node.
     *
     * @param form  The Parent node to style.
     * @param theme The theme to apply.
     */
    public static void injectTheme(Parent form, Theme theme) {
        form.getStylesheets().add(getDefaultStyle(theme));
    }

    /**
     * Retrieves the default stylesheet path for the specified theme.
     *
     * @param theme The theme to apply.
     * @return The path to the corresponding stylesheet.
     */
    public static String getDefaultStyle(Theme theme) {
        return Objects.requireNonNull(MessagePopupUtil.class.getResource(Objects.requireNonNull(getStylePath(theme)))).toExternalForm();
    }

    /**
     * Determines the appropriate stylesheet path based on the theme.
     *
     * @param theme The theme to evaluate.
     * @return The path to the corresponding stylesheet.
     */
    private static String getStylePath(Theme theme) {
        return switch (theme) {
            case SYSTEM -> ThemeDetector.isDarkTheme() ?
                    FxPopupConfig.pathDarkToolTip : FxPopupConfig.pathLightToolTip;
            case LIGHT -> FxPopupConfig.pathLightToolTip;
            case DARK -> FxPopupConfig.pathDarkToolTip;
        };
    }

    /**
     * Checks if a tooltip exists for the specified Node.
     *
     * @param node The Node to check.
     * @return True if a tooltip exists for the Node, false otherwise.
     */
    public static boolean isToolTip(Node node) {
        return MasterUtils.findNodeById(
                MasterUtils.getRoot(),
                getToolTipIdByNode(node)
        ) != null;
    }

    /**
     * Retrieves the tooltip associated with the specified Node.
     *
     * @param node The Node whose tooltip is to be retrieved.
     * @return The tooltip Node if it exists, null otherwise.
     */
    public static Node getToolTipByNode(Node node) {
        return MasterUtils.findNodeById(
                MasterUtils.getRoot(),
                getToolTipIdByNode(node)
        );
    }

    /**
     * Translate Tool Tip by HPos
     *
     * @param node The Node whose tooltip is to be retrieved.
     * @param toolTip The Tool Tip to move.
     * @param pos The HPos to calc translate.
     */
    public static void positionToolTip(Node node, Node toolTip, HPos pos, double space) {
        Bounds parentBoundsInRoot = node.localToScene(node.getLayoutBounds());
        Bounds toolTipBoundsInRoot = toolTip.localToScene(toolTip.getLayoutBounds());

        double parentX = parentBoundsInRoot.getMinX();
        double parentY = parentBoundsInRoot.getMinY();

        double nodeWidth = parentBoundsInRoot.getWidth();
        double nodeHeight = parentBoundsInRoot.getHeight();

        double toolTipWidth = toolTipBoundsInRoot.getWidth();
        double toolTipHeight = toolTipBoundsInRoot.getHeight();

        double translateX = pos.equals(HPos.RIGHT) ?
                parentX + nodeWidth + space :
                parentX - toolTipWidth - space;
        double translateY = parentY + (nodeHeight / 2) - (toolTipHeight / 2);

        toolTip.setTranslateX(translateX);
        toolTip.setTranslateY(translateY);
    }

    /**
     * Translate Tool Tip by VPos
     *
     * @param node The Node whose tooltip is to be retrieved.
     * @param toolTip The Tool Tip to move.
     * @param pos The VPos to calc translate.
     */
    public static void positionToolTip(Node node, Node toolTip, VPos pos, double space) {
        Bounds parentBoundsInRoot = node.localToScene(node.getLayoutBounds());
        Bounds toolTipBoundsInRoot = toolTip.localToScene(toolTip.getLayoutBounds());

        double parentX = parentBoundsInRoot.getMinX();
        double parentY = parentBoundsInRoot.getMinY();

        double nodeWidth = parentBoundsInRoot.getWidth();
        double nodeHeight = parentBoundsInRoot.getHeight();

        double toolTipWidth = toolTipBoundsInRoot.getWidth();
        double toolTipHeight = toolTipBoundsInRoot.getHeight();

        double translateX = parentX + (nodeWidth / 2) - (toolTipWidth / 2);
        double translateY = pos.equals(VPos.BOTTOM) ?
                parentY + nodeHeight + space :
                parentY - toolTipHeight - space;

        toolTip.setTranslateX(translateX);
        toolTip.setTranslateY(translateY);
    }

    public static String getToolTipIdByNode(Node node) {
        return "ToolTip@" + Integer.toString(node.hashCode());
    }
}
