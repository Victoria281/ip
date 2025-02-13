package vic.enums;

/**
 * This enum represents different commands that can be issued by the user.
 */
public enum Command {
    BYE("bye"),
    TODO("todo"),
    DEADLINE("deadline"),
    EVENT("event"),
    MARK("mark"),
    UNMARK("unmark"),
    LIST("list"),
    FIND("find"),
    DELETE("delete"),
    NONE("none");

    private final String commandText;

    Command(String commandText) {
        this.commandText = commandText;
    }

    /**
     * Retrieves the text representation of the command.
     *
     * @return the command text
     */
    public static Command convertText(String text) {
        for (Command c : Command.values()) {
            if (c.commandText.equalsIgnoreCase(text)) {
                return c;
            }
        }
        return NONE;
    }
}
