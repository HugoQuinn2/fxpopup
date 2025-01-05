package io.github.hugoquinn2.fxpopup.utils;

import io.github.hugoquinn2.fxpopup.config.FxPopupConfig;
import io.github.hugoquinn2.fxpopup.config.StyleConfig;
import io.github.hugoquinn2.fxpopup.constants.FxPopIcon;
import io.github.hugoquinn2.fxpopup.service.SVGLoader;
import javafx.scene.shape.SVGPath;

public class SVGUtil {
    /**
     * Load .svg ico from resources.
     * @param fxPopIcon FxPopIcon to load.
     * @param scale Double value scale to apply.
     */
    public static SVGPath getIcon(FxPopIcon fxPopIcon, double scale) {
        if (fxPopIcon.equals(FxPopIcon.NOTHING))
            return null;

        // Extract svg code from .svg file
        String svgPathContent = SVGLoader.loadSVGFromResources(
                String.format(FxPopupConfig.iconPath, fxPopIcon.literalIcon)
        );

        SVGPath svg = new SVGPath();
        svg.setContent(svgPathContent);
        svg.setScaleX(scale);
        svg.setScaleY(scale);

        // Add ICON class
        svg.getStyleClass().add(StyleConfig.ICON);

        return svg;
    }
}
