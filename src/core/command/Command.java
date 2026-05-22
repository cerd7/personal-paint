package src.core.command;

public interface Command {
    void execute();
    void undo();
}
