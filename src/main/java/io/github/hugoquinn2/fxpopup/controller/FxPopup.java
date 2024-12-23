package io.github.hugoquinn2.fxpopup.controller;

import io.github.hugoquinn2.fxpopup.config.FxPopupConfig;
import io.github.hugoquinn2.fxpopup.constants.Theme;
import io.github.hugoquinn2.fxpopup.exceptions.ExceptionAnnotation;
import io.github.hugoquinn2.fxpopup.model.Message;
import io.github.hugoquinn2.fxpopup.utils.FxPopupUtil;
import io.github.hugoquinn2.fxpopup.utils.MasterUtils;
import io.github.hugoquinn2.fxpopup.utils.MessageFormUtil;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import static io.github.hugoquinn2.fxpopup.utils.FxPopupUtil.*;

public class FxPopup implements FxPopupInterface{
    private Pos pos;
    private Theme theme;

    public FxPopup() {
        pos = FxPopupConfig.defaultPos;
        theme = FxPopupConfig.defaultTheme;

        System.setProperty("prism.lcdtext", "false");
        System.setProperty("prism.text", "t2k");
        System.setProperty("prism.lcdtext", "true");
        System.setProperty("prism.order", "d3d,es2");

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
        FxPopupUtil.injectFxml(message, pos);

        if (duration != 0) {
            FxPopupUtil.setFadeTransition(message);
        }
    }

    @Override
    public void remove(Message message) {
        FxPopupUtil.removeFxml(message);
    }

    @Override
    public void show(Object model) throws Exception {
        Class<?> clazz = model.getClass();

        if (!clazz.isAnnotationPresent(MessageForm.class))
            throw new Exception(String.format("Object <%s> required annotation @MessageForm", clazz.getName()));

        Parent form = MessageFormUtil.generateForm(model, theme);

        if (form != null) {
            MasterUtils.findAndEditText(form, "titleForm", clazz.getAnnotation(MessageForm.class).name());
            MessageFormUtil.injectFxml(form, Pos.CENTER);
            MessageFormUtil.setClose(form);
        }
    }

    @Override
    public void removeMessageForm() {
        MessageFormUtil.removeMessageForm();
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
