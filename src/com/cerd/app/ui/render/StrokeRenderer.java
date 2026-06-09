package src.com.cerd.app.ui.render;

import java.awt.Graphics2D;
import java.awt.Point;

import src.com.cerd.app.core.model.Stroke;

public interface StrokeRenderer {
    void configure(Graphics2D g2d);
    void drawStroke(Graphics2D g2d, Stroke stroke);
    void drawSegment(Graphics2D g2d, Point start, Point end);
}
