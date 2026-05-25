package src.core.command;

import java.util.List;
import src.core.model.DrawModel;
import src.core.model.Stroke;

public class ClearCommand implements Command {
    private final DrawModel model;
    private List<Stroke> snapshot;

    public ClearCommand(DrawModel model){
        this.model = model;
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
