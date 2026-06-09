package src.com.cerd.app.core.command;

import java.util.List;
import src.com.cerd.app.core.model.DrawingModel;
import src.com.cerd.app.core.model.Stroke;

public class ClearCommand extends AbstractDrawingCommand {
    private List<Stroke> snapshot;

    public ClearCommand(DrawingModel model){
        super(model);
    }

    @Override
    public void execute(){
        snapshot = model.snapshot();
        model.clear();
    }

    @Override
    public void undo(){
        if(snapshot != null){
            model.restore(snapshot);
        }
    }
}
