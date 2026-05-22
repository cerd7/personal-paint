package src.ui;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import src.core.model.DrawModel;
import src.core.model.Stroke;

public class CanvasPanel extends JPanel {
    private final DrawModel model;
    private Stroke activeStroke;

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
            drawStroke(g, activeStroke);
        }
        if(activeStroke != null){
            drawStroke(g, activeStroke);
        }
    }

    private void drawStroke(Graphics g, Stroke stroke){
        Point prev = null;
        for(Point point : stroke.getPoints()){
            if(prev != null){
                g.drawLine(prev.x, prev.y, point.x, point.y);
            }
        }
    }
}
