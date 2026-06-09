package src.com.cerd.app.ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public interface CanvasSurface {
    void beginActiveStroke(Point start);
    void appendActivePoint(Point point);
    void clearActiveStroke();
    void setSelectionRect(Rectangle rect);
    void invalidateBuffer();
    void repaint();
    void addMouseListener(MouseListener listener);
    void addMouseMotionListener(MouseMotionListener listener);
}
