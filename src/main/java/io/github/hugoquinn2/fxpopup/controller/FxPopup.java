package io.github.hugoquinn2.fxpopup.controller;

import io.github.hugoquinn2.fxpopup.config.FxPopupConfig;
import io.github.hugoquinn2.fxpopup.constants.Theme;
import io.github.hugoquinn2.fxpopup.model.Message;
import io.github.hugoquinn2.fxpopup.utils.FxPopupUtil;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;

import static io.github.hugoquinn2.fxpopup.utils.FxPopupUtil.*;

public class FxPopup implements FxPopupInterface{
    private StackPane stackPane;
    private Pos pos;
    private Theme theme;

    public FxPopup(StackPane root) {
        this.stackPane = root;
        pos = FxPopupConfig.defaultPos;
        theme = FxPopupConfig.defaultTheme;
    }

    @Override
    public void add(Message message) {
        int duration = message.getDuration();

        if (message.getTheme() == null)
            message.setTheme(theme);
        if (message.getContent() == null)
            message.setContent(getDefaultContent());
        if (message.getCss() == null)
            message.setCss(getDefaultCss(message));

        if(message.getActionEvent() != null)
            FxPopupUtil.loadActionEvent(message);

        injectTheme(message);
        loadContent(message);
        FxPopupUtil.injectFxml(stackPane, message, pos);

        if (duration != 0) {
            FxPopupUtil.setFadeTransition(stackPane, message);
        }
    }

    @Override
    public void remove(Message message) {
        FxPopupUtil.removeFxml(stackPane, message);
    }

    public Pos getPos() {
        return pos;
    }

    public void setPos(Pos pos) {
        this.pos = pos;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }
}
