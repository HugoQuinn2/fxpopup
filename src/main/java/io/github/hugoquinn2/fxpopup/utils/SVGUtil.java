package io.github.hugoquinn2.fxpopup.utils;

import io.github.hugoquinn2.fxpopup.config.FxPopupConfig;
import io.github.hugoquinn2.fxpopup.constants.FxPopIcon;
import io.github.hugoquinn2.fxpopup.service.SVGLoader;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

public class SVGUtil {
    public static SVGPath getIcon(FxPopIcon fxPopIcon, double scale) {
        if (fxPopIcon.equals(FxPopIcon.NOTHING))
            return null;

        String svgPathContent = SVGLoader.loadSVGFromResources(String.format(FxPopupConfig.iconPath, fxPopIcon.literalIcon));
        SVGPath svg = new SVGPath();
        svg.setContent(svgPathContent);
        svg.setScaleX(scale);
        svg.setScaleY(scale);
        svg.getStyleClass().add("icon");

        return svg;
    }
}
