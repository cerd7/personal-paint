package src.com.cerd.app.ui.events;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.*;
import java.util.List;

import src.com.cerd.app.core.command.AddStrokeCommand;
import src.com.cerd.app.core.command.CommandManager;
import src.com.cerd.app.core.model.DrawingModel;
import src.com.cerd.app.core.model.Stroke;
import src.com.cerd.app.core.selection.BoundsSelectionPolicy;
import src.com.cerd.app.core.selection.SelectionPolicy;
import src.com.cerd.app.ui.CanvasSurface;

public class CanvasInputHandler {
    private final DrawingModel model;
    private final CommandManager commandManager;
    private final CanvasSurface canvasPanel;
    private final SelectionPolicy selectionPolicy;
    private Stroke activeStroke;

    private Point selectionStart;
    private Rectangle selectionRect;
    private boolean isSelecting;

    public CanvasInputHandler(DrawingModel model, CommandManager commandManager, CanvasSurface canvasPanel){
        this(model, commandManager, canvasPanel, new BoundsSelectionPolicy());
    }

    public CanvasInputHandler(DrawingModel model, CommandManager commandManager, CanvasSurface canvasPanel, SelectionPolicy selectionPolicy){
        this.model = model;
        this.commandManager = commandManager;
        this.canvasPanel = canvasPanel;
        this.selectionPolicy = selectionPolicy;
    }

    public void register(){
        canvasPanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if(e.isShiftDown()){
                    isSelecting = true;
                    selectionStart = e.getPoint();
                    selectionRect = new Rectangle(selectionStart.x, selectionStart.y,0,0);
                    canvasPanel.setSelectionRect(selectionRect);
                    canvasPanel.repaint();
                    return;
                }

                isSelecting = false;
                activeStroke = new Stroke();
                Point start = e.getPoint();
                activeStroke.addPoint(start);
                canvasPanel.beginActiveStroke(start);
            }
            
            public void mouseReleased(MouseEvent e) {
                if (isSelecting){
                    if(selectionRect != null) {
                        List<Stroke> selected = selectionPolicy.select(model.getStrokes(), selectionRect);
                        model.setSelection(selected);
                    }
                    selectionStart = null;
                    selectionRect = null;
                    canvasPanel.setSelectionRect(null);
                    canvasPanel.repaint();
                    return;
                }
                
                if (activeStroke != null && activeStroke.hasRenderablePoints()) {
                    Stroke completedStroke = activeStroke;
                    activeStroke = null;
                    canvasPanel.clearActiveStroke();
                    commandManager.execute(new AddStrokeCommand(model, completedStroke));
                    canvasPanel.invalidateBuffer();
                    return;
                }

                if (activeStroke != null) {
                    activeStroke = null;
                    canvasPanel.clearActiveStroke();
                    return;
                }

                activeStroke = null;
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
                    selectionRect.setBounds(x,y,w,h);
                    canvasPanel.setSelectionRect(selectionRect);
                    canvasPanel.repaint();
                    return;
                }
                
                if (activeStroke == null) return;
                Point currentPoint = e.getPoint();
                activeStroke.addPoint(currentPoint);
                canvasPanel.appendActivePoint(currentPoint);
            }
        });
    }
}
