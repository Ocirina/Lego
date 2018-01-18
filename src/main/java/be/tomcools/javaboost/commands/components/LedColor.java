package be.tomcools.javaboost.commands.components;

public enum LedColor {
    OFF("0"),
    PINK("1"),
    PURPLE("2"),
    BLUE("3"),
    LIGHT_BLUE("4"),
    CYAN("5"),
    GREEN("6"),
    YELLOW("7"),
    ORANGE("8"),
    RED("9"),
    WHITE("A");

    private String code;

    LedColor(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
