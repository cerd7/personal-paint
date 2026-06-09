package src.com.cerd.app.core.command;

import java.util.List;
import src.com.cerd.app.core.model.DrawingModel;
import src.com.cerd.app.core.model.Stroke;

public class PasteCommand extends AbstractDrawingCommand {
    private final List<Stroke> strokesToAdd;

    public PasteCommand(DrawingModel model, List<Stroke> strokesToAdd) {
        super(model);
        this.strokesToAdd = strokesToAdd;
    }

    @Override
    public void execute() {
        for(Stroke stroke : strokesToAdd){
            model.addStroke(stroke);
        }
    }

    @Override
    public void undo() {
        for(int i=0;i<strokesToAdd.size(); i++){
            model.removeLastStroke();
        }
    }

}
