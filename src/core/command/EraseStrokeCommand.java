package src.core.command;

import src.core.model.DrawModel;
import src.core.model.Stroke;

public class EraseStrokeCommand implements Command {
    private final DrawModel model;
    private Stroke removed;

    public EraseStrokeCommand(DrawModel model){
        this.model = model;
    }

    @Override
    public void execute(){
        removed = model.removeLastStroke();
    }

    @Override
    public void undo(){
        if(removed != null){
            model.addStroke(removed);
        }
    }
}
