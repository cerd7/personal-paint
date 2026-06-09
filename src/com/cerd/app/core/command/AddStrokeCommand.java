package src.com.cerd.app.core.command;

import src.com.cerd.app.core.model.Stroke;
import src.com.cerd.app.core.model.DrawingModel;

public class AddStrokeCommand extends AbstractDrawingCommand {
    private final Stroke stroke;

    public AddStrokeCommand(DrawingModel model, Stroke stroke){
        super(model);
        this.stroke = stroke;
    }

    @Override
    public void execute(){
        model.addStroke(stroke);
    }

    @Override
    public void undo(){
        model.removeLastStroke();
    }
}
