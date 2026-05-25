package src.ui.events;

import java.awt.event.*;
import src.core.command.AddStrokeCommand;
import src.core.command.CommandManager;
import src.core.model.DrawModel;
import src.core.model.Stroke;
import src.ui.CanvasPanel;

public class CanvasInputHandler {
    private final DrawModel model;
    private final CommandManager commandManager;
    private final CanvasPanel canvasPanel;
    private Stroke activeStroke;

    public CanvasInputHandler(DrawModel model, CommandManager commandManager, CanvasPanel canvasPanel){
        this.model = model;
        this.commandManager = commandManager;
        this.canvasPanel = canvasPanel;
    }

    public void register(){
        canvasPanel.addMouseListener(new MouseAdapter() {
            
            public void mousePressed(MouseEvent e) {
                activeStroke = new Stroke();
                activeStroke.addPoint(e.getPoint());
                canvasPanel.setActiveStroke(activeStroke);
            }
            
            public void mouseReleased(MouseEvent e) {
                if (activeStroke != null && activeStroke.hasRenderablePoints()) {
                    commandManager.execute(new AddStrokeCommand(model, activeStroke));
                }
                activeStroke = null;
                canvasPanel.setActiveStroke(null);
                canvasPanel.repaint();
            }
        });

        canvasPanel.addMouseMotionListener(new MouseAdapter() {
            
            public void mouseDragged(MouseEvent e) {
                if (activeStroke == null) {
                    return;
                }
                activeStroke.addPoint(e.getPoint());
                canvasPanel.repaint();
            }
        });
    }
}
