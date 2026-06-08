package src.com.cerd.app.core.command;

import src.com.cerd.app.core.model.Stroke;
import src.com.cerd.app.core.model.DrawModel;

public class AddStrokeCommand implements Command {
    private final DrawModel model;
    private final Stroke stroke;

    public AddStrokeCommand(DrawModel model, Stroke stroke){
        this.model = model;
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
