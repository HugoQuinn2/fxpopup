package io.github.hugoquinn2.fxpopup.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageForm {
    String name();
    Class<? extends FxPopupForm<?>> validator();
}
