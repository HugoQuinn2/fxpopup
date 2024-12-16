package io.github.hugoquinn2.fxpopup.controller;

public interface FxPopupForm<T> {
    boolean validate(T form);
    void isValidForm(T form);
}
