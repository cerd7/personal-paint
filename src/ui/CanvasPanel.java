package src.ui;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;
import java.awt.*;

import src.core.model.DrawModel;
import src.core.model.Stroke;

public class CanvasPanel extends JPanel {
    private final DrawModel model;
    private Stroke activeStroke;
    private Rectangle selectionRect;

    public CanvasPanel(DrawModel model){
        this.model = model;
        setBackground(Color.WHITE);
    }

    public void setActiveStroke(Stroke stroke){
        this.activeStroke = stroke;
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        for(Stroke stroke : model.getStrokes()){
            drawStroke(g, stroke);
        }
        if(activeStroke != null){
            drawStroke(g, activeStroke);
        }

        if (selectionRect != null) {
            g.setColor(new Color(0, 120, 215));
            g.drawRect(selectionRect.x, selectionRect.y, selectionRect.width, selectionRect.height);
        }
    }

    private void drawStroke(Graphics g, Stroke stroke){
        Point prev = null;
        for(Point point : stroke.getPoints()){
            if(prev != null) g.drawLine(prev.x, prev.y, point.x, point.y);
            prev = point;
        }
    }

    public void setSelectionRect(Rectangle rect) {
        this.selectionRect = rect;
    }

    public void registerKeyBindings(Runnable onCopy, Runnable onPaste){
        InputMap im = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        im.put(KeyStroke.getKeyStroke("control C"), "copy");
        am.put("copy", new AbstractAction() {
            public void actionPerformed(ActionEvent e){
                onCopy.run();
            }
        });

        im.put(KeyStroke.getKeyStroke("control V"), "paste");
        am.put("paste", new AbstractAction() {
            public void actionPerformed(ActionEvent e){
                onPaste.run();
            }
        });
    }
}