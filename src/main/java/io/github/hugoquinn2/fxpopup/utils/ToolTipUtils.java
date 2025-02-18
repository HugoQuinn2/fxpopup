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
}
