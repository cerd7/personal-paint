package src.com.cerd.app.core.command;

public interface Command {
    void execute();
    void undo();
}
