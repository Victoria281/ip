package main.java.Vic.enums;

public enum Command {
    BYE("bye"),
    TODO("todo"),
    DEADLINE("deadline"),
    EVENT("event"),
    MARK("mark"),
    UNMARK("unmark"),
    LIST("list"),
    DELETE("delete"),
    NONE("none");

    private final String commandText;

    Command(String commandText) {
        this.commandText = commandText;
    }

    public String getCommandText() {
        return this.commandText;
    }

    public static Command convertText(String text) {
        for (Command c : Command.values()) {
            if (c.commandText.equalsIgnoreCase(text)) {
                return c;
            }
        }
        return NONE;
    }
}