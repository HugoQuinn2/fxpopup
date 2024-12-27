package io.github.hugoquinn2.fxpopup.controller;

import io.github.hugoquinn2.fxpopup.constants.FieldType;
import io.github.hugoquinn2.fxpopup.constants.FxPopIcon;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MessageField {
    String label() default "";
    String placeholder() default "";
    String context() default "";
    FieldType type() default FieldType.TEXT;
    FxPopIcon icon() default FxPopIcon.NOTHING;
    String[] data() default {};

    boolean disable() default false;
    boolean required() default false;
}
