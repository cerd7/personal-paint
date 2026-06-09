package src.com.cerd.app.core.command;

import src.com.cerd.app.core.model.DrawingModel;
import src.com.cerd.app.core.model.Stroke;

public class EraseStrokeCommand extends AbstractDrawingCommand {
    private Stroke removed;

    public EraseStrokeCommand(DrawingModel model){
        super(model);
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
