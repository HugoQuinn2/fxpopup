package io.github.hugoquinn2.fxpopup.model;

import io.github.hugoquinn2.fxpopup.constants.FxPopIcon;
import io.github.hugoquinn2.fxpopup.utils.SVGUtil;
import javafx.scene.control.Label;

public class Icon extends Label {
    private FxPopIcon icon;
    private double scale = 1;

    public Icon(){
    }

    public Icon(FxPopIcon icon) {
        this(icon, 1);
    }

    public Icon(FxPopIcon icon, double scale) {
        this.setGraphic(SVGUtil.getIcon(icon, scale));
    }

    public void setIcon(FxPopIcon icon) {
        this.icon = icon;
        this.setGraphic(SVGUtil.getIcon(icon, scale));
    }

    public void setScale(double scale) {
        this.scale = scale;
        this.setGraphic(SVGUtil.getIcon(icon, scale));
    }

    public FxPopIcon getIcon() {
        return icon;
    }

    public double getScale() {
        return scale;
    }

    @Override
    public String toString() {
        return "Icon{" +
                "icon=" + icon +
                ", scale=" + scale +
                '}';
    }
}
