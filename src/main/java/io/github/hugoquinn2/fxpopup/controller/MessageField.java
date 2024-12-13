package io.github.hugoquinn2.fxpopup.controller;

import io.github.hugoquinn2.fxpopup.constants.FieldType;
import io.github.hugoquinn2.fxpopup.constants.FxPopIcon;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MessageField {
    String label();
    String placeholder();
    FieldType type() default FieldType.TEXT;
    FxPopIcon icon() default FxPopIcon.NOTHING;

    boolean editable() default true;
    int maxValue() default 0;
    int minValue() default 0;
    boolean required() default false;
    String regex() default "";
}
