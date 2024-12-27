package io.github.hugoquinn2.fxpopup.model;

import io.github.hugoquinn2.fxpopup.config.FxPopupConfig;
import io.github.hugoquinn2.fxpopup.constants.FxPopIcon;
import io.github.hugoquinn2.fxpopup.utils.SVGUtil;
import javafx.scene.control.Label;

public class Icon extends Label {
    public Icon(){
    }

    public Icon(FxPopIcon icon) {
        this.setGraphic(SVGUtil.getIcon(icon, FxPopupConfig.iconScale));
    }

    public Icon(FxPopIcon icon, double scale) {
        this.setGraphic(SVGUtil.getIcon(icon, scale));
    }
}
