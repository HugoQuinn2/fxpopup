package io.github.hugoquinn2.fxpopup.controller;

public interface FxPopupForm<T> {
    boolean validate(T form) throws Exception;
    void isValidForm(T form) throws Exception;
}
