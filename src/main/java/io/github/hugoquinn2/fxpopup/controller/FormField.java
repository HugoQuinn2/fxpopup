package io.github.hugoquinn2.fxpopup.controller;

import io.github.hugoquinn2.fxpopup.constants.FieldType;
import io.github.hugoquinn2.fxpopup.constants.FxPopIcon;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to mark fields that will be part of a message form.
 * Provides metadata for how the field should be displayed and validated.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FormField {

    /**
     * Label for the field, typically displayed next to the input field.
     * Default is an empty string.
     */
    String label() default "";

    /**
     * Placeholder text for the input field, typically displayed when the field is empty.
     * Default is an empty string.
     */
    String placeholder() default "";

    /**
     * Context information for the field, could be used for additional validation or field grouping.
     * Default is an empty string.
     */
    String context() default "";

    /**
     * Defines the type of field. Uses the FieldType enum to specify types like TEXT, NUMBER, etc.
     * Default is FieldType.TEXT.
     */
    FieldType type() default FieldType.TEXT;

    /**
     * Icon associated with the field, defined by the FxPopIcon enum.
     * Default is FxPopIcon.NOTHING.
     */
    FxPopIcon icon() default FxPopIcon.NOTHING;

    /**
     * Additional data that can be associated with the field, such as values for a dropdown.
     * Default is an empty array.
     */
    String[] data() default {};

    /**
     * Whether the field should be disabled.
     * Default is false, meaning the field is enabled.
     */
    boolean disable() default false;

    /**
     * Whether the field is required for form submission.
     * Default is false, meaning the field is optional.
     */
    boolean required() default false;

    String toolTip() default "";
}
