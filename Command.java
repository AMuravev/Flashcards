package flashcards;

public enum Command {
    ADD("add"),
    REMOVE("remove"),
    IMPORT("import"),
    EXPORT("export"),
    ASK("ask"),
    HARDER_CARDS("hardest card"),
    LOGS("log"),
    RESET("reset stats"),
    NOT_FOUND(""),
    EXIT("exit");

    private final String command;

    Command(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public static Command getCommand(String str) {

        for (Command command : Command.values()) {
            if (command.getCommand().equals(str)) {
                return command;
            }
        }
        return Command.NOT_FOUND;
    }
}