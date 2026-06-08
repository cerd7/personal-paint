package src.com.cerd.app.core.command;

import src.com.cerd.app.core.model.DrawModel;
import src.com.cerd.app.core.model.Stroke;

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
