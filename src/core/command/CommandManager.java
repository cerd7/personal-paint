package src.core.command;

import java.util.*;

public class CommandManager {
    private final Deque<Command> undoStack = new ArrayDeque<>();
    private final Deque<Command> redoStack = new ArrayDeque<>();

    public void execute(Command command){
        command.execute();
        undoStack.push(command);
        redoStack.clear();
    }

    public void undo(){
        if(undoStack.isEmpty()){
            return;
        }
        Command command = undoStack.pop();
        command.undo();
        redoStack.push(command);
    }

    public void redo(){
        if(redoStack.isEmpty()){
            return;
        }
        Command command = redoStack.pop();
        command.execute();
        undoStack.push(command);
    }
}
