package io.github.hugoquinn2.fxpopup.controller;

import io.github.hugoquinn2.fxpopup.exceptions.ValidationException;

public interface FxPopupForm<T> {
    boolean validate(T form) throws Exception;
    void isValidForm(T form);
}
