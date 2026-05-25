package src.ui.events;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

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

    private Point selectionStart;
    private Rectangle selectionRect;
    private boolean isSelecting;

    public CanvasInputHandler(DrawModel model, CommandManager commandManager, CanvasPanel canvasPanel){
        this.model = model;
        this.commandManager = commandManager;
        this.canvasPanel = canvasPanel;
    }

    public void register(){
        canvasPanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if(e.isShiftDown()){
                    isSelecting = true;
                    selectionStart = e.getPoint();
                    selectionRect = new Rectangle(selectionStart);
                    canvasPanel.setSelectionRect(selectionRect);
                    canvasPanel.repaint();
                    return;
                }

                isSelecting = false;
                activeStroke = new Stroke();
                activeStroke.addPoint(e.getPoint());
                canvasPanel.setActiveStroke(activeStroke);
            }
            
            public void mouseReleased(MouseEvent e) {
                if (isSelecting){
                    if(selectionRect != null) {
                        List<Stroke> selected = new ArrayList<>();
                        for (Stroke stroke : model.getStrokes()) {
                            if (selectionRect.intersects(stroke.getBounds())) {
                                selected.add(stroke);
                            }
                        }
                        model.setSelection(selected);
                    }
                    selectionStart = null;
                    selectionRect = null;
                    canvasPanel.setSelectionRect(null);
                    canvasPanel.repaint();
                    return;
                }
                
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
                if (isSelecting){
                    if(selectionStart == null) return;
                    int x = Math.min(selectionStart.x, e.getX());
                    int y = Math.min(selectionStart.y, e.getY());
                    int w = Math.abs(selectionStart.x - e.getX());
                    int h = Math.abs(selectionStart.y - e.getY());
                    selectionRect = new Rectangle(x,y,w,h);
                    canvasPanel.setSelectionRect(selectionRect);
                    canvasPanel.repaint();
                    return;
                }
                
                if (activeStroke == null) return;
                activeStroke.addPoint(e.getPoint());
                canvasPanel.repaint();
            }
        });
    }
}
