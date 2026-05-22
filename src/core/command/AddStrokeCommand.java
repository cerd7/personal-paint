package src.core.command;

import src.core.model.Stroke;
import src.core.model.DrawModel;

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
