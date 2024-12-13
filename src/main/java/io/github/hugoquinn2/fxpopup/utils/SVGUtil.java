package io.github.hugoquinn2.fxpopup.utils;

import io.github.hugoquinn2.fxpopup.config.FxPopupConfig;
import io.github.hugoquinn2.fxpopup.constants.FxPopIcon;
import io.github.hugoquinn2.fxpopup.service.SVGLoader;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

public class SVGUtil {
    public static SVGPath getIcon(FxPopIcon fxPopIcon) {
        String svgPathContent = SVGLoader.loadSVGFromResources(String.format(FxPopupConfig.iconPath, fxPopIcon.literalIcon));
        SVGPath svg = new SVGPath();
        svg.setContent(svgPathContent);

        return svg;
    }
}
