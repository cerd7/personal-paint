package src.com.cerd.app.core.command;

import src.com.cerd.app.core.model.DrawingModel;

public abstract class AbstractDrawingCommand implements Command {
    protected final DrawingModel model;

    protected AbstractDrawingCommand(DrawingModel model) {
        this.model = model;
    }
}
