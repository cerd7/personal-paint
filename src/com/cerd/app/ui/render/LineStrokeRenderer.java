package src.com.cerd.app.ui.render;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import src.com.cerd.app.core.model.Stroke;

public class LineStrokeRenderer implements StrokeRenderer {
    @Override
    public void configure(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    @Override
    public void drawStroke(Graphics2D g2d, Stroke stroke) {
        Point previous = null;
        for (Point point : stroke.getPoints()) {
            if (previous != null) {
                g2d.drawLine(previous.x, previous.y, point.x, point.y);
            }
            previous = point;
        }
    }

    @Override
    public void drawSegment(Graphics2D g2d, Point start, Point end) {
        g2d.drawLine(start.x, start.y, end.x, end.y);
    }
}
