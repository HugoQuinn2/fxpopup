package io.github.hugoquinn2.fxpopup.constants;

public enum FxPopIcon {
    NOTHING(""),
    EYE("eye"),
    EYE_CLOSE("eye-closed"),
    CLOSE("close"),
    USER("user"),
    DATE("date"),
    PADLOCK("padlock"),
    WIFI("wifi"),
    MODEM("modem"),
    LAN_CABLE("lan-cable"),
    LEFT("left"),
    RIGHT("right"),
    MAIL("mail"),
    CHEVRON_DOWN("chevron-down"),
    PHONE("phone"),
    CHECK("check"),
    CHECK_SQUARE("check-square"),
    CLOSE_SQUARE("close-square"),
    SQUARE("square"),
    MINUS_SQUARE("minus-square"),
    VISA("visa"),
    MASTER_CARD("master-card"),
    AMEX("amex"),
    CARD("card");

    public String literalIcon;

    FxPopIcon(String literalIcon) {
        this.literalIcon = literalIcon;
    }

    public String literalIcon() {
        return this.literalIcon;
    }
}
