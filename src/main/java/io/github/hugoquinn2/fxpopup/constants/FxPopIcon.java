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
    PHONE("phone");

    public String literalIcon;

    FxPopIcon(String literalIcon) {
        this.literalIcon = literalIcon;
    }

    public String literalIcon() {
        return this.literalIcon;
    }
}
