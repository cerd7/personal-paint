package src.com.cerd.app.core.command;

import java.util.List;
import src.com.cerd.app.core.model.DrawModel;
import src.com.cerd.app.core.model.Stroke;

public class PasteCommand implements Command {
    private final DrawModel model;
    private final List<Stroke> strokesToAdd;

    public PasteCommand(DrawModel model, List<Stroke> strokesToAdd) {
        this.model = model;
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
