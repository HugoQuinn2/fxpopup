package io.github.hugoquinn2.fxpopup.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to define a message form class.
 * This annotation associates a form class with a validator that handles validation logic.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Form {

    /**
     * The name of the form. This will typically be used for identifying
     * the form in the UI or for other organizational purposes.
     *
     * @return the name of the form
     */
    String name();

    /**
     * A validator class that will be used to perform validation on the form.
     * The class must extend FxPopupForm and implement appropriate validation logic.
     *
     * @return the validator class for the form
     */
    Class<? extends FxPopupForm<?>> validator();
}
